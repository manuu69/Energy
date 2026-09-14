package org.example.energy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.energy.common.error.mapper.ErrorMapper;
import org.example.energy.common.exception.handler.GlobalExceptionHandler;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.empleado.controller.EmpleadoController;
import org.example.energy.empleado.dto.EmpleadoCreateDTO;
import org.example.energy.empleado.dto.EmpleadoResponseDTO;
import org.example.energy.empleado.dto.EmpleadoUpdateDTO;
import org.example.energy.empleado.service.EmpleadoService;
import org.example.energy.testUtil.EmpleadoTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.example.energy.testUtil.EmpleadoTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
@Import({GlobalExceptionHandler.class, JacksonAutoConfiguration.class})
public class EmpleadoControllerTest {

    private final static String API_URL = "/empleados";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    /*@MockitoBean
    private ObjectMapper objectMapper;*/

    @MockitoBean
    private EmpleadoService empleadoService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    void getAll_returnsPageOfEmpleadoResponseDTO() throws Exception {
        Page<EmpleadoResponseDTO> page = new PageImpl<>(
                List.of(EmpleadoTestData.crearEmpleadoResponseDTO())
        );

        when(empleadoService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath(".content[0].empleadoId").value(2));

        verify(empleadoService).getAll(any(Pageable.class));
    }

    @Test
    void getById_whenEmpleadoExists_returnsEmpleadoResponseDTO() throws Exception {
        Integer empleadoId = 2;
        EmpleadoResponseDTO dto = crearEmpleadoResponseDTO();

        when(empleadoService.getById(empleadoId)).thenReturn(dto);

        mockMvc.perform(get(API_URL + "/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empleadoId").value(dto.empleadoId()))
                .andExpect(jsonPath("$.nombre").value(dto.nombre()))
                .andExpect(jsonPath("$.email").value(dto.email()));

        verify(empleadoService).getById(empleadoId);
    }

    @Test
    void getById_whenEmpleadoDoesNotExist_throwsResourceNotFoundException() throws Exception {
        Integer empleadoId = 999;

        when(empleadoService.getById(empleadoId))
                .thenThrow(new ResourceNotFoundException("No existe el empleado con ID: " + empleadoId));

        mockMvc.perform(get(API_URL + "/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(empleadoService).getById(empleadoId);
    }

    @Test
    void create_whenValidData_shouldReturn201CreatedAndResponseDTO() throws Exception {
        EmpleadoCreateDTO createDTO = crearEmpleadoCreateDTO(2);
        EmpleadoResponseDTO responseDTO = crearEmpleadoResponseDTO();

        when(empleadoService.create(any(EmpleadoCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.empleadoId").value(2))
                .andExpect(jsonPath("$.nombre").value("Carlos Mendoza"));

        verify(empleadoService).create(any(EmpleadoCreateDTO.class));
    }

    @Test
    void create_whenInvalidData_shouldReturn400BadRequest() throws Exception {
        EmpleadoCreateDTO invalidDTO = new EmpleadoCreateDTO(
                "", "email-invalido", null, null, new BigDecimal("-100.00"), null
        );

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(empleadoService);
    }

    @Test
    void update_whenEmpleadoExistsAndValidData_shouldReturnUpdatedDTO() throws Exception {
        Integer empleadoId = 2;
        EmpleadoUpdateDTO updateDTO = crearEmpleadoUpdateDTO();
        EmpleadoResponseDTO responseDTO = crearEmpleadoResponseDTO();

        when(empleadoService.update(eq(empleadoId), any(EmpleadoUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put(API_URL + "/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empleadoId").value(responseDTO.empleadoId()));

        verify(empleadoService).update(eq(empleadoId), any(EmpleadoUpdateDTO.class));
    }

    @Test
    void update_whenEmpleadoDoesNotExist_shouldReturn404NotFound() throws Exception {
        Integer empleadoId = 999;
        EmpleadoUpdateDTO updateDTO = crearEmpleadoUpdateDTO();

        when(empleadoService.update(eq(empleadoId), any(EmpleadoUpdateDTO.class)))
                .thenThrow(new ResourceNotFoundException("No existe el empleado con ID: " + empleadoId));

        mockMvc.perform(put(API_URL + "/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());

        verify(empleadoService).update(eq(empleadoId), any(EmpleadoUpdateDTO.class));
    }

    @Test
    void delete_whenEmpleadoCanBeDeleted_shouldReturn204NoContent() throws Exception {
        Integer empleadoId = 2;

        doNothing().when(empleadoService).delete(empleadoId);

        mockMvc.perform(delete(API_URL + "/{id}", empleadoId))
                .andExpect(status().isNoContent());

        verify(empleadoService).delete(empleadoId);
    }

    /*@Test
    void delete_whenHasSubordinadosActivos_shouldReturn409Or400() throws Exception {
        Integer jefeId = 1;

        doThrow(new IllegalStateException("No se puede desactivar al empleado porque tiene subordinados activos"))
                .when(empleadoService).delete(jefeId);

        mockMvc.perform(delete("/api/v1/empleados/{id}", jefeId))
                .andExpect(status().isConflict()); // O isBadRequest() según tu GlobalExceptionHandler

        verify(empleadoService).delete(jefeId);
    }*/
}