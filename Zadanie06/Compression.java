import java.lang.Math;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Compression implements CompressionInterface {
	public ArrayList<String> words = new ArrayList<String>();
	Map<String, String> encode = new HashMap<String, String>();
	Map<String, String> decode = new HashMap<String, String>();

	public void addWord(String word) {
		words.add(word);
	}

	public void compress() {
		encode.clear();
		decode.clear();

		Map<String, Long> heatMap = words
			.stream()
        	.collect(Collectors.groupingBy(
				Function.identity(),
				Collectors.counting()
			));

		LinkedHashMap<String, Long> sortedHeatMap = new LinkedHashMap<>();
		heatMap
			.entrySet()
			.stream()
			.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
			.forEachOrdered(e -> sortedHeatMap.put(e.getKey(), e.getValue()));

		int bestkeySize = 0;
		int bestTotalSize = Integer.MAX_VALUE;
		int bestHeaderLength = Integer.MAX_VALUE;

		for (int keySize = 0; keySize < sortedHeatMap.size(); ++keySize) {
			int headerWordSize = words.get(0).length();
			int bodyWordSize = keySize == 0 ? headerWordSize : headerWordSize + 1;
			int headerLength = (int) Math.pow(2, keySize - 1);
			int headerSize = headerLength * (headerWordSize + keySize);

			int bodySize = sortedHeatMap
				.entrySet()
				.stream()
				.skip(headerLength)
				.map(e -> (int) e.getValue().intValue())
				.reduce(0, (acc, e) -> acc + (e * bodyWordSize));

			int totalSize = headerSize + bodySize;

			if (bestTotalSize > totalSize) {
				bestkeySize = keySize;
				bestTotalSize = totalSize;
				bestHeaderLength = headerLength;
			}
		}

		List<String> codedWords = sortedHeatMap
			.keySet()
			.stream()
			.limit(bestHeaderLength)
			.collect(Collectors.toList());

		for (int i = 0; i < bestHeaderLength; ++i) {
			String key = Integer.toBinaryString(i);
			while (key.length() < bestkeySize) key = "0" + key;
			decode.put(key, codedWords.get(i));
			encode.put(codedWords.get(i), key);
		}

	}

	public Map<String, String> getHeader() {
		return decode;
	}
		
	public String getWord() {
		String word = words.get(0);
		words.remove(0);

		if (encode.containsKey(word))
			return encode.get(word);
		else
			return "1" + word;
	}
	
}