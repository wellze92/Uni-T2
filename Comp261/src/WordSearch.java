 /**
  *  @author Andrew Wells 300196972 2012
  * This program needs to learn the input for alll of the .txt files in the folder
  * 
  * 
  */
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.tree.TreeNode;


public class WordSearch {
    
	public List<String> Words = new ArrayList<String>();
	public Map<File,HashMap<String,Integer>> files = new HashMap<File,HashMap<String,Integer>>();
	public static void main(String args[]){
	WordSearch ws = new WordSearch(args); //avoids the static problem
		

	
	}
	
	public WordSearch(String args[]){ //needs to call the compare methods once i work out how to call all txt
		if (args.length <= 0){
			throw new Error("You need to enter some files");}
		System.out.println("Now reading String from Phrases.txt");
		readPhrases();
		System.out.println("Done reading from Phrases.\nNow i am searching through your files ");
		for(int i = 0; i < args.length;i++){
			
			File fi = new File(args[i]);
		    
			HashMap<String,Integer> m1 = search(fi);
			files.put(fi,m1);
			System.out.println(files.size());
		}
	docCompare(args);
	}
	
	public char[] testFile(File f){
		 char[] doc = new char[250000];
	      
	       try{FileReader reader = new FileReader(f);
	       int docSize = reader.read(doc,0,200000); // docSize is the number of characters read
	       reader.close();
	       }catch (IOException e){System.out.println("Document: "+f+" "+ e);}
	      return doc;
	}
	
	public void readPhrases(){
		try{
		File f1 = new File("Phrases");
		Scanner sc = new Scanner(f1);
		while (sc.hasNext()){
		Words.add(sc.nextLine());	
		}
		sc.close();
		}
		catch(IOException e){System.out.println("Failed to read file.");}
	}
	
	/** The tries method still needs to be implemented
	 * in this part somewhere 
	 */
	
	
	
	public HashMap<String,Integer> search(File f1){
		HashMap<String,Integer> List = new HashMap<String,Integer>();
		char[] txt = testFile(f1);
	    for (String s2: Words)
	    	
	    	KnuthMorris(s2.toCharArray(), txt,s2,List);
	    return List;
	}
	
	public int[] tableCon(char[] Str){
		
		int[] table = new int[Str.length]; 
		table[0] = -1;
	    table[1] = 0;
		int j = 0;
		int pos = 2;
		
		while (pos < Str.length){
			if (Str[pos-1] == Str[j]){
				table[pos] = j+1;
				pos++;
				j++;
			}
			else if (j>0){
				j = table[j];
			}
			else{
				j =0;
				table[pos] = 0;
				pos++;
			}
		}
		return table;
	}
	
	/** 
	 * 
	 *  done 1. Get Map finished
	 * done 2. Fix the return type
	 * 
	 * 
	 */
		
	public void KnuthMorris(char[] Str, char[] text,String curr,Map<String,Integer> List){
		int t = 0;
		int s = 0;
		int[] Table = tableCon(Str); 
		while(t+s < text.length-1 && !(text[t] ==' ' && text[t+1] == ' '  )){
			if (Str[s] == text[t+s]){
				s = s + 1;
				if (s == Str.length){
					if (List.containsKey(curr)){
					int u =  List.get(curr) + 1;
					List.remove(curr);
					List.put(curr, u);}
					else{List.put(curr, 1);}
					
				    s=0;
				    t++;
				}
			}
			else {
				t = t+s - Table[s];
				if (Table[s] == -1){
					s = 0;}
				else s = Table[s];
			}
		}
		if(List.get(curr) == null)
			List.put(curr, 0);
	}
	
	public void createTyre(char[] txt){
		int s = 1;
		Node root = new Node(txt[0],null);
		Node node = root;
		while (s < txt.length){
			if (node.children.get(s) == null){
				Node child = new Node(txt[s],node);
			}
		}
	}
	
	
	/** THIS IS NOW PART 2 .... COMPARING DOCUMENTS */

    public void docCompare(String args[]){
    	
    	for (int i = 0; i < args.length;i++){
    		File f1 = new File(args[i]);
    		HashMap<String,Integer> map = files.get(f1);
    		HashMap<String,Double> compareVal = new HashMap<String,Double>();
    		for (int j = 0; j < args.length;j++){
    			File f2 = new File(args[j]);
    			HashMap<String,Integer> mapsec = files.get(f2);
    			compare(map,mapsec,compareVal,f1,f2);
    			
    		}
    	String[] three =findThree(compareVal);
    	System.out.println("For " + f1.getName() + " the three most common files are " + three[0] + " , " + three[1] + " and  " + three[2]);
    	}
     }
	
	public void compare(HashMap<String,Integer> mapcur, HashMap<String,Integer> othermap,HashMap<String,Double> fina,File f1, File f2){
		double top = 0;
		double bottoma = 0;
		double bottomb = 0;
		for(String s:Words){
			int temp = 0;
			
			
			temp = othermap.get(s);
			double pro = mapcur.get(s) * temp;
			top = top + pro;
			bottoma = bottoma + (mapcur.get(s) * mapcur.get(s));
			bottomb = bottomb + (temp * temp);
		}
		double io = top/ (bottoma * bottomb);
		fina.put(f2.getName(),io);	
	}
	/** 
	 * needs absolutes !!!!! DONE!!!
	 * */
	public String[] findThree(HashMap<String,Double> map){
		double first = Integer.MIN_VALUE;
		double second = Integer.MIN_VALUE;
		double thrid = Integer.MIN_VALUE;
		String[] set = new String[3];	
		for (Entry<String,Double> e: map.entrySet()){
			if (Math.abs(e.getValue()) > first){
				first = Math.abs(e.getValue());
				set[0] = e.getKey();
			}
			else if (Math.abs(e.getValue()) > second){
				second = Math.abs(e.getValue());
				set[1] = e.getKey();
			}
			else if (Math.abs(e.getValue()) > thrid){
				thrid = Math.abs(e.getValue());
				set[2] = e.getKey();
			}
		}
		return set;
	}
	
	
	

public static class Node{
	char c;
	Node Parent;
	List<Node> children = new ArrayList<Node>();
	public Node(char a,Node parent){
		c = a;
		Parent = parent;
	}
}

}
	
