package dev.shor0s.logitrack.mapper;

import dev.shor0s.logitrack.dto.request.UsuarioRequestDTO;
import dev.shor0s.logitrack.dto.response.RolResponseDTO;
import dev.shor0s.logitrack.dto.response.UsuarioResponseDTO;
import dev.shor0s.logitrack.model.Rol;
import dev.shor0s.logitrack.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO entityToDto(Usuario entity, RolResponseDTO rol) {
        if (entity == null) return null;
        return new UsuarioResponseDTO(
                entity.getIdUsuario(),
                rol,
                entity.getNombre(),
                entity.getActivo()
        );
    }

    public Usuario dtoToEntity(UsuarioRequestDTO dto, Rol rol) {
        if (dto == null) return null;
        Usuario entity = new Usuario();
        entity.setNombre(dto.nombre());
        entity.setContrasenia(dto.contrasenia());
        entity.setRol(rol);
        return entity;
    }

    public void updateEntityFromDto(Usuario entity, UsuarioRequestDTO dto, Rol rol) {
        if (entity == null || dto == null) return;
        entity.setNombre(dto.nombre());
        if (dto.contrasenia() != null && !dto.contrasenia().isBlank()) {
            entity.setContrasenia(dto.contrasenia());
        }
        entity.setRol(rol);
    }
}
