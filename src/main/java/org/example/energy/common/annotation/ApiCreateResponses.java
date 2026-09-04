package org.example.energy.common.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.energy.common.error.dto.ErrorResponseDTO;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Petición incorrecta",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                implementation = ErrorResponseDTO.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Recurso relacionado no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                implementation = ErrorResponseDTO.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno inesperado",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                implementation = ErrorResponseDTO.class
                        )
                )
        )
})
public @interface ApiCreateResponses {
}

