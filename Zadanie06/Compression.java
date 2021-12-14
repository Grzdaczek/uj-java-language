import java.lang.Math;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Compression implements CompressionInterface {
	public ArrayList<String> words = new ArrayList<String>();
	Map<String, String> encode = new HashMap<String, String>();
	Map<String, String> decode = new HashMap<String, String>();
	List<Entry<String, Long>> heatList = new ArrayList<Entry<String, Long>>();

	class CodeParams {
		public int codeSize;
		public int keySize;
		public int compressedSize;

		CodeParams(int iCodeSize, int iKeySize, int iCompressedSize) {
			codeSize = iCodeSize;
			keySize = iKeySize;
			compressedSize = iCompressedSize;
		}
	}

	void calcHeatList() {
		heatList.clear();

		Map<String, Long> heatMap = words
			.stream()
			.collect(Collectors.groupingBy(
				Function.identity(),
				Collectors.counting()
			));

		heatMap
			.entrySet()
			.stream()
			.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
			.forEachOrdered(e -> heatList.add(e));
	}

	CodeParams calcCodeParams(int codeSize) {
		int wordSize = words.get(0).length();

		if (codeSize == 0) {
			int compressedSize = wordSize * words.size();
			return new CodeParams(0, 0, compressedSize);
		}
		else {
			int keySize = (int) Math.ceil((Math.log(codeSize) / Math.log(2))) + 1;

			int bodySize = heatList
				.stream()
				.skip(codeSize)
				.map(e -> (int)(e.getValue() * (wordSize + 1)))
				.reduce(0, Integer::sum);
			
			int headerSize = codeSize * (keySize + wordSize);
			int compressedSize = bodySize + headerSize;

			return new CodeParams(codeSize, keySize, compressedSize);
		}
	}

	public void addWord(String word) {
		words.add(word);
	}

	public void compress() {
		calcHeatList();
		encode.clear();
		decode.clear();

		CodeParams bestCodeParams = IntStream
			.range(0, heatList.size())
			.mapToObj(n -> calcCodeParams(n))
			.min((CodeParams p1, CodeParams p2) -> p1.compressedSize < p2.compressedSize ? -1 : 1)
			.get();

		for (int i = 0; i < bestCodeParams.codeSize; i++) {
			String key = Integer.toBinaryString(i);
			String word = heatList.get(i).getKey();
			
			while (key.length() < bestCodeParams.codeSize)
				key = "0" + key;

			decode.put(key, word);
			encode.put(word, key);
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