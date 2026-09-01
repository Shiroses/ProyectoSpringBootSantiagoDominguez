package dev.shor0s.logitrack.service;

import dev.shor0s.logitrack.dto.request.UsuarioRequestDTO;
import dev.shor0s.logitrack.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponseDTO> listarTodos();
    List<UsuarioResponseDTO> listarActivos();
    List<UsuarioResponseDTO> buscarPorNombre(String nombre);
    List<UsuarioResponseDTO> listarPorRol(Integer idRol);
    UsuarioResponseDTO buscarPorId(Integer id);
    UsuarioResponseDTO crear(UsuarioRequestDTO dto);
    UsuarioResponseDTO actualizar(Integer id, UsuarioRequestDTO dto);
    void desactivar(Integer id); // Soft Delete
}