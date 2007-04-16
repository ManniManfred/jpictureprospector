/** comment */
public class SVNDemo {
	
	public SVNDemo() {
		System.out.println("SVNDemo");
		addiere(3, 5, 6);
	}
	
	public void addiere(int zahl1, int zahl2, int zahl3) {
		System.out.println(zahl1 + zahl2 + zahl3);
	}
	public void sub() {
		System.out.println("super");
	}
	public static void main(String[] args) {
		new SVNDemo();
	}
}
