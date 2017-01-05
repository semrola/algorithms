import java.util.*;

public class HullTest {
	public static int[][] getPoints(Random rand, int n) {
		int[][] result = new int[2][n];

		int count = 0;

		while (count < n) {
			int x = rand.nextInt(2 * n + 1) - n;
			int y = rand.nextInt(2 * n + 1) - n;
			if (x * x + y * y < n * n) {
				boolean duplicate = false;
				for (int i = 0; i < count; i++)
					if (x == result[0][i] && y == result[1][i])
						duplicate = true;
				if (!duplicate) {
					result[0][count] = x;
					result[1][count] = y;
					count++;
				}
			}
		}

		return result;
	}

	public static void main(String[] args) {
		int nTests = 14;

		for (int i = 0; i < nTests; i++) {
			int[] x;
			int[] y;
			int solution = 0;
			int[][] res;
			switch (i) {
			case 0:
				res = getPoints(new Random(0), 1);
				x = res[0];
				y = res[1];
				solution = 1;
				break;
			case 1:
				res = getPoints(new Random(0), 3);
				x = res[0];
				y = res[1];
				solution = 1;
				break;
			case 2:
				res = getPoints(new Random(0), 5);
				x = res[0];
				y = res[1];
				solution = 1;
				break;
			case 3:
				x = new int[] { 1, 2, 3, 4, 5 };
				y = new int[] { 1, 2, 3, 4, 5 };
				solution = 1;
				break;
			case 4:
				res = getPoints(new Random(0), 10);
				x = res[0];
				y = res[1];
				solution = 2;
				break;
			case 5:
				res = getPoints(new Random(0), 20);
				x = res[0];
				y = res[1];
				solution = 3;
				break;
			case 6:
				res = getPoints(new Random(0), 50);
				x = res[0];
				y = res[1];
				solution = 6;
				break;
			case 7:
				res = getPoints(new Random(1), 100);
				x = new int[] { 1, 2, 3, 4, 4, 4, 4, 3, 2, 1, 1, 1 };
				y = new int[] { 4, 4, 4, 4, 3, 2, 1, 1, 1, 1, 2, 3 };
				solution = 1;
				break;
			case 8:
				res = getPoints(new Random(1), 100);
				x = res[0];
				y = res[1];
				solution = 11;
				break;
			case 9:
				res = getPoints(new Random(1), 5000);
				x = res[0];
				y = res[1];
				solution = 135;
				break;
			case 10:
				res = getPoints(new Random(5), 10000);
				x = res[0];
				y = res[1];
				solution = 215;
				break;
			case 11:
				res = getPoints(new Random(6), 10000);
				x = res[0];
				y = res[1];
				solution = 213;
				break;
			default:
				x = new int[0];
				y = new int[0];
				return;
			}

			if (x.length > 0) {
				System.out.format("n = %5d", x.length, solution);

				try {
					long time = System.currentTimeMillis();

					int result = Chull.solve(x, y);
					time = System.currentTimeMillis() - time;

					if (result != solution) {
						System.out.println("\t\tINCORRECT (returned " + result
								+ ", should be " + solution + ")");
						if (x.length < 100) {
							System.out.println("Points:");
							System.out.print("int[] a={");
							for (int j = 0; j < x.length; j++)
								System.out.print(x[j]+",");
							System.out.println("}");
							System.out.print("int[] b={");
							for (int j = 0; j < x.length; j++)
								System.out.print(y[j]+",");
							System.out.println("}");
							for (int j = 0; j < x.length; j++)
								System.out.println(x[j]+","+y[j]);
						}
					} else
						System.out.println("\t\tCORRECT (" + time + " ms)");
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}

		}
	}
}
