package components;


// 1.1.1 Creation of the Client class
public class Client {
	
	// 1.1.1 Client attributes declarations
	
	private String name;
	private String firstName;
	private int clientNumber;
	
	// Used to increment and keep a unique id
	private static int clientId = 0;
	
	// 1.1.1 Creation of Methods
	
	public Client(String name, String firstName) {
		this.name = name;
		this.firstName = firstName;
		this.clientNumber = ++clientId;
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
