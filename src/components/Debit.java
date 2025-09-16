package components;

//1.3.3 Creation of the Debit class
public class Debit extends Flow {
	public Debit(int targetAccountNumber, double amount, String comment) {
		super(targetAccountNumber, amount, comment);
	}
}
