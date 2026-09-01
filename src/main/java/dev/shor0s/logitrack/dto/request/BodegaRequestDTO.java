package dev.shor0s.logitrack.dto.request;

import jakarta.validation.constraints.*;

public record BodegaRequestDTO(

        @NotBlank(message = "El nombre de la bodega es obligatorio")
        @Size(max = 150, message = "El nombre no puede exceder los 150 caracteres")
        String nombre,

        @NotBlank(message = "La ubicación es obligatoria")
        @Size(max = 150, message = "La ubicación no puede exceder los 150 caracteres")
        String ubicacion,

        @NotNull(message = "La capacidad máxima es obligatoria")
        @Min(value = 0, message = "La capacidad máxima debe ser un valor mayor o igual a 0")
        Integer capacidad,

        @NotNull(message = "La capacidad actual es obligatoria")
        @Min(value = 0, message = "La capacidad actual debe ser un valor mayor o igual a 0")
        Integer capacidadActual,

        @NotNull(message = "El ID del encargado es obligatorio")
        Integer idEncargado,

        @NotNull(message = "El estado activo/inactivo es obligatorio")
        Boolean activo

) {}
