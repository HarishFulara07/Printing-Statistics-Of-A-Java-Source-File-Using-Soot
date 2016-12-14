import java.io.IOException;

public class Test2 {
	public int a;
	protected int b;
	int d;
	public String e;
	Boolean f;
	protected char g;
	float h;
	long i;
	boolean j;
	public Integer k;
	Float l;
	Long m;
	
	public static void main(String[] args) throws IOException {
		Test2 f = new Test2();
		f.a = 7;
		f.b = 14;
		int x = (f.bar(21, 41, "Hello")+f.a) * f.b;
		double y = x / 2.0;
		Test2.bar2(10, 20, "msg");
		System.out.println(x+y);
	}
	
	public int bar (int n, int m, String msg) throws NullPointerException {
		int x = 22122252;
		bar3();
		return 42 + x;
	}
	
	public static int bar2 (int n, int m, String ch) throws ClassCastException {
		if(n < 12) {
			return n+m;
		}
		return n + 42 + m;
	}
	
	public void bar3() throws NullPointerException, RuntimeException {
		int x = 2001;
		
		for(int i = 0; i < x; ++i) {
			i = x*2;
		}
	}
}