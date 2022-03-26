package tcg.core;

public class ThreadTest {
	public static void main(String[] args) {
		ThreadTest t = new ThreadTest();
		System.out.println(t);
		Demo(t);
		System.out.println(t);
	}
	
	public static void Demo(ThreadTest t) {
		t = new ThreadTest();
	}
}
