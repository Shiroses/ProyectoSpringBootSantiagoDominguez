package dev.shor0s.logitrack.dto.response;

import java.math.BigDecimal;

public record CatalogoResponseDTO(
        Integer idCatalogo,
        CategoriaResponseDTO categoria,
        String nombre,
        BigDecimal precio,
        Boolean activo
) {}