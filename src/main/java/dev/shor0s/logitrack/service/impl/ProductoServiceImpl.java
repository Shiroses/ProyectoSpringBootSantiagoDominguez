package dev.shor0s.logitrack.service.impl;

import dev.shor0s.logitrack.dto.request.ProductoRequestDTO;
import dev.shor0s.logitrack.dto.response.BodegaResponseDTO;
import dev.shor0s.logitrack.dto.response.CatalogoResponseDTO;
import dev.shor0s.logitrack.dto.response.ProductoDetalleResponseDTO;
import dev.shor0s.logitrack.mapper.*;
import dev.shor0s.logitrack.model.Bodega;
import dev.shor0s.logitrack.model.Catalogo;
import dev.shor0s.logitrack.model.Producto;
import dev.shor0s.logitrack.model.ProductoId;
import dev.shor0s.logitrack.repository.BodegaRepository;
import dev.shor0s.logitrack.repository.CatalogoRepository;
import dev.shor0s.logitrack.repository.ProductoRepository;
import dev.shor0s.logitrack.service.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CatalogoRepository catalogoRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoMapper productoMapper;
    private final CatalogoMapper catalogoMapper;
    private final CategoriaMapper categoriaMapper;
    private final BodegaMapper bodegaMapper;
    private final UsuarioMapper usuarioMapper;
    private final RolMapper rolMapper;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               CatalogoRepository catalogoRepository,
                               BodegaRepository bodegaRepository,
                               ProductoMapper productoMapper,
                               CatalogoMapper catalogoMapper,
                               CategoriaMapper categoriaMapper,
                               BodegaMapper bodegaMapper,
                               UsuarioMapper usuarioMapper,
                               RolMapper rolMapper) {
        this.productoRepository = productoRepository;
        this.catalogoRepository = catalogoRepository;
        this.bodegaRepository = bodegaRepository;
        this.productoMapper = productoMapper;
        this.catalogoMapper = catalogoMapper;
        this.categoriaMapper = categoriaMapper;
        this.bodegaMapper = bodegaMapper;
        this.usuarioMapper = usuarioMapper;
        this.rolMapper = rolMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDetalleResponseDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDetalleResponseDTO> listarPorBodega(Integer idBodega) {
        if (!bodegaRepository.existsById(idBodega)) {
            throw new EntityNotFoundException("La Bodega con ID " + idBodega + " no existe.");
        }
        return productoRepository.findById_IdBodega(idBodega).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDetalleResponseDTO> listarPorCatalogo(Integer idCatalogo) {
        if (!catalogoRepository.existsById(idCatalogo)) {
            throw new EntityNotFoundException("El elemento de Catálogo con ID " + idCatalogo + " no existe.");
        }
        return productoRepository.findById_IdCatalogo(idCatalogo).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDetalleResponseDTO> listarProductosConStockBajo(Integer limiteStock) {
        return productoRepository.findByStockLessThan(limiteStock).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDetalleResponseDTO buscarPorId(Integer idCatalogo, Integer idBodega) {
        ProductoId id = new ProductoId(idCatalogo, idBodega);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el producto en el catálogo " + idCatalogo + " asignado a la bodega " + idBodega));
        return convertirAResponseDTO(producto);
    }

    @Override
    @Transactional
    public ProductoDetalleResponseDTO crear(ProductoRequestDTO dto) {
        Catalogo catalogo = catalogoRepository.findById(dto.idCatalogo())
                .orElseThrow(() -> new EntityNotFoundException("No se puede registrar el producto. El catálogo con ID " + dto.idCatalogo() + " no existe."));

        Bodega bodega = bodegaRepository.findById(dto.idBodega())
                .orElseThrow(() -> new EntityNotFoundException("No se puede registrar el producto. La bodega con ID " + dto.idBodega() + " no existe."));

        Producto producto = productoMapper.dtoToEntity(dto, catalogo, bodega);
        Producto guardado = productoRepository.save(producto);

        return convertirAResponseDTO(guardado);
    }

    @Override
    @Transactional
    public ProductoDetalleResponseDTO actualizarStock(Integer idCatalogo, Integer idBodega, ProductoRequestDTO dto) {
        ProductoId id = new ProductoId(idCatalogo, idBodega);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la asignación del producto en catálogo " + idCatalogo + " con la bodega " + idBodega));

        productoMapper.updateEntityFromDto(producto, dto);
        Producto actualizado = productoRepository.save(producto);

        return convertirAResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer idCatalogo, Integer idBodega) {
        ProductoId id = new ProductoId(idCatalogo, idBodega);
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("No existe el registro de producto en bodega con el ID especificado.");
        }
        productoRepository.deleteById(id);
    }

    private ProductoDetalleResponseDTO convertirAResponseDTO(Producto producto) {
        CatalogoResponseDTO catalogoDTO = catalogoMapper.entityToDto(
                producto.getCatalogo(),
                categoriaMapper.entityToDto(producto.getCatalogo().getCategoria())
        );

        BodegaResponseDTO bodegaDTO = bodegaMapper.entityToDto(
                producto.getBodega(),
                usuarioMapper.entityToDto(
                        producto.getBodega().getEncargado(),
                        rolMapper.entityToDto(producto.getBodega().getEncargado().getRol())
                )
        );

        return productoMapper.entityToDto(producto, catalogoDTO, bodegaDTO);
    }
}
