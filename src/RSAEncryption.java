/*
 * reference for is coPrime:
 * https://stackoverflow.com/questions/28575416/finding-out-if-two-numbers-are-relatively-prime
 * */
import java.math.*;
import java.util.Random;
import java.util.Vector;

public class RSAEncryption {	
	private Pair publicKey;
	private Pair privateKey;
	
	private static int blockSize = 4;
	private BigInteger num = new BigInteger("128");
	
	private Vector<BigInteger> encryptValues;
	private Vector<BigInteger> decryptValues;
	
	public RSAEncryption() {
		
		encryptValues = new Vector<BigInteger>();
		decryptValues = new Vector<BigInteger>();

	}

/*============================GRAB MESSAGE AND ENCRYPT IT===================================*/	
	
	private char[] explodeMessage(String msg) 
	{
		int addAmount = msg.length() % blockSize;
		if(addAmount != 0) {
			for(int i = 0; i < blockSize - addAmount; i++) {
				msg = msg + 0;
			}
		}
		return msg.toCharArray();
	}

	private void printEncryptValues()
	{
		System.out.println("in encrypt");
		for(BigInteger M: encryptValues)
		{
			System.out.println(M);
		}
	}
	private void blockMessage(long e, long n, char[] charMessage) 
	{
		encryptValues.removeAllElements();

		for(int i = 0; i < charMessage.length; i+=blockSize) {
			BigInteger val = new BigInteger("0");
			for(int j = i; j < i + blockSize; j++) {
				
				long powVal = charMessage[j] *(int)Math.pow(128, (j % blockSize));
				val = val.add(new BigInteger(Long.toString(powVal)));
				
			} 
			encryptMessage(val, e, n);
		}
	

	}
	private void encryptMessage(BigInteger val, long e, long n) {
		BigInteger C = new BigInteger("0");
		BigInteger exp = new BigInteger(Long.toString(e));
		BigInteger mod = new BigInteger(Long.toString(n));
		C = val.modPow(exp, mod);
		//System.out.println(C);
		encryptValues.addElement(C);
	}

	
	public Vector<BigInteger> encrypt(Pair publicKey, String message)
	{
		encryptValues.removeAllElements();

		char[] charMessage = explodeMessage(message);
		printEncryptValues();
		blockMessage(publicKey.getVal1(), publicKey.getVal2(), charMessage);
		printEncryptValues();
		return encryptValues; 
		
		
	}
/*==================================DECRYPT MESSAGE======================================*/		
	private void setDecryptValues(Vector<BigInteger> encryptedValues, Pair privateKey) 
	{
		decryptValues.removeAllElements();
		BigInteger M = new BigInteger("0");
		
		for(BigInteger C: encryptedValues) {
			M = C.modPow(new BigInteger(Long.toString(privateKey.getVal1())), new BigInteger(Long.toString(privateKey.getVal2())));
			decryptValues.addElement(M);
		}
		decryptMessage();
		//printDecryptValues();
		
	}
	private String decryptMessage() {
		String msg = new String();

		for(BigInteger M: decryptValues) {
			while(true) {
				if(M.intValue() == 0) 
				{
					break;
				}
				int val = M.mod(num).intValue();
				
				char letter = (char)val;
				M = M.shiftRight(7);	
				if(letter != '0') 		
					msg += letter;
				
			}
		}
		return msg;
		
	}
	
	private void printDecryptValues() 
	{
		System.out.println("in decrypt");
		for(BigInteger M: decryptValues) 
		{
			System.out.println(M);
		}
	}
	
	public String decrypt(Pair privateKey, Vector<BigInteger> encryptedValues)
	{
		System.out.println("Encrypted values in decrypt");
		for(BigInteger b: encryptedValues)
		{
			System.out.println(b);
		}
		decryptValues.removeAllElements();
		System.out.println("Before");
		printDecryptValues();
		setDecryptValues(encryptedValues, privateKey);
		System.out.println("After");
		printDecryptValues();
		return decryptMessage();
	}
	


	
	
}
