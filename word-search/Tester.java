import java.util.Random;


public class Tester {

	static int maxSize=50;
	static int maxWords=500000;
	static int maxLength=maxSize;
	static int maxThreads=16;
	
	static char[][] grid;
	static String[] words;
	
	public static void main(String[] args) {
		grid=new char[maxSize][maxSize];
		words=new String[maxWords];
		
		Random r=new Random();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				int  n = r.nextInt(26) + 1;
				grid[i][j]=(char) n;
			}
		}
		
		for (int i = 0; i < words.length; i++) {
			int len=r.nextInt(48)+2;
			StringBuilder sb=new StringBuilder();
			for (int j = 0; j < len; j++) {
//				int a=r.nextInt(maxLength-1)+0;
//				int b=r.nextInt(maxLength-1)+0;
				int a=r.nextInt(maxLength-1)+0;
				int b=r.nextInt(maxLength-1)+0;
				sb.append(r.nextInt(26) + 97);
			}
			words[i]=sb.toString();
		}
		
		
		long st = System.nanoTime();
		String[] ena=Finder.solve(words, grid, 1);
		System.out.println((System.nanoTime() - st) / 1000000 + " ms");
		System.out.println(ena.length);
		
		long sta = System.nanoTime();
		String[] dve=Finder.solve(words, grid, 4);
		System.out.println((System.nanoTime() - sta) / 1000000 + " ms");
		System.out.println(dve.length);
	}

}
