import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

class ConcretePosition implements Position {
	Integer x;
	Integer y;

	ConcretePosition(int iX, int iY) {
		x = iX;
		y = iY;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
			ConcretePosition other = (ConcretePosition) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public int hashCode() {
		return x.hashCode() * y.hashCode();
	}
}

public class Graphics implements GraphicsInterface {
	public CanvasInterface canvas = null;

	@Override
	public void fillWithColor(Position startingPosition, Color color) throws
		GraphicsInterface.WrongStartingPosition,
		GraphicsInterface.NoCanvasException
	{
		if (canvas == null)
			throw new GraphicsInterface.NoCanvasException();

		try {
			canvas.setColor(startingPosition, color);
		}
		catch (Exception e) {
			throw new GraphicsInterface.WrongStartingPosition();
		}

		Set<Position> done = new HashSet<Position>();
		Queue<Position> queue = new ArrayDeque<Position>();

		queue.add(startingPosition);

		while (!queue.isEmpty()) {
			Position p = queue.remove();
			done.add(p);

			try {
				canvas.setColor(p, color);

				List<Position> nextPositions = new ArrayList<Position>(4);
				nextPositions.add(new ConcretePosition(p.getCol(), p.getRow() + 1));
				nextPositions.add(new ConcretePosition(p.getCol(), p.getRow() - 1));
				nextPositions.add(new ConcretePosition(p.getCol() + 1, p.getRow()));
				nextPositions.add(new ConcretePosition(p.getCol() - 1, p.getRow()));

				nextPositions.stream().forEach(q -> {
					if (!done.contains(q) || queue.contains(q)) queue.add(q);
				});
			}
			catch (CanvasInterface.BorderColorException e) {
				try {
					canvas.setColor(p, e.previousColor);
				}
				catch (Exception f) {
					throw new GraphicsInterface.WrongStartingPosition();
				}
			}
			catch (CanvasInterface.CanvasBorderException e) {
			}
		}
	}

	@Override
	public void setCanvas(CanvasInterface iCanvas) {
		canvas = iCanvas;
	}
}