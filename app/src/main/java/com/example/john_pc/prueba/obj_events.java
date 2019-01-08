package com.example.john_pc.prueba;

public class obj_events {

    protected long id;
    protected String variable;
    protected String fecha_inicio;
    protected String fecha_fin;

    public obj_events(long id, String variable, String fecha_inicio, String fecha_fin) {

        this.id = id;
        this.variable = variable;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;

    }

    public long getId() {

        return id;

    }

    public void setId(long id) {

        this.id = id;

    }

    public String getVariable() {

        return variable;

    }

    public void setVariable(String variable) {

        this.variable = variable;

    }

    public String getFecha_inicio() {

        return fecha_inicio;

    }

    public void setFecha_inicio(String fecha_inicio) {

        this.fecha_inicio = fecha_inicio;

    }

    public String getFecha_fin() {

        return fecha_fin;

    }

    public void setFecha_fin(String fecha_fin) {

        this.fecha_fin = fecha_fin;

    }

}
