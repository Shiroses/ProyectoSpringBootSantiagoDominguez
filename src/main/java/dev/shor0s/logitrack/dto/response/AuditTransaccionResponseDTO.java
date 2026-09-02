package dev.shor0s.logitrack.dto.response;

import java.time.LocalDateTime;

public record AuditTransaccionResponseDTO(
        Long idAuditTransaccion,
        CatalogoResponseDTO catalogo,
        UsuarioResponseDTO usuario,
        BodegaResponseDTO bodega,
        String tipo,
        String mensaje,
        LocalDateTime fecha
) {}