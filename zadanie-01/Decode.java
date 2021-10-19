public class Decode extends DecoderInterface {

	private StringBuilder builder = new StringBuilder();
	private int width = 0;
	private int recived = 0;

	public void input(int bit) {
		if(bit == 1) {
			recived += 1;
		}
		else if(this.recived != 0) {
			if(this.width == 0)
				this.width = this.recived;
			builder.append((this.recived / this.width) - 1);
			this.recived = 0;
		}
	}

	public String output() {
		return builder.toString();
	}

	public void reset() {
		this.builder = new StringBuilder();
		this.width = 0;
		this.recived = 0;
	}
}