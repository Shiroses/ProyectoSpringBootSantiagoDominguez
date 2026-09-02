package dev.shor0s.logitrack.service;

import dev.shor0s.logitrack.dto.response.AuditGeneralResponseDTO;

import java.util.List;

public interface AuditGeneralService {
    List<AuditGeneralResponseDTO> listarTodos();
    List<AuditGeneralResponseDTO> listarPorCatalogo(Integer idCatalogo);
    List<AuditGeneralResponseDTO> listarPorBodega(Integer idBodega);
    List<AuditGeneralResponseDTO> listarPorCategoria(Integer idCategoria);
    List<AuditGeneralResponseDTO> listarPorTipo(String tipo);
    AuditGeneralResponseDTO buscarPorId(Long id);
}