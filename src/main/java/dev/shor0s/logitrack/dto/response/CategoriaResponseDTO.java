package dev.shor0s.logitrack.dto.response;

public record CategoriaResponseDTO(
        Integer idCategoria,
        String nombre,
        Boolean activo
) {}