import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Decrypter implements DecrypterInterface {
	private Map<Character, Character> codeMap;
	private Map<Character, Character> decodeMap;

	private Integer[] lengths(String[] strings) {
		return Stream
			.of(strings)
			.map(String::length)
			.toArray(Integer[]::new);
	}

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

		if (encryptedDocument == null) {
			return;
		}
		
		String[] wfais = {"Wydział", "Fizyki", "Astronomii", "i", "Informatyki", "Stosowanej"};
		
		String[] words = Stream
			.of(encryptedDocument.split(" |\n|\r|\t|,"))
			.filter(item -> !item.equals(""))
			.toArray(String[]::new);

		Integer index =  Collections.indexOfSubList(
			Arrays.asList(lengths(words)),
			Arrays.asList(lengths(wfais))
		);

		if (index == -1 ) {
			return;
		}

		String a = String.join("", Arrays.asList(wfais));
		String b = String.join("", Arrays.asList(words).subList(index, index + wfais.length));

		for (Integer i = 0; i < a.length(); i++) {
			codeMap.put(a.charAt(i), b.charAt(i));
			decodeMap.put(b.charAt(i), a.charAt(i));
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
