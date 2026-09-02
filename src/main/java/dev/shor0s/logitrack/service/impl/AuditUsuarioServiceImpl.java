package dev.shor0s.logitrack.service.impl;

import dev.shor0s.logitrack.dto.response.AuditUsuarioResponseDTO;
import dev.shor0s.logitrack.mapper.RolMapper;
import dev.shor0s.logitrack.mapper.UsuarioMapper;
import dev.shor0s.logitrack.model.AuditUsuario;
import dev.shor0s.logitrack.repository.AuditUsuarioRepository;
import dev.shor0s.logitrack.repository.UsuarioRepository;
import dev.shor0s.logitrack.service.AuditUsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditUsuarioServiceImpl implements AuditUsuarioService {

    private final AuditUsuarioRepository auditUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RolMapper rolMapper;

    @Override
    public List<AuditUsuarioResponseDTO> listarTodos() {
        return auditUsuarioRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public List<AuditUsuarioResponseDTO> listarPorUsuario(Integer idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new EntityNotFoundException("El usuario con ID " + idUsuario + " no existe.");
        }
        return auditUsuarioRepository.findByUsuario_IdUsuario(idUsuario).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public List<AuditUsuarioResponseDTO> listarPorTipo(String tipo) {
        return auditUsuarioRepository.findByTipo(tipo).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public AuditUsuarioResponseDTO buscarPorId(Long id) {
        AuditUsuario audit = auditUsuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de auditoría de usuario no encontrado con ID: " + id));
        return convertirAResponseDTO(audit);
    }

    private AuditUsuarioResponseDTO convertirAResponseDTO(AuditUsuario audit) {
        var usuarioDTO = usuarioMapper.entityToDto(
                audit.getUsuario(),
                rolMapper.entityToDto(audit.getUsuario().getRol())
        );

        return new AuditUsuarioResponseDTO(
                audit.getIdAudUsuario(),
                usuarioDTO,
                audit.getTipo(),
                audit.getMensaje(),
                audit.getFecha()
        );
    }
}