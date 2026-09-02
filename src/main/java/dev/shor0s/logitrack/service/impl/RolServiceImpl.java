package dev.shor0s.logitrack.service.impl;

import dev.shor0s.logitrack.dto.response.RolResponseDTO;
import dev.shor0s.logitrack.mapper.RolMapper;
import dev.shor0s.logitrack.model.Rol;
import dev.shor0s.logitrack.repository.RolRepository;
import dev.shor0s.logitrack.service.RolService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    public RolServiceImpl(RolRepository rolRepository, RolMapper rolMapper) {
        this.rolRepository = rolRepository;
        this.rolMapper = rolMapper;
    }

    @Override
    public List<RolResponseDTO> listarTodos() {
        return rolRepository.findAll().stream()
                .map(rolMapper::entityToDto)
                .toList();
    }

    @Override
    public List<RolResponseDTO> listarActivos() {
        return rolRepository.findByActivoTrue().stream()
                .map(rolMapper::entityToDto)
                .toList();
    }

    @Override
    public RolResponseDTO buscarPorId(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
        return rolMapper.entityToDto(rol);
    }
}