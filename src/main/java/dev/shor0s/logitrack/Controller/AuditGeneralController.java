package dev.shor0s.logitrack.Controller;

import dev.shor0s.logitrack.dto.response.AuditGeneralResponseDTO;
import dev.shor0s.logitrack.service.AuditGeneralService;
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

@Tag(name = "Auditoría General (Solo Lectura)", description = "Consulta del historial de auditoría de bodegas, categorías y catálogo (administrado por BD)")
@RestController
//http://localhost:8080/api/v1/auditoria-general
@RequestMapping("/api/v1/auditoria-general")
@RequiredArgsConstructor
@Validated
public class AuditGeneralController {

    private final AuditGeneralService auditGeneralService;

    @Operation(summary = "Obtiene todos los registros de auditoría general", description = "No requiere parámetro alguno")
    @GetMapping
    public ResponseEntity<List<AuditGeneralResponseDTO>> listarTodos() {
        return ResponseEntity.ok(auditGeneralService.listarTodos());
    }

    //http://localhost:8080/api/v1/auditoria-general/1
    @Operation(summary = "Obtiene un registro de auditoría general por ID", description = "Requiere el ID bigint en la URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de auditoría encontrado"),
            @ApiResponse(responseCode = "404", description = "Registro de auditoría no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuditGeneralResponseDTO> buscarPorId(
            @Parameter(description = "ID del registro de auditoría general", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(auditGeneralService.buscarPorId(id));
    }

    //http://localhost:8080/api/v1/auditoria-general/catalogo/2
    @Operation(summary = "Obtiene auditorías asociadas a un catálogo", description = "Requiere el ID del catálogo")
    @GetMapping("/catalogo/{idCatalogo}")
    public ResponseEntity<List<AuditGeneralResponseDTO>> listarPorCatalogo(
            @Parameter(description = "ID del catálogo", example = "2")
            @PathVariable Integer idCatalogo) {
        return ResponseEntity.ok(auditGeneralService.listarPorCatalogo(idCatalogo));
    }

    //http://localhost:8080/api/v1/auditoria-general/bodega/1
    @Operation(summary = "Obtiene auditorías asociadas a una bodega", description = "Requiere el ID de la bodega")
    @GetMapping("/bodega/{idBodega}")
    public ResponseEntity<List<AuditGeneralResponseDTO>> listarPorBodega(
            @Parameter(description = "ID de la bodega", example = "1")
            @PathVariable Integer idBodega) {
        return ResponseEntity.ok(auditGeneralService.listarPorBodega(idBodega));
    }

    //http://localhost:8080/api/v1/auditoria-general/categoria/3
    @Operation(summary = "Obtiene auditorías asociadas a una categoría", description = "Requiere el ID de la categoría")
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<AuditGeneralResponseDTO>> listarPorCategoria(
            @Parameter(description = "ID de la categoría", example = "3")
            @PathVariable Integer idCategoria) {
        return ResponseEntity.ok(auditGeneralService.listarPorCategoria(idCategoria));
    }

    @Operation(summary = "Obtiene los registros por tipo de operación",
            description = "Ejemplo: http://localhost:8080/api/v1/auditoria-general/buscarPorTipo?tipo=DELETE")
    @GetMapping("/buscarPorTipo")
    public ResponseEntity<List<AuditGeneralResponseDTO>> listarPorTipo(
            @Parameter(description = "Tipo de operación auditada", example = "DELETE")
            @RequestParam String tipo) {
        return ResponseEntity.ok(auditGeneralService.listarPorTipo(tipo));
    }
}