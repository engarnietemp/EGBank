package components;

import java.util.concurrent.atomic.AtomicInteger;

// 1.1.1 Creation of the Client class
public class Client {
	
	// 1.1.1 Client attributes declarations
	
	private String name;
	private String firstName;
	private final int clientNumber;

	private static final AtomicInteger CLIENT_COUNTER = new AtomicInteger(0);
	
	// 1.1.1 Creation of Methods
	
	public Client(String name, String firstName) {
		this.name = name;
		this.firstName = firstName;
		this.clientNumber = CLIENT_COUNTER.incrementAndGet();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getClientNumber() {
		return clientNumber;
	}

	@Override
	public String toString() {
	    return "Client{" +
	            "name='" + name + '\'' +
	            ", firstName='" + firstName + '\'' +
	            ", clientNumber=" + clientNumber +
	            '}';
	}
	
}
