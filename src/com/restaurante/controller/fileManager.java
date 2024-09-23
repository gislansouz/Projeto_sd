package com.restaurante.controller;
    import java.io.*;
    import java.util.*;
    import com.restaurante.modelo.Reserva;
        
        public class fileManager {
            private static final String FILE_NAME = "reservas.txt";  

            static {
            File file = new File(FILE_NAME);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                    System.out.println("Arquivo criado: " + FILE_NAME);
                }
            } catch (IOException e) {
                System.err.println("Erro ao criar o arquivo: " + e.getMessage());
            }
        }
        
            public static String addReserva(Reserva reserva) {
                String[] resultado =listReservas(reserva.getAlunoId()).split("\n");
                if(resultado.length <3){
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                    writer.write(reserva.toString());
                    writer.newLine();
                    return "Reserva adicionada com sucesso.";
                } catch (IOException e) {
                    
                    return "Erro ao adicionar a reserva: " + e.getMessage();
                }
            }else{
                return "Numero de reservas excedido";
            }
            }
        
            public static String removeReserva(String alunoId,String reservaId) {
                File file = new File(FILE_NAME);
                List<Reserva> reservas = new ArrayList<>();
        
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Reserva reserva = Reserva.fromString(line);
                        if (!reserva.getReservaId().equals(reservaId)&&!reserva.getAlunoId().equals(alunoId)) {
                            reservas.add(reserva);
                        }
                    }
                } catch (IOException e) {
                    return "Erro ao ler o arquivo: " + e.getMessage();
                }
        
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (Reserva reserva : reservas) {
                        writer.write(reserva.toString());
                        writer.newLine();
                    }
                    return "Reserva removida, se existia.";
                } catch (IOException e) {
                    return "Erro ao remover a reserva: " + e.getMessage();
                }
            }
        
            public static String listReservas(String alunoId) {
                File file = new File(FILE_NAME);
                StringBuilder response = new StringBuilder();
        
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Reserva reserva = Reserva.fromString(line);
                        if (reserva.getAlunoId().equals(alunoId)) 
                            response.append(reserva.toString()).append("\n");
                    }
                    return response.length() == 0 ? "Nenhuma reserva encontrada." : response.toString();
                } catch (IOException e) {
                    return "Erro ao ler o arquivo: " + e.getMessage();
                }
            }
        }
        

