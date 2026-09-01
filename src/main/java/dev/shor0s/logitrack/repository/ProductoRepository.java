package dev.shor0s.logitrack.repository;

import dev.shor0s.logitrack.model.Producto;
import dev.shor0s.logitrack.model.ProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, ProductoId> {
    List<Producto> findById_IdBodega(Integer idBodega);
    List<Producto> findById_IdCatalogo(Integer idCatalogo);
    List<Producto> findByStockLessThan(Integer stock);
}