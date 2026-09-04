package org.example.energy.cliente.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.energy.common.enums.Segmento;
import org.example.energy.common.enums.TipoCliente;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("eliminado = false")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Integer clienteId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", unique = true, length = 150)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoCliente tipo;

    @Column(name = "ciudad", length = 50)
    private String ciudad;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "eliminado", nullable = false)
    private boolean eliminado = false;

    @Column(name = "fecha_eliminacion")
    private LocalDate fechaEliminacion;

    @Column(name = "eliminado_por", length = 100)
    private String eliminadoPor;

    @Enumerated(EnumType.STRING)
    @Column(name = "segmento", nullable = false)
    private Segmento segmento;  // Nuevo, Regular, Premium, VIP

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Cliente cliente = (Cliente) o;
        return getClienteId() != null && Objects.equals(getClienteId(), cliente.getClienteId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
