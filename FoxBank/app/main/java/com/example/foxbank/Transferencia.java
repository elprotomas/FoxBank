package com.example.foxbank;

public class Transferencia {
    private String tipoTransaccion;
    private String monto;
    private String fecha;
    private String descripcion;

    // Constructor
    public Transferencia(String tipoTransaccion, String monto, String fecha, String descripcion) {
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    // Getters
    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public String getMonto() {
        return monto;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
