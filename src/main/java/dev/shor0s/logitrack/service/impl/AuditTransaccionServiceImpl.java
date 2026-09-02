package dev.shor0s.logitrack.service.impl;

import dev.shor0s.logitrack.dto.request.AuditTransaccionRequestDTO;
import dev.shor0s.logitrack.dto.response.AuditTransaccionResponseDTO;
import dev.shor0s.logitrack.mapper.*;
import dev.shor0s.logitrack.model.AuditTransaccion;
import dev.shor0s.logitrack.model.Bodega;
import dev.shor0s.logitrack.model.Catalogo;
import dev.shor0s.logitrack.model.Usuario;
import dev.shor0s.logitrack.repository.AuditTransaccionRepository;
import dev.shor0s.logitrack.repository.BodegaRepository;
import dev.shor0s.logitrack.repository.CatalogoRepository;
import dev.shor0s.logitrack.repository.UsuarioRepository;
import dev.shor0s.logitrack.service.AuditTransaccionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditTransaccionServiceImpl implements AuditTransaccionService {

    private final AuditTransaccionRepository auditRepository;
    private final CatalogoRepository catalogoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaRepository bodegaRepository;

    private final CatalogoMapper catalogoMapper;
    private final CategoriaMapper categoriaMapper;
    private final UsuarioMapper usuarioMapper;
    private final RolMapper rolMapper;
    private final BodegaMapper bodegaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuditTransaccionResponseDTO> listarTodas() {
        return auditRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditTransaccionResponseDTO> listarPorCatalogo(Integer idCatalogo) {
        if (!catalogoRepository.existsById(idCatalogo)) {
            throw new EntityNotFoundException("El catálogo con ID " + idCatalogo + " no existe.");
        }
        return auditRepository.findByCatalogo_IdCatalogo(idCatalogo).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditTransaccionResponseDTO> listarPorUsuario(Integer idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new EntityNotFoundException("El usuario con ID " + idUsuario + " no existe.");
        }
        return auditRepository.findByUsuario_IdUsuario(idUsuario).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditTransaccionResponseDTO> listarPorBodega(Integer idBodega) {
        if (!bodegaRepository.existsById(idBodega)) {
            throw new EntityNotFoundException("La bodega con ID " + idBodega + " no existe.");
        }
        return auditRepository.findByBodega_IdBodega(idBodega).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuditTransaccionResponseDTO buscarPorId(Long id) {
        AuditTransaccion audit = auditRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Auditoría no encontrada con ID: " + id));
        return convertirAResponseDTO(audit);
    }

    @Override
    @Transactional
    public AuditTransaccionResponseDTO registrar(AuditTransaccionRequestDTO dto) {
        Catalogo catalogo = catalogoRepository.findById(dto.idCatalogo())
                .orElseThrow(() -> new EntityNotFoundException("No se puede auditar. El catálogo con ID " + dto.idCatalogo() + " no existe."));

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new EntityNotFoundException("No se puede auditar. El usuario con ID " + dto.idUsuario() + " no existe."));

        Bodega bodega = bodegaRepository.findById(dto.idBodega())
                .orElseThrow(() -> new EntityNotFoundException("No se puede auditar. La bodega con ID " + dto.idBodega() + " no existe."));

        AuditTransaccion audit = AuditTransaccion.builder()
                .catalogo(catalogo)
                .usuario(usuario)
                .bodega(bodega)
                .tipo(dto.tipo())
                .mensaje(dto.mensaje())
                .build();

        AuditTransaccion guardado = auditRepository.save(audit);
        return convertirAResponseDTO(guardado);
    }

    private AuditTransaccionResponseDTO convertirAResponseDTO(AuditTransaccion audit) {
        var catalogoDTO = catalogoMapper.entityToDto(
                audit.getCatalogo(),
                categoriaMapper.entityToDto(audit.getCatalogo().getCategoria())
        );

        var usuarioDTO = usuarioMapper.entityToDto(
                audit.getUsuario(),
                rolMapper.entityToDto(audit.getUsuario().getRol())
        );

        var bodegaDTO = bodegaMapper.entityToDto(
                audit.getBodega(),
                usuarioMapper.entityToDto(
                        audit.getBodega().getEncargado(),
                        rolMapper.entityToDto(audit.getBodega().getEncargado().getRol())
                )
        );

        return new AuditTransaccionResponseDTO(
                audit.getIdAuditTransaccion(),
                catalogoDTO,
                usuarioDTO,
                bodegaDTO,
                audit.getTipo(),
                audit.getMensaje(),
                audit.getFecha()
        );
    }
}