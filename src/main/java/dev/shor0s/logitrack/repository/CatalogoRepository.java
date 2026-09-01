package dev.shor0s.logitrack.repository;

import dev.shor0s.logitrack.model.Catalogo;
import dev.shor0s.logitrack.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {
    List<Catalogo> findByActivoTrue();
    List<Catalogo> findByNombreContainingIgnoreCase(String nombre);
    List<Catalogo> findByCategoria_IdCategoria(Integer idCategoria);
    List<Catalogo> findByActivoTrueOrderByPrecioAsc();
    List<Catalogo> findByActivoTrueOrderByPrecioDesc();
}