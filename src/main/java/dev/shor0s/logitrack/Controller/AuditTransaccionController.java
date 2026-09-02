package dev.shor0s.logitrack.Controller;

import dev.shor0s.logitrack.dto.request.AuditTransaccionRequestDTO;
import dev.shor0s.logitrack.dto.response.AuditTransaccionResponseDTO;
import dev.shor0s.logitrack.service.AuditTransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Auditoría de Transacciones", description = "Procesa el registro y consulta del historial de auditoría de transacciones")
@RestController
//http://localhost:8080/api/v1/auditoria-transacciones
@RequestMapping("/api/v1/auditoria-transacciones")
@RequiredArgsConstructor
@Validated
public class AuditTransaccionController {

    private final AuditTransaccionService auditService;

    @Operation(summary = "Registra una nueva transacción de auditoría", description = "Requiere un RequestBody o un JSON con los IDs relacionales, tipo y mensaje.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción auditada exitosamente!"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos / body mal estructurado"),
            @ApiResponse(responseCode = "404", description = "Alguna de las entidades relacionadas no existe")
    })
    @PostMapping
    public ResponseEntity<AuditTransaccionResponseDTO> registrar(@Valid @RequestBody AuditTransaccionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditService.registrar(dto));
    }

    @Operation(summary = "Obtiene todas las transacciones auditadas", description = "No requiere parámetro alguno")
    @GetMapping
    public ResponseEntity<List<AuditTransaccionResponseDTO>> listarTodas() {
        return ResponseEntity.ok(auditService.listarTodas());
    }

    //http://localhost:8080/api/v1/auditoria-transacciones/1
    @Operation(summary = "Obtiene un registro de auditoría por su ID de transacción", description = "Requiere el ID bigint en la URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de auditoría encontrado"),
            @ApiResponse(responseCode = "404", description = "Registro de auditoría no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuditTransaccionResponseDTO> buscarPorId(
            @Parameter(description = "ID de la transacción auditada", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(auditService.buscarPorId(id));
    }

    //http://localhost:8080/api/v1/auditoria-transacciones/catalogo/2
    @Operation(summary = "Obtiene los registros de auditoría filtrados por ítem de catálogo", description = "Requiere el ID del catálogo")
    @GetMapping("/catalogo/{idCatalogo}")
    public ResponseEntity<List<AuditTransaccionResponseDTO>> listarPorCatalogo(
            @Parameter(description = "ID del ítem en catálogo", example = "2")
            @PathVariable Integer idCatalogo) {
        return ResponseEntity.ok(auditService.listarPorCatalogo(idCatalogo));
    }

    //http://localhost:8080/api/v1/auditoria-transacciones/usuario/1
    @Operation(summary = "Obtiene los registros de auditoría de un usuario específico", description = "Requiere el ID del usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AuditTransaccionResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Integer idUsuario) {
        return ResponseEntity.ok(auditService.listarPorUsuario(idUsuario));
    }

    //http://localhost:8080/api/v1/auditoria-transacciones/bodega/3
    @Operation(summary = "Obtiene los registros de auditoría asociados a una bodega", description = "Requiere el ID de la bodega")
    @GetMapping("/bodega/{idBodega}")
    public ResponseEntity<List<AuditTransaccionResponseDTO>> listarPorBodega(
            @Parameter(description = "ID de la bodega", example = "3")
            @PathVariable Integer idBodega) {
        return ResponseEntity.ok(auditService.listarPorBodega(idBodega));
    }
}