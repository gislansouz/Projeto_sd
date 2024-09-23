package com.restaurante.servidor.esqueletos;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.restaurante.modelo.Reserva;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReservaEsqueleto {
    private static final Gson gson = new Gson();

	public String add(String args) {
        String resultado="";
        // (1) Desempacota argumento de entrada
        Random random = new Random();
        JsonObject jsonRequest = gson.fromJson(args, JsonObject.class);
        JsonObject argsJson = jsonRequest.getAsJsonObject("args");
        JsonObject reservaJson = argsJson.getAsJsonObject("reserva");
        
        //verificação ser algum dos campos está nulo
        if (reservaJson == null ||
        reservaJson.get("alunoId").getAsString().isEmpty() || 
        reservaJson.get("data").getAsString().isEmpty() ||
        reservaJson.get("tipoRefeicao").getAsString().isEmpty() ||
        reservaJson.getAsJsonArray("preferenciasAlimentares").isEmpty()) {
            resultado= "Erro: Um ou mais campos obrigatórios estão ausentes ou são nulos.";
        }else{

        Reserva reserva = new Reserva(
            reservaJson.get("alunoId").getAsString(),
            Integer.toString(random.nextInt(10000)),
            Reserva.converteData(reservaJson.get("data").getAsString()),
            reservaJson.get("tipoRefeicao").getAsString(),
            JSONArrayToList(reservaJson.getAsJsonArray("preferenciasAlimentares"))
            );
		// (2) chama o metodo do servente
       resultado=reserva.salvarReserva()+" - "+reserva.getReservaId();

        }
		// (3) empacota resposta do método servente e retorna
        return resultado;
	}

	public String list(String args) {
        String resultado="";
		// (1) Desempacota argumento de entrada
        JsonObject jsonRequest = gson.fromJson(args, JsonObject.class);
        JsonObject argsJson = jsonRequest.getAsJsonObject("args");
        if (argsJson.get("alunoId").getAsString().isEmpty()){
            resultado= "Erro: Um ou mais campos obrigatórios estão ausentes ou são nulos.";
        }else{
        String alunoId = argsJson.get("alunoId").getAsString();
		// (2) chama o metodo do servente
        resultado = Reserva.listarReservas(alunoId);
        }
		// (3) empacota resposta do método servente e retorna

        return resultado;
	}

	public String remove(String args) {
        String resultado="";
		// (1) Desempacota argumento de entrada
        JsonObject jsonRequest = gson.fromJson(args, JsonObject.class);
        JsonObject argsJson = jsonRequest.getAsJsonObject("args");
        if (argsJson.get("reservaId").getAsString().isEmpty() || argsJson.get("alunoId").getAsString().isEmpty()){
            resultado= "Erro: Um ou mais campos obrigatórios estão ausentes ou são nulos.";
        }else{
        String reservaId = argsJson.get("reservaId").getAsString();
        String alunoId =argsJson.get("alunoId").getAsString();
		// (2) chama o metodo do servente
        resultado= Reserva.removerReserva(alunoId,reservaId);
        }
		// (3) empacota resposta do método servente e retorna
        return resultado;
	}

     private static List<String> JSONArrayToList(JsonArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsString());
        }
        return list;
    }
}

