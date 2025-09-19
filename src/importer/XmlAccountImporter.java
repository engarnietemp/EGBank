package importer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import components.Account;
import components.Client;
import components.CurrentAccount;
import components.SavingsAccount;


public final class XmlAccountImporter {
    private XmlAccountImporter() {}
    
    
    public static Map<Integer, Account> loadAccounts(String filePath) throws Exception {
        Map<Integer, Account> accounts = new HashMap<>();
        Map<Integer, Client> clients = new HashMap<>();
        
        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(Files.newInputStream(Paths.get(filePath)));
        
        NodeList accountNodes = doc.getElementsByTagName("account");
        
        for (int i = 0; i < accountNodes.getLength(); i++) {
            Element elem = (Element) accountNodes.item(i);
            
            // Account Data
            int accountNumber = Integer.parseInt(getValue(elem, "accountNumber"));
            String type = getValue(elem, "type");
            String label = getValue(elem, "label");
            double balance = Double.parseDouble(getValue(elem, "balance"));
            
            // Client Data
            Element clientElem = (Element) elem.getElementsByTagName("client").item(0);
            int clientNumber = Integer.parseInt(getValue(clientElem, "clientNumber"));
            
            Client client = clients.computeIfAbsent(clientNumber,
                num -> new Client(
                    getValue(clientElem, "name"),
                    getValue(clientElem, "firstName"),
                    clientNumber
                ));
            
            // Create Account
            Account account = type.equals("CurrentAccount")
                ? new CurrentAccount(label, client, accountNumber, balance)
                : new SavingsAccount(label, client, accountNumber, balance);
            
            accounts.put(accountNumber, account);
        }
        
        return accounts;
    }
    
    private static String getValue(Element parent, String tag) {
        return parent.getElementsByTagName(tag).item(0).getTextContent();
    }    
}
