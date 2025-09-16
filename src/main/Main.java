package main;

import java.util.ArrayList;
import java.util.Collection;

import components.Client;

// 1.1.2 Creation of main class for tests
public class Main {

	public static void main(String[] args) {
		Collection<Client> clients = generateTestSet(10);
		displayClients(clients);
	}
	
	
	public static Collection<Client> generateTestSet(int setSize) {
		ArrayList<Client> clients = new ArrayList<>();
		
		String name = "name";
		String firstName = "firstName";
		
		for(int i = 0 ; i < setSize ; i++) {
			Client client = new Client(name+i, firstName+i);
			clients.add(client);
		}
		
		return clients;
	}
	
	public static void displayClients(Collection<Client> clients) {
		clients.stream()
			   .forEach(System.out::println);
	}

}
