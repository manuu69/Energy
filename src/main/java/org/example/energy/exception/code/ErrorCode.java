package org.example.energy.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    RESOURCE_NOT_FOUND(
            "ERR_001",
            "El recurso solicitado no existe",
            HttpStatus.NOT_FOUND
    ),

    INVALID_INPUT(
            "ERR_002",
            "Los datos enviados en la petición no son válidos",
            HttpStatus.BAD_REQUEST
    ),

    BUSINESS_RULE_VIOLATION(
            "ERR_003",
            "Se ha violado una regla de negocio",
            HttpStatus.UNPROCESSABLE_CONTENT
    ),

    DATABASE_CONFLICT(
            "ERR_004",
            "Conflicto en la base de datos (registro duplicado o clave foránea)",
            HttpStatus.CONFLICT
    ),

    MALFORMED_JSON(
            "ERR_005",
            "El cuerpo de la petición JSON es sintácticamente incorrecto",
            HttpStatus.BAD_REQUEST
    ),

    METHOD_NOT_ALLOWED(
            "ERR_006",
            "Método HTTP no soportado para este endpoint",
            HttpStatus.METHOD_NOT_ALLOWED
    ),

    INTERNAL_ERROR(
            "ERR_500",
            "Ha ocurrido un error interno inesperado",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),

    CLIENTE_YA_DADO_DE_BAJA(
            "ERR_101",
            "El cliente ya está dado de baja",
            HttpStatus.CONFLICT
    ),


    CLIENTE_CON_DEUDA_PENDIENTE(
            "ERR_102",
            "El cliente tiene deuda PENDIENTE, no se puede dar de baja",
            HttpStatus.UNPROCESSABLE_CONTENT
    ),

    CONTRATO_NO_ACTIVO(
            "ERR_103",
            "El contrato no está activo",
            HttpStatus.UNPROCESSABLE_CONTENT
    ),

    CONTRATO_YA_DADO_DE_BAJA(
            "ERR_110",
            "El contrato ya está dado de baja",
            HttpStatus.CONFLICT
    ),

    CONTRATO_YA_SUSPENDIDO(
            "ERR_111",
            "El contrato ya está suspendido",
            HttpStatus.CONFLICT
    ),

    CONTRATO_YA_ACTIVO(
            "ERR_112",
            "El contrato ya está activo",
            HttpStatus.CONFLICT
    ),

    ESTADO_CONTRATO_NO_VALIDO(
            "ERR_113",
            "El estado del contrato no es valido",
            HttpStatus.CONFLICT
    ),

    LIMITE_CONTRATOS_ALCANZADO(
            "ERR_104",
            "El cliente ha alcanzado el límite de contratos permitidos",
            HttpStatus.UNPROCESSABLE_CONTENT
    ),

    LECTURA_FECHA_INVALIDA(
            "ERR_105",
            "La fecha de la lectura es anterior a la última registrada",
            HttpStatus.BAD_REQUEST
    ),

    LECTURA_CONSUMO_NEGATIVO(
            "ERR_106",
            "El consumo no puede ser negativo",
            HttpStatus.BAD_REQUEST
    ),

    FACTURA_YA_PAGADA(
            "ERR_107",
            "La factura ya está pagada y no puede modificarse",
            HttpStatus.CONFLICT
    ),

    TARIFA_SIN_TRAMO(
            "ERR_108",
            "No existe tramo de tarifa para el consumo indicado",
            HttpStatus.UNPROCESSABLE_CONTENT
    ),

    FACTURA_YA_CANCELADA(
            "ERR_109",
            "La factura ya está cancelada",
            HttpStatus.CONFLICT
    ),

    ;

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;
}
