package dev.shor0s.logitrack.service.impl;

import dev.shor0s.logitrack.dto.request.CatalogoRequestDTO;
import dev.shor0s.logitrack.dto.response.CatalogoResponseDTO;
import dev.shor0s.logitrack.dto.response.CategoriaResponseDTO;
import dev.shor0s.logitrack.exceptions.ResourceNotFoundException;
import dev.shor0s.logitrack.mapper.CatalogoMapper;
import dev.shor0s.logitrack.mapper.CategoriaMapper;
import dev.shor0s.logitrack.model.Catalogo;
import dev.shor0s.logitrack.model.Categoria;
import dev.shor0s.logitrack.repository.CatalogoRepository;
import dev.shor0s.logitrack.repository.CategoriaRepository;
import dev.shor0s.logitrack.service.CatalogoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    private final CatalogoRepository catalogoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CatalogoMapper catalogoMapper;
    private final CategoriaMapper categoriaMapper;

    public CatalogoServiceImpl(CatalogoRepository catalogoRepository,
                               CategoriaRepository categoriaRepository,
                               CatalogoMapper catalogoMapper,
                               CategoriaMapper categoriaMapper) {
        this.catalogoRepository = catalogoRepository;
        this.categoriaRepository = categoriaRepository;
        this.catalogoMapper = catalogoMapper;
        this.categoriaMapper = categoriaMapper;
    }

    @Override
    public List<CatalogoResponseDTO> listarTodos() {
        return catalogoRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public List<CatalogoResponseDTO> listarActivos() {
        return catalogoRepository.findByActivoTrue().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public List<CatalogoResponseDTO> buscarPorNombre(String nombre) {
        return catalogoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public List<CatalogoResponseDTO> listarPorCategoria(Integer idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new ResourceNotFoundException("La Categoría con ID " + idCategoria + " no existe.");
        }
        return catalogoRepository.findByCategoria_IdCategoria(idCategoria).stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Override
    public CatalogoResponseDTO buscarPorId(Integer id) {
        Catalogo catalogo = catalogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catálogo no encontrado con ID: " + id));
        return convertirAResponseDTO(catalogo);
    }

    @Override
    public CatalogoResponseDTO crear(CatalogoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.idCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("No se puede crear el producto en catálogo. La Categoría con ID " + dto.idCategoria() + " no existe."));

        Catalogo catalogo = catalogoMapper.dtoToEntity(dto, categoria);
        catalogo.setActivo(true);

        Catalogo guardado = catalogoRepository.save(catalogo);
        return convertirAResponseDTO(guardado);
    }

    @Override
    public CatalogoResponseDTO actualizar(Integer id, CatalogoRequestDTO dto) {
        Catalogo catalogo = catalogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar. Catálogo no encontrado con ID: " + id));

        Categoria categoria = categoriaRepository.findById(dto.idCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("No se puede actualizar el catálogo. La Categoría con ID " + dto.idCategoria() + " no existe."));

        catalogoMapper.updateEntityFromDto(catalogo, dto, categoria);

        Catalogo actualizado = catalogoRepository.save(catalogo);
        return convertirAResponseDTO(actualizado);
    }

    @Override
    public void desactivar(Integer id) {
        Catalogo catalogo = catalogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catálogo no encontrado con ID: " + id));
        catalogo.setActivo(false);
        catalogoRepository.save(catalogo);
    }

    private CatalogoResponseDTO convertirAResponseDTO(Catalogo catalogo) {
        CategoriaResponseDTO categoriaDTO = categoriaMapper.entityToDto(catalogo.getCategoria());
        return catalogoMapper.entityToDto(catalogo, categoriaDTO);
    }
}