package dev.shor0s.logitrack.Controller;

import dev.shor0s.logitrack.dto.response.AuditUsuarioResponseDTO;
import dev.shor0s.logitrack.service.AuditUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Auditoría de Usuarios (Solo Lectura)", description = "Consulta del historial de auditoría de cambios en usuarios (administrado por BD)")
@RestController
//http://localhost:8080/api/v1/auditoria-usuarios
@RequestMapping("/api/v1/auditoria-usuarios")
@RequiredArgsConstructor
@Validated
public class AuditUsuarioController {

    private final AuditUsuarioService auditUsuarioService;

    @Operation(summary = "Obtiene todos los registros de auditoría de usuarios", description = "No requiere parámetro alguno")
    @GetMapping
    public ResponseEntity<List<AuditUsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(auditUsuarioService.listarTodos());
    }

    //http://localhost:8080/api/v1/auditoria-usuarios/1
    @Operation(summary = "Obtiene un registro de auditoría de usuario por su ID", description = "Requiere el ID bigint en la URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de auditoría encontrado"),
            @ApiResponse(responseCode = "404", description = "Registro de auditoría no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuditUsuarioResponseDTO> buscarPorId(
            @Parameter(description = "ID del registro de auditoría", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(auditUsuarioService.buscarPorId(id));
    }

    //http://localhost:8080/api/v1/auditoria-usuarios/usuario/2
    @Operation(summary = "Obtiene los registros de auditoría asociados a un usuario", description = "Requiere el ID del usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AuditUsuarioResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID del usuario", example = "2")
            @PathVariable Integer idUsuario) {
        return ResponseEntity.ok(auditUsuarioService.listarPorUsuario(idUsuario));
    }

    @Operation(summary = "Obtiene los registros de auditoría por tipo de operación",
            description = "Ejemplo: http://localhost:8080/api/v1/auditoria-usuarios/buscarPorTipo?tipo=UPDATE")
    @GetMapping("/buscarPorTipo")
    public ResponseEntity<List<AuditUsuarioResponseDTO>> listarPorTipo(
            @Parameter(description = "Tipo de operación auditada", example = "UPDATE")
            @RequestParam String tipo) {
        return ResponseEntity.ok(auditUsuarioService.listarPorTipo(tipo));
    }
}