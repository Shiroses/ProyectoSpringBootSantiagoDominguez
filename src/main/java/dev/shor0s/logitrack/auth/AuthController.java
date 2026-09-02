package dev.shor0s.logitrack.auth;

import dev.shor0s.logitrack.config.JwtService;
import dev.shor0s.logitrack.exceptions.BusinessRuleException;
import dev.shor0s.logitrack.model.Usuario;
import dev.shor0s.logitrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {

        // 1. Buscar usuario por el nombre ingresado
        Usuario usuario = usuarioRepository.findByNombre(request.username())
                .orElseThrow(() -> new BusinessRuleException("Credenciales inválidas"));

        // 2. Verificar estado activo
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new BusinessRuleException("El usuario se encuentra inactivo");
        }

        // 3. Comparación directa en texto plano
        if (!request.password().equals(usuario.getContrasenia())) {
            throw new BusinessRuleException("Credenciales inválidas");
        }

        // 4. Generar Token JWT
        String token = jwtService.generateToken(usuario.getNombre());

        // 5. Devolver JSON estructurado con token, rol e idUsuario
        return Map.of(
                "token", token,
                "role", usuario.getRol().getNombre(),
                "idUsuario", usuario.getIdUsuario()
        );
    }
}