package com.example.urbify.models;

public class ReporteVisitanteDTO {
    private String placa;
    private boolean masDe72Horas;
    private String nombrePropietario;
    private int apartamento;
    private Integer visitasMes;

    public ReporteVisitanteDTO(String placa, boolean masDe72Horas, String nombrePropietario, int apartamento, Integer visitasMes) {
        this.placa = placa;
        this.masDe72Horas = masDe72Horas;
        this.nombrePropietario = nombrePropietario;
        this.apartamento = apartamento;
        this.visitasMes = visitasMes;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public boolean isMasDe72Horas() {
        return masDe72Horas;
    }

    public void setMasDe72Horas(boolean masDe72Horas) {
        this.masDe72Horas = masDe72Horas;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public int getApartamento() {
        return apartamento;
    }

    public void setApartamento(int apartamento) {
        this.apartamento = apartamento;
    }

    public Integer getVisitasMes() {
        return visitasMes;
    }

    public void setVisitasMes(Integer visitasMes) {
        this.visitasMes = visitasMes;
    }

    @Override
    public String toString() {
        return "ReporteVisitanteDTO{" +
                "placa='" + placa + '\'' +
                ", masDe72Horas=" + masDe72Horas +
                ", nombrePropietario='" + nombrePropietario + '\'' +
                ", apartamento='" + apartamento + '\'' +
                ", visitasMes=" + visitasMes +
                '}';
    }
}
