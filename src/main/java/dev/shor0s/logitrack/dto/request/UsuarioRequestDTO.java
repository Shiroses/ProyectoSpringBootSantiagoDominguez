package dev.shor0s.logitrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @NotNull(message = "El ID del rol es obligatorio")
        Integer idRol,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
        String nombre,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 4, max = 16, message = "La contraseña debe tener entre 4 y 16 caracteres")
        String contrasenia,

        @NotNull(message = "El estado activo/inactivo es obligatorio")
        Boolean activo

) {}
