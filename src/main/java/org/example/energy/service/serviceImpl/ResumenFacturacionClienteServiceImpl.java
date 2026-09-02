package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.dashboard.resumen.ResumenFacturacionClienteResponseDTO;
import org.example.energy.entity.view.ResumenFacturacionClienteView;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.ResumenFacturacionClienteMapper;
import org.example.energy.repository.view.ResumenFacturacionClienteRepository;
import org.example.energy.service.ResumenFacturacionClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ResumenFacturacionClienteServiceImpl implements ResumenFacturacionClienteService {

    private final ResumenFacturacionClienteRepository repository;
    private final ResumenFacturacionClienteMapper mapper;

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<ResumenFacturacionClienteResponseDTO> getAll(Pageable pageable) {
        Page<ResumenFacturacionClienteView> resumenes = repository.findAll(pageable);
        return resumenes.map(mapper::toDTO);
    }

    /**
     * @param clienteId
     * @return
     */
    @Override
    public ResumenFacturacionClienteResponseDTO getByClienteId(Integer clienteId) {
        ResumenFacturacionClienteView resumen = repository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con el id: " + clienteId));
        return mapper.toDTO(resumen);
    }
}
