package dev.shor0s.logitrack.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuditGeneralResponseDTO(
        Long idAuditGeneral,
        CatalogoResponseDTO catalogo,
        BodegaResponseDTO bodega,
        CategoriaResponseDTO categoria,
        String tipo,
        String mensaje,
        LocalDateTime fecha
) {}