package org.example.energy.service;

import org.example.energy.cliente.mapper.ClienteMapper;
import org.example.energy.cliente.repository.ClienteRepository;
import org.example.energy.cliente.service.ClienteServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;


}
