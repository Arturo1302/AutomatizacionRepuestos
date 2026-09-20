package com.repuestos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "repuestos")
public class Repuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 80)
    private String marca;

    @Column(name = "modelo_compatible", length = 120)
    private String modeloCompatible;

    @Column(name = "anio_desde")
    private Integer anioDesde;

    @Column(name = "anio_hasta")
    private Integer anioHasta;

    @Column(length = 20)
    private String cilindraje;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private BigDecimal precio;

    private Integer stock;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", insertable = false, updatable = false)
    private LocalDateTime actualizadoEn;

    // --- Getters y setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModeloCompatible() { return modeloCompatible; }
    public void setModeloCompatible(String modeloCompatible) { this.modeloCompatible = modeloCompatible; }

    public Integer getAnioDesde() { return anioDesde; }
    public void setAnioDesde(Integer anioDesde) { this.anioDesde = anioDesde; }

    public Integer getAnioHasta() { return anioHasta; }
    public void setAnioHasta(Integer anioHasta) { this.anioHasta = anioHasta; }

    public String getCilindraje() { return cilindraje; }
    public void setCilindraje(String cilindraje) { this.cilindraje = cilindraje; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
}
