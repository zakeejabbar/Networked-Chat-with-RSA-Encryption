import java.io.Serializable;

public class Pair implements Serializable
{
	private long val1;
	private long val2;
	public Pair(long x, long y) 
	{
		val1 = x;
		val2 = y;
	}
	public long getVal1() {
		return val1;
	}
	public void setVal1(int val1) {
		this.val1 = val1;
	}
	public long getVal2() {
		return val2;
	}
	public void setVal2(int val2) {
		this.val2 = val2;
	}
	
}
