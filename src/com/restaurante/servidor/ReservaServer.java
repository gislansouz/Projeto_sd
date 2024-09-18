package com.restaurante.servidor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.restaurante.servidor.Despachante;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ReservaServer {
    private static final int PORT = 9876;
    private static final Gson gson = new Gson();
    private static final Map<String, String> responseHistory = new HashMap<>(); // Histórico de respostas
    static Despachante despachante;
    static String requestId="";

    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(PORT);
            System.out.println("Servidor aguardando em porta " + PORT + "...");

            while (true) {
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);


                String requestData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Requisição recebida: " + requestData);
                String response = processRequest(requestData);

                // Prevenir duplicatas
                if (!responseHistory.containsKey(requestId)) {
                    responseHistory.put(requestId, response);
                    sendResponse(socket, response, receivePacket.getAddress(), receivePacket.getPort());
                } else {
                    String respostaduplicada =responseHistory.get(requestId);
                    sendResponse(socket, respostaduplicada, receivePacket.getAddress(), receivePacket.getPort());
                }
                // Limite do tamanho do cache (por exemplo, manter as últimas 5 requisições)
                if (responseHistory.size() > 5) {
                    responseHistory.remove(responseHistory.keySet().iterator().next());  // Remover o mais antigo
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private static String processRequest(String requestData) {
        try {
            despachante = new Despachante();
            JsonObject jsonRequest = new JsonObject();
            jsonRequest = gson.fromJson(requestData, JsonObject.class);
            String command = jsonRequest.get("methodName").getAsString();
            String objname = jsonRequest.get("objName").getAsString();
            requestId = jsonRequest.get("requestId").getAsString();
            String resultado = despachante.SelecionarEsqueleto("com.restaurante.servidor.esqueletos."+objname+"Esqueleto", command.toLowerCase(), requestData);
            return resultado;
        } catch (Exception e) {
            return "Erro ao processar a requisição: " + e.getMessage();
        }
    }

    private static void sendResponse(DatagramSocket socket, String response, InetAddress clientAddress, int clientPort) throws Exception {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse = gson.fromJson(response, JsonObject.class);
        jsonResponse.addProperty("requestId", requestId);
        
        String reply=gson.toJson(jsonResponse);

        byte[] sendBuffer = reply.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
        socket.send(sendPacket);
        System.out.println("resposta enviada");
    }
}