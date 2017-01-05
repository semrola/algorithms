import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GrahamsScan {
	public static final long serialVersionUID = 1L;

	// variables
	Random rnd;
	int pNum = 100;
	int xPoints[];
	int yPoints[];
	int num;
	int xPoints2[];
	int yPoints2[];
	int w, h;
	//

	class pData implements Comparable<pData> {
		int index;
		double angle;
		long distance;

		pData(int i, double a, long d) {
			index = i;
			angle = a;
			distance = d;
		}

		// for sorting
		public int compareTo(pData p) {
			if (this.angle < p.angle)
				return -1;
			else if (this.angle > p.angle)
				return 1;
			else {
				if (this.distance < p.distance)
					return -1;
				else if (this.distance > p.distance)
					return 1;
			}
			return 0;
		}
	}

	public void init() {

		rnd = new Random();

		xPoints = new int[pNum];
		yPoints = new int[pNum];

		grahamsScan();

	}

	public double angle(int o, int a) {
		return Math.atan((double) (yPoints[a] - yPoints[o])
				/ (double) (xPoints[a] - xPoints[o]));
	}

	public long distance(int a, int b) {
		return ((xPoints[b] - xPoints[a]) * (xPoints[b] - xPoints[a]) + (yPoints[b] - yPoints[a])
				* (yPoints[b] - yPoints[a]));
	}

	public int ccw(int p1, int p2, int p3) {
		return (xPoints[p2] - xPoints[p1]) * (yPoints[p3] - yPoints[p1])
				- (yPoints[p2] - yPoints[p1]) * (xPoints[p3] - xPoints[p1]);
	}

	public void swap(int[] stack, int a, int b) {
		int tmp = stack[a];
		stack[a] = stack[b];
		stack[b] = tmp;
	}

	public void grahamsScan() {
		// random points
		int x, y;
		for (int i = 0; i < pNum; i++) {
			xPoints[i] = 25 + rnd.nextInt(w - 50);
			yPoints[i] = 25 + rnd.nextInt(h - 50);
		}

		// convex hull routine

		// (0) find the lowest point
		int min = 0;
		for (int i = 1; i < pNum; i++) {
			if (yPoints[i] == yPoints[min]) {
				if (xPoints[i] < xPoints[min])
					min = i;
			} else if (yPoints[i] < yPoints[min])
				min = i;
		}
		// System.out.println("min: " + min);

		ArrayList<pData> al = new ArrayList<pData>();
		double ang;
		long dist;
		// (1) calculate angle and distance from base
		for (int i = 0; i < pNum; i++) {
			if (i == min)
				continue;
			ang = angle(min, i);
			if (ang < 0)
				ang += Math.PI;
			dist = distance(min, i);
			al.add(new pData(i, ang, dist));
		}
		// (2) sort by angle and distance
		Collections.sort(al);

		// for (Iterator<pData> i=al.iterator(); i.hasNext(); ) {
		// pData data = i.next();
		// System.out.println(data.index + ":" + data.angle + "," +
		// data.distance);
		// }

		// (3) create stack
		int stack[] = new int[pNum + 1];
		int j = 2;
		for (int i = 0; i < pNum; i++) {
			if (i == min)
				continue;
			pData data = al.get(j - 2);
			stack[j++] = data.index;
		}
		stack[0] = stack[pNum];
		stack[1] = min;

		// (4)
		int tmp;
		int M = 2;
		for (int i = 3; i <= pNum; i++) {
			while (ccw(stack[M - 1], stack[M], stack[i]) <= 0)
				M--;
			M++;
			swap(stack, i, M);
		}

		// assign border points
		num = M;
		xPoints2 = new int[num];
		yPoints2 = new int[num];
		for (int i = 0; i < num; i++) {
			xPoints2[i] = xPoints[stack[i + 1]];
			yPoints2[i] = yPoints[stack[i + 1]];
		}
	}
}
