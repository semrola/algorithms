import java.util.ArrayList;

public class Grammar {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static boolean solve(String[] grammar, String word)
	// You may assume that your method will be tested in the following setting:
	// - grammar will contain between 1 and 100 strings,
	// - each string will represent one production (possibly with multiple right
	// hand sides, separated by | (see examples)),
	// - word can be empty or up to 10000 terminal symbols (characters) long.
	{
		// Your solution.
		Earley e=new Earley(grammar, word);
		return e.parse();		
		//return e.solved();
	}
}

class Earley {
	public final static String dot="�";
	public final static String is="->";
	public final static String pip="|";
	public final static String pipReg="\\|";
	
	String niz;	//niz ki iscemo ce ustreza gramatiki
	String input;	//input string s premikajoco se piko
	ArrayList<ArrayList<State>> set;	//vsi seti - posamezna pravila s piko
	ArrayList<State> currentSet;	//referenca na trenutni set
	ArrayList<State> rules;	//zacetna pravila brez pike
	String[] rulesPlain;
	
	public Earley(String[] grammar, String word) {
		niz=word;
		input=word;
		rulesPlain=grammar.clone();
		set=new ArrayList<ArrayList<State>>(niz.length()+1);
		rules=new ArrayList<State>();
		for (int i = 0; i < grammar.length; i++) {
			String[] grammarSplit=grammar[i].split(is);
			String left=grammarSplit[0];
			String[] allProductions=grammarSplit[1].split(pipReg);
			for (int j = 0; j < allProductions.length; j++) {
				String right=allProductions[j];
				rules.add(new State(left,right,0));
			}
		}
	}
	
	public static void main(String[] args) {
		String grammar[] = {"S->~"};
		String word="~";
		Earley e=new Earley(grammar, word);
		System.out.println(e.parse());
	}
	
	boolean parse() {
		//v prvi set damo zacetno stanje
		set.add(new ArrayList<State>());
		set.get(0).add(new State("S'",dot+"S",0)); //v prvi set dodam state
		
		for (int i = 0; i < niz.length()+1; i++) {	//cez celoten vhodni niz (+1 zaradi pike)
			if(set.size()==i) return false;
			currentSet=set.get(i);
			//vsako iteracijo premaknemo piko v inputu za 1 mesto
			input=moveDotToPos(input, i);
			int currentSize=0;
			int lastSize=1;
			while(lastSize!=currentSize) {	//ponavljamo dokler se st. stanj ne spreminja vec
				lastSize=set.get(i).size();
				
				for (int j = 0; j < currentSet.size(); j++) {
					State st=currentSet.get(j);
					int dotPos=st.righthand.indexOf(dot);
					
					char c;
					if(dotPos<st.righthand.length()-1) {
						c=st.righthand.charAt(dotPos+1);
					}
					else {
						c='\0';
					}
					if((isNonTerminal(c) || isEmpty(c))) {//da ne gremo iz besede
						//nonterminal - predictor
						doPredictor(st,c,i);
					}
					if(isTerminal(c) || isEmpty(c)) {
						//terminal - scanner
						doScanner(st,c,i);
					}
					doCompleter(st,i);
				}
				currentSize=set.get(i).size();
			}
		}
		
		return solved();
	}
	
	boolean solved() {
		ArrayList<State> lastSet=set.get(set.size()-1);
		//System.out.println("Set:"+lastSet);
		//State lastState=lastSet.get(lastSet.size()-1);
		State res=new State("S'","S"+dot,0);
		for (int i = 0; i < lastSet.size(); i++) {
			if(equals(lastSet.get(i), res)) {
				return true;
			}
		}
		return false;
	}
	
	void doPredictor(State st, char pr, int j) {
		State rule;
		for (int i = 0; i < rules.size(); i++) {
			rule=rules.get(i);
			if(rule.lefthand.startsWith(String.valueOf(pr))) {
				State s=new State(rule.lefthand,dot+rule.righthand,j);
				if(!isInSet(s, currentSet)) {
					currentSet.add(s);
				}
			}
			if(isEmpty(pr)) {
				String r=st.righthand;
				r=r.replace(dot+pr, pr+dot);
				State s=new State(st.lefthand,r,st.origin);
				if(!isInSet(s, currentSet)) {
					currentSet.add(s);
				}
			}
		}
	}
	
	void doScanner(State st, char t, int j) {
		if(j+1<input.length() && t==input.charAt(j+1)) {
			String r=st.righthand;
			r=r.replace(dot+t, t+dot);
			if(set.size()==j+1) {
				set.add(new ArrayList<State>());
			}
			State newS=new State(st.lefthand,r,st.origin);
			if(!isInSet(newS, set.get(j+1))) {
				set.get(j+1).add(newS);
			}
		}
	}
	
	void doCompleter(State st, int k) {
		if(String.valueOf(st.righthand.charAt(st.righthand.length()-1)).equals(dot)) {	//zadnji znak pika?
			ArrayList<State> alJ=set.get(st.origin);
			ArrayList<State> alK=set.get(k);
			String B=st.lefthand;
			for (int i = 0; i < alJ.size(); i++) {
				State s=alJ.get(i);
				String r=s.righthand;
				char c;
				if(r.indexOf(dot)<r.length()-1) {
					c=r.charAt(r.indexOf(dot)+1);
				}
				else {
					c='\0';
				}
				if(String.valueOf(c).equals(B)) {
					r=r.replace(dot+B, B+dot);
					State newS=new State(s.lefthand,r,s.origin);
					if(!isInSet(newS, alK)) {
						alK.add(newS);
					}
				}
			}
		}
	}
	
	boolean isInSet(State st, ArrayList<State> al) {
		for (int i = 0; i < al.size(); i++) {
			if(equals(st,al.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	boolean equals(State s1, State s2) {
		return s1.lefthand.equals(s2.lefthand) &&
				s1.righthand.equals(s2.righthand) &&
				s1.origin==s2.origin;
	}
	
	ArrayList<State> find(char left) {
		ArrayList<State> al=new ArrayList<State>();
		for (int i = 0; i < rules.size(); i++) {
			if(rules.get(i).lefthand.startsWith(String.valueOf(left))) {
				al.add(rules.get(i));
			}
		}
		return al;
	}
	
	public static boolean isTerminal(char c) {
		String s=String.valueOf(c);
		return s.compareTo("a")>=0 && s.compareTo("z")<=0;
	}
	
	public static boolean isNonTerminal(char c) {
		String s=String.valueOf(c);
		return s.compareTo("A")>=0 && s.compareTo("Z")<=0;
	}
	
	public static boolean isEmpty(char c) {
		String s=String.valueOf(c);
		return s.compareTo("~")==0;
	}
	
	public static String insertDot(String a, int pos) {
		StringBuilder sb=new StringBuilder(a);
		sb.insert(pos,dot);
		return sb.toString();
	}
	
	public String moveDotToPos(String s, int i) {
		String a=s.replace(dot, "");
		StringBuilder sb=new StringBuilder(a);
		sb.insert(i,dot);
		return sb.toString();
	}
}

class State {
	String plain;	//brez pike
	String lefthand;	//desno od ->
	String righthand;	//cel string levo od -> (s piko, brez | )
	int origin;
	
	public State() {
		
	}
	
	public State(String state, int origin) {
		plain=state;
		String[] splitted=state.split(Earley.is);
		lefthand=splitted[0];
		righthand=splitted[1];
		this.origin=origin;
	}
	
	public State(String left, String right, int origin) {
		lefthand=left;
		righthand=right;
		this.origin=origin;
		this.plain=left+Earley.is+right;
	}
	
	public void insertDot(int pos) {
		
	}
	
	@Override
	public String toString() {
		return "["+lefthand+Earley.is+righthand+","+origin+"]";
	}
}
