package dev.shor0s.logitrack.service;

import dev.shor0s.logitrack.dto.response.AuditUsuarioResponseDTO;

import java.util.List;

public interface AuditUsuarioService {
    List<AuditUsuarioResponseDTO> listarTodos();
    List<AuditUsuarioResponseDTO> listarPorUsuario(Integer idUsuario);
    List<AuditUsuarioResponseDTO> listarPorTipo(String tipo);
    AuditUsuarioResponseDTO buscarPorId(Long id);
}