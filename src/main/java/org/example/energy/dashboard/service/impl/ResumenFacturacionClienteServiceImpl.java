package org.example.energy.dashboard.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;
import org.example.energy.dashboard.repository.ResumenFacturacionClienteRepository;
import org.example.energy.dashboard.service.ResumenFacturacionClienteService;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class ResumenFacturacionClienteServiceImpl implements ResumenFacturacionClienteService {

    private final ResumenFacturacionClienteRepository resumenRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ResumenFacturacionClienteResponseDTO> getAll(Pageable pageable) {
        log.debug(
                "Consultando resumenes de facturación de clientes paginados. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<ResumenFacturacionClienteResponseDTO> resumenes = resumenRepository.findAll(pageable);

        log.info(
                "Consulta de resumenes de facturación realizada. totalElements={}, totalPages={}, currentPage={}",
                resumenes.getTotalElements(),
                resumenes.getTotalPages(),
                resumenes.getNumber()
        );

        return resumenes;
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenFacturacionClienteResponseDTO getByClienteId(Integer clienteId) {
        log.debug("Buscando resumen de facturación para clienteId={}", clienteId);

        return resumenRepository.findById(clienteId)
                .map(resumen -> {
                    log.info("Resumen de facturación encontrado para clienteId={}", clienteId);
                    return resumen;
                })
                .orElseThrow(() -> {
                    log.warn("No se encontró resumen de facturación para clienteId={}", clienteId);
                    return new ResourceNotFoundException(
                            "No se encontró resumen para el cliente con ID: " + clienteId
                    );
                });
    }
}