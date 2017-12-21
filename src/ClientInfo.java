import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ClientInfo implements Serializable{
	private Pair publicKey;
	private String username;
	private ObjectOutputStream out;
	
	public ClientInfo(Pair pk, String name, ObjectOutputStream oos) {
		publicKey = pk;
		username = name; 
		out = oos; 
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public Pair getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(Pair publicKey) {
		this.publicKey = publicKey;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
