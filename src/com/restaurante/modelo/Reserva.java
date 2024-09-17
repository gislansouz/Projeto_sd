package com.restaurante.modelo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import com.restaurante.controller.fileManager;
import java.util.Locale;

public class Reserva implements Serializable{

    private String alunoId;
    private String reservaId;
    private Date data;
    private String tipoRefeicao;
    private List<String> preferenciasAlimentares;
    static SimpleDateFormat formate = new SimpleDateFormat("MMM d, yyyy, hh:mm:ss a",Locale.ENGLISH);
    static SimpleDateFormat formatosimples = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.ENGLISH);

    public Reserva(String alunoId,String reservaId ,Date data, String tipoRefeicao, List<String> preferenciasAlimentares) {
        this.alunoId = alunoId;
        this.reservaId=reservaId;
        this.data = data;
        this.tipoRefeicao = tipoRefeicao;
        this.preferenciasAlimentares = preferenciasAlimentares;
    }
    public Reserva() {
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

  @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("|");
        for (String pref : preferenciasAlimentares) {
            joiner.add(pref);
        }
        return alunoId + ","+ reservaId +"," + formatosimples.format(data) + "," + tipoRefeicao + "," + joiner.toString();
    }

    public static Reserva fromString(String linha) {
        String[] parts = linha.split(",",5);
        String alunoId = parts[0];
        String reservaId=parts[1];
        String tipoDeRefeicao = parts[3];
        List<String> preferenciasAlimentares = List.of(parts[4].split("\\|"));
        return new Reserva(alunoId,reservaId, converteDatasimples(parts[2]), tipoDeRefeicao, preferenciasAlimentares);
    }

     // Chama métodos do FileManager para manipulação
     public String salvarReserva() {
        return fileManager.addReserva(this);
    }

    public static String removerReserva(String reservaId) {
        return fileManager.removeReserva(reservaId);
    }

    public static String listarReservas(String alunoId) {
        return fileManager.listReservas(alunoId);
    }

    public static Date converteData(String date){
            try{
            Date data = formate.parse(date);
            return data;
            } catch (ParseException e) {
             // TODO Auto-generated catch block
            e.printStackTrace();
            }
            return new Date();
    }
    public static Date converteDatasimples(String date){
        try{
        Date data = formatosimples.parse(date);
        return data;
        } catch (ParseException e) {
         // TODO Auto-generated catch block
        e.printStackTrace();
        }
        return new Date();
}

}