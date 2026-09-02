package dev.shor0s.logitrack.service.impl;

import dev.shor0s.logitrack.dto.response.CategoriaResponseDTO;
import dev.shor0s.logitrack.mapper.CategoriaMapper;
import dev.shor0s.logitrack.model.Categoria;
import dev.shor0s.logitrack.repository.CategoriaRepository;
import dev.shor0s.logitrack.service.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Override
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::entityToDto)
                .toList();
    }

    @Override
    public List<CategoriaResponseDTO> listarActivas() {
        return categoriaRepository.findByActivoTrue().stream()
                .map(categoriaMapper::entityToDto)
                .toList();
    }

    @Override
    public CategoriaResponseDTO buscarPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        return categoriaMapper.entityToDto(categoria);
    }
}