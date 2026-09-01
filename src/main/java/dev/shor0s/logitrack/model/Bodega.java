package dev.shor0s.logitrack.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bodegas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bodega")
    private Integer idBodega;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "ubicacion", nullable = false, length = 150)
    private String ubicacion;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "capacidad_actual", nullable = false)
    private Integer capacidadActual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encargado", nullable = false)
    private Usuario encargado;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}