package dev.shor0s.logitrack.mapper;

import dev.shor0s.logitrack.dto.request.CatalogoRequestDTO;
import dev.shor0s.logitrack.dto.response.CatalogoResponseDTO;
import dev.shor0s.logitrack.dto.response.CategoriaResponseDTO;
import dev.shor0s.logitrack.model.Catalogo;
import dev.shor0s.logitrack.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CatalogoMapper {

    public CatalogoResponseDTO entityToDto(Catalogo entity, CategoriaResponseDTO categoria) {
        if (entity == null) return null;
        return new CatalogoResponseDTO(
                entity.getIdCatalogo(),
                categoria,
                entity.getNombre(),
                entity.getPrecio(),
                entity.getActivo()
                );
    }

    public Catalogo dtoToEntity(CatalogoRequestDTO dto, Categoria categoria) {
        if (dto == null) return null;
        Catalogo entity = new Catalogo();
        entity.setNombre(dto.nombre());
        entity.setPrecio(dto.precio());
        entity.setCategoria(categoria);
        return entity;
    }

    public void updateEntityFromDto(Catalogo entity, CatalogoRequestDTO dto, Categoria categoria) {
        if (entity == null || dto == null) return;
        entity.setNombre(dto.nombre());
        entity.setPrecio(dto.precio());
        entity.setCategoria(categoria);
    }
}