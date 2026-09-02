package dev.shor0s.logitrack.Controller;

import dev.shor0s.logitrack.dto.request.ProductoRequestDTO;
import dev.shor0s.logitrack.dto.response.ProductoDetalleResponseDTO;
import dev.shor0s.logitrack.service.ProductoService;
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

@Tag(name = "Producto (Stock en Bodega)", description = "Procesa el CRUD e inventario de productos por bodega (Clave Compuesta)")
@RestController
//http://localhost:8080/api/v1/productos
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Validated
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Asigna o ingresa stock de un producto a una bodega", description = "Requiere un RequestBody o JSON con idCatalogo, idBodega y stock.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto asignado a bodega exitosamente!"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos / body mal estructurado")
    })
    @PostMapping
    public ResponseEntity<ProductoDetalleResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(dto));
    }

    @Operation(summary = "Obtiene la lista completa de stock de productos en bodegas", description = "No requiere parámetro alguno")
    @GetMapping
    public ResponseEntity<List<ProductoDetalleResponseDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    //http://localhost:8080/api/v1/productos/bodega/1
    @Operation(summary = "Obtiene el inventario completo de una bodega específica", description = "Requiere el ID de la bodega")
    @GetMapping("/bodega/{idBodega}")
    public ResponseEntity<List<ProductoDetalleResponseDTO>> listarPorBodega(
            @Parameter(description = "ID de la bodega", example = "1")
            @PathVariable Integer idBodega) {
        return ResponseEntity.ok(productoService.listarPorBodega(idBodega));
    }

    //http://localhost:8080/api/v1/productos/catalogo/2
    @Operation(summary = "Obtiene la distribución en bodegas de un ítem de catálogo", description = "Requiere el ID del ítem de catálogo")
    @GetMapping("/catalogo/{idCatalogo}")
    public ResponseEntity<List<ProductoDetalleResponseDTO>> listarPorCatalogo(
            @Parameter(description = "ID del ítem en catálogo", example = "2")
            @PathVariable Integer idCatalogo) {
        return ResponseEntity.ok(productoService.listarPorCatalogo(idCatalogo));
    }

    @Operation(summary = "Obtiene productos con stock menor a un umbral",
            description = "Requiere parámetro de búsqueda opcional. Ejemplo: http://localhost:8080/api/v1/productos/stockBajo?limite=5")
    @GetMapping("/stockBajo")
    public ResponseEntity<List<ProductoDetalleResponseDTO>> listarProductosConStockBajo(
            @Parameter(description = "Límite superior de stock para filtrar", example = "5")
            @RequestParam(defaultValue = "5") Integer limite) {
        return ResponseEntity.ok(productoService.listarProductosConStockBajo(limite));
    }

    //http://localhost:8080/api/v1/productos/catalogo/1/bodega/2
    @Operation(summary = "Obtiene el detalle de stock por clave compuesta (Catálogo - Bodega)", description = "Requiere idCatalogo e idBodega en la URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de stock encontrado"),
            @ApiResponse(responseCode = "404", description = "Registro de stock no encontrado")
    })
    @GetMapping("/catalogo/{idCatalogo}/bodega/{idBodega}")
    public ResponseEntity<ProductoDetalleResponseDTO> buscarPorId(
            @Parameter(description = "ID del catálogo", example = "1") @PathVariable Integer idCatalogo,
            @Parameter(description = "ID de la bodega", example = "2") @PathVariable Integer idBodega) {
        return ResponseEntity.ok(productoService.buscarPorId(idCatalogo, idBodega));
    }

    //http://localhost:8080/api/v1/productos/catalogo/1/bodega/2
    @Operation(summary = "Actualiza la cantidad de stock de un producto en bodega", description = "Requiere los IDs compuestos en la URL y el JSON con la nueva cantidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente!"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @PutMapping("/catalogo/{idCatalogo}/bodega/{idBodega}")
    public ResponseEntity<ProductoDetalleResponseDTO> actualizarStock(
            @Parameter(description = "ID del catálogo", example = "1") @PathVariable Integer idCatalogo,
            @Parameter(description = "ID de la bodega", example = "2") @PathVariable Integer idBodega,
            @Valid @RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.ok(productoService.actualizarStock(idCatalogo, idBodega, dto));
    }

    //http://localhost:8080/api/v1/productos/catalogo/1/bodega/2
    @Operation(summary = "Elimina un producto de una bodega", description = "Eliminación física del registro de stock en esa bodega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado de la bodega exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @DeleteMapping("/catalogo/{idCatalogo}/bodega/{idBodega}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del catálogo", example = "1") @PathVariable Integer idCatalogo,
            @Parameter(description = "ID de la bodega", example = "2") @PathVariable Integer idBodega) {
        productoService.eliminar(idCatalogo, idBodega);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}