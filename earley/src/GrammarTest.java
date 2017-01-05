import java.util.*;

public class GrammarTest
{
	public static void main(String[] args) 
	{
		int nTests = 100;
		
		for (int i = 0; i < nTests; i++)
		{
			String grammar = "";
			HashMap<String,Boolean> testCases = new HashMap<String,Boolean>();

			 switch (i) {
			    case 0: // not CNF
			    	grammar = "S->aA|Aa|aAAA;A->aA|bA|~";
			    	testCases.put("~",false);
			    	testCases.put("a",true);
			    	testCases.put("b",false);
			    	testCases.put("aa",true);
			    	testCases.put("aaaaaaaaa",true);
			    	testCases.put("abbbbbbbb",true);
			    	testCases.put("ababababa",true);
			    	break;
			   case 1: // CNF
				   grammar = "S->~";
				   testCases.put("a",false);
			       testCases.put("b",false);
			       testCases.put("~",true);
			       break;
			   case 2: // CNF
				   grammar = "S->a";
				   testCases.put("a",true);
				   testCases.put("aa",false);
			       testCases.put("b",false);
			       testCases.put("~",false);
			       break;
			   case 3: // not CNF
				   grammar = "S->AAAAAAAAAAAAA;A->a|AA|~";
				   testCases.put("a",true);
				   testCases.put("aa",true);
			       testCases.put("b",false);
			       testCases.put("~",true);
			       break;
			   case 4: // CNF
				   grammar = "S->AB|BC;A->BA|a;B->CC|b;C->AB|a";
				   testCases.put("ababa",true);
				   testCases.put("baaba",true);
			       testCases.put("~",false);
			       testCases.put("a",false);
			       testCases.put("ab",true);
			       break;
			   case 5: // not CNF
				   grammar = "S->ABABABAB;A->~|a;B->~|b";
				   testCases.put("a",true);
				   testCases.put("b",true);
			       testCases.put("abababab",true);
			       testCases.put("ababababab",false);
			       testCases.put("abab",true);
			       testCases.put("~",true);
			       break;
			   default:
				   grammar = "";
				   return;
			   }

		 
			  if (!grammar.equals(""))
			   {
				  System.out.println("--------------------------------------------------------");
				  System.out.println(grammar);
				   Iterator<Map.Entry<String, Boolean>> it = testCases.entrySet().iterator();
				    while (it.hasNext()) 
				    {
				        Map.Entry<String, Boolean> pairs = (Map.Entry<String, Boolean>)it.next();
				        System.out.format("%20s ", pairs.getKey());
				        
				        try
				        {
				         	if (Grammar.solve(grammar.split(";"), pairs.getKey()) != pairs.getValue()) 
				        		System.out.println("\t\tINCORRECT, should have returned " + pairs.getValue());
				        	else 
				        		System.out.println("\t\tOK ["+pairs.getValue()+"]");
				        } catch (Exception e) {System.out.println(e.toString());}
				    	
				        it.remove();
				    }
			   }
 
		}		
	}
}