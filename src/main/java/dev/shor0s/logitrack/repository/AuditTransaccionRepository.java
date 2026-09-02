package dev.shor0s.logitrack.repository;

import dev.shor0s.logitrack.model.AuditTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditTransaccionRepository extends JpaRepository<AuditTransaccion, Long> {
    List<AuditTransaccion> findByCatalogo_IdCatalogo(Integer idCatalogo);
    List<AuditTransaccion> findByUsuario_IdUsuario(Integer idUsuario);
    List<AuditTransaccion> findByBodega_IdBodega(Integer idBodega);
    List<AuditTransaccion> findByTipo(String tipo);
}