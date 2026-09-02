package dev.shor0s.logitrack.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp, int status, String message, String errorCode
) {
}