package org.example.energy.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class SwaggerBrowserLauncher {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @EventListener(ApplicationReadyEvent.class)
    public void openSwagger() {
        String url = "http://localhost:" + serverPort + contextPath + "/swagger-ui.html";

        System.out.println("Intentando abrir Swagger en: " + url);

        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            System.out.println("Swagger abierto correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudo abrir Swagger automáticamente. Abre manualmente: " + url);
            e.printStackTrace();
        }
    }
}