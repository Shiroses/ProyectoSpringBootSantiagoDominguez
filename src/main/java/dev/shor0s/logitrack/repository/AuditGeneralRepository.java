package dev.shor0s.logitrack.repository;

import dev.shor0s.logitrack.model.AuditGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditGeneralRepository extends JpaRepository<AuditGeneral, Long> {
    List<AuditGeneral> findByCatalogo_IdCatalogo(Integer idCatalogo);
    List<AuditGeneral> findByBodega_IdBodega(Integer idBodega);
    List<AuditGeneral> findByCategoria_IdCategoria(Integer idCategoria);
    List<AuditGeneral> findByTipo(String tipo);
}