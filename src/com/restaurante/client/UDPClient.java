package com.restaurante.client;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPClient {
    String server_andress = null;
    DatagramSocket socket=null;
    int server_port = 0;
    private static final int TIMEOUT_MS = 2000; // Timeout de 2 segundos

	public UDPClient(String serverIP, int port) {
            server_andress=serverIP;
            server_port=port;
	}

	public void sendRequest(String jsonMessage) throws Exception {
                socket = new DatagramSocket();
                byte[] sendBuffer = jsonMessage.getBytes();
                InetAddress serverAddress = InetAddress.getByName(server_andress);
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, server_port);
                socket.send(sendPacket);
        }

	public String getReply(DatagramSocket socket) throws Exception{
            socket.setSoTimeout(TIMEOUT_MS);
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                try {
                    socket.receive(receivePacket);
                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout atingido.");
                }
            
            String ResponseData = new String(receivePacket.getData(), 0, receivePacket.getLength());
            //System.out.println(ResponseData);
            return ResponseData;
    }
	}

