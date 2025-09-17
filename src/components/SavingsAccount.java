package components;

//1.2.2 Creation of the CurrentAccount and SavingsAccount
public class SavingsAccount extends Account {
    
    public SavingsAccount(String label, Client client) {
        super(label, client);
    }
    
    // 2.2. Keeping account balance & accountNumber during the XML import
    public SavingsAccount(String label, Client client, int accountNumber, double balance) {
        super(label, client, accountNumber, balance);
    }
}
