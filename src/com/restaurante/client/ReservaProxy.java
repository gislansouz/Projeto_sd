package com.restaurante.client;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.restaurante.client.Reserva;

public class ReservaProxy {
	int requestId = 0;
	private static final Gson gson = new Gson();

	public String list(String alunoId) {
		// (1) Empacota argumentos de entrada (ex: nomeAgenda)
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("alunoId", alunoId);

		// (2) Chama doOperation
		String response=doOperation("Reserva","LIST",jsonRequest);
		// (3) Desempacota argumento de resposta (retorno de doOperation)
		// (4) Retorna reposta desempacotada
        return response;
	}

	public String add(Reserva reserva) {
		// (1) Empacota argumentos de entrada
        // Criação da mensagem JSON
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.add("reserva", gson.toJsonTree(reserva));
		// (2) Chama doOperation

        String response=doOperation("Reserva","ADD",jsonRequest);
		// (3) Desempacota argumento de resposta (retorno de doOperation)
		// (4) Retorna reposta desempacotada
		return response;
	}

	public String remove(String alunoId,String reservaId) {
		// (1) Empacota argumentos de entrada
        // Criação da mensagem JSON para remover a reserva
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("alunoId", alunoId);
        jsonRequest.addProperty("reservaId", reservaId);
		// (2) Chama doOperation
		String response=doOperation("Reserva","REMOVE",jsonRequest);
		// (3) Desempacota argumento de resposta (retorno de doOperation)
		// (4) Retorna reposta desempacotada
		return response;
	}

	public String doOperation(String objectRef, String method, JsonElement args) {
		UDPClient udpClient = new UDPClient("localhost", 9876);
		String data = empacotaMensagem(objectRef, method, args);
		String resposta = "";
		int tentativas = 0;
		boolean sucesso = false;
	
		while (tentativas <= 5 && !sucesso) {
			try {
				// envio
				udpClient.sendRequest(data);
				// recebimento
				resposta = desempacotaMensagem(udpClient.getReply(udpClient.socket));
				sucesso = true; // Se a resposta for recebida, marca como sucesso
			} catch (SocketTimeoutException e) {
				tentativas++;
				System.out.println("Tentativa " + tentativas + " falhou devido a timeout.");
				if (tentativas == 5) {
					return "Servidor inalcançável";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "Erro desconhecido: " + e.getMessage();
			}
		}
	
		return resposta;
	}

    private String empacotaMensagem(String objectRef, String method, JsonElement args) {
		// empacota a Mensagem de requisicao
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("messageType", "Request");
        jsonRequest.addProperty("requestId", requestId);
        jsonRequest.addProperty("objName", objectRef);
        jsonRequest.addProperty("methodName", method);
        jsonRequest.add("args", args);
        return gson.toJson(jsonRequest);
	}

	private String desempacotaMensagem(String resposta) {
		// desempacota a mensagem de resposta
        JsonObject jsonResponse = gson.fromJson(resposta, JsonObject.class);
        String response = jsonResponse.get("args").getAsString();
		requestId++;
        return response;
	}
}
