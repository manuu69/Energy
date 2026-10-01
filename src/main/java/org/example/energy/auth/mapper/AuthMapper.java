package org.example.energy.auth.mapper;

import org.example.energy.auth.dto.AuthResponse;
import org.example.energy.auth.dto.UserRegisterDTO;
import org.example.energy.usuario.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {
    @Mapping(target = "token", source = "token")
    AuthResponse toAuthResponse(String token);

    Usuario toEntity(UserRegisterDTO request);
}
