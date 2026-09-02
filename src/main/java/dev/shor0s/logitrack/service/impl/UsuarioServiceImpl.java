package dev.shor0s.logitrack.service.impl;

import dev.shor0s.logitrack.dto.request.UsuarioRequestDTO;
import dev.shor0s.logitrack.dto.response.RolResponseDTO;
import dev.shor0s.logitrack.dto.response.UsuarioResponseDTO;
import dev.shor0s.logitrack.mapper.RolMapper;
import dev.shor0s.logitrack.mapper.UsuarioMapper;
import dev.shor0s.logitrack.model.Rol;
import dev.shor0s.logitrack.model.Usuario;
import dev.shor0s.logitrack.repository.RolRepository;
import dev.shor0s.logitrack.repository.UsuarioRepository;
import dev.shor0s.logitrack.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final RolMapper rolMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              UsuarioMapper usuarioMapper,
                              RolMapper rolMapper) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioMapper = usuarioMapper;
        this.rolMapper = rolMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarActivos() {
        return usuarioRepository.findByActivoTrue().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarPorRol(Integer idRol) {
        if (!rolRepository.existsById(idRol)) {
            throw new EntityNotFoundException("El Rol con ID " + idRol + " no existe.");
        }
        return usuarioRepository.findByRol_IdRol(idRol).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return convertirAResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        Rol rol = rolRepository.findById(dto.idRol())
                .orElseThrow(() -> new EntityNotFoundException("No se puede crear el usuario. El Rol con ID " + dto.idRol() + " no existe."));

        Usuario usuario = usuarioMapper.dtoToEntity(dto, rol);
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);
        return convertirAResponseDTO(guardado);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizar(Integer id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se puede actualizar. Usuario no encontrado con ID: " + id));

        Rol rol = rolRepository.findById(dto.idRol())
                .orElseThrow(() -> new EntityNotFoundException("No se puede actualizar el usuario. El Rol con ID " + dto.idRol() + " no existe."));

        usuarioMapper.updateEntityFromDto(usuario, dto, rol);

        Usuario actualizado = usuarioRepository.save(usuario);
        return convertirAResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public void desactivar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    private UsuarioResponseDTO convertirAResponseDTO(Usuario usuario) {
        RolResponseDTO rolDTO = rolMapper.entityToDto(usuario.getRol());
        return usuarioMapper.entityToDto(usuario, rolDTO);
    }
}