package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import components.Client;
import components.CurrentAccount;
import components.Flow;
import components.SavingsAccount;
import components.Account;
import components.Transfert;
import components.Credit;
import components.Debit;

import importer.JsonFlowImporter;
import importer.XmlAccountImporter;



// 1.1.2 Creation of main class for tests
public class Main {
	
	private static final String XML_PATH = "./data/accounts.xml";
	private static final String JSON_PATH = "./data/flows.json";

	public static void main(String[] args) {
	    try {
	        
	    	// 1.1.2 & 1.2.3 & 1.3.4 First way to generate collections
	        /*
	        Collection<Client> clients = generateClientCollection(10);
	        displayClients(clients);
	        Collection<Account> accounts = generateAccountCollection(clients);
	        displayAccounts(accounts);
	        Map<Integer, Account> accountsMap = buildAccountMap(accounts);
	        Collection<Flow> flows = generateFlows(accountsMap);
	        */
	        
	        // 2.1 Loading with JSON & XML Files
	    	
	        System.out.println("Chargement depuis XML et JSON...\n");
	        
	        Map<Integer, Account> accountsMap = XmlAccountImporter.loadAccounts(XML_PATH);
	        
	        System.out.println(accountsMap.size() + " comptes chargés depuis le fichier XML :\n"); 
	        displayAccountsSortedByBalance(accountsMap);
	        
	        List<Flow> flows = JsonFlowImporter.loadFlows(JSON_PATH);

	        System.out.println("\n" + flows.size() + " flows chargés depuis JSON :\n");
	        flows.stream().forEach(Flow::toString);
	        
	        System.out.println("\nApplication des flows...");
	        
	        applyFlows(accountsMap, flows);
	        
	        System.out.println("\n========== État final des comptes ==========\n");
	        displayAccountsSortedByBalance(accountsMap);
	        
	    } catch (Exception e) {
	        System.err.println("Erreur: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	
	// 1.1.2 Generation of the Client test set
	public static Collection<Client> generateClientCollection(int setSize) {
		ArrayList<Client> clients = new ArrayList<>();
		
		for(int i = 0 ; i < setSize ; i++) {
			Client client = new Client("name"+i, "firstName"+i);
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
			
			if(targetAccount != null) {
				targetAccount.setBalance(flow);
			} 
			else {
				System.err.println("Compte cible "+flow.getTargetAccountNumber() + "introuvable");
			}
			
			if(flow instanceof Transfert transfert) {
				Account issuerAccount = accounts.get(transfert.getAccountIssuerNumber());
				
				issuerAccount.setBalance(flow);
			}
		}
		checkNegativeBalances(accounts);
	}
	
	// 1.3.5 Check negative balance
	public static void checkNegativeBalances(Map<Integer, Account> accounts) {

	    Predicate<Account> hasNegativeBalance = account -> account.getBalance() < 0;
	    
	    Optional<Account> negativeAccount = accounts.values().stream()
	        .filter(hasNegativeBalance)
	        .findFirst();
	    
	    negativeAccount.ifPresent(account -> 
	        System.err.println("⚠️ Warning : Account n°" + account.getAccountNumber() 
	            + " has a negative balance : " + account.getBalance() + "€")
	    );		
	}
	
}
