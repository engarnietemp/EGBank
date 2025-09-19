package importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import components.Flow;
import components.Debit;
import components.Credit;
import components.Transfert;

public final class JsonFlowImporter {
    
	// We don't want to instantiate it.
    private JsonFlowImporter() {}
    
    public static List<Flow> loadFlows(String filePath) throws IOException {
        String json = Files.readString(Paths.get(filePath));
        List<Flow> flows = new ArrayList<>();
        
        Pattern pattern = Pattern.compile("\\{[^{}]*\"type\"[^{}]*\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);
        
        while (matcher.find()) {
            
        	String flowJson = matcher.group();
            Flow flow = parseFlow(flowJson);
            
            if (flow != null) {
                flows.add(flow);
            }
        }
        
        return flows;
    }
    
    private static Flow parseFlow(String json) {
        try {
            String type = extractValue(json, "type");
            
	        if (type == null || type.isEmpty()) {
	            System.err.println("Type manquant dans: " + json);
	            return null;
	        }
	        
            String comment = extractValue(json, "comment");
            double amount = Double.parseDouble(extractValue(json, "amount"));
            int targetAccount = Integer.parseInt(extractValue(json, "targetAccountNumber"));
            
            Flow flow = null;
            
            switch (type) {
                case "Credit":
                    flow = new Credit(targetAccount, amount, comment);
                    break;
                case "Debit":
                    flow = new Debit(targetAccount, amount, comment);
                    break;
                case "Transfert":
                    int issuerAccount = Integer.parseInt(extractValue(json, "accountIssuerNumber"));
                    flow = new Transfert(issuerAccount, targetAccount, amount, comment);
                    break;
	            default:
	                System.err.println("Unknowed Type flow : " + type);
	                return null;
            }
            
            String date = extractValue(json, "date");
            
            if (date != null) {
                flow.setDateOfFlow(LocalDateTime.parse(date));
            }
            return flow;
            
        } catch (Exception e) {
        	System.err.println("Erreur parsing flow: " + e.getMessage());
            return null;
        }
    }
    
    private static String extractValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"?([^,}\"]+)\"?");
        Matcher matcher = pattern.matcher(json);
       
        return matcher.find() ? matcher.group(1).trim() : null;
    }
}
