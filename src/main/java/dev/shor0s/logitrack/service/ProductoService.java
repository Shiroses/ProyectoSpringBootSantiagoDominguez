package dev.shor0s.logitrack.service;

import dev.shor0s.logitrack.dto.request.ProductoRequestDTO;
import dev.shor0s.logitrack.dto.response.ProductoDetalleResponseDTO;

import java.util.List;

public interface ProductoService {
    List<ProductoDetalleResponseDTO> listarTodos();
    List<ProductoDetalleResponseDTO> listarPorBodega(Integer idBodega);
    List<ProductoDetalleResponseDTO> listarPorCatalogo(Integer idCatalogo);
    List<ProductoDetalleResponseDTO> listarProductosConStockBajo(Integer limiteStock);
    ProductoDetalleResponseDTO buscarPorId(Integer idCatalogo, Integer idBodega);
    ProductoDetalleResponseDTO crear(ProductoRequestDTO dto);
    ProductoDetalleResponseDTO actualizarStock(Integer idCatalogo, Integer idBodega, ProductoRequestDTO dto);
    void eliminar(Integer idCatalogo, Integer idBodega);
}