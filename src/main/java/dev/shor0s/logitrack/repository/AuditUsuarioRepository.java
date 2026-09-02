package dev.shor0s.logitrack.repository;

import dev.shor0s.logitrack.model.AuditUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditUsuarioRepository extends JpaRepository<AuditUsuario, Long> {
    List<AuditUsuario> findByUsuario_IdUsuario(Integer idUsuario);
    List<AuditUsuario> findByTipo(String tipo);
}
