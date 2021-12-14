public class Test {
	public static void main(String[] args) {
		Compression c = new Compression();

		// String s = "001 001 001 010 111 011 001 001 110 000 001 001 001 001";
		// String s = "000 001 000 001 000 001 000 001 011 001 000 110 001 000 111 001 001 000 000 000 001";
		// String s = "001 010 100";
		
		String s = "0101 0101 0101 0101 0101 0101 0101 0101 0101 0101 0011 0011 0011 0011 0011 0011 0011 0011 0011 0011 0001 0001 0001 0001 0001 0001 0001 0001 0001 0001 0111 1000 0100 0111 0001 1000 1000 1001 0101 1000 1001 1001 0000 0010 0000 0101 0101 0100 0011 0100";
		
		for (String word : s.split(" "))
			c.addWord(word);

		c.compress();

		try {
			while(true)
				System.out.println(c.getWord());
		}
		catch (Exception e) {

		}

	}
}