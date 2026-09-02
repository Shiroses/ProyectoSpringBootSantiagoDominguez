package dev.shor0s.logitrack.dto.response;

import java.time.LocalDateTime;

public record AuditUsuarioResponseDTO(
        Long idAudUsuario,
        UsuarioResponseDTO usuario,
        String tipo,
        String mensaje,
        LocalDateTime fecha
) {}