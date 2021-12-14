public class Test {
	public static void main(String[] args) {
		Compression c = new Compression();

		// String[] A = {"001", "001", "001", "010", "111", "011", "001", "001", "110", "000", "001", "001", "001", "001"};
		String[] A ={"000", "001", "000", "001", "000", "001", "000", "001", "011", "001", "000", "110", "001", "000", "111", "001", "001", "000", "000", "000", "001"};
		// String[] A = {"001", "010", "100"};
		
		for (String a : A) {
			c.addWord(a);
		}

		c.compress();

		for (String a : A) {
			System.out.println(c.getWord());
		}


	}
}