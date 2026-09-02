package dev.shor0s.logitrack.Controller;

import dev.shor0s.logitrack.dto.request.UsuarioRequestDTO;
import dev.shor0s.logitrack.dto.response.UsuarioResponseDTO;
import dev.shor0s.logitrack.service.UsuarioService;
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

@Tag(name = "Usuario", description = "Procesa el CRUD de usuarios")
@RestController
//http://localhost:8080/api/v1/usuarios
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Ingresa datos de usuarios", description = "Requiere un RequestBody o un JSON para ingresar información.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario Creado exitosamente!"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos / body mal estructurado")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @Operation(summary = "Obtiene todos los usuarios", description = "No requiere parámetro alguno")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @Operation(summary = "Obtiene solo los usuarios activos", description = "Filtra únicamente los registros en estado activo")
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarActivos() {
        return ResponseEntity.ok(usuarioService.listarActivos());
    }

    //http://localhost:8080/api/v1/usuarios/1
    @Operation(summary = "Obtiene un usuario por ID", description = "Requiere el ID en la ruta URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @Parameter(description = "ID del usuario a buscar", example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(summary = "Obtiene los usuarios filtrados por nombre",
            description = "Requiere una variable de búsqueda de la forma http://localhost:8080/api/v1/usuarios/buscarPorNombre?nombre='Carlos'")
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNombre(
            @Parameter(description = "Nombre a filtrar", example = "Carlos")
            @RequestParam String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    //http://localhost:8080/api/v1/usuarios/rol/2
    @Operation(summary = "Obtiene los usuarios filtrados por rol", description = "Requiere el ID del rol")
    @GetMapping("/rol/{idRol}")
    public ResponseEntity<List<UsuarioResponseDTO>> listarPorRol(
            @Parameter(description = "ID del rol", example = "2")
            @PathVariable Integer idRol) {
        return ResponseEntity.ok(usuarioService.listarPorRol(idRol));
    }

    //http://localhost:8080/api/v1/usuarios/1
    @Operation(summary = "Actualiza los datos de un usuario", description = "Requiere ID en la ruta y JSON con los datos actualizados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente!"),
            @ApiResponse(responseCode = "400", description = "Datos no válidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    //http://localhost:8080/api/v1/usuarios/1
    @Operation(summary = "Desactiva un usuario", description = "Aplica eliminación lógica pasando el estado a inactivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario desactivado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del usuario a desactivar", example = "1")
            @PathVariable Integer id) {
        usuarioService.desactivar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}