package org.example.energy.mapper;

import org.example.energy.dto.error.ErrorResponseDTO;
import org.example.energy.dto.error.FieldErrorDTO;
import org.example.energy.exception.code.ErrorCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ErrorMapper {

    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(errorCode.getHttpStatus().value())")
    @Mapping(source = "errorCode.code", target = "errorCode")
    @Mapping(target = "error", expression = "java(errorCode.getHttpStatus().getReasonPhrase())")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "path", target = "path")
    @Mapping(source = "validationErrors", target = "validationErrors")
    ErrorResponseDTO toErrorResponseDTO(
            ErrorCode errorCode,
            String message,
            String path,
            List<FieldErrorDTO> validationErrors
    );

    default ErrorResponseDTO toErrorResponseDTO(ErrorCode errorCode, String detailMessage, String path) {
        return toErrorResponseDTO(errorCode, detailMessage, path, null);
    }

    default ErrorResponseDTO toErrorResponseDTO(ErrorCode errorCode, String path) {
        return toErrorResponseDTO(errorCode, errorCode.getDefaultMessage(), path, null);
    }
}