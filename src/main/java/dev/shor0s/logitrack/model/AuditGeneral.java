package dev.shor0s.logitrack.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_general")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditGeneral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_audit_general")
    private Long idAuditGeneral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catalogo")
    private Catalogo catalogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bodega")
    private Bodega bodega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, length = 250)
    private String mensaje;

    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDateTime fecha;
}