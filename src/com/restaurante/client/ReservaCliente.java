package com.restaurante.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ReservaCliente {

	ReservaProxy proxy;

	public ReservaCliente() {
		proxy = new ReservaProxy();
	}

	public int selecionaOperacao(int operacao) throws IOException {
		Scanner scanner = new Scanner(System.in);
		switch (operacao) {
		case 1:
		System.out.print("Digite o ID do aluno: ");
        String alunoId = scanner.nextLine();
		Date dataValida = solicitarData();
        System.out.print("Digite o tipo de refeição: ");
        String tipoDeRefeicao = scanner.nextLine();
        List<String> preferenciasAlimentares = new ArrayList<>();
        System.out.print("Digite as preferências alimentares (separadas por vírgula): ");
        String preferenciasInput = scanner.nextLine();
        String[] preferenciasArray = preferenciasInput.split(",");
        for (String pref : preferenciasArray) {
            preferenciasAlimentares.add(pref.trim());
        }
        Reserva reserva = new Reserva(alunoId, " " , dataValida, tipoDeRefeicao, preferenciasAlimentares);
		System.out.println(proxy.add(reserva));
			break;

		case 2:
		System.out.print("Digite o ID do aluno para remover a reserva: ");
        String alunoID = scanner.nextLine();
		System.out.print("Digite o ID da reserva para remover a reserva: ");
        String reservaId = scanner.nextLine();

		System.out.println(proxy.remove(alunoID, reservaId));
			break;

		case 3:
		System.out.print("Digite o ID do aluno para listar as reservas: ");
        String alunoIdd = scanner.nextLine();
		System.out.println(proxy.list(alunoIdd));
			break;
		case 4:
			break;

		default:
			System.out.println("Operação invalida, tente outra.");
			break;
		}
		return operacao;
	}

	public void printMenu() {
		System.out.println("\nEscolha uma opção:");
        System.out.println("1. Adicionar reserva");
        System.out.println("2. Remover reserva");
    	System.out.println("3. Listar reservas");
        System.out.println("4. Sair");
        System.out.print("Opção: ");
	}

	public static Date converteData(String date) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        formato.setLenient(false);  // Para garantir que a data está realmente no formato esperado
        try {
            // Tenta fazer o parsing da data
            Date data = formato.parse(date);
            return data;
        } catch (ParseException e) {
            // Caso o formato esteja incorreto, avisa o usuário
            System.out.println("Erro: A data não está no formato correto (dd/MM/yyyy HH:mm). Tente novamente.");
            return null; // Retorna null para indicar erro
        }
    }

	public static Date solicitarData() {
        Scanner scanner = new Scanner(System.in);
        Date data = null;

        // Continua solicitando a data até que seja fornecida uma válida
        while (data == null) {
            System.out.println("Insira a data no formato correto (dd/MM/yyyy HH:mm):");
            String input = scanner.nextLine();
            data = converteData(input);  // Tenta converter a data

        }

        return data;  // Retorna a data válida
    }

	public static void main(String[] args) {
		Scanner scannerop = new Scanner(System.in);
		ReservaCliente reservaclient = new ReservaCliente();
		int operacao = -1;
		do {
			reservaclient.printMenu();
			int option = scannerop.nextInt();
            scannerop.nextLine(); // Consome a nova linha
			try {
				operacao = reservaclient.selecionaOperacao(option);
			} catch (IOException ex) {
				System.out.println("Escolha uma das operações pelo número");
			}
		} while (operacao != 4);
	}
}
