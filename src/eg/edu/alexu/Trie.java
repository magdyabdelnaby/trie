package eg.edu.alexu;

import java.util.*;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class Trie implements Serializable {

	private static final long serialVersionUID = 2246146469845576931L;
	public static Map<Integer, String> dictionary = new HashMap<Integer, String>();
	public Map<String, List<Integer>> inverted_list = new HashMap<>();
	public Map<Integer, String> forward = new HashMap<>();
	BasicTrieNode root = null;
	
	/////////////////////////Top K////////////////////////
	static public Map<Integer, Double> Words_Length = new HashMap<Integer, Double>();
	static public Map<Integer, Double> Words_Frequency = new HashMap<Integer, Double>();
	static public List<Sorted_Words_Node> lstWordsLength = new Vector();
	static public List<Sorted_Words_Node> lstWordsFrequency = new Vector();
	static public Map<Integer, Double> Words_TopK_Value = new HashMap<Integer, Double>();
	static public String gstrQuery = ""; 
	//////////////////////////////////////////////////////

	@Override
	public String toString() {
		String s = "";
		s += root.toString(0);
		return s;
	}

	public BasicTrieNode exactSearch(String s) {
		BasicTrieNode cur = root;
		for (char ch : s.toCharArray()) {
			BasicTrieNode next = cur.children.get(ch);
			if (next == null)
				return cur;
			cur = next;
		}
		return cur;
	}

	BasicTrieNode insertString(BasicTrieNode root, String s, int id, int len) {
		
		///////////////////////////////////
		dictionary.put(id, s);
		Words_Length.put(id, s.length()* 1.0);
		lstWordsLength.add(new Sorted_Words_Node(id, s.length()));
		Words_TopK_Value.put(id, Words_Length.get(id)* Range.dLenghtWeight + Words_Frequency.get(id)* Range.dFrequencyWeight);
		
		// System.out.println(id+s);
		BasicTrieNode v = root;
		int d = 1;
		v.adjust(id, len);
		BasicTrieNode next = v;
		for (char ch : s.toCharArray()) {
			next = v.children.get(ch);
			if (next == null)
				v.children.put(ch, next = CreateTrieNode(v, ch));
			next.depth = d;
			d++;
			next.adjust(id, len);
			v = next;
		}
		v.adjust(id, len);
		v.leaf = true;
		return v;
	}

	BasicTrieNode CreateTrieNode(BasicTrieNode v, char ch) {
		return new BasicTrieNode(v, ch);
	}

	class pair implements Comparable<pair> {
		int id;
		String s;

		pair(int id, String s) {
			this.id = id;
			this.s = s;
		}

		@Override
		public int compareTo(pair o) {
			if (s.length() == o.s.length())
				return s.compareTo(o.s);
			return Integer.compare(s.length(), o.s.length());
		}

		public Integer getId() {
			return id;
		}
	}

	private void adjustForwardList(String fileName) {
		// forward list
		int linenumber = 0;
		try {

			File file = new File(fileName);
			FileInputStream fIn = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(fIn));
			while (true) {
				String line = in.readLine();
				if (line == null || line.equals(""))
					break;
				linenumber++;
				line = Utils.normalize(line);
				forward.put(linenumber, line);			
			}
			in.close();
		} catch (Exception e) {
		}
	}

	class Word_Frequency implements Comparable<Word_Frequency>
	{
		String word;
		double dFrequency;
		Word_Frequency(String word, double dFrequency)
		{
			this.word = word;
			this.dFrequency = dFrequency;
		}
		public int compareTo(Word_Frequency o) 
    	{
			return word.compareToIgnoreCase(o.word);
		}
	}
	private List<String> old_readandsortFile(String fileName) {
		List<String> words = new Vector<>();
		int linenumber = 0;
		try {

			File file = new File(fileName);
			FileInputStream fIn = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(fIn));
			List<Integer> records = new Vector<>();

			while (true) {
				String line = in.readLine();

				if (line == null || line.equals(""))
					break;
				linenumber++;
				line = Utils.normalize(line);

				// System.out.println(line);
				String[] ll = line.split(" ");
				for (String l : ll) {
					if (inverted_list.get(l) == null) {
						records = new Vector<>();
						records.add(linenumber);
						inverted_list.put(l, records);
					} else {
						records = inverted_list.get(l);
						records.add(linenumber);
					}
				}
			}

			for (String g : inverted_list.keySet()) {
				words.add(g);
			}

			Collections.sort(words);
			in.close();
			return words;
		} catch (Exception e) {

		}
		return null;
	}

	private List<String> readandsortFile(String fileName) {
		List<Word_Frequency> words = new Vector<>();
		int linenumber = 0;
		try {

			File file = new File(fileName);
			FileInputStream fIn = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(fIn));
			List<Integer> records = new Vector<>();

			while (true) {
				String line = in.readLine();

				if (line == null || line.equals(""))
					break;
				linenumber++;
				///////////////////////////////////
				String[] parts = line.split(",");
				line = parts[0];
				words.add(new Word_Frequency(line,Double.parseDouble(parts[1])));
				//Words_Frequency.put(id, 1 / Double.parseDouble(parts[1])); // 034556
				
				line = Utils.normalize(line);

				// System.out.println(line);
				String[] ll = line.split(" ");
				for (String l : ll) {
					if (inverted_list.get(l) == null) {
						records = new Vector<>();
						records.add(linenumber);
						inverted_list.put(l, records);
					} else {
						records = inverted_list.get(l);
						records.add(linenumber);
					}
				}
			}

			//for (String g : inverted_list.keySet()) {
			//	words.add(g);
			//}

			Collections.sort(words);
			in.close();
			//////////////////////Prepare lists/////////////////
			List<String> lstWords = new Vector();
			for(int i = 0 ; i < words.size();i++)
			{
				lstWords.add(words.get(i).word);
				if(words.get(i).dFrequency == 0)
					Words_Frequency.put(i, 2.0);
				else Words_Frequency.put(i, 1 / words.get(i).dFrequency);
				lstWordsFrequency.add(new Sorted_Words_Node(i, Words_Frequency.get(i)));
			}
			return lstWords;
		} catch (Exception e) {

		}
		return null;
	}

	private void Init(String fileName, boolean truncate) {
		// word ID
		int id = 0;
		root = CreateTrieNode(null, '\0');
		try {
			
			List<String> wrds = readandsortFile(fileName);
			for (String wrd : wrds) {
				String[] inputS = wrd.split("\t");

				String s = inputS[0];
				int l = s.length();
				if (truncate)
					if (s.length() > 10)
						s = s.substring(0, 10);
				insertString(root, s, id, l);
                //dictionary.put(id,s);
				id++;
			}
			adjustForwardList(fileName);
			///////////////////////////////////////////
			Collections.sort(lstWordsLength);
			//for(int i = 0; i < 100; i++)
			//	System.out.print(lstWordsLength.get(i).iWordIndex +"-"+lstWordsLength.get(i).dValue +",");
			//System.out.println();
			Collections.sort(lstWordsFrequency);
			//for(int i = 0; i < 100; i++)
			//	System.out.print(lstWordsFrequency.get(i).iWordIndex +"-"+lstWordsFrequency.get(i).dValue +",");
			//System.out.println();
			
		} catch (Exception e) {
			root = null;
			System.err.println("Error reading the input file");
			System.err.println(e.getMessage());
		}
	}

	public Trie(String filename) {
		Init(filename, false);
	}

	public Trie(String filename, boolean truncate) {
		Init(filename, truncate);
	}

}
