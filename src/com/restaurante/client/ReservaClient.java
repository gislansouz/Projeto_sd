package com.restaurante.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.restaurante.client.Reserva;

import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReservaClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9876;
    private static final Gson gson = new Gson();
    private static final int TIMEOUT_MS = 2000; // Timeout de 2 segundos

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            socket.setSoTimeout(TIMEOUT_MS); // Definir timeout para receber respostas

            while (true) {
                System.out.println("\nEscolha uma opção:");
                System.out.println("1. Adicionar reserva");
                System.out.println("2. Remover reserva");
                System.out.println("3. Listar reservas");
                System.out.println("4. Sair");
                System.out.print("Opção: ");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consome a nova linha

                String jsonMessage = "";
                switch (option) {
                    case 1:
                        jsonMessage = prepareAddMessage(scanner);
                        break;
                    case 2:
                        jsonMessage = prepareRemoveMessage(scanner);
                        break;
                    case 3:
                        jsonMessage = prepareListMessage(scanner);
                        break;
                    case 4:
                        System.out.println("Saindo...");
                        return;
                    default:
                        System.out.println("Opção inválida.");
                        continue;
                }

                // Enviar requisição e esperar pela resposta
                boolean responseReceived = false;
                int quantresquest=0;
                while (!responseReceived && quantresquest<=5) {
                    try {
                        sendRequest(socket, jsonMessage);
                        quantresquest++;
                        receiveResponse(socket);
                        responseReceived = true; // Resposta recebida com sucesso
                    } catch (SocketTimeoutException e) {
                        System.out.println("Timeout atingido. Reenviando requisição...");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String prepareAddMessage(Scanner scanner) {
        System.out.print("Digite o ID do aluno: ");
        String alunoId = scanner.nextLine();
        System.out.print("Digite a data (yyyy-mm-dd): ");
        String data = scanner.nextLine();
        System.out.print("Digite o tipo de refeição: ");
        String tipoDeRefeicao = scanner.nextLine();

        List<String> preferenciasAlimentares = new ArrayList<>();

        System.out.print("Digite as preferências alimentares (separadas por vírgula): ");
        String preferenciasInput = scanner.nextLine();
        String[] preferenciasArray = preferenciasInput.split(",");
        for (String pref : preferenciasArray) {
            preferenciasAlimentares.add(pref.trim());
        }
        Reserva reserva = new Reserva(alunoId, " " ,converteData(data), tipoDeRefeicao, preferenciasAlimentares);

        // Criação da mensagem JSON
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("messageType", "Request");
        jsonRequest.addProperty("objName", "Reserva");
        jsonRequest.addProperty("methodName", "ADD");
        jsonRequest.add("reserva", gson.toJsonTree(reserva));

        return gson.toJson(jsonRequest);
    }

    private static String prepareRemoveMessage(Scanner scanner) {
        System.out.print("Digite o ID da reserva para remover a reserva: ");
        String reservaId = scanner.nextLine();

        // Criação da mensagem JSON para remover a reserva
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("messageType", "Request");
        jsonRequest.addProperty("objName", "Reserva");
        jsonRequest.addProperty("methodName", "REMOVE");
        jsonRequest.addProperty("reservaId", reservaId);

        return gson.toJson(jsonRequest);
    }

    private static String prepareListMessage(Scanner scanner) {
        System.out.print("Digite o ID do aluno para listar as reservas: ");
        String alunoId = scanner.nextLine();

        // Criação da mensagem JSON para listar as reservas
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("messageType", "Request");
        jsonRequest.addProperty("objName", "Reserva");
        jsonRequest.addProperty("methodName", "LIST");
        jsonRequest.addProperty("alunoId", alunoId);

        return gson.toJson(jsonRequest);
    }

    private static void sendRequest(DatagramSocket socket, String jsonMessage) throws Exception {
        byte[] sendBuffer = jsonMessage.getBytes();
        InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, SERVER_PORT);
        socket.send(sendPacket);
    }

    private static void receiveResponse(DatagramSocket socket) throws Exception {
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);

        String ResponseData = new String(receivePacket.getData(), 0, receivePacket.getLength());

        JsonObject jsonResponse = gson.fromJson(ResponseData, JsonObject.class);
        String resposta = jsonResponse.get("response").getAsString();

        
        System.out.println("Resposta do servidor: \n" + resposta);
    }

    public static Date converteData(String date){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.ENGLISH);
            try{
            Date data = formato.parse(date);
            return data;
            } catch (ParseException e) {
             // TODO Auto-generated catch block
            e.printStackTrace();
            }
            return new Date();
    }
}
