package eg.edu.alexu;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Matcher {

	final int tau = 3;
	final int k = 20;

	ResultIterator res;
    public Matcher(PivotalTrie t) {
		trie = t;
		res = new ResultIterator();
	}

	private String typedWord;
	private String previousWord = "";

	private Map<BasicTrieNode, PivotalActiveNode> activenodes = null;
	private PivotalTrie trie;

	private int wrd_cnt(String queryString) {
		queryString = queryString.trim();
		if (queryString.length() == 0)
			return 0;
		else
			return queryString.split(" ").length;

	}

	void deletion(String queryString, String previousQueryString) {

		int word_cnt = wrd_cnt(queryString);
		int previous_word_cnt = wrd_cnt(previousQueryString);

		if (word_cnt == previous_word_cnt) {
			String prefix = Utils.getLastPrefix(queryString);
			String old_prefix = Utils.getLastPrefix(previousQueryString);
			activenodes = new HashMap<>();
			activenodes = trie.matchPrefixInc(prefix, old_prefix, activenodes, tau);
			RecordIterator r = new RecordIterator(trie, activenodes);
			res.replaceRecordIterator(r);
		} else if (word_cnt < previous_word_cnt) {
			for (int i = word_cnt; i < previous_word_cnt; i++) {
				res.remove(i);
			}
			res.resetAll();
		}

	}

	void incremental(String queryString, String previousQueryString) {
		String prefix = Utils.getLastPrefix(queryString);
		String old_prefix = Utils.getLastPrefix(previousQueryString);

		int word_cnt = wrd_cnt(queryString);
		int previous_word_cnt = wrd_cnt(previousQueryString);

		boolean newword = false;
		if (word_cnt > previous_word_cnt) {
			newword = true;
		}

		activenodes = trie.matchPrefixInc(prefix, old_prefix, activenodes, tau);
		RecordIterator r = new RecordIterator(trie, activenodes);

		if (newword) {
			res.addRecordIterator(r);
		} else {
			res.replaceRecordIterator(r);
		}
	}

	void buildfromscratch(String queryString) {
		int wrd_cnt = wrd_cnt(queryString);
		String[] word = queryString.split(" ");
		res = new ResultIterator();
		for (int i = 0; i < wrd_cnt; i++) {
			activenodes = trie.matchPrefix(word[i], tau);
			RecordIterator r = new RecordIterator(trie, activenodes);
			res.addRecordIterator(r);
		}
	}

	List<String> getRecordsString(List<Pair> ids) {
		List<String> records = new Vector<>();
		for (Pair r : ids) {
			records.add(r.id+" "+trie.forward.get(r.id)+ " "+r.n);
		}
		return records;
	}

	public List<String> getCandidate(String s) {
		typedWord = Utils.normalize(s.trim());

		if (typedWord.equals(previousWord))
			return null;

		if (typedWord.equals(""))
			return null;

		List<Pair> candidaterecords = new Vector<>();

		if (typedWord.startsWith(previousWord) && !typedWord.equals(previousWord)) {
			incremental(typedWord, previousWord);
		} else if (previousWord.startsWith(typedWord)) {
			deletion(typedWord, previousWord);
		} else {
			// need to rebuild all
			buildfromscratch(typedWord);
		}
//		Set<Integer> lookup = new HashSet<>();
//
//		for (int i = 0; res.hasNext() && i < k; i++) {
//			Pair n = res.next();
//			if (n != null) {
//				if (!lookup.contains(n.id)) {
//					lookup.add(n.id);
//					candidaterecords.add(n);
//				}
//			}
//		}
//
//		previousWord = typedWord;
		List<String> ret = new Vector<>();
//		Collections.sort(candidaterecords);
//		List<String> candidateRecordString = getRecordsString(candidaterecords);
//		int l = 0;
//		for (String word : candidateRecordString) {
//			ret.add(word);
//			if (l > k)
//				break;
//			l++;
//		}
		Map<Integer,Integer> SelectedRecords = new HashMap();
        //for(int i = 0 ; i < res.records_it.size();i++)
		if(res.records_it.size()> 0)
        {
			int i = res.records_it.size() -1;
        	for(int j = 0 ; j < res.records_it.get(i).lstSortedWords.size();j++)
        	{
        		List <Integer> lstRecords = trie.inverted_list.get( trie.dictionary.get(res.records_it.get(i).lstSortedWords.get(j).iWordIndex));
        		for(int m = 0 ; m < lstRecords.size();m++)
        		{
        			if(SelectedRecords.containsKey(lstRecords.get(m)))
        				continue;
        			SelectedRecords.put(lstRecords.get(m), 0);
        			ret.add(trie.forward.get(lstRecords.get(m)) + "                 ===>   " + res.records_it.get(i).lstSortedWords.get(j).dValue);
        		}
        	}
        }
		return ret;
	}
}