import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decrypter implements DecrypterInterface {
	private Map<Character, Character> codeMap;
	private Map<Character, Character> decodeMap;

	private void reset() {
		codeMap = new HashMap<Character, Character>();
		decodeMap = new HashMap<Character, Character>();
	}

	private List<Integer> signature(String str) {
		List<Integer> sig = new ArrayList<Integer>();
		List<Character> set = new ArrayList<Character>();
		for (Character c : str.toCharArray()) {
			if (!set.contains(c)) set.add(c);
			sig.add(set.indexOf(c));
		}

		return sig;
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
		Pattern p = Pattern.compile("(\\b|\\s|^)([^\\s,]{7})\\s+([^\\s,]{6}),\\s+([^\\s,]{10})\\s+([^\\s,]{1})\\s+([^\\s,]{11})\\s+([^\\s,]{10})(\\b|\\s|$)");
        Matcher matcher = p.matcher(encryptedDocument);
		
		outer: while (matcher.find()) {
			String matched = matcher.group().replaceAll("\\s+|,", "");

			if (!signature(wfais).equals(signature(matched))) continue;

			for (Integer i = 0; i < wfais.length(); i++) {
				Character a = wfais.charAt(i);
				Character b = matched.charAt(i);
				codeMap.put(a, b);
				decodeMap.put(b, a);
			}
			// at this point, valid patter has been found
			break;
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
