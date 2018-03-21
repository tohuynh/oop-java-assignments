import java.util.*;

/*
 Shape data for ShapeClient:
 "0 0  0 1  1 1  1 0"
 "10 10  10 11  11 11  11 10"
 "0.5 0.5  0.5 -10  1.5 0"
 "0.5 0.5  0.75 0.75  0.75 0.2"
*/

public class Shape {
	private List<Point> points;
	private Point center;
	private double radius;
	
	public Shape(String pointsInput) {
		points = new ArrayList<Point>();
		double sumX = 0.0;
		double sumY = 0.0;
		int count = 0;
		Scanner s = new Scanner(pointsInput);
		while (s.hasNextDouble()) {
			double x = s.nextDouble();
			double y = s.nextDouble();
			sumX += x;
			sumY += y;
			Point p = new Point(x,y);
			points.add(p);
			count++;
			
		}
		s.close();
		
		center = new Point(sumX/count, sumY/count);
		
		double minDistance = Double.POSITIVE_INFINITY;
		for (Point p: points) {
			double distance = center.distance(p);
			if (distance < minDistance) minDistance = distance;
		}
		
		radius = minDistance;
		
	}
	
	public boolean cross(Shape other) {
		for (int i = 0; i < points.size(); i++) {
			int nextPointIndex = (i < points.size() - 1) ? i + 1 : 0;
			if ((other.contains(points.get(i)) ^ other.contains(points.get(nextPointIndex)))) {
				return true;
			}
		}
		return false;
	}
	
	public int encircle(Shape other) {
		if (this.contains(other.center)) {
			return 2;
		} else if (this.intersects(other)) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private boolean contains(Point a) {
		return center.distance(a) <= radius;
	}
	
	private boolean intersects(Shape other) {
		return center.distance(other.center) <= radius + other.radius;
	}
}

