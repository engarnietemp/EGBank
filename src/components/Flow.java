package components;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

// 1.3.2 Creation of the Flow class
public abstract class Flow {

	// 1.3.2 Flow attributes declarations
	
	private static final AtomicInteger FLOW_COUNTER = new AtomicInteger(0);
	
	private int targetAccountNumber;
	private final int identifier;
	private double amount;

	private String comment;
	private Boolean effect;
	private LocalDateTime dateOfFlow;
	
	// 1.3.2 Flow methods declarations
	
	protected Flow(int targetAccountNumber, double amount, String comment) {
		
		this.targetAccountNumber = targetAccountNumber;
		this.amount = amount;
		this.comment = comment;
		
		this.identifier = FLOW_COUNTER.incrementAndGet();
		this.dateOfFlow = LocalDateTime.now().plusDays(2); // +2 days here
		this.effect = true;
	}

	public int getTargetAccountNumber() {
		return targetAccountNumber;
	}

	public double getAmount() {
		return amount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getEffect() {
		return effect;
	}

	public void setEffect(Boolean effect) {
		this.effect = effect;
	}

	public LocalDateTime getDateOfFlow() {
		return dateOfFlow;
	}

	public void setDateOfFlow(LocalDateTime dateOfFlow) {
		this.dateOfFlow = dateOfFlow;
	}

	public int getIdentifier() {
		return identifier;
	}
	
	@Override
	public String toString() {
	    return getClass().getSimpleName() + "{" +
	           "id=" + identifier +
	           ", target=" + targetAccountNumber +
	           ", amount=" + amount +
	           ", comment='" + comment + '\'' +
	           ", date=" + dateOfFlow +
	           '}';
	}
}
