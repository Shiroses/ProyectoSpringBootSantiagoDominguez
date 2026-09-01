package dev.shor0s.logitrack.mapper;

import dev.shor0s.logitrack.dto.request.ProductoRequestDTO;
import dev.shor0s.logitrack.dto.response.BodegaResponseDTO;
import dev.shor0s.logitrack.dto.response.CatalogoResponseDTO;
import dev.shor0s.logitrack.dto.response.ProductoDetalleResponseDTO;
import dev.shor0s.logitrack.model.Bodega;
import dev.shor0s.logitrack.model.Catalogo;
import dev.shor0s.logitrack.model.Producto;
import dev.shor0s.logitrack.model.ProductoId;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDetalleResponseDTO entityToDto(Producto entity, CatalogoResponseDTO catalogo, BodegaResponseDTO bodega) {
        if (entity == null) return null;
        return new ProductoDetalleResponseDTO(
                entity.getStock(),
                catalogo,
                bodega
        );
    }

    public Producto dtoToEntity(ProductoRequestDTO dto, Catalogo catalogo, Bodega bodega) {
        if (dto == null) return null;
        Producto entity = new Producto();
        entity.setId(new ProductoId(catalogo.getIdCatalogo(), bodega.getIdBodega()));
        entity.setStock(dto.stock());
        entity.setCatalogo(catalogo);
        entity.setBodega(bodega);
        return entity;
    }

    public void updateEntityFromDto(Producto entity, ProductoRequestDTO dto) {
        if (entity == null || dto == null) return;
        entity.setStock(dto.stock());
    }
}
