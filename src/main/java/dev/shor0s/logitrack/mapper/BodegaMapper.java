package dev.shor0s.logitrack.mapper;

import dev.shor0s.logitrack.dto.request.BodegaRequestDTO;
import dev.shor0s.logitrack.dto.response.BodegaResponseDTO;
import dev.shor0s.logitrack.dto.response.UsuarioResponseDTO;
import dev.shor0s.logitrack.model.Bodega;
import dev.shor0s.logitrack.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class BodegaMapper {

    public BodegaResponseDTO entityToDto(Bodega entity, UsuarioResponseDTO encargado) {
        if (entity == null) return null;
        return new BodegaResponseDTO(
                entity.getIdBodega(),
                entity.getNombre(),
                entity.getUbicacion(),
                entity.getCapacidad(),
                entity.getCapacidadActual(),
                encargado,
                entity.getActivo()
        );
    }

    public Bodega dtoToEntity(BodegaRequestDTO dto, Usuario encargado) {
        if (dto == null) return null;
        Bodega entity = new Bodega();
        entity.setNombre(dto.nombre());
        entity.setUbicacion(dto.ubicacion());
        entity.setCapacidad(dto.capacidad());
        entity.setCapacidadActual(0);
        entity.setEncargado(encargado);
        return entity;
    }

    public void updateEntityFromDto(Bodega entity, BodegaRequestDTO dto, Usuario encargado) {
        if (entity == null || dto == null) return;
        entity.setNombre(dto.nombre());
        entity.setUbicacion(dto.ubicacion());
        entity.setCapacidad(dto.capacidad());
        entity.setEncargado(encargado);
    }
}