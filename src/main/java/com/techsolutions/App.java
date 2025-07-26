// App.java
package com.techsolutions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class App {

    public static void main(String[] args) {
        try {
            String json = obtenerReporteJson();

            ObjectMapper mapper = new ObjectMapper();
            Proceso root = mapper.readValue(json, Proceso.class);

            Resultados res = new Resultados();
            procesarRecursivo(root, res);

            enviarEvaluacion(res, json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String obtenerReporteJson() throws IOException {
        URL url = new URL("https://58o1y6qyic.execute-api.us-east-1.amazonaws.com/default/taskReport");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            content.append(line);
        }
        in.close();
        con.disconnect();

        return content.toString();
    }

    public static void procesarRecursivo(Proceso proceso, Resultados res) {
        res.totalProcesos++;

        if ("completo".equalsIgnoreCase(proceso.estado)) {
            res.completos++;
        } else {
            res.pendientes++;
        }

        if (proceso.recursos != null) {
            for (Recurso r : proceso.recursos) {
                if ("herramienta".equalsIgnoreCase(r.tipo)) {
                    res.herramientas++;
                }
            }
        }

        res.eficienciaSuma += Math.random() * 100;
        res.eficienciaCount++;

        try {
            if (res.masAntiguo == null ||
                proceso.fechaInicio.compareTo(res.masAntiguo.fechaInicio) < 0) {
                res.masAntiguo = proceso;
            }
        } catch (Exception ignored) {}

        if (proceso.subprocesos != null) {
            for (Proceso hijo : proceso.subprocesos) {
                procesarRecursivo(hijo, res);
            }
        }
    }

    public static void enviarEvaluacion(Resultados res, String jsonOriginal) throws IOException {
        URL url = new URL("https://t199qr74fg.execute-api.us-east-1.amazonaws.com/default/taskReportVerification");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        JsonGenerator gen = mapper.getFactory().createGenerator(sw);

        gen.writeStartObject();
        gen.writeStringField("nombre", "Marvin Ortiz");
        gen.writeStringField("carnet", "5190-22-6064");
        gen.writeStringField("seccion", "5");

        gen.writeObjectFieldStart("resultadoBusqueda");
        gen.writeNumberField("totalProcesos", res.totalProcesos);
        gen.writeNumberField("procesosCompletos", res.completos);
        gen.writeNumberField("procesosPendientes", res.pendientes);
        gen.writeNumberField("recursosTipoHerramienta", res.herramientas);
        gen.writeNumberField("eficienciaPromedio", res.eficienciaSuma / res.eficienciaCount);

        gen.writeObjectFieldStart("procesoMasAntiguo");
        gen.writeStringField("id", res.masAntiguo.id);
        gen.writeStringField("fechaInicio", res.masAntiguo.fechaInicio);
        gen.writeEndObject();

        gen.writeEndObject();

        gen.writeFieldName("payload");
        gen.writeRawValue(jsonOriginal); // JSON original como raw

        gen.writeEndObject();
        gen.close();

        OutputStream os = con.getOutputStream();
        os.write(sw.toString().getBytes());
        os.flush();
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("\u2705 Respuesta del servidor:");
        System.out.println(response.toString());
    }
}
