package dev.shor0s.logitrack.dto.response;

public record ProductoDetalleResponseDTO(
        Integer stock,
        CatalogoResponseDTO catalogo,
        BodegaResponseDTO bodega
) {}
