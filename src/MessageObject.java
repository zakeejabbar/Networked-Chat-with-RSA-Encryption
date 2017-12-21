import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultListModel;

public class MessageObject implements Serializable
{
	private char type;
	private Pair publicKey;
	private String message;
	private String name;
	private String myName;
	private Vector<BigInteger> encryptedValues;
	
	
	/*to add a new user*/
	public MessageObject(char t, Pair pk, String n) {
		type = t;
		publicKey = pk;
		this.name = n;
	}
	public MessageObject(char t){
		type = t;
	}
	//remove 
	public MessageObject(char t, Pair pk) {
		type = t;
		publicKey = pk;
	}

	/*send encrypted values*/
	public MessageObject(char t, String name, Vector<BigInteger> val, String mN) {
		type = t;
		this.name = name;
		encryptedValues = val;
		myName = mN;
	}
	
	public Vector<BigInteger> getEncryptedValues() {
		return encryptedValues;
	}
	public MessageObject(char t, String n , String m)
	{
		type = t;
		name = n;
		message = m;
	}
	public MessageObject(char t, String n) {
		type = t;
		name = n;
	}

	public char getType()
	{
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public Pair getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(Pair publicKey) {
		this.publicKey = publicKey;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message; 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMyName() {
		return myName;
	}
}
