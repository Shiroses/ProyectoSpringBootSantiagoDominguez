package dev.shor0s.logitrack.service;

import dev.shor0s.logitrack.dto.response.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {
    List<CategoriaResponseDTO> listarTodas();
    List<CategoriaResponseDTO> listarActivas();
    CategoriaResponseDTO buscarPorId(Integer id);
}