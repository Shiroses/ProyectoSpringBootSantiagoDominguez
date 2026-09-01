package dev.shor0s.logitrack.mapper;

import dev.shor0s.logitrack.dto.request.CatalogoRequestDTO;
import dev.shor0s.logitrack.dto.response.CatalogoResponseDTO;
import dev.shor0s.logitrack.dto.response.CategoriaResponseDTO;
import dev.shor0s.logitrack.dto.response.RolResponseDTO;
import dev.shor0s.logitrack.model.Catalogo;
import dev.shor0s.logitrack.model.Categoria;
import dev.shor0s.logitrack.model.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public RolResponseDTO entityToDto(Rol entity) {
        if (entity == null) return null;
        return new RolResponseDTO(
                entity.getIdRol(),
                entity.getNombre(),
                entity.getActivo()
        );
    }
}