package dev.shor0s.logitrack.Controller;

import dev.shor0s.logitrack.dto.request.BodegaRequestDTO;
import dev.shor0s.logitrack.dto.response.BodegaResponseDTO;
import dev.shor0s.logitrack.service.BodegaService;
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

@Tag(name = "Bodega", description = "Procesa el CRUD de bodegas físicas")
@RestController
//http://localhost:8080/api/v1/bodegas
@RequestMapping("/api/v1/bodegas")
@RequiredArgsConstructor
@Validated
public class BodegaController {

    private final BodegaService bodegaService;

    @Operation(summary = "Ingresa datos de bodega", description = "Requiere un RequestBody o un JSON para ingresar información.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bodega Creada exitosamente!"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos / body mal estructurado")
    })
    @PostMapping
    public ResponseEntity<BodegaResponseDTO> crear(@Valid @RequestBody BodegaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaService.crear(dto));
    }

    @Operation(summary = "Obtiene todas las bodegas", description = "No requiere parámetro alguno")
    @GetMapping
    public ResponseEntity<List<BodegaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(bodegaService.listarTodas());
    }

    @Operation(summary = "Obtiene solo las bodegas activas", description = "Filtra únicamente las bodegas habilitadas")
    @GetMapping("/activas")
    public ResponseEntity<List<BodegaResponseDTO>> listarActivas() {
        return ResponseEntity.ok(bodegaService.listarActivas());
    }

    //http://localhost:8080/api/v1/bodegas/1
    @Operation(summary = "Obtiene una bodega por ID", description = "Requiere el ID en la ruta URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega encontrada"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponseDTO> buscarPorId(
            @Parameter(description = "ID de la bodega a buscar", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(bodegaService.buscarPorId(id));
    }

    @Operation(summary = "Obtiene las bodegas filtradas por nombre",
            description = "Requiere una variable de búsqueda de la forma http://localhost:8080/api/v1/bodegas/buscarPorNombre?nombre='Bodega Central'")
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<BodegaResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre a filtrar", example = "Bodega Central")
            @RequestParam String nombre) {
        return ResponseEntity.ok(bodegaService.buscarPorNombre(nombre));
    }

    //http://localhost:8080/api/v1/bodegas/encargado/3
    @Operation(summary = "Obtiene bodegas asignadas a un encargado", description = "Requiere el ID del encargado")
    @GetMapping("/encargado/{idEncargado}")
    public ResponseEntity<List<BodegaResponseDTO>> buscarPorEncargado(
            @Parameter(description = "ID del usuario encargado", example = "3")
            @PathVariable Integer idEncargado) {
        return ResponseEntity.ok(bodegaService.buscarPorEncargado(idEncargado));
    }

    //http://localhost:8080/api/v1/bodegas/1
    @Operation(summary = "Actualiza los datos de una bodega", description = "Requiere ID en la ruta y JSON con los datos actualizados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega actualizada exitosamente!"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponseDTO> actualizar(
            @Parameter(description = "ID de la bodega a actualizar", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody BodegaRequestDTO dto) {
        return ResponseEntity.ok(bodegaService.actualizar(id, dto));
    }

    //http://localhost:8080/api/v1/bodegas/1
    @Operation(summary = "Desactiva una bodega", description = "Aplica eliminación lógica pasando la bodega a inactiva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bodega desactivada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID de la bodega a desactivar", example = "1")
            @PathVariable Integer id) {
        bodegaService.desactivar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}