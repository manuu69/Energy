package org.example.energy.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "vw_clientes_activos")
@Getter
public class ClienteActivoView {
    @Id
    @Column(name = "cliente_id")
    private Integer clienteId;

    private String nombre;
    private String email;
    private String tipo;
    private String ciudad;
    private String segmento;
}
