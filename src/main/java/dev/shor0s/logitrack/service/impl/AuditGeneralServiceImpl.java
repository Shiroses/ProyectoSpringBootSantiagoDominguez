package dev.shor0s.logitrack.service.impl;

import dev.shor0s.logitrack.dto.response.AuditGeneralResponseDTO;
import dev.shor0s.logitrack.dto.response.BodegaResponseDTO;
import dev.shor0s.logitrack.dto.response.CatalogoResponseDTO;
import dev.shor0s.logitrack.dto.response.CategoriaResponseDTO;
import dev.shor0s.logitrack.mapper.*;
import dev.shor0s.logitrack.model.AuditGeneral;
import dev.shor0s.logitrack.repository.AuditGeneralRepository;
import dev.shor0s.logitrack.repository.BodegaRepository;
import dev.shor0s.logitrack.repository.CatalogoRepository;
import dev.shor0s.logitrack.repository.CategoriaRepository;
import dev.shor0s.logitrack.service.AuditGeneralService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditGeneralServiceImpl implements AuditGeneralService {

    private final AuditGeneralRepository auditGeneralRepository;
    private final CatalogoRepository catalogoRepository;
    private final BodegaRepository bodegaRepository;
    private final CategoriaRepository categoriaRepository;

    private final CatalogoMapper catalogoMapper;
    private final CategoriaMapper categoriaMapper;
    private final BodegaMapper bodegaMapper;
    private final UsuarioMapper usuarioMapper;
    private final RolMapper rolMapper;

    @Override
    public List<AuditGeneralResponseDTO> listarTodos() {
        return auditGeneralRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public List<AuditGeneralResponseDTO> listarPorCatalogo(Integer idCatalogo) {
        if (!catalogoRepository.existsById(idCatalogo)) {
            throw new EntityNotFoundException("El catálogo con ID " + idCatalogo + " no existe.");
        }
        return auditGeneralRepository.findByCatalogo_IdCatalogo(idCatalogo).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public List<AuditGeneralResponseDTO> listarPorBodega(Integer idBodega) {
        if (!bodegaRepository.existsById(idBodega)) {
            throw new EntityNotFoundException("La bodega con ID " + idBodega + " no existe.");
        }
        return auditGeneralRepository.findByBodega_IdBodega(idBodega).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public List<AuditGeneralResponseDTO> listarPorCategoria(Integer idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new EntityNotFoundException("La categoría con ID " + idCategoria + " no existe.");
        }
        return auditGeneralRepository.findByCategoria_IdCategoria(idCategoria).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public List<AuditGeneralResponseDTO> listarPorTipo(String tipo) {
        return auditGeneralRepository.findByTipo(tipo).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public AuditGeneralResponseDTO buscarPorId(Long id) {
        AuditGeneral audit = auditGeneralRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de auditoría general no encontrado con ID: " + id));
        return convertirAResponseDTO(audit);
    }

    private AuditGeneralResponseDTO convertirAResponseDTO(AuditGeneral audit) {
        CatalogoResponseDTO catalogoDTO = null;
        if (audit.getCatalogo() != null) {
            catalogoDTO = catalogoMapper.entityToDto(
                    audit.getCatalogo(),
                    categoriaMapper.entityToDto(audit.getCatalogo().getCategoria())
            );
        }

        BodegaResponseDTO bodegaDTO = null;
        if (audit.getBodega() != null) {
            bodegaDTO = bodegaMapper.entityToDto(
                    audit.getBodega(),
                    usuarioMapper.entityToDto(
                            audit.getBodega().getEncargado(),
                            rolMapper.entityToDto(audit.getBodega().getEncargado().getRol())
                    )
            );
        }

        CategoriaResponseDTO categoriaDTO = null;
        if (audit.getCategoria() != null) {
            categoriaDTO = categoriaMapper.entityToDto(audit.getCategoria());
        }

        return new AuditGeneralResponseDTO(
                audit.getIdAuditGeneral(),
                catalogoDTO,
                bodegaDTO,
                categoriaDTO,
                audit.getTipo(),
                audit.getMensaje(),
                audit.getFecha()
        );
    }
}