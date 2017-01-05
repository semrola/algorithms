import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

public class Chull {
	
	private static int ccw(Point p, Point q, Point r) {
		return (q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x);
	}

	public static int solve(int[] x, int[] y) {
		if (x.length == 0)
			return 0;
		if (x.length < 4)
			return 1;

		ArrayList<Point> pts = new ArrayList<Point>();
		ArrayList<Point> upper = new ArrayList<Point>();
		ArrayList<Point> lower = new ArrayList<Point>();

		//zapisemo tocke v arraylist - O(n)
		for (int i = 0; i < y.length; i++) {
			pts.add(new Point(x[i], y[i]));
		}
		//sortiramo tocke glede na comparator, ki je v Point - O(nlogn)
		Collections.sort(pts);
		Point p;
		int st = 0;
		while (pts.size() > 3) {
			//pocistimo seznama - O(1)
			upper.clear();
			lower.clear();
			//sprehod od prve tocke proti zadnji - O(n) (operacije znotraj O(1))
			for (int i = 0; i < pts.size(); i++) {
				p=pts.get(i);
				while (lower.size() >= 2 && ccw(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) < 0) {
					lower.remove(lower.size()-1);
				}
				lower.add(p);
			}

			//sprehod od zadnje tocke proti prvi - O(n) (operacije znotraj O(1))
			for (int i = pts.size() - 1; i >= 0; i--) {
				p=pts.get(i);
				while (upper.size() >= 2 && ccw(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) < 0) {
					upper.remove(upper.size()-1);
				}
				upper.add(p);
			}
			//odstranimo zadnjo, ker je enaka prvi - O(1)
			lower.remove(lower.size() - 1);
			upper.remove(upper.size() - 1);

			//odstranimo zgornji in spodnji del ovojnice iz seznama vseh tock - O(m) m-trenutno st. tock na ovojnici 
			pts.removeAll(lower);
			pts.removeAll(upper);
			st++;
		}

		if (pts.size() > 0)
			st++;
		return st;
	}

	//ena skrajnost: na vsaki plasti vzamemo minimalno st. tock = 3 -> vec iteracij iskanja plasti (n/3)*(O(nlogn)+O(3))==O((n^2)*logn) - to je wc
	//druga skrajnost: ena iteracija/ovojnica -> vzamemo vse toèke na plasti - 1*(O(nlogn)+O(n)) == O(nlogn) - to je bc
	//sredinska moznost: na vsaki plasti vzamemo enako st. tock n/m*(O(nlogn)+O(m))==O(n^2logn)
	
}

class Point implements Comparable<Point> {
	int x;
	int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

	@Override
	public int compareTo(Point o) {
		if (this.x < o.x)
			return -1;
		else if (this.x == o.x) {
			if (this.y < o.y) {
				return -1;
			}
		}
		return 1;
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
}