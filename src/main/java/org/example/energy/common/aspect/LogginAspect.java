package org.example.energy.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class LogginAspect {

    @Pointcut("within(org.example.energy..controller..*)")
    public void controllerMethods() {}

    // 2. ADVICE: Define qué hacer cuando se intercepta un método.
    @Around("controllerMethods()")
    public Object logHttpCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        // Obtenemos información de la petición HTTP actual
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String httpMethod = request != null ? request.getMethod() : "UNKNOWN";
        String uri = request != null ? request.getRequestURI() : "UNKNOWN";
        String methodName = joinPoint.getSignature().toShortString();

        // Código que se ejecuta ANTES del controlador
        log.info("==> HTTP {} {} [Método: {}]", httpMethod, uri, methodName);
        long startTime = System.currentTimeMillis();

        Object result;
        try {
            // EJECUCIÓN DEL MÉTODO DEL CONTROLLER
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            // Código si el controlador lanza una excepción
            long duration = System.currentTimeMillis() - startTime;
            log.error("<== HTTP {} {} [Falló en {} ms] - Excepción: {}",
                    httpMethod, uri, duration, ex.getMessage());
            throw ex; // Volvemos a lanzar la excepción para que la maneje el ControllerAdvice
        }

        // Código que se ejecuta DESPUÉS del controlador (si todo sale bien)
        long duration = System.currentTimeMillis() - startTime;
        log.info("<== HTTP {} {} [Completado en {} ms]", httpMethod, uri, duration);

        return result;
    }
}