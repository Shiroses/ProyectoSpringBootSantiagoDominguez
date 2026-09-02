package dev.shor0s.logitrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuditTransaccionRequestDTO(
        @NotNull(message = "El ID del catálogo es obligatorio")
        Integer idCatalogo,

        @NotNull(message = "El ID del usuario es obligatorio")
        Integer idUsuario,

        @NotNull(message = "El ID de la bodega es obligatorio")
        Integer idBodega,

        @NotBlank(message = "El tipo de transacción es obligatorio")
        @Size(max = 50, message = "El tipo no puede superar 50 caracteres")
        String tipo,

        @NotBlank(message = "El mensaje es obligatorio")
        @Size(max = 250, message = "El mensaje no puede superar 250 caracteres")
        String mensaje
) {}