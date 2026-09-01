package dev.shor0s.logitrack.service.impl;

import dev.shor0s.logitrack.dto.request.BodegaRequestDTO;
import dev.shor0s.logitrack.dto.response.BodegaResponseDTO;
import dev.shor0s.logitrack.dto.response.RolResponseDTO;
import dev.shor0s.logitrack.dto.response.UsuarioResponseDTO;
import dev.shor0s.logitrack.exceptions.ResourceNotFoundException;
import dev.shor0s.logitrack.mapper.BodegaMapper;
import dev.shor0s.logitrack.mapper.RolMapper;
import dev.shor0s.logitrack.mapper.UsuarioMapper;
import dev.shor0s.logitrack.model.Bodega;
import dev.shor0s.logitrack.model.Usuario;
import dev.shor0s.logitrack.repository.BodegaRepository;
import dev.shor0s.logitrack.repository.UsuarioRepository;
import dev.shor0s.logitrack.service.BodegaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaMapper bodegaMapper;
    private final UsuarioMapper usuarioMapper;
    private final RolMapper rolMapper;
public BodegaServiceImpl(BodegaRepository bodegaRepository,
                         UsuarioRepository usuarioRepository,
                         BodegaMapper bodegaMapper,
                         UsuarioMapper usuarioMapper,
                         RolMapper rolMapper) {
    this.bodegaRepository = bodegaRepository;
    this.usuarioRepository = usuarioRepository;
    this.bodegaMapper = bodegaMapper;
    this.usuarioMapper = usuarioMapper;
    this.rolMapper = rolMapper;
}

@Override
public List<BodegaResponseDTO> listarTodas() {
    return bodegaRepository.findAll().stream()
            .map(this::convertirAResponseDTO)
            .toList();
}

@Override
public List<BodegaResponseDTO> listarActivas() {
    return bodegaRepository.findByActivoTrue().stream()
            .map(this::convertirAResponseDTO)
            .toList();
}

@Override
public List<BodegaResponseDTO> buscarPorNombre(String nombre) {
    return bodegaRepository.findByNombreContainingIgnoreCase(nombre).stream()
            .map(this::convertirAResponseDTO)
            .toList();
}

@Override
public List<BodegaResponseDTO> buscarPorEncargado(Integer idEncargado) {
    if (!usuarioRepository.existsById(idEncargado)) {
        throw new ResourceNotFoundException("El encargado con ID " + idEncargado + " no existe.");
    }
    return bodegaRepository.findByEncargado_IdUsuario(idEncargado).stream()
            .map(this::convertirAResponseDTO)
            .toList();
}

@Override
public BodegaResponseDTO buscarPorId(Integer id) {
    Bodega bodega = bodegaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + id));
    return convertirAResponseDTO(bodega);
}

@Override
public BodegaResponseDTO crear(BodegaRequestDTO dto) {
    Usuario encargado = usuarioRepository.findById(dto.idEncargado())
            .orElseThrow(() -> new ResourceNotFoundException("No se puede crear la bodega. El encargado con ID " + dto.idEncargado() + " no existe."));

    Bodega bodega = bodegaMapper.dtoToEntity(dto, encargado);
    bodega.setActivo(true);

    Bodega guardada = bodegaRepository.save(bodega);
    return convertirAResponseDTO(guardada);
}

@Override
public BodegaResponseDTO actualizar(Integer id, BodegaRequestDTO dto) {
    Bodega bodega = bodegaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Bodega no encontrada con ID: " + id));

    Usuario encargado = usuarioRepository.findById(dto.idEncargado())
            .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar la bodega. El encargado con ID " + dto.idEncargado() + " no existe."));

    bodegaMapper.updateEntityFromDto(bodega, dto, encargado);

    Bodega actualizada = bodegaRepository.save(bodega);
    return convertirAResponseDTO(actualizada);
}

@Override
public void desactivar(Integer id) {
    Bodega bodega = bodegaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + id));
    bodega.setActivo(false);
    bodegaRepository.save(bodega);
}

private BodegaResponseDTO convertirAResponseDTO(Bodega bodega) {
    RolResponseDTO rolDTO = rolMapper.entityToDto(bodega.getEncargado().getRol());
    UsuarioResponseDTO encargadoDTO = usuarioMapper.entityToDto(bodega.getEncargado(), rolDTO);
    return bodegaMapper.entityToDto(bodega, encargadoDTO);
}
}