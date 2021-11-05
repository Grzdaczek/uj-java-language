import java.util.Map;

public class Test {
	public static void main(String[] args) {
		Decrypter d = new Decrypter();
		
		// String input = "Dobry Wieczór\nWydział Fizyki, Astronomii\ni\nInformatyki Stosowanej";
		String input = "Dxb80 1*9c3ó8 1023*4Ł f*30{*, 5678x!xW** * }!#x8W470{* s7x6xw4!9i";

		d.setInputText(input);

		for (Map.Entry<Character, Character> entry : d.getCode().entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue().toString());
		}

		System.out.println("--------");

		for (Map.Entry<Character, Character> entry : d.getDecode().entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue().toString());
		}
	}
}
