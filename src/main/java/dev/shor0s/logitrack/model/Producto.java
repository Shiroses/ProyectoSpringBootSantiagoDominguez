package dev.shor0s.logitrack.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @EmbeddedId
    private ProductoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCatalogo")
    @JoinColumn(name = "id_catalogo")
    private Catalogo catalogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idBodega")
    @JoinColumn(name = "id_bodega")
    private Bodega bodega;

    private Integer stock;
}