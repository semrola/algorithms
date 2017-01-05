import java.util.Random;

public class SkipListALG implements Sem2Interface {

	private static final int maxLevel=32;
	private Node header;
	private int level=1;
	
	@Override
	public boolean contains(String s) {
		Node x=header;
		Node already=null;
		for (int i = level-1; i >= 0; i--) {
			while(x.next[i] != null && x.next[i]!=already && x.next[i].value.compareTo(s) < 0) {
				x=x.next[i];
			}
			already=x.next[i];
		}
		x=x.next[0];
		
		return x.value.compareTo(s)==0;
	}	

	@Override
	public boolean add(String s) {
		Node[] update=new Node[maxLevel];
		Node x=header;
		Node already=null;
		for (int i = level-1; i >= 0; i--) {
			while(x.next[i] != null && x.next[i]!=already && x.next[i].value.compareTo(s) < 0) {
				x=x.next[i];
			}
			already=x.next[i];
			update[i]=x;
		}
		x=x.next[0];
		
		if(x!=null && x.value.compareTo(s)==0) {
			return false;
		}
		else {
			int newLevel=randomLvl();
			if(newLevel > level) {
				for (int i = level; i <= newLevel; i++) {
					update[i]=header;
				}
				level=newLevel;
			}
			x=new Node(s, newLevel);
			for (int i = 0; i < newLevel ; i++) {
				x.next[i]=update[i].next[i];
				update[i].next[i]=x;
			}
			return true;
		}
	}

	@Override
	public void removeBetween(String a, String b) {
		int rez=a.compareTo(b);
		if(rez == 0) {
			remove(a);
		}
		else if(rez > 0) {
			return;
		}
		else {
			Node[] update=new Node[maxLevel];
			Node x=header;
			Node already=null;
			for (int i = level-1; i >= 0; i--) {
				while(x.next[i] != null && x.next[i]!=already && x.next[i].value.compareTo(a) < 0) {
					x=x.next[i];
				}
				already=x.next[i];
				update[i]=x;
			}
			x=x.next[0];
			
			Node[] update1=new Node[maxLevel];
			Node x1=update[level-1];
			
			already=null;
			for (int i = level-1; i >= 0; i--) {
				while(x1.next[i] != null && x1.next[i]!=already && x1.next[i].value.compareTo(b) <= 0) {
					x1=x1.next[i];
				}
				already=x1.next[i];
				update1[i]=x1;
			}
			
			int i=0;
			
			if(x1!=null && x1.value!=null) {	//na vseh nivojih vozlisca prevezemo
				for (; i < x1.next.length; i++) {
					update[i].next[i]=x1.next[i];
				}
			}
			
			for (; i < level; i++) {	//se na preostalih nivojih prevezemo
				if(update1[i].next[i]==null) {
					update[i].next[i]=null;
				}
				else {
					update[i].next[i]=update1[i].next[i];
				}
			}
			
		}
		
	}

	@Override
	public boolean remove(String s) {
		Node[] update=new Node[maxLevel];
		Node x=header;
		Node already=null;
		for (int i = level-1; i >= 0; i--) {
			while(x.next[i] != null && x.next[i]!=already && x.next[i].value.compareTo(s) < 0) {
				x=x.next[i];
			}
			already=x.next[i];
			update[i]=x;
		}
		x=x.next[0];
		
		if(x!=null && x.value.compareTo(s)==0) {
			for (int i = 0; i < level; i++) {	//na vseh nivojih prevezi
				if(update[i].next[i] != x) {	//ce ni na tem nivoju, tudi na visjem ni
					break;
				}
				update[i].next[i]=x.next[i];	//prevezi
			}
			while(level-1 > 1 && header.next[level-1] == null) {	//ce so kazalci na null, nizaj nivo
				level--;
			}
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		header=new Node(maxLevel);
		System.gc();
	}

	@Override
	public String[] toArray() {
		String s=toString();
		if(s.equals("")) return new String[0];
//		String[] ss=s.substring(1,s.length()-1).split(",");
		String[] ss=s.split(",");
		return ss;
	}

	@Override
	public String studentId() {
		return "63090142";
	}
	
	public SkipListALG() {
		clear();
	}
	
	private static Random r=new Random();
	private static int kovanec() {
		return r.nextInt(2);
	}
	
	private int randomLvl() {
		int lvl=1;
		while (kovanec()<1 && lvl<maxLevel) {
			lvl++;
		}
		return lvl;
	}
	
	public String toString()
	{
	    StringBuilder sb=new StringBuilder();
	    Node n=header.next[0];
	    while (n != null) {
	        sb.append(n.value);
	        n = n.next[0];
	        if (n != null)
	            sb.append(",");
	    }    	    
	    return sb.toString();
	}
	
	public static void main(String[] args) {

	}
	
	public void details() {
		Node x=header.next[0];
		StringBuilder sb=new StringBuilder();
		while(x!=null) {
			sb.append(x.value+":("+x.next.length+")"+"[");
			for (int i = 0; i < x.next.length; i++) {
				if(x.next[i]!=null) {
					sb.append(x.next[i].value+" ");
				}
			}
			sb.deleteCharAt(sb.toString().length()-1);
			sb.append("] ");
			x=x.next[0];
		}
		System.out.println(sb);
	}
	
	public static void printArray(String[] s) {
		for (int i = 0; i < s.length; i++) {
			System.out.print(s[i]+" ");
		}
	}

}

class Node {
	String value;
	Node[] next;
	
	public Node() {
		this(null,0);
	}
	
	public Node(String s) {
		this.value=s;
	}
	
	public Node(int lvl) {
		this(null,lvl);
	}
	
	public Node(String s, int lvl) {
		value=s;
		next=new Node[lvl];
	}
}