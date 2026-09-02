package dev.shor0s.logitrack.service;

import dev.shor0s.logitrack.dto.request.AuditTransaccionRequestDTO;
import dev.shor0s.logitrack.dto.response.AuditTransaccionResponseDTO;

import java.util.List;

public interface AuditTransaccionService {
    List<AuditTransaccionResponseDTO> listarTodas();
    List<AuditTransaccionResponseDTO> listarPorCatalogo(Integer idCatalogo);
    List<AuditTransaccionResponseDTO> listarPorUsuario(Integer idUsuario);
    List<AuditTransaccionResponseDTO> listarPorBodega(Integer idBodega);
    AuditTransaccionResponseDTO buscarPorId(Long id);
    AuditTransaccionResponseDTO registrar(AuditTransaccionRequestDTO dto);
}
