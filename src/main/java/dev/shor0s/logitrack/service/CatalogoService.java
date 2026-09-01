package dev.shor0s.logitrack.service;

import dev.shor0s.logitrack.dto.request.CatalogoRequestDTO;
import dev.shor0s.logitrack.dto.response.CatalogoResponseDTO;

import java.util.List;

public interface CatalogoService {
    List<CatalogoResponseDTO> listarTodos();
    List<CatalogoResponseDTO> listarActivos();
    List<CatalogoResponseDTO> buscarPorNombre(String nombre);
    List<CatalogoResponseDTO> listarPorCategoria(Integer idCategoria);
    CatalogoResponseDTO buscarPorId(Integer id);
    CatalogoResponseDTO crear(CatalogoRequestDTO dto);
    CatalogoResponseDTO actualizar(Integer id, CatalogoRequestDTO dto);
    void desactivar(Integer id); // Soft Delete
}