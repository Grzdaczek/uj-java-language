public class Test{
	
	public static void main(String[] args) {
		Decode d = new Decode();
		d.input(0);
		d.input(0);
		d.input(0);
		d.input(0);
		d.input(0);
		d.input(1);
		d.input(1);
		d.input(0);
		d.input(1);
		d.input(1);
		d.input(1);
		d.input(1);
		d.input(0);
		d.input(0);
		d.input(0);
		d.input(0);
		d.input(0);
		d.input(0);
		d.input(0);
		d.input(0);
		d.input(1);
		d.input(1);
		d.input(0);
		d.input(1);
		d.input(1);
		d.input(1);
		d.input(1);
		d.input(1);
		d.input(1);
		d.input(0);
		d.input(0);
		d.input(1);
		d.input(1);
		d.input(0);
		System.out.println("expected \"01020\", got: \"" + d.output() + "\"");
	}
}