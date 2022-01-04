import java.io.Console;

public class Test {
	public static void main(String[] args) {
		PasswordCrackerInterface cracker = new PasswordCracker();
		for (int i = 1; i <= 10; i++) {
			String pass = cracker.getPassword("localhost", 8080);
			
			if (pass == null) {
				System.out.println("FAILED: Test " + i + "/10");
			} else {
				System.out.println("PASSED: Test " + i + "/10 : " + pass);
			}
		}
	}
}
