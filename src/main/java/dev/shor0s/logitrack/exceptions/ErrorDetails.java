package dev.shor0s.logitrack.exceptions;

import java.time.LocalDateTime;

public record ErrorDetails(
        LocalDateTime timestamp,
        String message,
        String path
) {}
