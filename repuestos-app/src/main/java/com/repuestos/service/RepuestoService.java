package com.repuestos.service;

import com.repuestos.model.Repuesto;
import com.repuestos.repository.RepuestoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class RepuestoService {

    private static final Logger log = LoggerFactory.getLogger(RepuestoService.class);

    private final RepuestoRepository repuestoRepository;
    private final RestTemplate restTemplate;

    @Value("${n8n.ingesta-url}")
    private String n8nIngestaUrl;

    @Value("${n8n.consulta-url}")
    private String n8nConsultaUrl;

    public RepuestoService(RepuestoRepository repuestoRepository, RestTemplate restTemplate) {
        this.repuestoRepository = repuestoRepository;
        this.restTemplate = restTemplate;
    }

    public Repuesto crear(Repuesto repuesto) {
        Repuesto guardado = repuestoRepository.save(repuesto);
        notificarIngesta(guardado);
        return guardado;
    }

    // Carga masiva: guarda todos en MySQL primero, y solo despues dispara
    // la ingesta hacia n8n una vez a cada uno ya tiene su id definitivo.
    // Se espera un poco entre cada uno para no disparar el rate limit de OpenRouter.
    public List<Repuesto> crearVarios(List<Repuesto> repuestos) {
        List<Repuesto> guardados = repuestoRepository.saveAll(repuestos);
        for (Repuesto r : guardados) {
            notificarIngesta(r);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return guardados;
    }

    public Repuesto actualizar(Long id, Repuesto datos) {
        Repuesto existente = repuestoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Repuesto no encontrado: " + id));

        existente.setSku(datos.getSku());
        existente.setNombre(datos.getNombre());
        existente.setMarca(datos.getMarca());
        existente.setModeloCompatible(datos.getModeloCompatible());
        existente.setAnioDesde(datos.getAnioDesde());
        existente.setAnioHasta(datos.getAnioHasta());
        existente.setCilindraje(datos.getCilindraje());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());

        Repuesto actualizado = repuestoRepository.save(existente);
        // Mismo id -> Qdrant hace upsert y sobreescribe el vector anterior
        notificarIngesta(actualizado);
        return actualizado;
    }

    public List<Repuesto> listar() {
        return repuestoRepository.findAll();
    }

    public Repuesto obtener(Long id) {
        return repuestoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Repuesto no encontrado: " + id));
    }

    // Envía el repuesto a n8n para que genere el embedding y lo guarde en Qdrant.
    // Si n8n falla, solo se registra el error: un problema temporal de n8n/ngrok
    // no debe tumbar el guardado en MySQL.
    private void notificarIngesta(Repuesto repuesto) {
        try {
            restTemplate.postForEntity(n8nIngestaUrl, repuesto, String.class);
        } catch (Exception e) {
            log.error("No se pudo notificar a n8n para el SKU {}: {}", repuesto.getSku(), e.getMessage());
        }
    }

    // El frontend solo habla con el backend; es el backend quien reenvía la
    // consulta y el historial de la conversación a n8n/Qdrant y devuelve la respuesta tal cual.
    @SuppressWarnings("unchecked")
    public Map<String, Object> buscar(Map<String, Object> body) {
        return restTemplate.postForObject(n8nConsultaUrl, body, Map.class);
    }
}