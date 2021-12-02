import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

enum Direction {
	NONE,
	N_S,
	W_E,
	NW_SE,
	NE_SW,
}

class PosDir implements Position {
	Direction dir;
	Integer x;
	Integer y;

	public PosDir(Integer _x, Integer _y, Direction _dir) {
		x = _x;
		y = _y;
		dir = _dir;
	}

	public Direction getDir() {
		return dir;
	}

	@Override
	public int getCol() {
		return x;
	}

	@Override
	public int getRow() {
		return y;
	}

	@Override
	public String toString() {
		return "[x: " + x.toString() + ", y: " + y.toString() + ", dir: " + dir.toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PosDir other = (PosDir) obj;
		return x == other.x && y == other.y && dir == other.dir;
	}

	@Override
	public int hashCode() {
		return x.hashCode() * y.hashCode() * dir.hashCode();
	}

	Position2D toPos2D() {
		return new Position2D(x, y);
	}

	PosDir toOrthogonal() {
		Direction newDir;
		switch (dir) {
			case N_S: newDir = Direction.W_E; break;
			case W_E: newDir = Direction.N_S; break;
			case NE_SW: newDir = Direction.NW_SE; break;
			case NW_SE: newDir = Direction.NE_SW; break;
			default: newDir = Direction.NONE; break;
		}

		return new PosDir(x, y, newDir);
	}

	static boolean orthogonal(PosDir a, PosDir b) {
		// if (a.getDir() == Direction.N_S && b.getDir() == Direction.W_E) return true;
		// if (a.getDir() == Direction.W_E && b.getDir() == Direction.N_S) return true;
		// if (a.getDir() == Direction.NE_SW && b.getDir() == Direction.NW_SE) return true;
		// if (a.getDir() == Direction.NW_SE && b.getDir() == Direction.NE_SW) return true;
		// return false;
		return a.toOrthogonal().equals(b);
	}
}

class Line {
	public String name = null;
	public PosDir firstPoint = null;
	public PosDir lastPoint = null;
	public Set<LineSegment> segments = new HashSet<LineSegment>();
	public List<PosDir> points = null;

	public Line(String _name, Position _first, Position _last) {
		name = _name;
		firstPoint = new PosDir(_first.getCol(), _first.getRow(), Direction.NONE);
		lastPoint = new PosDir(_last.getCol(), _last.getRow(), Direction.NONE);
	}

	public void addLineSegment(LineSegment seg) {
		segments.add(seg);
	}

	public void genPoints() {
		points = new ArrayList<PosDir>();

		PosDir head = firstPoint;
		Direction lastDir = Direction.NONE;
		while (!head.equals(lastPoint)) {
			PosDir _head = head;
			LineSegment segment = segments.stream()
				.filter(s -> s.getFirstPosition()
				.equals(_head.toPos2D()))
				.findFirst()
				.get();

			Integer deltaX = segment.getLastPosition().getCol() - segment.getFirstPosition().getCol();
			Integer deltaY = segment.getLastPosition().getRow() - segment.getFirstPosition().getRow();
			Integer len = Math.max(Math.abs(deltaX), Math.abs(deltaY));
			Integer dX = deltaX / len;
			Integer dY = deltaY / len;
			Integer x = segment.getFirstPosition().getCol();
			Integer y = segment.getFirstPosition().getRow();

			Direction segDir = Direction.NONE;
			if      ( dX == 0 && dY != 0) segDir = Direction.N_S;
			else if ( dX != 0 && dY == 0) segDir = Direction.W_E;
			else if ( dX * dY == 1 )      segDir = Direction.NW_SE;
			else if ( dX * dY == -1 )     segDir = Direction.NE_SW;

			for (Integer i = 0; i < len; i++) {
				Direction pointDir = Direction.NONE;
				if (lastDir != segDir) {
					pointDir = Direction.NONE;
					lastDir = segDir;
				} 
				else {
					pointDir = segDir;
				}

				PosDir pos = new PosDir(x + (dX * i), y + (dY * i), pointDir);
				points.add(pos);
			}

			Position p = segment.getLastPosition();
			head = new PosDir(p.getCol(), p.getRow(), Direction.NONE);
		}
		points.add(lastPoint);

		// for (Position p : points) {
		// 	System.out.println(p);
		// }
	}

	@Override
	public String toString() {
		return name;
	}
}

class LinePair implements BusLineInterface.LinesPair {
	Line lineA;
	Line lineB;

	LinePair(Line a, Line b) {
		lineA = a;
		lineB = b;
	}

	@Override
	public String getFirstLineName() {
		return lineA.name;
	}

	@Override
	public String getSecondLineName() {
		return lineB.name;
	}
}

public class BusLine implements BusLineInterface {
	public Map<String, Line> lines = new HashMap<String, Line>();

	public Map<String, List<Position>> linesPositions;
	public Map<BusLineInterface.LinesPair, Set<Position>> intersectionOfLinesPair;
	public Map<String, List<Position>> intersectionPositions;
	public Map<String, List<String>> intersectionsWithLines;

	@Override
	public void addBusLine(String busLineName, Position firstPoint, Position lastPoint) {
		Line line = new Line(busLineName, firstPoint, lastPoint);
		lines.put(busLineName, line);
	}

	@Override
	public void addLineSegment(String busLineName, LineSegment lineSegment) {
		lines.get(busLineName).addLineSegment(lineSegment);
	}

	@Override
	public void findIntersections() {
		// Interpoluj punkty przez które przebiega trasa
		lines
			.values()
			.forEach(l -> l.genPoints());

		// Utwórz mapę wszystkich punktów z listami tras które przez nie przechodzą
		HashMap<Position, Line> allPoints = new HashMap<Position, Line>();
		lines
			.values()
			.forEach(l -> l.points.forEach(p -> {
				allPoints.put(p, l);
			}));

		// Utwórz mapę wszystkich linii z listami punktów przez które przechodzą
		linesPositions = new HashMap<String, List<Position>>();
		lines
			.values()
			.forEach(l -> linesPositions.put(
				l.name,
				// l.points.stream().
				l.points.stream().collect(Collectors.toList())
			));

		// Utwórz mapę lini z listami kolenjuch przcięć z innymi liniami
		intersectionsWithLines = new HashMap<String, List<String>>();
		intersectionPositions = new HashMap<String, List<Position>>();
		lines
			.values()
			.forEach(l -> {
				intersectionsWithLines.put(l.name, new ArrayList<String>());
				intersectionPositions.put(l.name, new ArrayList<Position>());

				l.points.forEach(p -> {
					Line crossing = allPoints.get(p.toOrthogonal());

					if (crossing != null && p.getDir() != Direction.NONE) {
						intersectionsWithLines.get(l.name).add(crossing.name);
						intersectionPositions.get(l.name).add(p);
					}
				});
			});

		// intersectionsWithLines = lines
		// .values()
		// .stream()
		// .collect(Collectors.toMap(l -> l.name, l -> {
		// 	List<String> list = new ArrayList<String>();
			
		// 	l.points.forEach(p -> {
		// 		if (p.getDir() == Direction.NONE)
		// 			return;
				
		// 		Line crossing = allPoints.get(p.toOrthogonal());
				
		// 		if (crossing != null)
		// 			list.add(crossing.name);
		// 	});

		// 	return list;
		// }));

		System.out.println(intersectionsWithLines);
		System.out.println(intersectionPositions);

		// allPoints
		// 	.entrySet()
		// 	.stream()
		// 	.forEach(e -> {
		// 		System.out.println(e);
		// 	});

		// ¯\_(ツ)_/¯
	}

	@Override
	public Map<BusLineInterface.LinesPair, Set<Position>> getIntersectionOfLinesPair() {
		// if (intersectionOfLinesPair == null) findIntersections();
		return intersectionOfLinesPair;
	}

	@Override
	public Map<String, List<Position>> getIntersectionPositions() {
		// if (intersectionPositions == null) findIntersections();
		return intersectionPositions;
	}

	@Override
	public Map<String, List<String>> getIntersectionsWithLines() {
		// if (intersectionsWithLines == null) findIntersections();
		return intersectionsWithLines;
	}

	@Override
	public Map<String, List<Position>> getLines() {
		// if (linesPositions == null) findIntersections();
		return linesPositions;
	}

}
