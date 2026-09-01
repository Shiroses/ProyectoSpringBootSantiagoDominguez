package dev.shor0s.logitrack.service;

import dev.shor0s.logitrack.dto.request.BodegaRequestDTO;
import dev.shor0s.logitrack.dto.response.BodegaResponseDTO;

import java.util.List;

public interface BodegaService {
    List<BodegaResponseDTO> listarTodas();
    List<BodegaResponseDTO> listarActivas();
    List<BodegaResponseDTO> buscarPorNombre(String nombre);
    List<BodegaResponseDTO> buscarPorEncargado(Integer idEncargado);
    BodegaResponseDTO buscarPorId(Integer id);
    BodegaResponseDTO crear(BodegaRequestDTO dto);
    BodegaResponseDTO actualizar(Integer id, BodegaRequestDTO dto);
    void desactivar(Integer id); // Soft Delete
}