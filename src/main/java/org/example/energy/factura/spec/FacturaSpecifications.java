package org.example.energy.factura.spec;

import jakarta.persistence.criteria.Predicate;
import org.example.energy.factura.dto.FacturaFilter;
import org.example.energy.factura.entity.Factura;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FacturaSpecifications {

    public static Specification<Factura> conFiltros(FacturaFilter f) {
        return (root, query, cb) -> {
            if (f == null) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (f.contratoId() != null) {
                predicates.add(cb.equal(root.get("contrato").get("contratoId"), f.contratoId()));
            }
            if (f.clienteId() != null) {
                predicates.add(cb.equal(root.get("contrato").get("cliente").get("clienteId"), f.clienteId()));
            }
            if (f.estadoPago() != null) {
                predicates.add(cb.equal(root.get("estadoPago"), f.estadoPago()));
            }
            if (f.fechaEmisionDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaEmision"), f.fechaEmisionDesde()));
            }
            if (f.fechaEmisionHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaEmision"), f.fechaEmisionHasta()));
            }
            if (f.fechaVencimientoHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaVencimiento"), f.fechaVencimientoHasta()));
            }
            if (f.importeMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("importe"), f.importeMin()));
            }
            if (f.importeMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("importe"), f.importeMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}