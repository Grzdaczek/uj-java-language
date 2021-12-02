public class Test {
	public static void main(String[] args) {
		var b = new BusLine();

		b.addBusLine("a", new Position2D(1, 1), new Position2D(2, 6));
		b.addLineSegment("a", new LineSegment(new Position2D(1, 1), new Position2D(7, 7))); // 1
		b.addLineSegment("a", new LineSegment(new Position2D(7, 1), new Position2D(2, 6))); // 3
		b.addLineSegment("a", new LineSegment(new Position2D(7, 7), new Position2D(7, 1))); // 2

		b.addBusLine("b", new Position2D(4, 7), new Position2D(7, 7));
		b.addLineSegment("b", new LineSegment(new Position2D(4, 7), new Position2D(7, 7)));

		b.addBusLine("c", new Position2D(1, 1), new Position2D(4, 2));
		b.addLineSegment("c", new LineSegment(new Position2D(1, 1), new Position2D(1, 4)));
		b.addLineSegment("c", new LineSegment(new Position2D(1, 4), new Position2D(4, 4)));
		b.addLineSegment("c", new LineSegment(new Position2D(4, 4), new Position2D(8, 4)));
		b.addLineSegment("c", new LineSegment(new Position2D(8, 4), new Position2D(8, 2)));
		b.addLineSegment("c", new LineSegment(new Position2D(8, 2), new Position2D(4, 2)));

		/*

		  1 2 3 4 5 6 7 8  X
		1 .           a
		2 c a   c c . + c
		3 c   a   a   a c
		4 c c c x c c + c
		5     a   a   a
		6   a       a a
		7       b b b .
		8        

		Y

		*/


		b.findIntersections();
	}
}
