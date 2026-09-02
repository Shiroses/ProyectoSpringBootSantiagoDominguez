package dev.shor0s.logitrack.Controller;

import dev.shor0s.logitrack.dto.request.CatalogoRequestDTO;
import dev.shor0s.logitrack.dto.response.CatalogoResponseDTO;
import dev.shor0s.logitrack.service.CatalogoService;
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

@Tag(name = "Catálogo", description = "Procesa el CRUD del catálogo maestro de productos")
@RestController
//http://localhost:8080/api/v1/catalogo
@RequestMapping("/api/v1/catalogo")
@RequiredArgsConstructor
@Validated
public class CatalogoController {

    private final CatalogoService catalogoService;

    @Operation(summary = "Ingresa datos al catálogo", description = "Requiere un RequestBody o un JSON para ingresar información.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ítem de catálogo Creado exitosamente!"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos / body mal estructurado")
    })
    @PostMapping
    public ResponseEntity<CatalogoResponseDTO> crear(@Valid @RequestBody CatalogoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crear(dto));
    }

    @Operation(summary = "Obtiene todo el catálogo", description = "No requiere parámetro alguno")
    @GetMapping
    public ResponseEntity<List<CatalogoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(catalogoService.listarTodos());
    }

    @Operation(summary = "Obtiene solo los ítems de catálogo activos", description = "Filtra los ítems vigentes")
    @GetMapping("/activos")
    public ResponseEntity<List<CatalogoResponseDTO>> listarActivos() {
        return ResponseEntity.ok(catalogoService.listarActivos());
    }

    //http://localhost:8080/api/v1/catalogo/1
    @Operation(summary = "Obtiene un ítem de catálogo por ID", description = "Requiere el ID en la ruta URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ítem de catálogo encontrado"),
            @ApiResponse(responseCode = "404", description = "Ítem de catálogo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CatalogoResponseDTO> buscarPorId(
            @Parameter(description = "ID del ítem en catálogo", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(catalogoService.buscarPorId(id));
    }

    @Operation(summary = "Obtiene ítems del catálogo filtrados por nombre",
            description = "Requiere una variable de búsqueda de la forma http://localhost:8080/api/v1/catalogo/buscarPorNombre?nombre='Teclado'")
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<CatalogoResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre a filtrar", example = "Teclado")
            @RequestParam String nombre) {
        return ResponseEntity.ok(catalogoService.buscarPorNombre(nombre));
    }

    //http://localhost:8080/api/v1/catalogo/categoria/2
    @Operation(summary = "Obtiene ítems filtrados por categoría", description = "Requiere el ID de la categoría")
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<CatalogoResponseDTO>> listarPorCategoria(
            @Parameter(description = "ID de la categoría", example = "2")
            @PathVariable Integer idCategoria) {
        return ResponseEntity.ok(catalogoService.listarPorCategoria(idCategoria));
    }

    //http://localhost:8080/api/v1/catalogo/1
    @Operation(summary = "Actualiza los datos de un ítem del catálogo", description = "Requiere ID en la ruta y JSON con los datos actualizados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ítem de catálogo actualizado exitosamente!"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos"),
            @ApiResponse(responseCode = "404", description = "Ítem no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CatalogoResponseDTO> actualizar(
            @Parameter(description = "ID del ítem a actualizar", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody CatalogoRequestDTO dto) {
        return ResponseEntity.ok(catalogoService.actualizar(id, dto));
    }

    //http://localhost:8080/api/v1/catalogo/1
    @Operation(summary = "Desactiva un ítem del catálogo", description = "Aplica eliminación lógica pasando el ítem a inactivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ítem de catálogo desactivado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Ítem no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del ítem a desactivar", example = "1")
            @PathVariable Integer id) {
        catalogoService.desactivar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}