package org.example.energy.testUtil;

import org.example.energy.zona.dto.ZonaCreateDTO;
import org.example.energy.zona.dto.ZonaResponseDTO;
import org.example.energy.zona.dto.ZonaUpdateDTO;
import org.example.energy.zona.entity.Zona;

public final class ZonaTestData {

    private ZonaTestData() {
    }

    public static Zona crearZona() {
        Zona zona = new Zona();
        zona.setZonaId(1);
        zona.setNombre("Zona Norte");
        zona.setNivel(1);
        zona.setDescripcion("Sector industrial norte");
        return zona;
    }

    public static ZonaCreateDTO crearZonaCreateDTO() {
        return new ZonaCreateDTO(
                "Zona Norte",
                1,
                "Sector industrial norte"
        );
    }

    public static ZonaResponseDTO crearZonaResponseDTO() {
        return new ZonaResponseDTO(
                1,
                "Zona Norte",
                1,
                "Sector industrial norte"
        );
    }

    public static ZonaUpdateDTO crearZonaUpdateDTO() {
        return new ZonaUpdateDTO(
                "Zona Norte Modificada",
                2,
                "Sector industrial y residencial norte"
        );
    }
}