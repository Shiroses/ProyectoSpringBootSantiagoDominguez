package dev.shor0s.logitrack.repository;

import dev.shor0s.logitrack.model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Integer> {
    List<Bodega> findByActivoTrue();
    List<Bodega> findByEncargado_IdUsuario(Integer idEncargado);
    List<Bodega> findByNombreContainingIgnoreCase(String nombre);
    List<Bodega> findByCapacidadActualLessThan(Integer capacidad);
}