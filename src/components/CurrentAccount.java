package components;

// 1.2.2 Creation of the CurrentAccount and SavingsAccount
public class CurrentAccount extends Account{
	
	public CurrentAccount(String label, Client client) {
		super(label,client);
	}
	
	// Keeping the balance & accountNumber during the XML import
    public CurrentAccount(String label, Client client, int accountNumber, double balance) {
        super(label, client, accountNumber, balance);
    }
}
