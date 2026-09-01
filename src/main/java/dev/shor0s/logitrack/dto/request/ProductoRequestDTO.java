package dev.shor0s.logitrack.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductoRequestDTO(

        @NotNull(message = "El ID del catálogo es obligatorio")
        Integer idCatalogo,

        @NotNull(message = "El ID de la bodega es obligatorio")
        Integer idBodega,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock debe ser un valor mayor o igual a 0")
        Integer stock

) {}