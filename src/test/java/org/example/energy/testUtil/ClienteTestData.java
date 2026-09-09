package org.example.energy.testUtil;

import org.example.energy.cliente.dto.ClienteCreateDTO;
import org.example.energy.cliente.dto.ClienteResponseDTO;
import org.example.energy.cliente.dto.ClienteUpdateDTO;
import org.example.energy.cliente.entity.Cliente;
import org.example.energy.common.enums.Segmento;
import org.example.energy.common.enums.TipoCliente;

import java.time.LocalDate;

public final class ClienteTestData {

    private ClienteTestData() {
    }

    public static Cliente crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setClienteId(1);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTipo(TipoCliente.RESIDENCIAL);
        cliente.setCiudad("Madrid");
        cliente.setFechaAlta(LocalDate.of(2024, 1, 15));
        cliente.setSegmento(Segmento.REGULAR);
        return cliente;
    }

    public static ClienteCreateDTO crearClienteCreateDTO() {
        return new ClienteCreateDTO(
                "Juan Pérez",
                "juan.perez@example.com",
                "RESIDENCIAL",
                "Madrid",
                LocalDate.of(2024, 1, 15),
                "REGULAR"
        );
    }

    public static ClienteResponseDTO crearClienteResponseDTO() {
        return new ClienteResponseDTO(
                1,
                "Juan Pérez",
                "juan.perez@example.com",
                TipoCliente.RESIDENCIAL,
                "Madrid",
                LocalDate.of(2024, 1, 15),
                "REGULAR"
        );
    }

    public static ClienteUpdateDTO crearClienteUpdateDTO() {
        return new ClienteUpdateDTO(
                "Juan Pérez Modificado",
                "juan.actualizado@example.com",
                TipoCliente.EMPRESA,
                "Barcelona",
                Segmento.PREMIUM
        );
    }

    public static Cliente crearClienteConTipo(TipoCliente tipo) {
        Cliente cliente = crearCliente();
        cliente.setTipo(tipo);
        return cliente;
    }

    public static ClienteResponseDTO crearClienteResponseDTOConTipo(TipoCliente tipo) {
        return new ClienteResponseDTO(
                1,
                "Juan Pérez",
                "juan.perez@example.com",
                tipo,
                "Madrid",
                LocalDate.of(2024, 1, 15),
                "REGULAR"
        );
    }
}