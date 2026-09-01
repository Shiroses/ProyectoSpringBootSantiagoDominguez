package dev.shor0s.logitrack.dto.response;

public record BodegaResponseDTO(
        Integer idBodega,
        String nombre,
        String ubicacion,
        Integer capacidad,
        Integer capacidadActual,
        UsuarioResponseDTO encargado,
        Boolean activo
) {}