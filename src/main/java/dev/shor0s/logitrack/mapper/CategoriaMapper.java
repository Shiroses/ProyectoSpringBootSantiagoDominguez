package dev.shor0s.logitrack.mapper;

import dev.shor0s.logitrack.dto.request.UsuarioRequestDTO;
import dev.shor0s.logitrack.dto.response.CategoriaResponseDTO;
import dev.shor0s.logitrack.dto.response.RolResponseDTO;
import dev.shor0s.logitrack.dto.response.UsuarioResponseDTO;
import dev.shor0s.logitrack.model.Categoria;
import dev.shor0s.logitrack.model.Rol;
import dev.shor0s.logitrack.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaResponseDTO entityToDto(Categoria entity) {
        if (entity == null) return null;
        return new CategoriaResponseDTO(
                entity.getIdCategoria(),
                entity.getNombre(),
                entity.getActivo()
        );
    }
}
