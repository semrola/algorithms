import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
 
public class FinderTest {
  public static void makeGrid(Random r, char[][] grid, int dim, String[] words, int n) {
    for (int i=0; i<dim; i++) {
      for (int j=0; j<dim; j++) {
        grid[i][j] = (char) (r.nextInt(26) + 97);
      }
    }
 
    for (int i=0; i<n; i++) {
      int wl = r.nextInt(50) + 1;
      StringBuffer sb = new StringBuffer(wl);
      for (int j=0; j<wl; j++) {
        char c = (char) (r.nextInt(26) + 97);
        sb.append(c);
      }
      words[i] = sb.toString();
    }
  }
 
  public static void main(String[] args) {
    int dim = 50;
    char[][] grid = new char[dim][dim];
 
    int n = 500000;
    String[] words = new String[n];
 
    makeGrid(new Random(1), grid, dim, words, n);
 
    String[] result = Finder.solve(words, grid, 1);    

    HashSet<String> hs = new HashSet<String>();
    
    for (String string : result) {
    	hs.add(string);
	}
    System.out.println(hs.size());
  }
}