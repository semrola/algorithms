import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

class MonotoneHull1 {

	public static void main(String[] args) {
		
	}
	
	ArrayList<Point> pts;
	
	public MonotoneHull1(int[] x, int[] y) {
		pts=new ArrayList<Point>();
		for (int i = 0; i < y.length; i++) {
			pts.add(new Point(x[i], y[i]));
//			System.out.println(x[i]+","+y[i]);
		}
		
//		System.out.println("Points:");
//		System.out.print("int[] a={");
//		for (int j = 0; j < x.length; j++)
//			System.out.print(x[j]+",");
//		System.out.println("}");
//		System.out.print("int[] b={");
//		for (int j = 0; j < x.length; j++)
//			System.out.print(y[j]+",");
//		System.out.println("}");
		
		sort();
	}
	
	public int count() {
		if(pts.size() == 0) return 0;
		if(pts.size() < 4)  return 1;
		int st=0;
		while(pts.size() > 3) {
			hull();	//dobimo ovojnico
			//odstranimo tocke z ovojnice
			remove();
			st++;
		}
		if(pts.size()>0) st++;
		return st;
	}
	
	private void remove() {
//		for (int i = 0; i < pts.size(); i++) {
//			Point p=pts.get(i);
//			for (int j = 0; j < upper.size(); j++) {
//				if(p.equals(upper.get(j))) {
//					pts.remove(i);
//					i--;
//				}
//			}
//			for (int j = 0; j < lower.size(); j++) {
//				if(p.equals(lower.get(j))) {
//					pts.remove(i);
//					i--;
//				}
//			}
//		}
		pts.removeAll(current);
	}
	
	ArrayList<Point> upper;
	ArrayList<Point> lower;
	ArrayList<Point> current;
	ArrayList<Point> removal;
	
	private void hull() {
		upper=new ArrayList<Point>();
		lower=new ArrayList<Point>();
		
		for (int i = 0; i < pts.size(); i++) {
			while(lower.size()>=2 && ccw(lower.get(lower.size()-2),lower.get(lower.size()-1),pts.get(i))<0) {
				lower.remove(lower.size()-1);
			}
			lower.add(pts.get(i));
		}
		
		for (int i = pts.size()-1; i >= 0 ; i--) {
			while(upper.size()>=2 && ccw(upper.get(upper.size()-2),upper.get(upper.size()-1),pts.get(i))<0) {
				upper.remove(upper.size()-1);
			}
			upper.add(pts.get(i));
		}
		lower.remove(lower.size()-1);
		upper.remove(upper.size()-1);
		current=new ArrayList<Point>(lower);
		current.addAll(upper);
		
// 		removal=new ArrayList<Point>();
//		for (int i = 0; i < current.size()-1; i++) {
//			int i1=i;
//			int i2=i+1;
//			if(i2==current.size()) i2=0;
//			Point p1=current.get(i1);
//			Point p2=current.get(i2);
//			for (int j = 0; j < pts.size(); j++) {
//				Point m=pts.get(j);
//				if(!current.contains(m) && isPointBetweenTwoPoints(p1, m, p2)) {
//					current.add(m);
//				}
//			}
//		}
		
		
		
//		System.out.println(upper);
//		System.out.println(lower);
	}
	
	private final static double dec=10000.0;
	private static boolean isPointBetweenTwoPoints(Point p1,Point m, Point p2) {
		double d1=distance(p1, m) + distance(m, p2);
		double d2=distance(p1, p2);
		d1=Math.round(d1*dec)/dec;
		d2=Math.round(d2*dec)/dec;
		return d1 == d2;
	}
	
	private static double distance(Point p1, Point p2) {
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}
	
	private static boolean CCW(Point p, Point q, Point r) {
		int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

		if (val > 0)
			return false;
		return true;
	}
	
	
	//def cross(o, a, b):
    //return (a[0] - o[0]) * (b[1] - o[1]) - (a[1] - o[1]) * (b[0] - o[0])
	private static double ccw(Point p, Point q, Point r) {
		return (q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x);
	}
	
	private void sort() {
		Collections.sort(pts, new PointComparator());
	}

}


public class Chull1 {

	public static int solve(int[] x, int[] y) {
		// Jarvis j = new Jarvis(x, y);
		// j.convexHull(points);
		// System.out.println(j.layersCount());
		// System.out.println(j.getAngle(new Point(2,1), new Point(1,1)));
		// return -1;
		// return j.find();
		// j.layersCount();
		// return j.layers;
//		Graham g = new Graham(x, y);
//		return g.layers();
		MonotoneHull1 mh=new MonotoneHull1(x, y);
		return mh.count();
	}

	public static int solve1(int[] x, int[] y) {

		ArrayList<Point> al = new ArrayList<Point>();
		for (int i = 0; i < y.length; i++) {
			al.add(new Point(x[i], y[i]));
		}
		// System.out.println(al);

		int lyrs = 0;
		boolean[] visited = new boolean[x.length];

		if (x.length < 4) {
			return 1;
		}

		int minAngleIndex = 0;
		int currentIndex = 0;
		Integer lowest = dobiNajnizjo(x, y, visited);
		currentIndex = lowest;
		visited[lowest] = true;
		int[] layer = new int[x.length]; // na kateri plasti so

		int nob = x.length;
		while (nob > 1) {
			minAngleIndex = dobiKot(currentIndex, x, y, layer);
			currentIndex = minAngleIndex;
			if (visited[currentIndex]) {
				lyrs++;
				for (int i = 0; i < visited.length; i++) {
					if (visited[i] && layer[i] == 0) {
						layer[i] = lyrs;
					}
				}

				if (vsiDone(visited)) {
					return lyrs;
				}

				currentIndex = dobiNajnizjo(x, y, visited);
			} else {
				visited[minAngleIndex] = true;
			}
			nob = neobiskani(visited);
		}
		if (neobiskani(visited) != 0)
			lyrs++;
		return lyrs;
	}

	static boolean vsiDone(boolean[] visited) {
		for (int i = 0; i < visited.length; i++) {
			if (!visited[i]) {
				return false;
			}
		}
		return true;
	}

	static int neobiskani(boolean[] visited) {
		int a = 0;
		for (int i = 0; i < visited.length; i++) {
			if (!visited[i]) {
				a++;
			}
		}
		return a;
	}

	static Integer dobiNajnizjo(int[] x, int[] y, boolean[] vist) {
		int lowIndex = 0;
		int low = y[0];
		for (int i = 0; i < y.length; i++) {
			if (y[i] < low && !vist[i]) {
				low = y[i];
				lowIndex = i;
			}
		}
		return lowIndex;
	}

	static double prejsnji;

	static int dobiKot(int index, int[] x, int[] y, int[] lyr) {
		double[] alls = new double[y.length];

		int minIndex = 0;
		double minAngle = 0;
		boolean first = true;
		double angle, clAngle;

		for (int i = 0; i < x.length; i++) {
			if (i != index && lyr[i] == 0) {
				clAngle = getAngle(x[index], y[index], x[i], y[i]);
				alls[i] = clAngle;
				angle = clAngle - prejsnji;
				if (angle < 0) {
					angle = 360 + angle; // angle+=360
				}
				if (first) {
					minAngle = angle;
					minIndex = i;
					first = false;
				} else {
					if (angle < minAngle) {
						minAngle = angle;
						minIndex = i;
					} else if (angle == minAngle) {
						// vzami tisto ki je blizje od current
						double d1 = getDistance(x[index], y[index], x[i], y[i]);
						double d2 = getDistance(x[index], y[index],
								x[minIndex], y[minIndex]);
						if (d1 < d2) {
							minIndex = i;
						}
					}
				}

			}
		}
		prejsnji = alls[minIndex];
		return minIndex;
	}

	static double getDistance(int x1, int y1, int x2, int y2) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}

	static double getAngle(int x1, int y1, int x2, int y2) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		double ang = Math.atan2(dy, dx) * 180 / Math.PI;
		return ang < 0 ? 360 + ang : ang;
	}

	public static void main(String[] args) {
		 int[] a = { 1, 2, 3, 4, 5 };
		 int[] b = { 1, 2, 3, 4, 5 };
//		int[] a={3,1,-3,-3,-3};
//		int[] b={0,-3,1,-3,0};
		for (int i = 0; i < b.length; i++) {
			System.out.println(a[i]+","+b[i]);
		}
		System.out.println(solve(a, b));
		// Jarvis j = new Jarvis(a, b);
		// j.layersCount();
		// // System.out.println(j.find());
		// System.out.println(j.layers);

	}

	public static String studentId() {
		return "63090142";
	}
}

class Jarvis {

	ArrayList<Point> points;
	HashSet<Point> hs = new HashSet<Point>();
	Point min;
	Point current;
	int layers;

	public Jarvis(int[] x, int[] y) {
		min = new Point(x[0], y[0]);
		points = new ArrayList<Point>(x.length);
		for (int i = 0; i < y.length; i++) {
			Point p = new Point(x[i], y[i]);
			points.add(p);
			if (p.isLower(min)) {
				min = p;
			}
		}
		current = min;
	}

	ArrayList<Double> angles = new ArrayList<Double>(); // dodaj init capacity
	double minAngle;
	Point minAnglePoint;

	void layersCount() {
		layers = 0;
		if (points.size() < 4) {
			layers = 1;
			return;
		}
		ArrayList<Point> currentLayer = new ArrayList<Point>();
		currentLayer.add(current);
		// System.out.println("INIT: " + points);
		while (points.size() > 3) {
			getMinAngle();
			current = minAnglePoint; // v naslednjo tocko
			if (current.visited) {
				layers++; // smo naokoli
				// System.out.println("Layer "+layers+": "+currentLayer);
				// iz points izbrisi vse ki so v currentLayer
				for (int i = 0; i < currentLayer.size(); i++) {
					Point p = currentLayer.get(i);
					remove(p.x, p.y);
				}
				currentLayer.clear();
				getLowestPoint();
				current = min;
				if (current == null) {
					return;
				}
			} else {

				current.visited = true;
				currentLayer.add(minAnglePoint);
			}
		}
		// System.out.println("Points: "+points);
		if (points.size() > 0) {
			layers++;
		}
		return;
	}

	double prevAngle = 0;

	void getMinAngle() {
		ArrayList<Double> allAnglesClean = new ArrayList<Double>(
				points.size() - 1);
		ArrayList<Double> allAngles = new ArrayList<Double>(points.size() - 1);
		double[] alls = new double[points.size()];

		int minIndex = 0;
		boolean first = true;
		double angle, clAngle;
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (!current.equals(p)) {
				clAngle = getAngle(current, p);
				allAnglesClean.add(clAngle);
				alls[i] = clAngle;
				angle = clAngle - prevAngle;
				allAngles.add(angle);
				if (angle < 0) {
					angle = 180 + angle; // angle+=360
				}
				if (first) {
					minAngle = angle;
					minAnglePoint = p;
					minIndex = i;
					first = false;
				} else {
					if (angle < minAngle) {
						minAngle = angle;
						minAnglePoint = p;
						minIndex = i;
					} else if (angle == minAngle) {
						// vzami tisto ki je blizje od current
						double d1 = getDistance(current, p);
						double d2 = getDistance(current, minAnglePoint);
						if (d1 < d2) {
							minAnglePoint = p;
							minIndex = i;
						}
					}
				}

			}
		}
		prevAngle = alls[minIndex];
	}

	void getLowestPoint() {
		if (points.size() == 0) {
			min = null;
			return;
		}
		min = points.get(0);
		for (Point p : points) {
			if (p.isLower(min)) {
				min = p;
			}
		}
	}

	double getDistance(Point p1, Point p2) {
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}

	double getAngle(Point p1, Point p2) {
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		double ang = Math.atan2(dy, dx) * 180 / Math.PI;
		return ang < 0 ? 360 + ang : ang;
	}

	boolean contains(Collection<Point> lst, Point p) {
		for (Point d : lst) {
			if (p.equals(d)) {
				return true;
			}
		}
		return false;
	}

	public int find() {
		if (points.size() == 0)
			return 0;
		else if (points.size() < 4)
			return 1;

		int st = 0;
		do {
			convexHull(points.toArray(new Point[points.size()]));
			st++;
		} while (points.size() > 0);
		return st;
	}

	public void convexHull(Point[] points) {
		int n = points.length;
		/** if less than 3 points return **/
		if (n < 3)
			return;
		int[] next = new int[n];
		Arrays.fill(next, -1);

		/** find the leftmost point **/
		int leftMost = 0;
		for (int i = 1; i < n; i++)
			if (points[i].x < points[leftMost].x)
				leftMost = i;
		int p = leftMost, q;
		/** iterate till p becomes leftMost **/
		do {
			/** wrapping **/
			q = (p + 1) % n;
			for (int i = 0; i < n; i++)
				if (CCW(points[p], points[i], points[q]))
					q = i;

			next[p] = q;
			p = q;
		} while (p != leftMost);

		for (int i = 0; i < next.length; i++)
			if (next[i] != -1) {
				int x = points[i].x;
				int y = points[i].y;
				remove(x, y);
			}
	}

	private void remove(int x, int y) {
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).x == x && points.get(i).y == y) {
				points.remove(i);
				break;
			}
		}
	}

	private boolean CCW(Point p, Point q, Point r) {
		int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

		if (val >= 0)
			return false;
		return true;
	}

}

class Point1 {
	int x;
	int y;
	boolean visited;

	public Point1(int x, int y) {
		this.x = x;
		this.y = y;
		visited = false;
	}

	boolean isLower(Point p) {
		return this.y < p.y;
	}

	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Point))
			return false;
		Point s = (Point) other;
		return this.y == s.y && this.x == s.x;
	}
	
	@Override
    public int hashCode() {
		return ((int)Math.pow(this.x, 2))+this.y;
    }
}

class Graham {

	int[] x;
	int[] y;

	public Graham(int[] a, int[] b) {
		x = a.clone();
		y = b.clone();
	}

	int layers() {
		if (x.length < 4) {
			return 1;
		}

		int st = 0;
		int lowestIndex = dobiNajnizjo();
		ArrayList<Point> currentLayer = new ArrayList<Point>();
		ArrayList<AnglePoint> anglesFromCurrentLowest = new ArrayList<AnglePoint>(
				x.length - 1);
		calcAngles(anglesFromCurrentLowest, lowestIndex);
		Collections.sort(anglesFromCurrentLowest);

		return st;
	}

	void calcAngles(ArrayList<AnglePoint> al, int low) {
		double ang = 0;
		double dist = 0;
		for (int i = 0; i < x.length; i++) {
			if (i == low)
				continue;
			ang = getAngle(x[low], y[low], x[i], y[i]);
			dist = getDistance(x[low], y[low], x[i], y[i]);
			al.add(new AnglePoint(ang, new Point(x[i], y[i]), dist, i));
		}
	}

	Integer dobiNajnizjo() {
		int lowIndex = 0;
		int low = y[0];
		for (int i = 0; i < y.length; i++) {
			if (y[i] == low) {
				if (x[i] < x[lowIndex]) {
					low = y[i];
					lowIndex = i;
				}
			} else if (y[i] < low) {
				low = y[i];
				lowIndex = i;
			}
		}
		return lowIndex;
	}

	static double getDistance(Point p1, Point p2) {
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}

	static double getDistance(int p1x, int p1y, int p2x, int p2y) {
		int dx = p2x - p1x;
		int dy = p2y - p1y;
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}

	static boolean ccw(int p1x, int p1y, int p2x, int p2y, int p3x, int p3y) {
		return ((p2x - p1x) * (p3y - p1y) - (p2y - p1y) * (p3x - p1x)) >= 0;
	}

	static boolean ccw(Point p1, Point p2, Point p3) {
		return ((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)) >= 0;
	}

	static double getAngle(int p1x, int p1y, int p2x, int p2y) {
		int dx = p2x - p1x;
		int dy = p2y - p1y;
		double ang = Math.atan2(dy, dx) * 180 / Math.PI;
		return ang < 0 ? 360 + ang : ang;
	}

	class AnglePoint implements Comparable<AnglePoint> {
		double angle;
		Point p;
		double distance;
		int index;

		public AnglePoint(double ang, Point p, double dist, int i) {
			angle = ang;
			this.p = p;
			distance = dist;
			index = i;
		}

		@Override
		public String toString() {
			return "{" + this.angle + "," + this.p + "}";
		}

		public int compare(AnglePoint arg0, AnglePoint arg1) {
			if (arg0.angle < arg1.angle)
				return -1;
			else if (arg0.angle > arg1.angle)
				return 1;
			else
				return 0;
		}

		@Override
		public int compareTo(AnglePoint arg0) {
			if (this.angle < arg0.angle)
				return -1;
			else if (this.angle > arg0.angle)
				return 1;
			else {
				if (this.distance < arg0.distance) {
					return -1;
				} else if (this.distance > arg0.distance) {
					return 1;
				}
				return 0;
			}
		}
	}
}

class PointComparator implements Comparator<Point> {

	@Override
	public int compare(Point o1, Point o2) {
		if(o1.x<o2.x) return -1;
		else if(o1.x==o2.x) {
			if(o1.y<o2.y) {
				return -1;
			}
		}
		return 1;
	}
	
}