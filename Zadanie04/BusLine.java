import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

	Position2D toPos2D() {
		return new Position2D(x, y);
	}

	static boolean orthogonal(PosDir a, PosDir b) {
		if (a.getDir() == Direction.N_S && b.getDir() == Direction.W_E) return true;
		if (a.getDir() == Direction.W_E && b.getDir() == Direction.N_S) return true;
		if (a.getDir() == Direction.NE_SW && b.getDir() == Direction.NW_SE) return true;
		if (a.getDir() == Direction.NW_SE && b.getDir() == Direction.NE_SW) return true;
		return false;
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

		for (Position p : points) {
			System.out.println(p);
		}
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
	public Map<LinesPair, Position> intersections = null;

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
		// Utwórz mapę wszystkich punktów z listami tras które przez nie przechodzą
		// HashMap<Position, List<Line>> allPoints = new HashMap<Position, List<Line>>();
		for (Line l : lines.values()) {
			l.genPoints();

			// for (Position p : l.points) {
			// 	if (!allPoints.containsKey(p))
			// 		allPoints.put(p, new ArrayList<Line>());
				
			// 	allPoints.get(p).add(l);
			// }
		}

		// interface direction { public Integer direction(Line l, Position p);	}
		// direction d = (Line l, Position p) -> {
		// 	Integer x = p.getCol();
		// 	Integer y = p.getRow();
			
		// 	Position posN = new Position2D(x, y-1);
		// 	Position posS = new Position2D(x, y+1);
		// 	Position posW = new Position2D(x-1, y);
		// 	Position posE = new Position2D(x+1, y);
		// 	Position posNW = new Position2D(x-1, y-1);
		// 	Position posNE = new Position2D(x+1, y-1);
		// 	Position posSW = new Position2D(x-1, y+1);
		// 	Position posSE = new Position2D(x+1, y+1);
		// 	Boolean hasN = allPoints.containsKey(posN) && allPoints.get(posN).contains(p);
		// 	Boolean hasS = allPoints.containsKey(posS) && allPoints.get(posS).contains(p);
		// 	Boolean hasW = allPoints.containsKey(posW) && allPoints.get(posW).contains(p);
		// 	Boolean hasE = allPoints.containsKey(posE) && allPoints.get(posE).contains(p);
		// 	Boolean hasNW = allPoints.containsKey(posNW) && allPoints.get(posNW).contains(p);
		// 	Boolean hasNE = allPoints.containsKey(posNE) && allPoints.get(posNE).contains(p);
		// 	Boolean hasSW = allPoints.containsKey(posSW) && allPoints.get(posSW).contains(p);
		// 	Boolean hasSE = allPoints.containsKey(posSE) && allPoints.get(posSE).contains(p);

		// 	if 		( hasN && hasS ) return 1;
		// 	else if ( has) 

		// 	return null;
		// };

		// // Odrzuć punkty w których nie ma tras krzyżujących się pod kątem prostym 
		// HashMap<Position, List<LinesPair>> intersectionPoints = new HashMap<Position, List<LinesPair>>();
		// for (Position p : allPoints.keySet()) {
		// 	if (allPoints.get(p).size() < 1) continue;
		// 	// intersectionPoints.put(p, allPoints.get(p));
			
		// }

		// intersectionPoints
		// 	.entrySet()
		// 	.stream()
		// 	.forEach(e -> {
		// 		System.out.println(e);
		// 	});

		// ¯\_(ツ)_/¯
	}

	@Override
	public Map<BusLineInterface.LinesPair, Set<Position>> getIntersectionOfLinesPair() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<Position>> getIntersectionPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getIntersectionsWithLines() {
		// Map<String, List<String>> map = new HashMap<String, List<String>>();
		// for (Line line : lines.values()) {
		// 	for (Position p : line.points) {
		// 		for (Line line : lines.values()) {
		// 			if ()
		// 		}
		// 	}
		// }

		return null;
	}

	@Override
	public Map<String, List<Position>> getLines() {
		// Map<String, List<Position>> map = new HashMap<String, List<Position>>();
		// for (Line line : lines.values())
		// 	map.put(line.name, line.points);

		// return map;
		return null;
	}

}
