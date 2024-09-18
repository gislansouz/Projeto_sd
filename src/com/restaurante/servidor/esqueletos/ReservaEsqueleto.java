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
        // (1) Desempacota argumento de entrada
        Random random = new Random();
        JsonObject jsonRequest = gson.fromJson(args, JsonObject.class);
        JsonObject argsJson = jsonRequest.getAsJsonObject("args");
        JsonObject reservaJson = argsJson.getAsJsonObject("reserva");
        Reserva reserva = new Reserva(
            reservaJson.get("alunoId").getAsString(),
            Integer.toString(random.nextInt(10000)),
            Reserva.converteData(reservaJson.get("data").getAsString()),
            reservaJson.get("tipoRefeicao").getAsString(),
            JSONArrayToList(reservaJson.getAsJsonArray("preferenciasAlimentares"))
            );
		// (2) chama o metodo do servente
        String resultado=reserva.salvarReserva()+reserva.getReservaId();
		// (3) empacota resposta do método servente e retorna
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("messageType", "Reply");
        jsonResponse.addProperty("requestId", " ");
        jsonResponse.addProperty("objName", "Reserva");
        jsonResponse.addProperty("methodName", "ADD");
        jsonResponse.addProperty("args", resultado);
        return gson.toJson(jsonResponse);
	}

	public String list(String args) {
		// (1) Desempacota argumento de entrada
        JsonObject jsonRequest = gson.fromJson(args, JsonObject.class);
        JsonObject argsJson = jsonRequest.getAsJsonObject("args");
        String alunoId = argsJson.get("alunoId").getAsString();
		// (2) chama o metodo do servente
        String resultado = Reserva.listarReservas(alunoId);
		// (3) empacota resposta do método servente e retorna
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("messageType", "Reply");
        jsonResponse.addProperty("requestId", " ");
        jsonResponse.addProperty("objName", "Reserva");
        jsonResponse.addProperty("methodName", "LIST");
        jsonResponse.addProperty("args", resultado);
        return gson.toJson(jsonResponse);
	}

	public String remove(String args) {
		// (1) Desempacota argumento de entrada
        JsonObject jsonRequest = gson.fromJson(args, JsonObject.class);
        JsonObject argsJson = jsonRequest.getAsJsonObject("args");
        String reservaId = argsJson.get("reservaId").getAsString();
		// (2) chama o metodo do servente
        String resultado= Reserva.removerReserva(reservaId);

		// (3) empacota resposta do método servente e retorna
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("messageType", "Reply");
        jsonResponse.addProperty("requestId", " ");
        jsonResponse.addProperty("objName", "Reserva");
        jsonResponse.addProperty("methodName", "REMOVE");
        jsonResponse.addProperty("args", resultado);
        return gson.toJson(jsonResponse);
	}

     private static List<String> JSONArrayToList(JsonArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsString());
        }
        return list;
    }
}

