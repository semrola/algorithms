import java.text.DecimalFormat;
import java.util.ArrayList;


public class Test {

	public static void main(String[] args) {
		Point p1=new Point(1, 1);
		Point p2=new Point(2, 2);
		Point p3=new Point(5, 5);
		
		System.out.println(isPointBetweenTwoPoints(p1, p2, p3));
		
		ArrayList<Point> a1=new ArrayList<Point>();
		ArrayList<Point> a2=new ArrayList<Point>();
		a1.add(new Point(1,2));
		a2.add(new Point(1, 2));
		System.out.println("sad: "+a1.contains(a2.get(0))+" - "+a2.contains(a1.get(0)));
		
	}
	
	private static boolean isPointBetweenTwoPoints(Point p1, Point m, Point p2) {
		Double d1=distance(p1, m);
		Double d2=distance(m, p2);
		Double d3=distance(p1, p2);
		Double d=d1+d2;
		d=Math.round(d*100000.0)/100000.0;
		System.out.println(d1+" "+d2+" "+d3);
		int val=d.compareTo(d3);
		System.out.println(d+" "+d3);
		System.out.println(val);
		if(val==0) return true;
		else return false;
	}
	
	private static double distance(Point p1, Point p2) {
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		DecimalFormat f = new DecimalFormat("##.00000000");
		return Math.round(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2))*100000.0)/100000.0;
	}
}
