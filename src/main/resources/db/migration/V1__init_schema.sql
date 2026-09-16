-- ==========================================
-- 1. TABLAS Y SECUENCIAS
-- ==========================================

CREATE TABLE public.zonas (
                              zona_id SERIAL PRIMARY KEY,
                              nombre VARCHAR(100) NOT NULL,
                              nivel INT NOT NULL CHECK (nivel >= 0 AND nivel <= 4),
                              padre_id INT REFERENCES public.zonas(zona_id),
                              descripcion VARCHAR(200),
                              CONSTRAINT chk_raiz CHECK (((nivel = 0) AND (padre_id IS NULL)) OR ((nivel > 0) AND (padre_id IS NOT NULL)))
);

CREATE TABLE public.clientes (
                                 cliente_id SERIAL PRIMARY KEY,
                                 nombre VARCHAR(100) NOT NULL,
                                 email VARCHAR(150) UNIQUE,
                                 tipo VARCHAR(20) CHECK (tipo IN ('RESIDENCIAL', 'EMPRESA', 'INDUSTRIAL')),
                                 ciudad VARCHAR(50),
                                 fecha_alta DATE NOT NULL,
                                 eliminado BOOLEAN DEFAULT FALSE,
                                 fecha_eliminacion DATE,
                                 eliminado_por VARCHAR(100),
                                 segmento VARCHAR(20) DEFAULT 'Nuevo'
);

CREATE TABLE public.contratos (
                                  contrato_id SERIAL PRIMARY KEY,
                                  cliente_id INT NOT NULL REFERENCES public.clientes(cliente_id),
                                  tarifa VARCHAR(20),
                                  potencia_kw NUMERIC(6,2) NOT NULL,
                                  fecha_inicio DATE NOT NULL,
                                  estado VARCHAR(20) CHECK (estado IN ('ACTIVO', 'BAJA', 'SUSPENDIDO')),
                                  zona_id INT REFERENCES public.zonas(zona_id)
);

CREATE TABLE public.empleados (
                                  empleado_id SERIAL PRIMARY KEY,
                                  nombre VARCHAR(100) NOT NULL,
                                  email VARCHAR(150) NOT NULL UNIQUE,
                                  departamento VARCHAR(50) NOT NULL,
                                  rol VARCHAR(50) NOT NULL,
                                  salario NUMERIC(10,2) NOT NULL,
                                  fecha_alta DATE NOT NULL,
                                  jefe_id INT REFERENCES public.empleados(empleado_id),
                                  activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE public.lecturas (
                                 lectura_id SERIAL PRIMARY KEY,
                                 contrato_id INT NOT NULL REFERENCES public.contratos(contrato_id),
                                 fecha DATE NOT NULL,
                                 consumo_kwh NUMERIC(10,2) NOT NULL CHECK (consumo_kwh >= 0),
                                 tipo_lectura VARCHAR(20)
);

CREATE TABLE public.facturas (
                                 factura_id SERIAL PRIMARY KEY,
                                 contrato_id INT NOT NULL REFERENCES public.contratos(contrato_id),
                                 fecha_emision DATE NOT NULL,
                                 importe NUMERIC(10,2) NOT NULL,
                                 estado_pago VARCHAR(20),
                                 fecha_vencimiento DATE NOT NULL,
                                 fecha_pago DATE
);

CREATE TABLE public.incidencias (
                                    incidencia_id SERIAL PRIMARY KEY,
                                    contrato_id INT NOT NULL REFERENCES public.contratos(contrato_id),
                                    tipo VARCHAR(50),
                                    fecha_apertura DATE NOT NULL,
                                    fecha_cierre DATE,
                                    estado VARCHAR(20)
);

CREATE TABLE public.historico_tarifas (
                                          historico_id SERIAL PRIMARY KEY,
                                          contrato_id INT NOT NULL REFERENCES public.contratos(contrato_id),
                                          tarifa VARCHAR(50) NOT NULL,
                                          precio_kwh NUMERIC(6,4) NOT NULL,
                                          potencia_kw NUMERIC(6,2) NOT NULL,
                                          fecha_inicio DATE NOT NULL,
                                          fecha_fin DATE,
                                          motivo_cambio VARCHAR(100),
                                          CONSTRAINT chk_fechas CHECK ((fecha_fin IS NULL) OR (fecha_fin > fecha_inicio))
);

CREATE TABLE public.tramos_tarifa (
                                      tramo_id SERIAL PRIMARY KEY,
                                      tarifa VARCHAR(50) NOT NULL,
                                      descripcion VARCHAR(80) NOT NULL,
                                      kwh_min NUMERIC(10,2) NOT NULL,
                                      kwh_max NUMERIC(10,2) NOT NULL,
                                      precio_kwh NUMERIC(6,4) NOT NULL,
                                      peaje_kwh NUMERIC(6,4) DEFAULT 0 NOT NULL,
                                      vigente_desde DATE NOT NULL,
                                      vigente_hasta DATE,
                                      CONSTRAINT chk_rango CHECK (kwh_max > kwh_min)
);

CREATE TABLE public.auditoria (
                                  auditoria_id SERIAL PRIMARY KEY,
                                  tabla_nombre VARCHAR(50) NOT NULL,
                                  operacion VARCHAR(10) NOT NULL CHECK (operacion IN ('INSERT', 'UPDATE', 'DELETE')),
                                  registro_id INT NOT NULL,
                                  usuario VARCHAR(100) DEFAULT CURRENT_USER NOT NULL,
                                  fecha TIMESTAMP DEFAULT NOW() NOT NULL,
                                  datos_antes JSONB,
                                  datos_despues JSONB,
                                  ip_origen VARCHAR(45),
                                  aplicacion VARCHAR(100) DEFAULT CURRENT_SETTING('application_name', true)
);

-- ==========================================
-- 2. ÍNDICES
-- ==========================================

CREATE INDEX idx_auditoria_fecha ON public.auditoria USING btree (fecha DESC);
CREATE INDEX idx_auditoria_registro ON public.auditoria USING btree (tabla_nombre, registro_id);
CREATE INDEX idx_auditoria_tabla ON public.auditoria USING btree (tabla_nombre);
CREATE INDEX idx_auditoria_usuario ON public.auditoria USING btree (usuario);
CREATE INDEX idx_clientes_activos ON public.clientes USING btree (cliente_id) WHERE (eliminado = FALSE);
CREATE INDEX idx_contratos_zona ON public.contratos USING btree (zona_id);
CREATE INDEX idx_empleados_dpto ON public.empleados USING btree (departamento);
CREATE INDEX idx_empleados_jefe ON public.empleados USING btree (jefe_id);
CREATE INDEX idx_hist_tarifas_contrato ON public.historico_tarifas USING btree (contrato_id);
CREATE INDEX idx_hist_tarifas_vigente ON public.historico_tarifas USING btree (contrato_id, fecha_fin) WHERE (fecha_fin IS NULL);
CREATE INDEX idx_lecturas_contrato ON public.lecturas USING btree (contrato_id);
CREATE INDEX idx_tramos_tarifa_tipo ON public.tramos_tarifa USING btree (tarifa);
CREATE INDEX idx_zonas_nivel ON public.zonas USING btree (nivel);
CREATE INDEX idx_zonas_padre ON public.zonas USING btree (padre_id);

-- ==========================================
-- 3. FUNCIONES Y PROCEDIMIENTOS PL/PGSQL
-- ==========================================

CREATE OR REPLACE FUNCTION public.calcular_importe_lectura(p_contrato_id INT, p_consumo_kwh NUMERIC)
    RETURNS NUMERIC
    LANGUAGE plpgsql
AS $$
DECLARE
    v_tarifa_actual VARCHAR;
    v_tramo_actual tramos_tarifa%ROWTYPE;
    v_importe NUMERIC := 0;
BEGIN
    SELECT c.tarifa INTO v_tarifa_actual FROM contratos c WHERE c.contrato_id = p_contrato_id;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Contrato % no encontrado', p_contrato_id;
    END IF;

    SELECT tt.* INTO v_tramo_actual FROM tramos_tarifa tt
    WHERE tt.tarifa = v_tarifa_actual
      AND p_consumo_kwh BETWEEN tt.kwh_min AND tt.kwh_max
      AND tt.vigente_hasta IS NULL;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'No se encontró tramo para tarifa % y consumo %', v_tarifa_actual, p_consumo_kwh;
    END IF;

    v_importe := ROUND(p_consumo_kwh * (v_tramo_actual.precio_kwh + v_tramo_actual.peaje_kwh), 2);
    RETURN v_importe;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error: %', SQLERRM;
        RETURN NULL;
END;
$$;

CREATE OR REPLACE FUNCTION public.calcular_consumo_por_zona(p_zona_id INT)
    RETURNS TABLE(fecha_lectura DATE, consumo NUMERIC, tipo VARCHAR)
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT l.fecha, l.consumo_kwh, l.tipo_lectura
        FROM lecturas l
                 JOIN contratos c ON l.contrato_id = c.contrato_id
        WHERE c.zona_id = p_zona_id;
END;
$$;

CREATE OR REPLACE FUNCTION public.calcular_factura_periodo(p_contrato_id INT, p_fecha_inicio DATE, p_fecha_fin DATE)
    RETURNS TABLE(contrato_id INT, periodo_inicio DATE, periodo_fin DATE, consumo_total NUMERIC, importe_estimado NUMERIC)
    LANGUAGE plpgsql
AS $$
DECLARE
    v_consumo_total DECIMAL;
    v_importe_estimado DECIMAL;
BEGIN
    IF NOT EXISTS (SELECT 1 FROM contratos WHERE contrato_id = p_contrato_id) THEN
        RAISE EXCEPTION 'Contrato % no encontrado', p_contrato_id;
    END IF;

    SELECT COALESCE(SUM(l.consumo_kwh), 0) INTO v_consumo_total
    FROM lecturas l
    WHERE l.contrato_id = p_contrato_id AND l.fecha BETWEEN p_fecha_inicio AND p_fecha_fin;

    IF v_consumo_total = 0 THEN
        RAISE NOTICE 'No hay lecturas para el contrato % en ese periodo', p_contrato_id;
    END IF;

    v_importe_estimado := calcular_importe_lectura(p_contrato_id, v_consumo_total);

    RETURN QUERY
        SELECT p_contrato_id, p_fecha_inicio, p_fecha_fin, v_consumo_total, v_importe_estimado;
END;
$$;

CREATE OR REPLACE FUNCTION public.calcular_penalizacion(p_contrato_id INT)
    RETURNS NUMERIC
    LANGUAGE plpgsql
AS $$
DECLARE
    v_cant_fraudes INT := 0;
    v_porcentaje_penalizacion NUMERIC := 0.00;
BEGIN
    SELECT COUNT(*) INTO v_cant_fraudes FROM incidencias i
    WHERE i.tipo = 'fraude' AND i.estado = 'en_gestion' AND i.contrato_id = p_contrato_id;

    IF v_cant_fraudes = 0 THEN
        v_porcentaje_penalizacion := 0.00;
    ELSIF v_cant_fraudes = 1 THEN
        v_porcentaje_penalizacion := 0.10;
    ELSIF v_cant_fraudes > 1 THEN
        v_porcentaje_penalizacion := 0.20;
    END IF;
    RETURN v_porcentaje_penalizacion;
END;
$$;

/*CREATE OR REPLACE FUNCTION public.get_clientes_riesgo()
    RETURNS TABLE(nombre VARCHAR, ciudad VARCHAR, tiene_vencidas BOOLEAN, tiene_incidencias BOOLEAN, consumo_anomalo BOOLEAN, num_condiciones INT)
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        WITH facturas_vencidas AS (
            SELECT DISTINCT c.cliente_id, TRUE AS tiene_vencidas
            FROM clientes c
                     JOIN contratos co ON c.cliente_id = co.cliente_id
                     JOIN facturas f ON co.contrato_id = f.contrato_id
            WHERE f.estado_pago = 'vencida'
        ),
             incidencias_abiertas AS (
                 SELECT DISTINCT c.cliente_id, TRUE AS tiene_incidencias
                 FROM clientes c
                          JOIN contratos co ON c.cliente_id = co.cliente_id
                          JOIN incidencias i ON co.contrato_id = i.contrato_id
                 WHERE i.estado IN ('abierta', 'en_gestion')
             ),
             consumo_anomalo AS (
                 SELECT c.cliente_id, TRUE AS consumo_anomalo
                 FROM (
                          SELECT co.cliente_id, l.consumo_kwh, AVG(l.consumo_kwh) OVER (PARTITION BY l.contrato_id) AS media,
                                 ROW_NUMBER() OVER(PARTITION BY l.contrato_id ORDER BY l.fecha DESC) AS rn
                          FROM lecturas l
                                   JOIN contratos co ON l.contrato_id = co.contrato_id
                      ) sub
                 WHERE rn = 1 AND consumo_kwh > media * 1.5
             )
        SELECT
            c.nombre,
            c.ciudad,
            COALESCE(fv.tiene_vencidas, FALSE) AS tiene_vencidas,
            COALESCE(ia.tiene_incidencias, FALSE) AS tiene_incidencias,
            COALESCE(ca.consumo_anomalo, FALSE) AS consumo_anomalo,
            ((CASE WHEN fv.tiene_vencidas IS TRUE THEN 1 ELSE 0 END) +
             (CASE WHEN ia.tiene_incidencias IS TRUE THEN 1 ELSE 0 END) +
             (CASE WHEN ca.consumo_anomalo IS TRUE THEN 1 ELSE 0 END))::INT AS num_condiciones
        FROM clientes c
                 LEFT JOIN facturas_vencidas fv ON c.cliente_id = fv.cliente_id
                 LEFT JOIN incidencias_abiertas ia ON c.cliente_id = ia.cliente_id
                 LEFT JOIN consumo_anomalo ca ON c.cliente_id = ca.cliente_id
        WHERE (
                  (CASE WHEN fv.tiene_vencidas IS TRUE THEN 1 ELSE 0 END) +
                  (CASE WHEN ia.tiene_incidencias IS TRUE THEN 1 ELSE 0 END) +
                  (CASE WHEN ca.consumo_anomalo IS TRUE THEN 1 ELSE 0 END)
                  ) >= 2
        ORDER BY num_condiciones DESC;
END;
$$;*/

CREATE OR REPLACE FUNCTION public.get_consumo_stats(p_contrato_id INT)
    RETURNS TABLE(contrato_id INT, nombre_cliente VARCHAR, consumo_medio NUMERIC, consumo_maximo NUMERIC, ultimo_consumo NUMERIC, alerta VARCHAR)
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        WITH ultima_lectura AS (
            SELECT l.consumo_kwh
            FROM lecturas l
            WHERE l.contrato_id = p_contrato_id
            ORDER BY l.fecha DESC
            LIMIT 1
        )
        SELECT
            c.contrato_id,
            cl.nombre::VARCHAR,
            ROUND(AVG(l.consumo_kwh), 2)::NUMERIC AS consumo_medio,
            MAX(l.consumo_kwh)::NUMERIC AS consumo_maximo,
            (SELECT consumo_kwh FROM ultima_lectura)::NUMERIC AS ultimo_consumo,
            CASE
                WHEN AVG(l.consumo_kwh) > 2000 THEN 'ALERTA COBRA'::VARCHAR
                ELSE 'NORMAL'::VARCHAR
                END AS alerta
        FROM contratos c
                 JOIN clientes cl ON c.cliente_id = cl.cliente_id
                 JOIN lecturas l ON c.contrato_id = l.contrato_id
        WHERE c.contrato_id = p_contrato_id
        GROUP BY c.contrato_id, cl.nombre;
END;
$$;

CREATE OR REPLACE FUNCTION public.get_cursor_facturas_cliente(p_cliente_id INT)
    RETURNS REFCURSOR
    LANGUAGE plpgsql
AS $$
DECLARE
    v_cursor REFCURSOR := 'cursor_facturas';
BEGIN
    OPEN v_cursor FOR
        SELECT f.fecha_emision, f.importe, f.estado_pago, c.tarifa
        FROM facturas f
                 JOIN contratos c ON f.contrato_id = c.contrato_id
        WHERE c.cliente_id = p_cliente_id;
    RETURN v_cursor;
END;
$$;

CREATE OR REPLACE FUNCTION public.get_cursor_resumen_ciudad()
    RETURNS REFCURSOR
    LANGUAGE plpgsql
AS $$
DECLARE
    v_cursor REFCURSOR := 'cursor_resumen_ciudad';
BEGIN
    OPEN v_cursor FOR
        SELECT
            c.ciudad,
            SUM(f.importe) AS total_facturado,
            SUM(f.importe) FILTER (WHERE f.estado_pago = 'pagada') AS total_cobrado,
            SUM(f.importe) FILTER (WHERE f.estado_pago = 'pendiente') AS total_pendiente,
            ROUND(
                    COALESCE(
                            (SUM(f.importe) FILTER (WHERE f.estado_pago = 'pendiente') / NULLIF(SUM(f.importe), 0)) * 100,
                            0
                    ), 2
            ) AS porcentaje_morosidad
        FROM clientes c
                 JOIN contratos c2 ON c.cliente_id = c2.cliente_id
                 JOIN facturas f ON c2.contrato_id = f.contrato_id
        GROUP BY c.ciudad;
    RETURN v_cursor;
END;
$$;

CREATE OR REPLACE FUNCTION public.get_historico_tarifa(p_contrato_id INT, p_fecha DATE)
    RETURNS TABLE(contrato_id INT, tarifa VARCHAR, precio_kwh NUMERIC, fecha_inicio DATE, fecha_fin DATE)
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT h.contrato_id, h.tarifa, h.precio_kwh, h.fecha_inicio, h.fecha_fin
        FROM historico_tarifas h
        WHERE h.contrato_id = p_contrato_id
          AND p_fecha >= h.fecha_inicio
          AND (p_fecha <= h.fecha_fin OR h.fecha_fin IS NULL);
END;
$$;

CREATE OR REPLACE FUNCTION public.subir_potencia(p_ciudad VARCHAR)
    RETURNS INT
    LANGUAGE plpgsql
AS $$
DECLARE
    v_contador INT := 0;
    v_contratos_clientes RECORD;
BEGIN
    FOR v_contratos_clientes IN
        SELECT c.nombre, co.potencia_kw, co.contrato_id
        FROM clientes c
                 JOIN contratos co ON c.cliente_id = co.cliente_id
        WHERE co.estado = 'activo' AND c.tipo = 'industrial' AND c.ciudad = p_ciudad
        LOOP
            UPDATE contratos SET potencia_kw = potencia_kw * 1.15 WHERE contrato_id = v_contratos_clientes.contrato_id;
            v_contador := v_contador + 1;
        END LOOP;
    RETURN v_contador;
END;
$$;

CREATE OR REPLACE PROCEDURE public.actualizar_segmentos_masivo()
    LANGUAGE plpgsql
AS $$
DECLARE
    v_cursor CURSOR FOR SELECT * FROM clientes c WHERE c.eliminado = FALSE;
    v_fila RECORD;
    v_total_facturado DECIMAL := 0.00;
    v_segmento_nuevo VARCHAR;
    v_total_procesados INT := 0;
    v_cambiados INT := 0;
    v_vip INT := 0;
    v_premium INT := 0;
    v_regular INT := 0;
    v_nuevo INT := 0;
BEGIN
    OPEN v_cursor;
    LOOP
        FETCH v_cursor INTO v_fila;
        EXIT WHEN NOT FOUND;
        SELECT COALESCE(SUM(f.importe), 0) INTO v_total_facturado
        FROM facturas f JOIN contratos c ON f.contrato_id = c.contrato_id
        WHERE c.cliente_id = v_fila.cliente_id;

        v_segmento_nuevo := CASE
                                WHEN v_total_facturado > 5000 THEN 'VIP'
                                WHEN v_total_facturado > 2000 THEN 'Premium'
                                WHEN v_total_facturado > 500  THEN 'Regular'
                                ELSE 'Nuevo'
            END;

        CASE v_segmento_nuevo
            WHEN 'VIP'     THEN v_vip     := v_vip + 1;
            WHEN 'Premium' THEN v_premium := v_premium + 1;
            WHEN 'Regular' THEN v_regular := v_regular + 1;
            ELSE                v_nuevo   := v_nuevo + 1;
            END CASE;

        IF v_segmento_nuevo != v_fila.segmento THEN
            UPDATE clientes SET segmento = v_segmento_nuevo WHERE cliente_id = v_fila.cliente_id;
            v_cambiados := v_cambiados + 1;
        END IF;
        v_total_procesados := v_total_procesados + 1;
    END LOOP;
    CLOSE v_cursor;
    RAISE NOTICE 'Procesados % clientes', v_total_procesados;
    RAISE NOTICE 'VIP: %, Premium: %, Regular: %, Nuevo: %', v_vip, v_premium, v_regular, v_nuevo;
    RAISE NOTICE 'Clientes que cambiaron de segmento: %', v_cambiados;
END;
$$;

CREATE OR REPLACE PROCEDURE public.dar_baja_cliente(IN p_cliente_id INT)
    LANGUAGE plpgsql
AS $$
DECLARE
    v_cliente_existe BOOLEAN;
    v_tiene_deuda BOOLEAN;
BEGIN
    SELECT EXISTS (SELECT 1 FROM clientes c WHERE c.cliente_id = p_cliente_id AND c.eliminado = FALSE) INTO v_cliente_existe;

    IF NOT v_cliente_existe THEN
        RAISE EXCEPTION USING ERRCODE = 'P0002', MESSAGE = 'CLIENTE_NO_ENCONTRADO';
    END IF;

    SELECT EXISTS (
        SELECT 1 FROM facturas f
                          INNER JOIN contratos c ON c.contrato_id = f.contrato_id
        WHERE c.cliente_id = p_cliente_id AND f.estado_pago = 'PENDIENTE'
    ) INTO v_tiene_deuda;

    IF v_tiene_deuda THEN
        RAISE EXCEPTION USING ERRCODE = 'P0001', MESSAGE = 'CLIENTE_CON_DEUDA_PENDIENTE';
    END IF;

    UPDATE contratos SET estado = 'CANCELADO' WHERE cliente_id = p_cliente_id AND estado = 'ACTIVO';
    UPDATE clientes SET eliminado = TRUE, fecha_eliminacion = CURRENT_TIMESTAMP, eliminado_por = CURRENT_USER WHERE cliente_id = p_cliente_id;
END;
$$;

CREATE OR REPLACE PROCEDURE public.generar_alertas_morosidad()
    LANGUAGE plpgsql
AS $$
DECLARE
    v_cursor CURSOR FOR
        SELECT c.contrato_id, cl.nombre
        FROM contratos c
                 JOIN clientes cl ON c.cliente_id = cl.cliente_id
        WHERE c.estado = 'activo'
          AND EXISTS (
            SELECT 1 FROM facturas f WHERE f.contrato_id = c.contrato_id AND f.estado_pago = 'vencida'
        )
          AND NOT EXISTS (
            SELECT 1 FROM incidencias i WHERE i.contrato_id = c.contrato_id AND i.tipo = 'reclamacion' AND i.estado = 'abierta'
        );
    v_fila RECORD;
    v_cant_alertas INT := 0;
BEGIN
    OPEN v_cursor;
    LOOP
        FETCH v_cursor INTO v_fila;
        EXIT WHEN NOT FOUND;
        INSERT INTO incidencias (contrato_id, tipo, fecha_apertura, estado)
        VALUES (v_fila.contrato_id, 'reclamacion', CURRENT_DATE, 'abierta');
        v_cant_alertas := v_cant_alertas + 1;
        RAISE NOTICE 'Alerta generada para el contrato % del cliente %', v_fila.contrato_id, v_fila.nombre;
    END LOOP;
    CLOSE v_cursor;
    RAISE NOTICE 'Total de alertas generadas: %', v_cant_alertas;
END;
$$;

CREATE OR REPLACE PROCEDURE public.generar_facturas(IN p_mes INT)
    LANGUAGE plpgsql
AS $$
DECLARE
    v_anio INTEGER;
    v_fecha_emision DATE;
    v_fecha_vencimiento DATE;
    v_facturas_generadas INTEGER;
BEGIN
    IF p_mes IS NULL OR p_mes < 1 OR p_mes > 12 THEN
        RAISE EXCEPTION 'El mes debe estar entre 1 y 12';
    END IF;
    v_anio := EXTRACT(YEAR FROM CURRENT_DATE)::INTEGER;
    v_fecha_emision := make_date(v_anio, p_mes, 5);
    v_fecha_vencimiento := make_date(v_anio, p_mes, 25);

    INSERT INTO facturas (contrato_id, fecha_emision, importe, estado_pago, fecha_vencimiento)
    SELECT
        c.contrato_id,
        v_fecha_emision,
        ROUND((l.consumo_kwh * ht.precio_kwh)::NUMERIC, 2) AS importe,
        'pendiente',
        v_fecha_vencimiento
    FROM contratos c
             JOIN lecturas l ON l.contrato_id = c.contrato_id
             JOIN LATERAL (
        SELECT h.precio_kwh
        FROM historico_tarifas h
        WHERE h.contrato_id = c.contrato_id
          AND h.fecha_inicio <= l.fecha
          AND (h.fecha_fin IS NULL OR h.fecha_fin >= l.fecha)
        ORDER BY h.fecha_inicio DESC
        LIMIT 1
        ) ht ON TRUE
    WHERE LOWER(c.estado) = 'activo'
      AND EXTRACT(MONTH FROM l.fecha)::INTEGER = p_mes
      AND EXTRACT(YEAR FROM l.fecha)::INTEGER = v_anio
      AND NOT EXISTS (
        SELECT 1 FROM facturas f
        WHERE f.contrato_id = c.contrato_id
          AND EXTRACT(MONTH FROM f.fecha_emision)::INTEGER = p_mes
          AND EXTRACT(YEAR FROM f.fecha_emision)::INTEGER = v_anio
    );
    GET DIAGNOSTICS v_facturas_generadas = ROW_COUNT;
    RAISE NOTICE 'Facturas generadas: %', v_facturas_generadas;
END;
$$;

CREATE OR REPLACE PROCEDURE public.procesar_lecturas_anomalas()
    LANGUAGE plpgsql
AS $$
DECLARE
    v_cursor CURSOR FOR
        WITH lecturas_con_media AS (
            SELECT l.contrato_id, l.fecha, l.consumo_kwh, AVG(l.consumo_kwh) OVER (PARTITION BY l.contrato_id) AS media_contrato
            FROM lecturas l
        )
        SELECT m.contrato_id, m.fecha, m.consumo_kwh, ROUND(((m.consumo_kwh - m.media_contrato) / m.media_contrato) * 100, 1) AS desviacion
        FROM lecturas_con_media m
        WHERE m.consumo_kwh > (m.media_contrato * 1.5);
    v_fila RECORD;
BEGIN
    OPEN v_cursor;
    LOOP
        FETCH v_cursor INTO v_fila;
        EXIT WHEN NOT FOUND;
        RAISE NOTICE 'Contrato %, fecha %, consumo % kwh, desviacion %', v_fila.contrato_id, v_fila.fecha, v_fila.consumo_kwh, v_fila.desviacion;
    END LOOP;
    CLOSE v_cursor;
END;
$$;

CREATE OR REPLACE PROCEDURE public.registrar_lectura(
    IN p_contrato_id INT,
    IN p_fecha DATE,
    IN p_consumo_kwh NUMERIC,
    IN p_tipo_lectura VARCHAR DEFAULT 'REAL'
)
    LANGUAGE plpgsql
AS $$
DECLARE
    v_ultima_lectura DATE;
BEGIN
    IF NOT EXISTS (SELECT 1 FROM contratos WHERE contrato_id = p_contrato_id AND estado = 'ACTIVO') THEN
        RAISE EXCEPTION 'El contrato % no existe o no esta activo', p_contrato_id;
    END IF;

    IF p_consumo_kwh < 0 THEN
        RAISE EXCEPTION 'El consumo no puede ser negativo: %', p_consumo_kwh;
    END IF;

    SELECT MAX(fecha) INTO v_ultima_lectura FROM lecturas WHERE contrato_id = p_contrato_id;

    INSERT INTO lecturas (contrato_id, fecha, consumo_kwh, tipo_lectura)
    VALUES (p_contrato_id, p_fecha, p_consumo_kwh, p_tipo_lectura);

    RAISE NOTICE 'Lectura registrada: contrato %, fecha %, % kWh (%)', p_contrato_id, p_fecha, p_consumo_kwh, p_tipo_lectura;
END;
$$;

-- ==========================================
-- 4. FUNCIONES DE TRIGGER
-- ==========================================

CREATE OR REPLACE FUNCTION public.normalizar_clientes()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.email IS NULL THEN
        RAISE NOTICE 'El email no puede ser nulo o estar vacio';
    ELSE
        NEW.email := LOWER(TRIM(NEW.email));
    END IF;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.propagacion_baja()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
DECLARE
    v_incidencias_cerradas INT := 0;
    v_facturas_vencidas INT := 0;
BEGIN
    IF OLD.estado != 'baja' AND NEW.estado = 'baja' THEN
        UPDATE incidencias SET estado = 'cerrada', fecha_cierre = NOW()
        WHERE contrato_id = NEW.contrato_id AND estado IN ('abierta', 'en_gestion');
        GET DIAGNOSTICS v_incidencias_cerradas = ROW_COUNT;

        UPDATE facturas SET estado_pago = 'vencida'
        WHERE contrato_id = NEW.contrato_id AND estado_pago = 'pendiente';
        GET DIAGNOSTICS v_facturas_vencidas = ROW_COUNT;

        INSERT INTO auditoria (tabla_nombre, operacion, registro_id, datos_despues)
        VALUES ('contratos', 'UPDATE', NEW.contrato_id, jsonb_build_object('motivo', 'baja_contrato', 'contrato_id', NEW.contrato_id));
    END IF;

    RAISE NOTICE 'Contrato % dado de baja. Incidencias cerradas: %, Facturas vencidas: %', NEW.contrato_id, v_incidencias_cerradas, v_facturas_vencidas;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.tg_historico_cambio_tarifa()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
DECLARE
    v_precio_kwh_nuevo NUMERIC;
BEGIN
    IF OLD.tarifa IS DISTINCT FROM NEW.tarifa THEN
        RAISE NOTICE 'Detectado cambio de tarifa en contrato %: % -> %', NEW.contrato_id, OLD.tarifa, NEW.tarifa;

        UPDATE public.historico_tarifas SET fecha_fin = CURRENT_DATE
        WHERE contrato_id = NEW.contrato_id AND fecha_fin IS NULL;

        SELECT tt.precio_kwh INTO v_precio_kwh_nuevo
        FROM public.tramos_tarifa tt
        WHERE tt.tarifa = NEW.tarifa AND tt.vigente_hasta IS NULL
        LIMIT 1;

        IF NOT FOUND THEN
            v_precio_kwh_nuevo := 0.1500;
        END IF;

        INSERT INTO public.historico_tarifas (contrato_id, tarifa, precio_kwh, potencia_kw, fecha_inicio, fecha_fin, motivo_cambio)
        VALUES (NEW.contrato_id, NEW.tarifa, v_precio_kwh_nuevo, NEW.potencia_kw, CURRENT_DATE, NULL, 'Cambio automatico por Trigger (Sistema)');
    END IF;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.tr_fn_actualizar_segmento()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
DECLARE
    v_total_facturado DECIMAL := 0.00;
    v_cliente_id INT := 0;
    v_segmento VARCHAR := '';
BEGIN
    SELECT cl.cliente_id INTO v_cliente_id
    FROM contratos c JOIN clientes cl ON c.cliente_id = cl.cliente_id
    WHERE c.contrato_id = NEW.contrato_id;

    SELECT COALESCE(SUM(f.importe), 0) INTO v_total_facturado
    FROM facturas f JOIN contratos c ON f.contrato_id = c.contrato_id
    WHERE c.cliente_id = v_cliente_id;

    v_segmento := CASE
                      WHEN v_total_facturado > 5000 THEN 'VIP'
                      WHEN v_total_facturado > 2000 THEN 'Premium'
                      WHEN v_total_facturado > 500  THEN 'Regular'
                      ELSE 'Nuevo'
        END;

    UPDATE clientes SET segmento = v_segmento WHERE cliente_id = v_cliente_id;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.tr_fn_auditoria_generica()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO auditoria (tabla_nombre, operacion, registro_id, datos_antes, datos_despues)
    VALUES (
               TG_TABLE_NAME,
               TG_OP,
               COALESCE(
                       (row_to_json(NEW) ->> (LEFT(TG_TABLE_NAME, -1) || '_id'))::INT,
                       (row_to_json(OLD) ->> (LEFT(TG_TABLE_NAME, -1) || '_id'))::INT
               ),
               CASE WHEN TG_OP = 'INSERT' THEN NULL ELSE row_to_json(OLD)::jsonb END,
               CASE WHEN TG_OP = 'DELETE' THEN NULL ELSE row_to_json(NEW)::jsonb END
           );
    RETURN COALESCE(NEW, OLD);
END;
$$;

/*CREATE OR REPLACE FUNCTION public.tr_fn_cambios_tarifa()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE historico_tarifas SET fecha_fin = CURRENT_DATE - 1
    WHERE contrato_id = NEW.contrato_id AND fecha_fin IS NULL;

    INSERT INTO historico_tarifas(contrato_id, tarifa, precio_kwh, potencia_kw, fecha_inicio)
    SELECT NEW.contrato_id, NEW.tarifa, MIN(precio_kwh), NEW.potencia_kw, CURRENT_DATE
    FROM tramos_tarifa WHERE tarifa = NEW.tarifa;

    RETURN NEW;
END;
$$;*/

CREATE OR REPLACE FUNCTION public.tr_fn_limitar_contratos()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
DECLARE
    v_tipo VARCHAR;
    v_cant_contratos INT;
BEGIN
    SELECT tipo INTO v_tipo FROM clientes WHERE cliente_id = NEW.cliente_id;
    SELECT COUNT(*) INTO v_cant_contratos FROM contratos WHERE cliente_id = NEW.cliente_id AND estado = 'ACTIVO';

    IF LOWER(v_tipo) = 'residencial' AND v_cant_contratos >= 3 THEN
        RAISE EXCEPTION 'Cliente residencial no puede tener mas de 3 contratos activos. Tiene: %', v_cant_contratos;
    END IF;

    IF LOWER(v_tipo) = 'empresa' AND v_cant_contratos >= 10 THEN
        RAISE EXCEPTION 'Cliente empresa no puede tener mas de 10 contratos activos. Tiene: %', v_cant_contratos;
    END IF;

    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.tr_fn_proteger_factura_pagada()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    IF UPPER(OLD.estado_pago) = 'PAGADA' THEN
        IF TG_OP = 'UPDATE' THEN
            RAISE EXCEPTION 'No se puede modificar una factura que ya ha sido PAGADA.';
        ELSIF TG_OP = 'DELETE' THEN
            RAISE EXCEPTION 'No se puede eliminar una factura que ya ha sido PAGADA.';
        END IF;
    END IF;

    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    END IF;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.tr_fn_soft_delete()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE clientes SET eliminado = TRUE, fecha_eliminacion = CURRENT_DATE WHERE cliente_id = OLD.cliente_id;
    RETURN NULL;
END;
$$;

CREATE OR REPLACE FUNCTION public.tr_fn_validar_incidencia()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM contratos c WHERE c.contrato_id = NEW.contrato_id AND c.estado = 'ACTIVO') THEN
        RAISE EXCEPTION 'El contrato con ID % no existe o no esta activo', NEW.contrato_id;
    END IF;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.tr_fn_validar_lectura()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
DECLARE
    v_ultima_fecha DATE;
BEGIN
    IF NEW.consumo_kwh < 0 THEN
        RAISE EXCEPTION 'La lectura no puede ser menor a 0';
    END IF;

    SELECT MAX(fecha) INTO v_ultima_fecha FROM lecturas WHERE contrato_id = NEW.contrato_id;
    IF NEW.fecha <= v_ultima_fecha THEN
        RAISE EXCEPTION 'La fecha no puede ser anterior o igual a la ultima registrada';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM contratos WHERE contrato_id = NEW.contrato_id AND estado = 'ACTIVO') THEN
        RAISE EXCEPTION 'El contrato no esta activo';
    END IF;

    RETURN NEW;
END;
$$;

-- ==========================================
-- 5. TRIGGERS
-- ==========================================

CREATE TRIGGER tr_auditoria_facturas
    AFTER INSERT OR DELETE OR UPDATE ON public.facturas
    FOR EACH ROW EXECUTE FUNCTION public.tr_fn_auditoria_generica();

CREATE TRIGGER tr_auditoria_lecturas
    AFTER INSERT OR DELETE OR UPDATE ON public.lecturas
    FOR EACH ROW EXECUTE FUNCTION public.tr_fn_auditoria_generica();

CREATE TRIGGER tr_eliminado_logico
    BEFORE DELETE ON public.clientes
    FOR EACH ROW EXECUTE FUNCTION public.tr_fn_soft_delete();

CREATE TRIGGER tr_fn_auditar_contratos
    AFTER INSERT OR DELETE OR UPDATE ON public.contratos
    FOR EACH ROW EXECUTE FUNCTION public.tr_fn_auditoria_generica();

CREATE TRIGGER tr_propagar_baja_contrato
    AFTER UPDATE ON public.contratos
    FOR EACH ROW EXECUTE FUNCTION public.propagacion_baja();

CREATE TRIGGER tr_proteger_facturas
    BEFORE DELETE OR UPDATE ON public.facturas
    FOR EACH ROW EXECUTE FUNCTION public.tr_fn_proteger_factura_pagada();

-- ==========================================
-- 6. VISTAS
-- ==========================================

CREATE OR REPLACE VIEW public.vw_analisis_consumo_lecturas AS
WITH media_historica AS (
    SELECT l.lectura_id, l.contrato_id, l.fecha, l.consumo_kwh,
           ROUND(AVG(l.consumo_kwh) OVER (PARTITION BY l.contrato_id), 2) AS media_historica_contrato
    FROM public.lecturas l
)
SELECT lectura_id, contrato_id, fecha, consumo_kwh,
       ROUND((consumo_kwh - media_historica_contrato), 2) AS diferencia_con_media
FROM media_historica;

CREATE OR REPLACE VIEW public.vw_clientes_activos AS
SELECT cliente_id, nombre, email, tipo, ciudad, segmento
FROM public.clientes c
WHERE eliminado = FALSE
ORDER BY nombre;

CREATE OR REPLACE VIEW public.vw_incidencias_criticas AS
SELECT i.incidencia_id, i.contrato_id, i.tipo, c2.nombre,
       (CURRENT_DATE - i.fecha_apertura) AS dias_abierta
FROM public.incidencias i
         JOIN public.contratos c ON i.contrato_id = c.contrato_id
         JOIN public.clientes c2 ON c.cliente_id = c2.cliente_id
WHERE i.estado IN ('ABIERTA', 'EN_GESTION', 'CERRADA')
  AND i.tipo IN ('AVERIA', 'FALLO_MEDIDOR', 'CORTE_SUMINISTRO', 'FRAUDE', 'RECLAMACION');

CREATE OR REPLACE VIEW public.vw_facturas_pendientes AS
SELECT factura_id, contrato_id, fecha_emision, importe, estado_pago, fecha_vencimiento
FROM public.facturas f
WHERE LOWER(estado_pago) = 'pendiente';

CREATE OR REPLACE VIEW public.vw_ultima_lectura AS
SELECT lectura_id, contrato_id, fecha, consumo_kwh, tipo_lectura
FROM public.lecturas l
ORDER BY lectura_id DESC
LIMIT 1;

CREATE OR REPLACE VIEW public.vw_resumen_facturacion_cliente AS
SELECT c.cliente_id, c.nombre,
       COUNT(DISTINCT CASE WHEN c2.estado = 'ACTIVO' THEN c2.contrato_id ELSE NULL END) AS cantidad_contratos_activos,
       COALESCE(SUM(f.importe), 0) AS total_facturado,
       COUNT(DISTINCT CASE WHEN UPPER(f.estado_pago) IN ('PENDIENTE', 'VENCIDA') THEN f.factura_id ELSE NULL END) AS facturas_pendientes
FROM public.clientes c
         JOIN public.contratos c2 ON c.cliente_id = c2.cliente_id
         LEFT JOIN public.facturas f ON c2.contrato_id = f.contrato_id
WHERE c2.estado = 'ACTIVO'
GROUP BY c.cliente_id, c.nombre;

CREATE OR REPLACE VIEW public.vw_dashboard_resumen AS
SELECT
    (SELECT COUNT(*) FROM public.clientes c WHERE c.eliminado = FALSE) AS clientes_activos,
    (SELECT COUNT(*) FROM public.contratos c WHERE c.estado = 'ACTIVO') AS contratos_activos,
    (SELECT COUNT(*) FROM public.facturas f WHERE UPPER(f.estado_pago) = 'PENDIENTE') AS facturas_pendientes,
    (SELECT COALESCE(SUM(f.importe), 0) FROM public.facturas f WHERE UPPER(f.estado_pago) = 'PENDIENTE') AS importe_pendiente,
    (SELECT COUNT(*) FROM public.incidencias i WHERE UPPER(i.estado) = 'ABIERTA') AS incidencias_abiertas,
    (SELECT COUNT(*) FROM public.incidencias i WHERE UPPER(i.estado) = 'EN_GESTION') AS incidencias_en_gestion,
    (SELECT COALESCE(SUM(l.consumo_kwh), 0) FROM public.lecturas l WHERE l.fecha >= DATE_TRUNC('month', CURRENT_DATE) AND l.fecha < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month') AS consumo_mes_actual,
    (SELECT COALESCE(SUM(f.importe), 0) FROM public.facturas f WHERE f.fecha_emision >= DATE_TRUNC('month', CURRENT_DATE) AND f.fecha_emision < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' AND UPPER(f.estado_pago) <> 'CANCELADA') AS facturacion_mes_actual,
    (SELECT COALESCE(SUM(f.importe), 0) FROM public.facturas f WHERE f.fecha_emision >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month' AND f.fecha_emision < DATE_TRUNC('month', CURRENT_DATE) AND UPPER(f.estado_pago) <> 'CANCELADA') AS facturacion_mes_anterior,
    (SELECT COALESCE(SUM(f.importe), 0) FROM public.facturas f WHERE UPPER(f.estado_pago) = 'PAGADA') AS importe_cobrado_total,
    (SELECT COALESCE(SUM(f.importe), 0) FROM public.facturas f WHERE f.fecha_emision >= DATE_TRUNC('month', CURRENT_DATE) AND f.fecha_emision < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' AND UPPER(f.estado_pago) = 'PAGADA') AS importe_cobrado_mes_actual,
    (SELECT COUNT(*) FROM public.facturas f WHERE UPPER(f.estado_pago) = 'VENCIDA') AS facturas_vencidas,
    (SELECT COUNT(DISTINCT c.cliente_id) FROM public.clientes c JOIN public.contratos co ON c.cliente_id = co.cliente_id JOIN public.facturas f ON co.contrato_id = f.contrato_id WHERE UPPER(f.estado_pago) = 'PENDIENTE' AND f.fecha_vencimiento < CURRENT_DATE AND c.eliminado = FALSE) AS clientes_con_deuda,
    (SELECT SUM(f.importe) FROM public.facturas f WHERE UPPER(f.estado_pago) = 'PENDIENTE' AND f.fecha_vencimiento < CURRENT_DATE) AS importe_vencido,
    (SELECT COUNT(*) FROM public.vw_incidencias_criticas vic) AS incidencias_criticas;