import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decrypter implements DecrypterInterface {
	private Map<Character, Character> codeMap;
	private Map<Character, Character> decodeMap;

	private void reset() {
		codeMap = new HashMap<Character, Character>();
		decodeMap = new HashMap<Character, Character>();
	}

	public Decrypter() {
		reset();
	}

	@Override
	public void setInputText(String encryptedDocument) {
		reset();

		if (encryptedDocument == null)
			return;

		String wfais = "Wydział Fizyki, Astronomii i Informatyki Stosowanej".replaceAll("\\s+|,", "");
		Pattern p = Pattern.compile("(\\s|^)(\\S{7})\\s+(\\S{6}),\\s+(\\S{10})\\s+(\\S{1})\\s+(\\S{11})\\s+(\\S{10})(\\s|$)");

        Matcher matcher = p.matcher(encryptedDocument);
		
		if (!matcher.find()) 
			return;

		String matched = matcher.group().replaceAll("\\s+|,", "");

		for (Integer i = 0; i < wfais.length(); i++) {
			codeMap.put(wfais.charAt(i), matched.charAt(i));
			decodeMap.put(matched.charAt(i), wfais.charAt(i));
		}
	}

	@Override
	public Map<Character, Character> getCode() {
		return codeMap;
	}

	@Override
	public Map<Character, Character> getDecode() {
		return decodeMap;
	}
}
