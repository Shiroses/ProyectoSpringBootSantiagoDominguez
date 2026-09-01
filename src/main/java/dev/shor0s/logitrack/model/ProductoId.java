package dev.shor0s.logitrack.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductoId implements Serializable {

    @Column(name = "id_catalogo")
    private Integer idCatalogo;

    @Column(name = "id_bodega")
    private Integer idBodega;
}