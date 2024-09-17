package com.restaurante.client;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Reserva implements Serializable{

    private String alunoId;
    private String reservaId;
    private Date data;
    private String tipoRefeicao;
    private List<String> preferenciasAlimentares;

    public Reserva(String alunoId,String reservaId ,Date data, String tipoRefeicao, List<String> preferenciasAlimentares) {
        this.alunoId = alunoId;
        this.reservaId=reservaId;
        this.data = data;
        this.tipoRefeicao = tipoRefeicao;
        this.preferenciasAlimentares = preferenciasAlimentares;
    }

    // Getters e Setters
    public String getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(String alunoId) {
        this.alunoId = alunoId;
    }

    public String getReservaId() {
        return reservaId;
    }

    public void setReservaId(String reservaId) {
        this.reservaId = reservaId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTipoRefeicao() {
        return tipoRefeicao;
    }

    public void setTipoRefeicao(String tipoRefeicao) {
        this.tipoRefeicao = tipoRefeicao;
    }

    public List<String> getPreferenciasAlimentares() {
        return preferenciasAlimentares;
    }

    public void setPreferenciasAlimentares(List<String> preferenciasAlimentares) {
        this.preferenciasAlimentares = preferenciasAlimentares;
    }

}