import java.util.Random;

public class GenerateKeys {
	private int p;
	private int q;
	private int n;
	
	private long phi;
	private long e;
	private long d;
	
	private Pair publicKey;
	private Pair privateKey;
	
	public GenerateKeys(int p, int q) {
		this.p = p;
		this.q = q;
		n = p*q;
		
		phi = createPhi();
		e   = createE();
		d   = createD();
		
		this.publicKey = new Pair(e, n);
		this.privateKey = new Pair(d, n);
	}
	
	private int createPhi() {
		 return (p-1) * (q-1);
	}
/*===============================CREATE D AND E================================================*/
	private long createE() {
		long i = 2;
		long count = 0;
		Random rand = new Random();
		int x = rand.nextInt(100) + 100;
		while (i < n){
	        if (gcd(i, phi)==1) {
	            count++;
	            if(count == x) {
	            		break;
	            }
	            else
	            		i++;
	        }
	        else
	           i++;
		 }
		 return i;
	}
	private long createD() {
		long k = 2;
		long dVal;
		
		while((1 + (k*phi)) % e != 0) {
			k++;
		}
		dVal = (1 + (k*phi)) / e;
		return dVal;
		

	}
	public Pair getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(Pair publicKey) {
		this.publicKey = publicKey;
	}

	public Pair getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(Pair privateKey) {
		this.privateKey = privateKey;
	}

	private static long gcd(long val1, long val2) {
	    long t;
	    while(val2 != 0){
	        t = val1;
	        val1 = val2;
	        val2 = t % val2;
	    }
	    return val1;
	}
	
}
