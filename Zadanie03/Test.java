import java.util.Map;

public class Test {
	public static void main(String[] args) {
		Decrypter d = new Decrypter();
		
		String input = "Dobry Wieczór\nWydział Fizyki, Astronomii\ni\nInformatyki Stosowanej";
		// String input = "Dxb80 1*9c3ó8 1023*4Ł f*30{*, 5678x!xW** * }!#x8W470{* s7x6xw4!9i";
		// String input = "B>Ćjg}F ćgj>żg, ]ImSV@Vógg g A@ŹVSó}m>żg ;mVIVŚ}@kL y[ dSVĆj> 1}!ImŚV";
		// String input = " z8avk#s )kv8nk, z8avk#s   )kv8nk, \t qŚC2ż9ż5kk k &9(ż25#C8nk 7CżŚż>#9[Y 7CżŚż>#9[Y W2#nA>";
		// String input = " EżMłpĆź śpłżfp, lPgdFcFDpp p scvFdDĆgżfp 8gFPFJĆcT, EżMłpĆź   śpłżfp, \t lPgdFcFDpp p scvFdDĆgżfp 8gFPFJĆcTć 8gFPFJĆcTć RdĆfwJ";
		// String input = " @@ żĆw%ń<^F^ v{Ćnull%ńE :>FóĆ^O   rĆó>LĆ,  \t\t \t\n Mw%ĄńPń|ĆĆ  Ć  CP(ńĄ|^%>LĆ \n i%ńwń{^PN2";
		
		d.setInputText(input);

		for (Map.Entry<Character, Character> entry : d.getCode().entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue().toString());
		}
	}
}
