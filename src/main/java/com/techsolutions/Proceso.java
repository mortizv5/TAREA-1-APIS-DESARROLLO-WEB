package com.techsolutions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Proceso {
    public String id;
    public String nombre;
    public String tipo;
    public String estado;
    public String prioridad;
    public String fechaInicio;
    public String responsable;
    public List<Recurso> recursos;
    public List<Proceso> subprocesos;
}
