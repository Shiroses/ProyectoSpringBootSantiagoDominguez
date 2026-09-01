package dev.shor0s.logitrack.dto.response;

public record UsuarioResponseDTO(
        Integer idUsuario,
        RolResponseDTO rol,
        String nombre,
        Boolean activo
) {}