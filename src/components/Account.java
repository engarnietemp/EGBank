package components;

// 1.2.1 Creation of the class and attributes
public abstract class Account {

	protected double balance;
	protected int accountNumber;
	protected String label;
	protected Client client;
	
	private static int accountId = 0;
	
	protected Account(String label, Client client) {
		this.client = client;
		this.label = label;
		
		this.accountNumber = ++accountId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
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
