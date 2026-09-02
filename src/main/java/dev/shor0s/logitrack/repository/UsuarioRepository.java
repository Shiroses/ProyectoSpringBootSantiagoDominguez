package dev.shor0s.logitrack.repository;

import dev.shor0s.logitrack.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByActivoTrue();
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    List<Usuario> findByRol_IdRol(Integer idRol);
    Optional<Usuario> findByNombre(String nombre);
}