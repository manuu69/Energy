package org.example.energy.zona.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "zonas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zona_id")
    private Integer zonaId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "nivel", nullable = false)
    private Integer nivel;  // 0=país, 1=comunidad, 2=provincia, 3=ciudad, 4=zona

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    // Relación recursiva con sí misma
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    private Zona padre;
}
