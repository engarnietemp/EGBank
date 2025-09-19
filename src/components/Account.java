package components;

import java.util.concurrent.atomic.AtomicInteger;

// 1.2.1 Creation of the class and attributes
public abstract class Account {

	// 1.2.1 Client attributes declarations
	
	protected double balance;
	protected int accountNumber;
	protected String label;
	protected Client client;
	
	private static final AtomicInteger ACCOUNT_COUNTER = new AtomicInteger(0);
	
	// 1.2.1 Creation of Methods
	
	protected Account(String label, Client client) {
		this.client = client;
		this.label = label;	
		this.accountNumber = ACCOUNT_COUNTER.incrementAndGet();
	}
    
	// 2.2 Needed in the XML import to keep accountNumber & balance
	protected Account(String label, Client client, int accountNumber, double balance) {
        this.client = client;
        this.label = label;
        this.accountNumber = accountNumber;
        this.balance = balance;

        ACCOUNT_COUNTER.updateAndGet(current -> Math.max(current, accountNumber));
    }
	

	public double getBalance() {
		return balance;
	}
	
	public void resetBalance() {
		this.balance = 0;
	}
	
	// 1.3.5 new method setBalance
	public void setBalance(Flow flow) {
	    switch (flow.getClass().getSimpleName()) {
	        case "Credit" -> this.balance += flow.getAmount();
	        
	        case "Debit" -> this.balance -= flow.getAmount();
	        
	        case "Transfert" -> applyTransfer((Transfert) flow);
	        
	        default -> throw new IllegalArgumentException("Unknown flow type: " + flow.getClass().getSimpleName());
	    }
	}
	
	// 1.3.5 Used in setBalance
	private void applyTransfer(Transfert transfert) {
	    
		if (transfert.getTargetAccountNumber() == this.accountNumber) {
	        this.balance += transfert.getAmount();
	    } 
	    
	    else if (transfert.getAccountIssuerNumber() == this.accountNumber) {
	        this.balance -= transfert.getAmount();
	    }
	    else {
	    	System.out.println("Tranfer doesn't concern account " + this.accountNumber);
	    }
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	@Override
	public String toString() {
	    return "Account{" +
	            "Client='" + this.client + '\'' +
	            ", Label='" + this.label + '\'' +
	            ", Balance='" + this.balance + '\'' +
	            ", Account Number=" + this.accountNumber +
	            '}';
	}
}
