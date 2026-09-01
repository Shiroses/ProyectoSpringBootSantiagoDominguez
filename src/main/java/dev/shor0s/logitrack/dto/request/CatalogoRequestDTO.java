package dev.shor0s.logitrack.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CatalogoRequestDTO(

        @NotNull(message = "El ID de la categoría es obligatorio")
        Integer idCategoria,

        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(max = 150, message = "El nombre no puede exceder los 150 caracteres")
        String nombre,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El precio debe ser un valor mayor o igual a 0")
        @Digits(integer = 8, fraction = 2, message = "El precio debe tener como máximo 8 dígitos enteros y 2 decimales")
        BigDecimal precio,

        @NotNull(message = "El estado activo/inactivo es obligatorio")
        Boolean activo

) {}