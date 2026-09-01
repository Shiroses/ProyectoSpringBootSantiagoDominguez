package dev.shor0s.logitrack.service;

import dev.shor0s.logitrack.dto.response.RolResponseDTO;

import java.util.List;

public interface RolService {
    List<RolResponseDTO> listarTodos();
    List<RolResponseDTO> listarActivos();
    RolResponseDTO buscarPorId(Integer id);
}