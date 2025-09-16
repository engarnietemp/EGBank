package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import components.Client;
import components.CurrentAccount;
import components.Flow;
import components.SavingsAccount;
import components.Account;
import components.Transfert;
import components.Credit;
import components.Debit;


// 1.1.2 Creation of main class for tests
public class Main {

	public static void main(String[] args) {
		
		// 1.1.2
		Collection<Client> clients = generateClientCollection(10);
		displayClients(clients);
		
		// 1.2.3
		Collection<Account> accounts = generateAccountCollection(clients);
		displayAccounts(accounts);
		
		// 1.3.1
		Map<Integer, Account> accountsMap = buildAccountMap(accounts);
		
		// 1.3.4
		Collection<Flow> flows = generateFlows(accountsMap);
		
		// 1.3.5
		applyFlows(accountsMap, flows);
	
	}
	
	// 1.1.2 Generation of the Client test set
	public static Collection<Client> generateClientCollection(int setSize) {
		ArrayList<Client> clients = new ArrayList<>();
		
		String name = "name";
		String firstName = "firstName";
		
		for(int i = 0 ; i < setSize ; i++) {
			Client client = new Client(name+i, firstName+i);
			clients.add(client);
		}
		
		return clients;
	}
	
	
	// 1.1.2 Displaying client set
	public static void displayClients(Collection<Client> clients) {
		clients.stream()
			   .forEach(System.out::println);
	}

	
	// 1.2.3 Creation of the tablea account
	public static Collection<Account> generateAccountCollection(Collection<Client> clients) {
		ArrayList<Account> accounts = new ArrayList<>();
		for(Client client : clients) {
			CurrentAccount currentAccount = new CurrentAccount("Compte courant", client);
			SavingsAccount savingsAccount = new SavingsAccount("Epargne", client);
			
			currentAccount.resetBalance();
			savingsAccount.resetBalance();
			
			accounts.add(currentAccount);
			accounts.add(savingsAccount);
		}
		
		return accounts;
	}
	
	
	// 1.2.3 Displaying accounts test set
	public static void displayAccounts(Collection<Account> accounts) {
		accounts.stream()
				.forEach(System.out::println);
	}
	
	
	
	// 1.2.3 Displaying accounts in Balance asc order
	public static void displayAccountsSortedByBalance(Map<Integer, Account> accounts) {
		accounts.values().stream()
				.sorted(Comparator.comparingDouble(Account::getBalance))
				.forEach(System.out::println);
	}
	
	
	// 1.3.1 Adaptation of the table content
	public static Map<Integer, Account> buildAccountMap(Collection<Account> oldAccounts) {
		HashMap<Integer, Account> newAccounts = new HashMap<>();
		
		for(Account account : oldAccounts) {
			newAccounts.put(account.getAccountNumber(), account);
		}
		
		return newAccounts;
	}
	
	
	// 1.3.4 Creation of the flow Array
	public static Collection<Flow> generateFlows(Map<Integer, Account> accountsMap) {
	    ArrayList<Flow> flows = new ArrayList<>();
	    
	    flows.add(new Debit(1, 50.0, "Frais bancaires"));
	    
	    for (Account account : accountsMap.values()) {
	        if (account instanceof CurrentAccount) {
	            flows.add(new Credit(account.getAccountNumber(), 100.50, "Bonus courant"));
	        }
	    }
	    
	    for (Account account : accountsMap.values()) {
	        if (account instanceof SavingsAccount) {
	            flows.add(new Credit(account.getAccountNumber(), 1500.0, "Intérêts épargne"));
	        }
	    }

	    flows.add(new Transfert(1, 2, 50.0, "Virement interne"));
	    
	    return flows;
	}
	
	
	// 1.3.5 Updating accounts
	public static void applyFlows(Map<Integer, Account> accounts, Collection<Flow> flows) {
		for(Flow flow : flows) {
			
			Account targetAccount = accounts.get(flow.getTargetAccountNumber());
			targetAccount.setBalance(flow);
			
			if(flow instanceof Transfert transfert) {
				Account issuerAccount = accounts.get(transfert.getAccountIssuerNumber());
				
				issuerAccount.setBalance(flow);
			}
		}
	}
	
}
