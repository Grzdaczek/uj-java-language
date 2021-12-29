import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

class Point {
	public double x;
	public double y;

	Point (double _x, double _y) {
		x = _x;
		y = _y;
	}

	double getX () {
		return x;
	}

	double getY () {
		return y;
	}
}

class Line {
	public Point a;
	public Point b;
	public double width;

	Line (Point _a, Point _b, double _width) {
		a = _a;
		b = _b;
		width = _width;
	}

	double getWidth() {
		return width;
	}
}

class Canvas extends JPanel {
	public List<Point> nodes;
	public List<Line> edges;

	Canvas() {
		setBackground(new Color(255, 255, 255));
	}

	interface Transform {
		int calc(double p);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (nodes == null || edges == null)
			return;

		double minX = nodes.stream().min(Comparator.comparing(Point::getX)).get().x;
		double maxX = nodes.stream().max(Comparator.comparing(Point::getX)).get().x;
		double minY = nodes.stream().min(Comparator.comparing(Point::getY)).get().y;
		double maxY = nodes.stream().max(Comparator.comparing(Point::getY)).get().y;
		
		double minW = edges.stream().min(Comparator.comparing(Line::getWidth)).get().width;
		double maxW = edges.stream().max(Comparator.comparing(Line::getWidth)).get().width;

		var bounds = g2.getClipBounds();
		int size = Math.min(bounds.width, bounds.height);
		int margin = 100;

		final Transform tX = (double x) -> {
			double normalized = (x - minX) / (maxX - minX);
			int xx = (int) (normalized * (size - margin));
			return xx + ((bounds.width - size + margin) / 2);
		};

		final Transform tY = (double y) -> {
			double normalized = (y - maxY) / (minY - maxY);
			int yy = (int) (normalized * (size - margin));
			return yy + ((bounds.height - size + margin) / 2);
		};

		edges.forEach(line -> {
			double normalized = (line.width - minW) / (maxW - minW);
			int width = (int)(normalized * 12) + 3;
			g2.setStroke(new BasicStroke(width));
			g2.setColor(Color.BLACK);
			g2.drawLine(
				tX.calc(line.a.x),
				tY.calc(line.a.y),
				tX.calc(line.b.x),
				tY.calc(line.b.y)
			);
		});

		nodes.forEach(point -> {
			int r = 10;
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.WHITE);
			g2.fillOval(
				tX.calc(point.x) - r,
				tY.calc(point.y) - r,
				2 * r,
				2 * r
			);
			g2.setColor(Color.BLACK);
			g2.drawOval(
				tX.calc(point.x) - r,
				tY.calc(point.y) - r,
				2 * r,
				2 * r
			);
		});
	}
}

class MainFrame {
	JFrame frame = new JFrame();
	Canvas canvas = new Canvas();

	MainFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(a -> {
			this.loadFile();
			this.updateCanvas();
		});

		JPanel toolBar = new JPanel();
		toolBar.add(loadButton);

		frame.add(BorderLayout.PAGE_START, toolBar);
		frame.add(BorderLayout.CENTER, canvas);
		
		frame.setLocation(100, 100);
		frame.setSize(800, 600);
	}

	public void show() {
		frame.setVisible(true);
	}
	
	void loadFile() {
		List<Point> nodes = new ArrayList<Point>();
		List<Line> edges = new ArrayList<Line>();

		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(frame);

		if (result != JFileChooser.APPROVE_OPTION)
			return;
		
		File file = fileChooser.getSelectedFile();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;

			int nodesNumber = Integer.parseInt(br.readLine());

			for (int i = 0; i < nodesNumber; ++i) {
				line = br.readLine();
				String[] n = line.split(" ");
				int x = Integer.parseInt(n[0]);
				int y = Integer.parseInt(n[1]);
				nodes.add(new Point(x, y));
			}

			int edgesNumber = Integer.parseInt(br.readLine());

			for (int i = 0; i < edgesNumber; ++i) {
				line = br.readLine();
				String[] n = line.split(" ");
				int a = Integer.parseInt(n[0]) - 1;
				int b = Integer.parseInt(n[1]) - 1;
				int w = Integer.parseInt(n[2]);
				edges.add(new Line(nodes.get(a), nodes.get(b), w));
			}
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Błąd wczytywania pliku: \n" + e.getClass());
			return;
		}

		canvas.edges = edges;
		canvas.nodes = nodes;
	}

	void updateCanvas() {
		canvas.updateUI();
	}
}

public class Grapher {
	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.show();
	}
}
