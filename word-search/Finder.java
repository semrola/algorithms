import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Finder {

	public static String[] solve(String[] words, char[][] grid, int maxThreads) {
		WordSearch w;
		
		if(maxThreads == 1) {
			w = new WordSearch(words, grid, maxThreads - 1);
			return w.iskanje();
		}
		
		if (maxThreads > 1) {
			w = new WordSearch(words, grid, maxThreads - 1);
			return w.iskanjeParalel();
		}
		
		return null;
	}

}

class WordSearch {
	public static char[][] tabela; // tabela crk
	public static String[] seznam; // seznam vseh besed kot stringov
	public boolean[] found;
	int nitovje;

	HashMap<String, Boolean> res = new HashMap<>();
	HashSet<String> stringsFound = new HashSet<String>();

	Map<String, Boolean> resTh = Collections
			.synchronizedMap(new HashMap<String, Boolean>());
	Set<String> stsFound=Collections.synchronizedSet(new HashSet<String>());
	
	HashMap<Character, ArrayList<Integer[]>> index=new HashMap<Character, ArrayList<Integer[]>>();
	
	public WordSearch(String[] words, char[][] tabela1, int threads) {
		seznam = words;
		tabela = tabela1;
		found = new boolean[words.length];
		nitovje = threads;
		
		for (int i = 0; i < tabela.length; i++) {
			for (int j = 0; j < tabela.length; j++) {
				if(!index.containsKey(tabela[i][j])) {
					index.put(tabela[i][j], new ArrayList<Integer[]>());
				}
				index.get(tabela[i][j]).add(new Integer[]{i,j});
			}
		}
	}

	public String[] iskanje() // najdemo prvo ujemajoco crko/stevilo
	{
		String word;
		for (int v = 0; v < seznam.length; v++) // vse besede
		{
			word = seznam[v];
			if(word.length()>tabela.length) {
				continue;
			}
			if (res.containsKey(word)) { // resitev ze obstaja
				continue;
			}
			char c=word.charAt(0);
			ArrayList<Integer[]> indexes=index.get(c);
			if(indexes==null) {
				res.put(word, false);
				continue;
			}
			
			for (int i = 0; i < indexes.size(); i++) {
				Integer[] ab=indexes.get(i);
				int a=ab[0];
				int b=ab[1];
				
				if (seznam[v].length() == 1) {
					res.put(word, true);
					stringsFound.add(word);
					break;
				} else {
					if(check(a, b, v)==2) {
						res.put(word, true);
						stringsFound.add(word);
						break;
					}
				}
			}
			// ce pridemo cez celo tabelo in ne najdemo besede, to zabelezimo
			res.put(word, false);
		}
		return stringsFound.toArray(new String[stringsFound.size()]);
	}

	public String[] iskanjeParalel() // najdemo prvo ujemajoco crko/stevilo
	{
		ExecutorService exe = Executors.newFixedThreadPool(nitovje);
		String word;
		for (int v = 0; v < seznam.length; v++) // vse besede
		{
			word = seznam[v];
			if(word.length()>tabela.length) {
				continue;
			}
			if (resTh.containsKey(word)) { // resitev ze obstaja
				continue;
			}

			Callable<String> worker = this.new Another(v);
			exe.submit(worker);
		}

		exe.shutdown();
		while (!exe.isTerminated()) {
		}
		return stsFound.toArray(new String[stsFound.size()]);
	}

	public int check(int a, int b, int v) // preveri drugo crko/stevilo
											// in doloci smer
	{
		char c = seznam[v].charAt(1);
		int x=1;
		if (a - 1 >= 0)
			if (tabela[a - 1][b] == c) { // GOR
				x=tmp(0, a, b, -1, 0, v);
				if(x==2) return x;
			}
		
		if (a - 1 >= 0 && b + 1 < tabela.length)
			if (tabela[a - 1][b + 1] == c) { // DESNO GOR
				x=tmp(1, a, b, -1, 1, v);
				if(x==2) return x;
			}

		if (b + 1 < tabela[0].length)
			if (tabela[a][b + 1] == c) { // DESNO
				x=tmp(2, a, b, 0, 1, v);
				if(x==2) return x;
			}

		if (a + 1 < tabela.length && b + 1 < tabela[0].length)
			if (tabela[a + 1][b + 1] == c) { // DESNO DOL
				x=tmp(3, a, b, 1, 1, v);
				if(x==2) return x;
			}

		if (a + 1 < tabela.length)
			if (tabela[a + 1][b] == c) { // DOL
				x=tmp(4, a, b, 1, 0, v);
				if(x==2) return x;
			}

		if (a + 1 < tabela.length && b - 1 >= 0)
			if (tabela[a + 1][b - 1] == c) { // LEVO DOL
				x=tmp(5, a, b, 1, -1, v);
				if(x==2) return x;
			}

		if (b - 1 >= 0)
			if (tabela[a][b - 1] == c) { // LEVO
				x=tmp(6, a, b, 0, -1, v);
				if(x==2) return x;
			}

		if (a - 1 >= 0 && b - 1 >= 0)
			if (tabela[a - 1][b - 1] == c) { // LEVO GOR
				x=tmp(7, a, b, -1, -1, v);
				if(x==2) return x;
			}
		return x;
	}

	public int tmp(int smer, int a, int b, int smerA, int smerB, int v) {
		if (seznam[v].length() == 2) {
			return 2;
		}
		
		return direction(a + smerA, b + smerB, smerA, smerB, v);
	}

	public int direction(int a, int b, int m, int n, int v) {
		int tf = 1;
		for (int i = 2; i < seznam[v].length(); i++) {
			a += m;
			b += n;
			if (a >= 0 && b >= 0 && a < tabela.length && b < tabela[0].length) {
				if (seznam[v].charAt(i) == tabela[a][b]) {
					tf = 2;
				} else {
					tf = 1;
					break;
				}
			} else {
				tf = 1;
				break;
			}
		}
		return tf;
	}

	private class Another implements Callable<String> {
		int v;
		String word;

		public Another(int vv) {
			v = vv;
			word = seznam[v];
		}

		@Override
		public String call() {
			char c=word.charAt(0);
			ArrayList<Integer[]> indexes=index.get(c);
			if(indexes==null) {
				resTh.put(word, false);
				return "";
			}
			for (int i = 0; i < indexes.size(); i++) {
				Integer[] ab=indexes.get(i);
				int a=ab[0];
				int b=ab[1];
					
				if (word.length() == 1) {
					resTh.put(word, true);
					stsFound.add(word);
					return word;
				} else {
					if(check(a, b, v)==2) {
						resTh.put(word, true);
						stsFound.add(word);
						return word;
					}
				}
			}
			resTh.put(word, false);
			return "";
		}

	}
}
