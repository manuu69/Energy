package org.example.energy.cliente.spec;


import jakarta.persistence.criteria.Predicate;
import org.example.energy.cliente.dto.ClienteFilter;
import org.example.energy.cliente.entity.Cliente;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClienteSpecifications {

    public static Specification<Cliente> conFiltros(ClienteFilter f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (f.nombre() != null && !f.nombre().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + f.nombre().toLowerCase() + "%"));
            }
            if (f.ciudad() != null && !f.ciudad().isBlank()) {
                predicates.add(cb.equal(root.get("ciudad"), f.ciudad()));
            }
            if (f.tipo() != null && !f.tipo().isBlank()) {
                predicates.add(cb.equal(root.get("tipo"), f.tipo()));
            }
            if (f.eliminado() != null) {
                predicates.add(cb.equal(root.get("eliminado"), f.eliminado()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
