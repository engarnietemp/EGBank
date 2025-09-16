package components;

// 1.3.3 Creation of the Transfert class
public class Transfert extends Flow {
	private int accountIssuerNumber;
	
	public Transfert(int accountIssuerNumber, int targetAccountNumber, double amount, String comment) {
		super(targetAccountNumber, amount, comment);
		this.accountIssuerNumber = accountIssuerNumber;
	}
	
	public int getAccountIssuerNumber() {
		return this.accountIssuerNumber;
	}
}
