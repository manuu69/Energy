package org.example.energy.integration;

import org.example.energy.cliente.entity.Cliente;
import org.example.energy.cliente.repository.ClienteRepository;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.contrato.repository.ContratoRepository;
import org.example.energy.factura.entity.Factura;
import org.example.energy.factura.repository.FacturaRepository;
import org.example.energy.factura.service.FacturaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FacturacionIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaService facturaService;

    @Test
    void flujoCompleto_crearContratoYGenerarFactura_persisteEnBaseDeDatos() {
        // 1. ARRANGE: Preparamos y guardamos las entidades en la BD real
        Cliente cliente = new Cliente();
        cliente.setNombre("Carlos");
        cliente.setEmail("carlos@example.com");
        cliente = clienteRepository.save(cliente);

        Contrato contrato = new Contrato();
        contrato.setCliente(cliente);
        contrato.setPotenciaKw(new BigDecimal("5.5"));
        //contrato.set(true);
        contrato = contratoRepository.save(contrato);

        // 2. ACT: Ejecutamos el servicio real que queremos probar
        facturaService.generarFacturas(9); // Generar facturas para el mes 9

        // 3. ASSERT: Verificamos directamente en la base de datos
        List<Factura> facturasGuardadas = facturaRepository.findByContratoContratoId(contrato.getContratoId());

        assertThat(facturasGuardadas).isNotEmpty();
        assertThat(facturasGuardadas.get(0).getContrato().getContratoId()).isEqualTo(contrato.getContratoId());
    }
}
