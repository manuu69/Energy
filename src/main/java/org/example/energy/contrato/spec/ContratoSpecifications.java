package org.example.energy.contrato.spec;

import jakarta.persistence.criteria.Predicate;
import org.example.energy.contrato.dto.ContratoFilter;
import org.example.energy.contrato.entity.Contrato;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ContratoSpecifications {

    public static Specification<Contrato> conFiltros(ContratoFilter f) {
        return (root, query, cb) -> {
            if (f == null) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (f.clienteId() != null) {
                predicates.add(cb.equal(root.get("cliente").get("clienteId"), f.clienteId()));
            }
            if (f.tarifa() != null) {
                predicates.add(cb.equal(root.get("tarifa"), f.tarifa()));
            }
            if (f.estado() != null) {
                predicates.add(cb.equal(root.get("estado"), f.estado()));
            }
            if (f.zonaId() != null) {
                predicates.add(cb.equal(root.get("zona").get("zonaId"), f.zonaId()));
            }
            if (f.fechaInicioDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaInicio"), f.fechaInicioDesde()));
            }
            if (f.fechaInicioHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaInicio"), f.fechaInicioHasta()));
            }
            if (f.potenciaMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("potenciaKw"), f.potenciaMin()));
            }
            if (f.potenciaMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("potenciaKw"), f.potenciaMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}