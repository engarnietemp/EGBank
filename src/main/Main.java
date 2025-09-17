package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Optional;
import java.util.function.Predicate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import components.Client;
import components.CurrentAccount;
import components.Flow;
import components.SavingsAccount;
import components.Account;
import components.Transfert;
import components.Credit;
import components.Debit;

import java.time.LocalDateTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



// 1.1.2 Creation of main class for tests
public class Main {

	public static void main(String[] args) {
	    try {
	        // 1.1.2 & 1.2.3 & 1.3.4 First way to generate collections
	        /*
	        Collection<Client> clients = generateClientCollection(10);
	        Collection<Account> accounts = generateAccountCollection(clients);
	        Map<Integer, Account> accountsMap = buildAccountMap(accounts);
	        Collection<Flow> flows = generateFlows(accountsMap);
	        */
	        
	        // 2.1 Loading with JSON & XML Files
	    	
	        System.out.println("=== Chargement depuis XML et JSON ===\n");
	        
	        Map<Integer, Account> accountsMap = loadAccountsFromXML("./data/accounts.xml");
	        
	        displayAccountsSortedByBalance(accountsMap);
	        
	        List<Flow> flows = loadFlowsFromJson("./data/flows.json");
	        System.out.println("\n" + flows.size() + " flows chargés depuis JSON");
	        
	        System.out.println("\n=== Application des flows ===");
	        applyFlows(accountsMap, flows);
	        
	        System.out.println("\n=== État final des comptes ===\n");
	        displayAccountsSortedByBalance(accountsMap);
	        
	    } catch (Exception e) {
	        System.err.println("Erreur: " + e.getMessage());
	        e.printStackTrace();
	    }
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
	
	
	/* ==================== 2.1 JSON FILE OF FLOWS ==================== */
	
	public static List<Flow> loadFlowsFromJson(String filePath) throws IOException {
	    Path path = Paths.get(filePath);
	    List<Flow> flows = new ArrayList<>();
	    
	    try (BufferedReader reader = Files.newBufferedReader(path)) {
	        StringBuilder content = new StringBuilder();
	        String line;
	        
	        while ((line = reader.readLine()) != null) {
	            content.append(line);
	        }
	        
	        String json = content.toString();
	        
	        /* MANUAL PARSING */
	        
	        json = json.trim();
	        if (json.startsWith("[") && json.endsWith("]")) {
	            json = json.substring(1, json.length() - 1);
	        }
	        
	        List<String> flowObjects = splitJsonObjects(json);
	        
	        for (String flowJson : flowObjects) {
	            Flow flow = parseFlowFromJson(flowJson);
	            if (flow != null) {
	                flows.add(flow);
	            }
	        }
	    }
	    
	    return flows;
	}
	
	private static List<String> splitJsonObjects(String json) {
	    List<String> objects = new ArrayList<>();
	    
	    int braceCount = 0;
	    StringBuilder current = new StringBuilder();
	    
	    for (char c : json.toCharArray()) {
	        
	    	// Only 1 level of depth to parse
	    	if (c == '{') {
	            
	    		if (braceCount == 0 && !current.isEmpty()) {
	            	current = new StringBuilder();
	            }
	    		braceCount++;
	        }	        
	        current.append(c);
	        
	        if (c == '}') {        
	        	braceCount--;
	            
	            if (braceCount == 0) {
	                objects.add(current.toString().trim());
	                current = new StringBuilder();
	            }
	        }
	    }
	    
	    return objects;
	}
	
	private static Flow parseFlowFromJson(String jsonObject) {
	    try {
	        String type = extractJsonValue(jsonObject, "type");
	        
	        if (type == null || type.isEmpty()) {
	            System.err.println("Type manquant dans: " + jsonObject);
	            return null;
	        }
	        
	        String comment = extractJsonValue(jsonObject, "comment");
	        double amount = Double.parseDouble(extractJsonValue(jsonObject, "amount"));
	        int targetAccountNumber = Integer.parseInt(extractJsonValue(jsonObject, "targetAccountNumber"));
	        
	        Flow flow;
	        switch (type) {
	            case "Credit":
	                flow = new Credit(targetAccountNumber, amount, comment);
	                break;
	            
	            case "Debit":
	                flow = new Debit(targetAccountNumber, amount, comment);
	                break;
	            
	            case "Transfert":
	                String issuerStr = extractJsonValue(jsonObject, "accountIssuerNumber");
	                
	                if (issuerStr == null || issuerStr.isEmpty()) {
	                    System.err.println("Compte émetteur manquant pour le transfert");
	                    return null;
	                }
	                
	                int issuerAccountNumber = Integer.parseInt(issuerStr);
	                flow = new Transfert(issuerAccountNumber, targetAccountNumber, amount, comment);
	                break;
	            
	            default:
	                System.err.println("Unknowed Type flow : " + type);
	                return null;
	        }
	        
	        // Optionnal Date
	        String dateStr = extractJsonValue(jsonObject, "date");
	        if (dateStr != null && !dateStr.isEmpty()) {
	            flow.setDateOfFlow(LocalDateTime.parse(dateStr));
	        }
	        
	        return flow;
	        
	    } catch (NumberFormatException e) {
	        System.err.println("Erreur de format numérique: " + e.getMessage());
	        return null;
	    } catch (Exception e) {
	        System.err.println("Erreur parsing JSON: " + e.getMessage());
	        return null;
	    }
	}

	private static String extractJsonValue(String json, String key) {
	    Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"?([^,}\"]+)\"?");
	    Matcher matcher = pattern.matcher(json);
	    
	    return matcher.find() ? matcher.group(1).trim() : null;
	}
	
	/* ==================== 2.2 XML FILE OF ACCOUNTS ==================== */
	public static Map<Integer, Account> loadAccountsFromXML(String filePath) throws Exception {
	    
		Path path = Paths.get(filePath);
	    Map<Integer, Account> accountsMap = new HashMap<>();
	    
	    try (InputStream inputStream = Files.newInputStream(path)) {
	        
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        
	        Document doc = builder.parse(inputStream);
	        
	        doc.getDocumentElement().normalize();
	        
	        NodeList accountList = doc.getElementsByTagName("account");
	        
	        Map<Integer, Client> clientsCache = new HashMap<>();
	        
	        for (int i = 0; i < accountList.getLength(); i++) {
	            Node accountNode = accountList.item(i);
	            
	            if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
	            	
	                Element accountElement = (Element) accountNode;
	                
	                // Account Data
	                int accountNumber = Integer.parseInt(getElementValue(accountElement, "accountNumber"));
	                String type = getElementValue(accountElement, "type");
	                String label = getElementValue(accountElement, "label");
	                double balance = Double.parseDouble(getElementValue(accountElement, "balance"));
	                
	                // Client Data
	                Element clientElement = (Element) accountElement.getElementsByTagName("client").item(0);
	                int clientNumber = Integer.parseInt(getElementValue(clientElement, "clientNumber"));
	                String clientName = getElementValue(clientElement, "name");
	                String clientFirstName = getElementValue(clientElement, "firstName");
	                
	                // Create or retrieve Client
	                Client client = clientsCache.computeIfAbsent(clientNumber, 
	                    num -> new Client(clientName, clientFirstName, clientNumber));
	                
	                // Create Account with the associate constructor
	                Account account;
	                
	                if ("CurrentAccount".equals(type)) {
	                    account = new CurrentAccount(label, client, accountNumber, balance);
	                } 
	                else if ("SavingsAccount".equals(type)) {
	                    account = new SavingsAccount(label, client, accountNumber, balance);
	                } 
	                else {
	                    System.err.println("Type de compte inconnu: " + type);
	                    continue;
	                }
	                
	                accountsMap.put(accountNumber, account);
	            }
	        }
	        
	        System.out.println("Chargé " + accountsMap.size() + " comptes depuis XML");
	    } catch (Exception e) {
	        System.err.println("Erreur chargement XML: " + e.getMessage());
	        throw e;
	    }
	    
	    return accountsMap;
	}

	private static String getElementValue(Element parent, String tagName) {
	    NodeList nodeList = parent.getElementsByTagName(tagName);
	    
	    if (nodeList.getLength() > 0) {
	        
	    	Node node = nodeList.item(0);
	        
	    	if (node != null && node.getFirstChild() != null) {
	            return node.getFirstChild().getNodeValue();
	        }
	    }
	    return "";
	}
}
