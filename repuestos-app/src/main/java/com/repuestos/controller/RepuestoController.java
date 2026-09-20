package com.repuestos.controller;

import com.repuestos.model.Repuesto;
import com.repuestos.service.RepuestoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repuestos")
public class RepuestoController {

    private final RepuestoService repuestoService;

    public RepuestoController(RepuestoService repuestoService) {
        this.repuestoService = repuestoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Repuesto crear(@RequestBody Repuesto repuesto) {
        return repuestoService.crear(repuesto);
    }

    @PutMapping("/{id}")
    public Repuesto actualizar(@PathVariable Long id, @RequestBody Repuesto repuesto) {
        return repuestoService.actualizar(id, repuesto);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Repuesto> crearVarios(@RequestBody List<Repuesto> repuestos) {
        return repuestoService.crearVarios(repuestos);
    }

    @GetMapping
    public List<Repuesto> listar() {
        return repuestoService.listar();
    }

    @GetMapping("/{id}")
    public Repuesto obtener(@PathVariable Long id) {
        return repuestoService.obtener(id);
    }

    @PostMapping("/buscar")
    public Map<String, Object> buscar(@RequestBody Map<String, Object> body) {
        return repuestoService.buscar(body);
    }
}