package eg.edu.alexu;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

public class RecordIterator implements Iterator<Pair> {
	private PivotalTrie trie;
	// set of active nodes
	List<PivotalActiveNode> sorted_nodes = null;

	public RecordIterator(PivotalTrie trie, Map<BasicTrieNode, PivotalActiveNode> trienodes) {
		this.trie = trie;
		sorted_nodes = new Vector<>();
		sorted_nodes.addAll(trienodes.values());
		Collections.sort(sorted_nodes);
		printTopKNRA();
		printTopK_TA();
		topK_TRA();
		initalize();
	}
    //double dTauWeight = 0.4, dLenghtWeight = 0.3, dFrequencyWeight = 0.3;
	private void OLD_printTopKRanges()
    {
		System.out.println();
    	System.out.println("Top K Ranges : " + sorted_nodes.size());
		long startTime = System.nanoTime();
		
		
		Map<Integer,String> SelectedWords = new HashMap();
		List<List<Sorted_Words_Node>> L1 = new Vector<>();
		List<Sorted_Range_Node> lstRanges = new Vector<>();
    	TrieNode n1;
    	int iListIndex = 0;
    	for(int i = 0 ; i < sorted_nodes.size();i++)
    	{
    		List<Sorted_Words_Node> L = new Vector<>();
    		n1 = (TrieNode) (sorted_nodes.get(i).node);
    		//System.out.print("T is " + sorted_nodes.get(i).tau_px +" ==> " );
    		if(n1.rID == null)
    			continue;
    		for (int j = n1.rID.min; j <= n1.rID.max; j++) 
    		{
    			if(SelectedWords.containsKey(j))
    				continue;
    			String s = trie.dictionary.get(j);
    			SelectedWords.put(j, s);
    			//System.out.print(s + ", ");
    			L.add(new Sorted_Words_Node(j,Trie.Words_TopK_Value.get(j) +  sorted_nodes.get(i).tau_px * Range.dTauWeight));
    		}
    		if(L.size()>0)
    		{
    			Collections.sort(L);
    			lstRanges.add(new Sorted_Range_Node(iListIndex++,n1.rID.dTopkMinValue + sorted_nodes.get(i).tau_px * Range.dTauWeight));
    		
    			L1.add(L);
    			//System.out.println();
    		}
    	}
    	//if(lstRanges.size()==0)
    	//	return;
    	//System.out.println("============================");
    	Collections.sort(lstRanges);
    	int iCurrentK = 0;
    	Sorted_Range_Node swnFirstNode, swnSecondNode;
    	Sorted_Words_Node swTemp;
    	while(true)
    	{
    		
    		swnFirstNode = lstRanges.get(0);
    		if(lstRanges.size()> 1)
    			swnSecondNode = lstRanges.get(1);
    		else
    		{
    			swnSecondNode = lstRanges.get(0);
    			swnSecondNode.dValue = 100000;
    		}
    		swTemp = L1.get(swnFirstNode.iListIndex).get(swnFirstNode.iStartIndex);
    		while(swTemp.dValue <= swnSecondNode.dValue)
    		{
    			System.out.print("  ("+trie.dictionary.get(L1.get(swnFirstNode.iListIndex).get(swnFirstNode.iStartIndex).iWordIndex)+
    					") " + L1.get(swnFirstNode.iListIndex).get(swnFirstNode.iStartIndex).dValue);
    			swnFirstNode.iStartIndex ++;
    			iCurrentK++;
    			if(
    					(iCurrentK == Range.k)||
    					(swnFirstNode.iStartIndex == L1.get(swnFirstNode.iListIndex).size())
    			  )
    				break;
    			swTemp = L1.get(swnFirstNode.iListIndex).get(swnFirstNode.iStartIndex);
    		}
    		if(iCurrentK == Range.k)
				break;
    		if (swnFirstNode.iStartIndex == L1.get(swnFirstNode.iListIndex).size())
    			lstRanges.remove(0);
    		else
    		{
    			//lstRanges.get(0).iStartIndex = swnFirstNode.iStartIndex;
    			swnFirstNode.dValue = L1.get(swnFirstNode.iListIndex).get(swnFirstNode.iStartIndex).dValue;
    			////////////////////////////////////////////Insertion/////////////////////
    			if(lstRanges.size() == 2)
    			{
    				lstRanges.add(swnFirstNode);
    				lstRanges.remove(0);
    			}
    			else
    			{
    				int i = 2;
    				while(swnFirstNode.dValue > lstRanges.get(i).dValue)
	    			{
    					i++;
    					if(i == lstRanges.size())
    						break;
	    			}
    				if(i == lstRanges.size())
    					lstRanges.add(swnFirstNode);
    				else
    					lstRanges.add(i,swnFirstNode);
    				lstRanges.remove(0);
    			}
    		}
    		if(lstRanges.size() == 0)
    			break;
    	}
    	System.out.println();
    	long endTime = System.nanoTime();
		long duration = (endTime - startTime);
    	System.out.println("============="+duration+"===============");
    }
	private void topK_TRA()
    {
		System.out.println();
    	System.out.println("Top K Ranges : " + sorted_nodes.size());
		long startTime = System.nanoTime();
		
		
		Map<Integer,String> SelectedWords = new HashMap();
		List<List<Sorted_Words_Node>> L1 = new Vector<>();
		List<Sorted_Range_Node> lstRanges = new Vector<>();
    	TrieNode n1;
    	int iListIndex = 0;
    	for(int i = 0 ; i < sorted_nodes.size();i++)
    	{
    		List<Sorted_Words_Node> L = new Vector<>();
    		n1 = (TrieNode) (sorted_nodes.get(i).node);
    		//System.out.print("T is " + sorted_nodes.get(i).tau_px +" ==> " );
    		if(n1.rID == null)
    			continue;
    		for (int j = n1.rID.min; j <= n1.rID.max; j++) 
    		{
    			if(SelectedWords.containsKey(j))
    				continue;
    			String s = trie.dictionary.get(j);
    			SelectedWords.put(j, s);
    			//System.out.print(s + ", ");
    			L.add(new Sorted_Words_Node(j,Trie.Words_TopK_Value.get(j) +  sorted_nodes.get(i).tau_px * Range.dTauWeight));
    		}
    		if(L.size()>0)
    		{
    			Collections.sort(L);
    			L1.add(L);
    			//lstRanges.add(new Sorted_Range_Node(iListIndex,n1.rID.dTopkMinValue + sorted_nodes.get(i).tau_px * Range.dTauWeight));
    			lstRanges.add(new Sorted_Range_Node(iListIndex,L.get(0).dValue)); 
    			
    			iListIndex++;
    			//System.out.println();
    		}
    	}
    	if(lstRanges.size()==0)
    		return;
    	//System.out.println("============================");
    	Collections.sort(lstRanges);
    	int iCurrentK = 0;
    	Sorted_Range_Node swnFirstNode, swnSecondNode;
    	Sorted_Words_Node swTemp;
    	while(true)
    	{
    		
    		swnFirstNode = lstRanges.get(0);
    		if(lstRanges.size()> 1)
    			swnSecondNode = lstRanges.get(1);
    		else
    		{
    			swnSecondNode = lstRanges.get(0);
    			swnSecondNode.dValue = 100000;
    		}
    		swTemp = L1.get(swnFirstNode.iListIndex).get(swnFirstNode.iStartIndex);
    		while(swTemp.dValue <= swnSecondNode.dValue)
    		{
    			System.out.print("  ("+trie.dictionary.get(swTemp.iWordIndex)+
    					") " + swTemp.dValue);
    			swnFirstNode.iStartIndex ++;
    			iCurrentK++;
    			if(
    					(iCurrentK == Range.k)||
    					(swnFirstNode.iStartIndex == L1.get(swnFirstNode.iListIndex).size())
    			  )
    				break;
    			swTemp = L1.get(swnFirstNode.iListIndex).get(swnFirstNode.iStartIndex);
    		}
    		if(iCurrentK == Range.k)
				break;
    		if (swnFirstNode.iStartIndex == L1.get(swnFirstNode.iListIndex).size())
    			lstRanges.remove(0);
    		else
    		{
    			lstRanges.get(0).iStartIndex = swnFirstNode.iStartIndex;
    			lstRanges.get(0).dValue = L1.get(swnFirstNode.iListIndex).get(swnFirstNode.iStartIndex).dValue;
    			////////////////////////////////////////////Insertion/////////////////////
    			if(lstRanges.size() == 2)
    			{
    				lstRanges.add(lstRanges.get(0));
    				lstRanges.remove(0);
    			}
    			else
    			{
    				int i = 2;
    				while(lstRanges.get(0).dValue > lstRanges.get(i).dValue)
	    			{
    					i++;
    					if(i == lstRanges.size())
    						break;
	    			}
    				if(i == lstRanges.size())
    					lstRanges.add(lstRanges.get(0));
    				else
    					lstRanges.add(i,lstRanges.get(0));
    				lstRanges.remove(0);
    			}
    		}
    		if(lstRanges.size() == 0)
    			break;
    	}
    	System.out.println();
    	long endTime = System.nanoTime();
		long duration = (endTime - startTime);
    	System.out.println("============="+duration+"===============");
    }
	long lTopKNRAStartTime;
	long lTopK_TA_StartTime;
	private void printTopKNRA()
    {
		System.out.println("Top K NRA: " + sorted_nodes.size());
		lTopKNRAStartTime = System.nanoTime();
		Map<Integer,String> SelectedWords = new HashMap();
		List<Sorted_Words_Node> L1 = new Vector<>();
    	TrieNode n1;
    	
    	for(int i = 0 ; i < sorted_nodes.size();i++)
    	{
    		
    		n1 = (TrieNode) (sorted_nodes.get(i).node);
    		//System.out.print("T is " + sorted_nodes.get(i).tau_px +" ==> " );
    		if(n1.rID == null)
    			continue;
    		for (int j = n1.rID.min; j <= n1.rID.max; j++) 
    		{
    			if(SelectedWords.containsKey(j))
    				continue;
    			String s = trie.dictionary.get(j);
    			SelectedWords.put(j, s);
    			//System.out.print(s + ", ");
    			L1.add(new Sorted_Words_Node(j,sorted_nodes.get(i).tau_px));
    		}
    		//System.out.println();
    	}
    	topK_NRA(L1,Range.dTauWeight,Trie.lstWordsLength,Range.dLenghtWeight,
    			Trie.lstWordsFrequency,Range.dFrequencyWeight);
    	
    }
	private void printTopKNRA_dash()
    {
		System.out.println("Top K NRA: " + sorted_nodes.size());
		lTopKNRAStartTime = System.nanoTime();
		Map<Integer,String> SelectedWords = new HashMap();
		List<Sorted_Words_Node> L1 = new Vector<>();
		List<Sorted_Words_Node> L2 = new Vector<>();
		List<Sorted_Words_Node> L3 = new Vector<>();
    	TrieNode n1;
    	
    	for(int i = 0 ; i < sorted_nodes.size();i++)
    	{
    		
    		n1 = (TrieNode) (sorted_nodes.get(i).node);
    		//System.out.print("T is " + sorted_nodes.get(i).tau_px +" ==> " );
    		if(n1.rID == null)
    			continue;
    		for (int j = n1.rID.min; j <= n1.rID.max; j++) 
    		{
    			if(SelectedWords.containsKey(j))
    				continue;
    			String s = trie.dictionary.get(j);
    			SelectedWords.put(j, s);
    			//System.out.print(s + ", ");
    			L1.add(new Sorted_Words_Node(j,sorted_nodes.get(i).tau_px));
    			L2.add(new Sorted_Words_Node(j,trie.Words_Length.get(j)));
    			L3.add(new Sorted_Words_Node(j,trie.Words_Frequency.get(j)));
    		}
    		//System.out.println();
    	}
    	Collections.sort(L2);
    	Collections.sort(L3);
    	topK_NRA(L1,Range.dTauWeight,L2,Range.dLenghtWeight,L3,Range.dFrequencyWeight);
    	
    }
	public static int calculateED(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
	 
		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];
	 
		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
	 
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
	 
		//iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);
	 
				//if last two chars equal
				if (c1 == c2) {
					//update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;
	 
					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}
	 
		return dp[len1][len2];
	}
	private void printTopK_TA_Dash()
    {
		System.out.println();
		System.out.println("Top K TA: " + sorted_nodes.size());
		lTopK_TA_StartTime = System.nanoTime();
		Map<Integer,String> SelectedWords = new HashMap();
		List<Sorted_Words_Node> L1 = new Vector<>();
		List<Sorted_Words_Node> L2 = new Vector<>();
		List<Sorted_Words_Node> L3 = new Vector<>();
		
		Map<Integer,Double> mapL1 = new HashMap();
		Map<Integer,Double> mapL2 = new HashMap();
		Map<Integer,Double> mapL3 = new HashMap();
		
    	TrieNode n1;
    	
    	for(int i = 0 ; i < sorted_nodes.size();i++)
    	{
    		
    		n1 = (TrieNode) (sorted_nodes.get(i).node);
    		//System.out.print("T is " + sorted_nodes.get(i).tau_px +" ==> " );
    		if(n1.rID == null)
    			continue;
    		for (int j = n1.rID.min; j <= n1.rID.max; j++) 
    		{
    			if(SelectedWords.containsKey(j))
    				continue;
    			String s = trie.dictionary.get(j);
    			SelectedWords.put(j, s);
    			//System.out.print(s + ", ");
    			L1.add(new Sorted_Words_Node(j,sorted_nodes.get(i).tau_px));
    			mapL1.put(j, sorted_nodes.get(i).tau_px * 1.0);
    			L2.add(new Sorted_Words_Node(j,trie.Words_Length.get(j)));
    			mapL2.put(j, trie.Words_Length.get(j)* 1.0);
    			L3.add(new Sorted_Words_Node(j,trie.Words_Frequency.get(j)));
    			mapL3.put(j, trie.Words_Frequency.get(j));
    		}
    		//System.out.println();
    	}
    	Collections.sort(L2);
    	for(int i = 0; i < 100; i++)
			System.out.print(L2.get(i).iWordIndex +"-"+L2.get(i).dValue +",");
    	Collections.sort(L3);
    	System.out.println();
    	for(int i = 0; i < 100; i++)
			System.out.print(L3.get(i).iWordIndex +"-"+L3.get(i).dValue +",");
    	topK_TA_dash(L1,Range.dTauWeight,L2,Range.dLenghtWeight,L3,Range.dFrequencyWeight
    			,mapL1,mapL2,mapL3);
    	
    }
	private void printTopK_TA()
    {
		System.out.println();
		System.out.println("Top K TA: " + sorted_nodes.size());
		lTopK_TA_StartTime = System.nanoTime();
		Map<Integer,String> SelectedWords = new HashMap();
		List<Sorted_Words_Node> L1 = new Vector<>();
		
		Map<Integer,Double> mapL1 = new HashMap();
		
    	TrieNode n1;
    	
    	for(int i = 0 ; i < sorted_nodes.size();i++)
    	{
    		
    		n1 = (TrieNode) (sorted_nodes.get(i).node);
    		//System.out.print("T is " + sorted_nodes.get(i).tau_px +" ==> " );
    		if(n1.rID == null)
    			continue;
    		for (int j = n1.rID.min; j <= n1.rID.max; j++) 
    		{
    			if(SelectedWords.containsKey(j))
    				continue;
    			String s = trie.dictionary.get(j);
    			SelectedWords.put(j, s);
    			//System.out.print(s + ", ");
    			L1.add(new Sorted_Words_Node(j,sorted_nodes.get(i).tau_px));
    			mapL1.put(j, sorted_nodes.get(i).tau_px * 1.0);
    		}
    		//System.out.println();
    	}
    	topK_TA_dash(L1,Range.dTauWeight,Trie.lstWordsLength,Range.dLenghtWeight,Trie.lstWordsFrequency,Range.dFrequencyWeight
    			,mapL1,Trie.Words_Length,Trie.Words_Frequency);
    	
    }
//	void testTopk()
//	{
//		List<Sorted_Words_Node> L1 = new Vector<>();
//		List<Sorted_Words_Node> L2 = new Vector<>();
//		L1.add(new Sorted_Words_Node(5,50));
//		L1.add(new Sorted_Words_Node(1,40));
//		L1.add(new Sorted_Words_Node(3,30));
//		L1.add(new Sorted_Words_Node(2,20));
//		L1.add(new Sorted_Words_Node(4,10));
//		L2.add(new Sorted_Words_Node(3,50));
//		L2.add(new Sorted_Words_Node(2,40));
//		L2.add(new Sorted_Words_Node(1,30));
//		L2.add(new Sorted_Words_Node(4,20));
//		L2.add(new Sorted_Words_Node(5,10));
//		topK(L1,L2);
//	}
	
	long iCurrentElementsInSortedList = 0;
	double dMinSofar = 10000000.0,dTopK_TA_Threshold = 10000000.0;
	
    class Sorted_Range_Node implements Comparable<Sorted_Range_Node>
    {
    	public int iListIndex;
    	public double dValue;
    	public int iStartIndex = 0 ;
    	Sorted_Range_Node()
    	{
    	}
    	Sorted_Range_Node(int iListIndex, double dValue)
    	{
    		this.iListIndex = iListIndex;
    		this.dValue = dValue;
    	}
    	@Override
		public int compareTo(Sorted_Range_Node o) 
    	{
			return Double.compare(dValue, o.dValue);
		}
    }
    class Buffer_Node
    {
    	public int iSeenLists = 0;
    	public double dCurrentValue[] = new double[3];
    	public double dMaxValue = 0;
    	Buffer_Node()
    	{
    		this.dCurrentValue[0] = -1;
    		this.dCurrentValue[1] = -1;
    		this.dCurrentValue[2] = -1;
    	}
    }
    boolean updateBufferMaximumValue(Map<Integer,Buffer_Node> buffer, double dL1Value,double dL2Value,double dL3Value)
    {
    	boolean bStillLoop = false;
    	//System.out.println("============Curent Buffer=================");
    	Buffer_Node bf;
    	Iterator it = buffer.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            bf = (Buffer_Node)pair.getValue();
            bf.dMaxValue = 0;
//            if(bf.dCurrentValue[0] == -1)
//            	bf.dMaxValue = bf.dCurrentValue[1] + dL1Value;
//            else
//            	bf.dMaxValue = bf.dCurrentValue[0] + dL2Value;
            if(bf.dCurrentValue[0] == -1)
            	bf.dMaxValue += dL1Value;
            else
            	bf.dMaxValue += bf.dCurrentValue[0];
            if(bf.dCurrentValue[1] == -1)
            	bf.dMaxValue += dL2Value;
            else
            	bf.dMaxValue += bf.dCurrentValue[1];
            if(bf.dCurrentValue[2] == -1)
            	bf.dMaxValue += dL3Value;
            else
            	bf.dMaxValue += bf.dCurrentValue[1];
            //System.out.println(pair.getKey() + " ==> " + bf.dCurrentValue[0] + " , " + bf.dCurrentValue[1] + 
            //				" , " + bf.dCurrentValue[2] + " = " + bf.dMaxValue);
            if(bf.dMaxValue >= dMinSofar)
            	bStillLoop = true;
        }
        if(
        		(!bStillLoop)&&
        		(iCurrentElementsInSortedList < Range.k)
        		)
        	bStillLoop = true;
        return bStillLoop;
    }
    public List<Sorted_Words_Node> lstSortedWords;
    
    private void topK_NRA(List<Sorted_Words_Node> L1,double dL1Weight, List<Sorted_Words_Node> L2,double dL2Weight, 
    		          List<Sorted_Words_Node> L3,double dL3Weight)
    {
    	
    	Sorted_Words_Node swNode ;
    	Map<Integer,Buffer_Node> buffer = new HashMap<>();
    	lstSortedWords = new Vector<>();
    	for(int iCurrentIndex = 0;iCurrentIndex < L1.size();iCurrentIndex++)
    	{
    		//////////////////////////////////////List 1////////////////////////////////
    		if(!buffer.containsKey(L1.get(iCurrentIndex).iWordIndex))
    		{
    			buffer.put(L1.get(iCurrentIndex).iWordIndex, new Buffer_Node());
    		}
    		buffer.get(L1.get(iCurrentIndex).iWordIndex).iSeenLists ++;
    		buffer.get(L1.get(iCurrentIndex).iWordIndex).dCurrentValue[0] = L1.get(iCurrentIndex).dValue * dL1Weight;
    		if (buffer.get(L1.get(iCurrentIndex).iWordIndex).iSeenLists == 3)
    		{
    			swNode = new Sorted_Words_Node();
    			swNode.iWordIndex = L1.get(iCurrentIndex).iWordIndex;
    			dMinSofar = swNode.dValue = buffer.get(L1.get(iCurrentIndex).iWordIndex).dCurrentValue[0]+ 
						buffer.get(L1.get(iCurrentIndex).iWordIndex).dCurrentValue[1] + 
						buffer.get(L1.get(iCurrentIndex).iWordIndex).dCurrentValue[2];
    			iCurrentElementsInSortedList++;
    			lstSortedWords.add(swNode);
    			buffer.remove(L1.get(iCurrentIndex).iWordIndex);
    		}
    		//updateBufferMaximumValue(buffer,L1.get(iCurrentIndex).dValue);
			//////////////////////////////////////List 2////////////////////////////////
    		if(!buffer.containsKey(L2.get(iCurrentIndex).iWordIndex))
    		{
    			buffer.put(L2.get(iCurrentIndex).iWordIndex, new Buffer_Node());
    		}
			buffer.get(L2.get(iCurrentIndex).iWordIndex).iSeenLists ++;
			buffer.get(L2.get(iCurrentIndex).iWordIndex).dCurrentValue[1] = L2.get(iCurrentIndex).dValue * dL2Weight;
			if (buffer.get(L2.get(iCurrentIndex).iWordIndex).iSeenLists == 3)
			{
				swNode = new Sorted_Words_Node();
				swNode.iWordIndex = L2.get(iCurrentIndex).iWordIndex;
				dMinSofar = swNode.dValue = buffer.get(L2.get(iCurrentIndex).iWordIndex).dCurrentValue[0] + 
						buffer.get(L2.get(iCurrentIndex).iWordIndex).dCurrentValue[1]+ 
						buffer.get(L2.get(iCurrentIndex).iWordIndex).dCurrentValue[2];
				iCurrentElementsInSortedList++;
				lstSortedWords.add(swNode);
				buffer.remove(L2.get(iCurrentIndex).iWordIndex);
			}
			//////////////////////////////////////List 3////////////////////////////////
			if(!buffer.containsKey(L3.get(iCurrentIndex).iWordIndex))
			{
				buffer.put(L3.get(iCurrentIndex).iWordIndex, new Buffer_Node());
			}
			buffer.get(L3.get(iCurrentIndex).iWordIndex).iSeenLists ++;
			buffer.get(L3.get(iCurrentIndex).iWordIndex).dCurrentValue[2] = L3.get(iCurrentIndex).dValue * dL3Weight;
			if (buffer.get(L3.get(iCurrentIndex).iWordIndex).iSeenLists == 3)
			{
				swNode = new Sorted_Words_Node();
				swNode.iWordIndex = L3.get(iCurrentIndex).iWordIndex;
				dMinSofar = swNode.dValue = buffer.get(L3.get(iCurrentIndex).iWordIndex).dCurrentValue[0] + 
						buffer.get(L3.get(iCurrentIndex).iWordIndex).dCurrentValue[1]+ 
						buffer.get(L3.get(iCurrentIndex).iWordIndex).dCurrentValue[2];
				iCurrentElementsInSortedList++;
				lstSortedWords.add(swNode);
				buffer.remove(L3.get(iCurrentIndex).iWordIndex);
			}
			///////////////////////////////////////////////////////////////////////////////
			if (!updateBufferMaximumValue(buffer,L1.get(iCurrentIndex).dValue * dL1Weight,
					L2.get(iCurrentIndex).dValue  * dL2Weight,
					L3.get(iCurrentIndex).dValue  * dL3Weight))
				break;
    	}
    	Collections.sort(lstSortedWords);
    	/////////////////////Print Final Sorted list//////////////////
    	//System.out.println();
    	long endTime = System.nanoTime();
		long duration = (endTime - lTopKNRAStartTime);
    	System.out.println("============="+duration+"===============");
    	int iDisplayCount = Range.k;
    	if(iDisplayCount > lstSortedWords.size())
    		iDisplayCount = lstSortedWords.size();
    	//System.out.println("==============Final List=============");
    	for(int i = 0; i < iDisplayCount; i++ )
    		System.out.print("  (" + 
    				trie.dictionary.get(lstSortedWords.get(i).iWordIndex) + ") " + 
    				lstSortedWords.get(i).dValue);
//    	System.out.print(lstSortedWords.get(i).iWordIndex +" ==> (" + 
//				trie.dictionary.get(lstSortedWords.get(i).iWordIndex) + ") " + 
//				lstSortedWords.get(i).dValue);
    }
    private void topK_NRA_Dash(List<Sorted_Words_Node> L1,double dL1Weight, List<Sorted_Words_Node> L2,double dL2Weight, 
	          List<Sorted_Words_Node> L3,double dL3Weight)
	{
	
	Sorted_Words_Node swNode ;
	Map<Integer,Buffer_Node> buffer = new HashMap<>();
	lstSortedWords = new Vector<>();
	for(int iCurrentIndex = 0;iCurrentIndex < L1.size();iCurrentIndex++)
	{
		//////////////////////////////////////List 1////////////////////////////////
		if(!buffer.containsKey(L1.get(iCurrentIndex).iWordIndex))
		{
			buffer.put(L1.get(iCurrentIndex).iWordIndex, new Buffer_Node());
		}
		buffer.get(L1.get(iCurrentIndex).iWordIndex).iSeenLists ++;
		buffer.get(L1.get(iCurrentIndex).iWordIndex).dCurrentValue[0] = L1.get(iCurrentIndex).dValue * dL1Weight;
		if (buffer.get(L1.get(iCurrentIndex).iWordIndex).iSeenLists == 3)
		{
			swNode = new Sorted_Words_Node();
			swNode.iWordIndex = L1.get(iCurrentIndex).iWordIndex;
			dMinSofar = swNode.dValue = buffer.get(L1.get(iCurrentIndex).iWordIndex).dCurrentValue[0]+ 
					buffer.get(L1.get(iCurrentIndex).iWordIndex).dCurrentValue[1] + 
					buffer.get(L1.get(iCurrentIndex).iWordIndex).dCurrentValue[2];
			iCurrentElementsInSortedList++;
			lstSortedWords.add(swNode);
			buffer.remove(L1.get(iCurrentIndex).iWordIndex);
		}
		//updateBufferMaximumValue(buffer,L1.get(iCurrentIndex).dValue);
		//////////////////////////////////////List 2////////////////////////////////
		if(!buffer.containsKey(L2.get(iCurrentIndex).iWordIndex))
		{
			buffer.put(L2.get(iCurrentIndex).iWordIndex, new Buffer_Node());
		}
		buffer.get(L2.get(iCurrentIndex).iWordIndex).iSeenLists ++;
		buffer.get(L2.get(iCurrentIndex).iWordIndex).dCurrentValue[1] = L2.get(iCurrentIndex).dValue * dL2Weight;
		if (buffer.get(L2.get(iCurrentIndex).iWordIndex).iSeenLists == 3)
		{
			swNode = new Sorted_Words_Node();
			swNode.iWordIndex = L2.get(iCurrentIndex).iWordIndex;
			dMinSofar = swNode.dValue = buffer.get(L2.get(iCurrentIndex).iWordIndex).dCurrentValue[0] + 
					buffer.get(L2.get(iCurrentIndex).iWordIndex).dCurrentValue[1]+ 
					buffer.get(L2.get(iCurrentIndex).iWordIndex).dCurrentValue[2];
			iCurrentElementsInSortedList++;
			lstSortedWords.add(swNode);
			buffer.remove(L2.get(iCurrentIndex).iWordIndex);
		}
		//////////////////////////////////////List 3////////////////////////////////
		if(!buffer.containsKey(L3.get(iCurrentIndex).iWordIndex))
		{
			buffer.put(L3.get(iCurrentIndex).iWordIndex, new Buffer_Node());
		}
		buffer.get(L3.get(iCurrentIndex).iWordIndex).iSeenLists ++;
		buffer.get(L3.get(iCurrentIndex).iWordIndex).dCurrentValue[2] = L3.get(iCurrentIndex).dValue * dL3Weight;
		if (buffer.get(L3.get(iCurrentIndex).iWordIndex).iSeenLists == 3)
		{
			swNode = new Sorted_Words_Node();
			swNode.iWordIndex = L3.get(iCurrentIndex).iWordIndex;
			dMinSofar = swNode.dValue = buffer.get(L3.get(iCurrentIndex).iWordIndex).dCurrentValue[0] + 
					buffer.get(L3.get(iCurrentIndex).iWordIndex).dCurrentValue[1]+ 
					buffer.get(L3.get(iCurrentIndex).iWordIndex).dCurrentValue[2];
			iCurrentElementsInSortedList++;
			lstSortedWords.add(swNode);
			buffer.remove(L3.get(iCurrentIndex).iWordIndex);
		}
		///////////////////////////////////////////////////////////////////////////////
		if (!updateBufferMaximumValue(buffer,L1.get(iCurrentIndex).dValue * dL1Weight,
				L2.get(iCurrentIndex).dValue  * dL2Weight,
				L3.get(iCurrentIndex).dValue  * dL3Weight))
			break;
	}
	Collections.sort(lstSortedWords);
	/////////////////////Print Final Sorted list//////////////////
	//System.out.println();
	long endTime = System.nanoTime();
	long duration = (endTime - lTopKNRAStartTime);
	System.out.println("============="+duration+"===============");
	int iDisplayCount = Range.k;
	if(iDisplayCount > lstSortedWords.size())
		iDisplayCount = lstSortedWords.size();
	//System.out.println("==============Final List=============");
	for(int i = 0; i < iDisplayCount; i++ )
		System.out.print("  (" + 
				trie.dictionary.get(lstSortedWords.get(i).iWordIndex) + ") " + 
				lstSortedWords.get(i).dValue);
	//System.out.print(lstSortedWords.get(i).iWordIndex +" ==> (" + 
	//		trie.dictionary.get(lstSortedWords.get(i).iWordIndex) + ") " + 
	//		lstSortedWords.get(i).dValue);
	}
    private boolean addNodeToSortedWordsList(Sorted_Words_Node swn)
    {
    	if(swn.dValue == 1000000)
    		return false;
    	int iCounter = 0;
    	for(iCounter = 0 ; iCounter < lstSortedWords.size(); iCounter++)
    	{
    		if(swn.dValue < lstSortedWords.get(iCounter).dValue)
    		{
    			lstSortedWords.add(iCounter,swn);
    			if(swn.dValue <= dTopK_TA_Threshold)
    			{
    				for(int i = iCounter + 1 ; i < lstSortedWords.size();i++)
    				{
    					if(
    							(lstSortedWords.get(i).dValue <= dTopK_TA_Threshold) &&
    							(i >= Range.k)
    					  )
    						return true;
    				}
    				return false;
    			}else
    				return false;
    		}
    		
    	}
    	lstSortedWords.add(iCounter,swn);
    	if(
				(lstSortedWords.get(iCounter).dValue <= dTopK_TA_Threshold) &&
				(iCounter >= Range.k)
		  )
			return true;
    	return false;
    }
    private void topK_TA(List<Sorted_Words_Node> L1,double dL1Weight, List<Sorted_Words_Node> L2,double dL2Weight, 
	          List<Sorted_Words_Node> L3,double dL3Weight
	          ,Map<Integer,Double> mapL1,Map<Integer,Double> mapL2,Map<Integer,Double> mapL3)
    {
		Sorted_Words_Node swNode1, swNode2, swNode3 ;
		Map<Integer,Integer> buffer = new HashMap<>();
		lstSortedWords = new Vector<>();
		int iED2 = 0 , iEd3 = 0;
		double dMaxED = 0;
		for(int iCurrentIndex = 0;iCurrentIndex < L1.size();iCurrentIndex++)
		{
			swNode1 = new Sorted_Words_Node();
			swNode2 = new Sorted_Words_Node();
			swNode3 = new Sorted_Words_Node();
			
			swNode1.dValue = swNode2.dValue = swNode3.dValue = 1000000;
			//////////////////////////////////////List 1////////////////////////////////
			if(iCurrentIndex < L1.size())
			{
				if(!buffer.containsKey(L1.get(iCurrentIndex).iWordIndex))
				{
					if(L1.get(iCurrentIndex).dValue > dMaxED)
						dMaxED = L1.get(iCurrentIndex).dValue;
					buffer.put(L1.get(iCurrentIndex).iWordIndex,1);
					
					swNode1.iWordIndex = L1.get(iCurrentIndex).iWordIndex;
					swNode1.dValue = mapL1.get(swNode1.iWordIndex) *  dL1Weight +
							                    mapL2.get(swNode1.iWordIndex) *  dL2Weight + 
							                    mapL3.get(swNode1.iWordIndex) *  dL3Weight;
					//iCurrentElementsInSortedList++;
					//lstSortedWords.add(swNode1);
				}
			}
			if(!buffer.containsKey(L2.get(iCurrentIndex).iWordIndex))
			{
				buffer.put(L2.get(iCurrentIndex).iWordIndex,1);
				
				swNode2.iWordIndex = L2.get(iCurrentIndex).iWordIndex;
				if((iCurrentIndex < L1.size())&&
					(mapL1.containsKey(swNode2.iWordIndex))
				  )
				{
					if(mapL1.get(swNode2.iWordIndex) > dMaxED)
						dMaxED = mapL1.get(swNode2.iWordIndex);
					swNode2.dValue = mapL1.get(swNode2.iWordIndex) *  dL1Weight +
						         	 mapL2.get(swNode2.iWordIndex) *  dL2Weight + 
						             mapL3.get(swNode2.iWordIndex) *  dL3Weight;
					
				}else
				{
					iED2 = calculateED(trie.gstrQuery, 
					         trie.dictionary.get(swNode2.iWordIndex).substring(0, 
					        		 Math.min(trie.gstrQuery.length(),trie.dictionary.get(swNode2.iWordIndex).length())));
					swNode2.dValue = iED2 *  dL1Weight +
				         	         mapL2.get(swNode2.iWordIndex) *  dL2Weight + 
				                     mapL3.get(swNode2.iWordIndex) *  dL3Weight;
					if(dMaxED < iED2)
						dMaxED = iED2;
				}
				//iCurrentElementsInSortedList++;
				//lstSortedWords.add(swNode2);
			}
			if(!buffer.containsKey(L3.get(iCurrentIndex).iWordIndex))
			{
				buffer.put(L3.get(iCurrentIndex).iWordIndex,1);
				
				swNode3.iWordIndex = L3.get(iCurrentIndex).iWordIndex;
				if((iCurrentIndex < L1.size())&&
						(mapL1.containsKey(swNode3.iWordIndex))
					  )
				{
					if(mapL1.get(swNode3.iWordIndex) > dMaxED)
						dMaxED = mapL1.get(swNode3.iWordIndex);
					swNode3.dValue = mapL1.get(swNode3.iWordIndex) *  dL1Weight +
						         	 mapL2.get(swNode3.iWordIndex) *  dL2Weight + 
						             mapL3.get(swNode3.iWordIndex) *  dL3Weight;
				}else
				{
					iEd3 = calculateED(trie.gstrQuery, 
					         trie.dictionary.get(swNode3.iWordIndex).substring(0, 
					        		 Math.min(trie.gstrQuery.length(),trie.dictionary.get(swNode3.iWordIndex).length() )));
					swNode3.dValue = iEd3 *  dL1Weight +
				         	         mapL2.get(swNode3.iWordIndex) *  dL2Weight + 
				                     mapL3.get(swNode3.iWordIndex) *  dL3Weight;
					if(dMaxED < iEd3)
						dMaxED = iEd3;
				}
				//iCurrentElementsInSortedList++;
				//lstSortedWords.add(swNode3);
			}
			if(iCurrentIndex < L1.size())
			{
				dTopK_TA_Threshold = L1.get(iCurrentIndex).dValue *  dL1Weight +
			         			 	 L2.get(iCurrentIndex).dValue *  dL2Weight + 
			                         L3.get(iCurrentIndex).dValue *  dL3Weight;
			}else
			{
				if (iED2 > iEd3)
					iED2 = iEd3;
				dTopK_TA_Threshold = iED2 *  dL1Weight +
        			 	 L2.get(iCurrentIndex).dValue *  dL2Weight + 
                        L3.get(iCurrentIndex).dValue *  dL3Weight;
			}
			if(swNode1.dValue <= swNode2.dValue)
			{
				if(swNode1.dValue <= swNode3.dValue)
				{
					if(addNodeToSortedWordsList(swNode1))
						break;
					if(swNode2.dValue <= swNode3.dValue)
					{
						if(addNodeToSortedWordsList(swNode2))
							break;
						if(addNodeToSortedWordsList(swNode3))
							break;
					}else
					{
						if(addNodeToSortedWordsList(swNode3))
							break;
						if(addNodeToSortedWordsList(swNode2))
							break;
					}
				}else
				{
					if(addNodeToSortedWordsList(swNode3))
						break;
					if(addNodeToSortedWordsList(swNode1))
						break;
					if(addNodeToSortedWordsList(swNode2))
						break;
				}
				
			}else
			{
				if(swNode2.dValue <= swNode3.dValue)
				{
					if(addNodeToSortedWordsList(swNode2))
						break;
					if(swNode3.dValue <= swNode1.dValue)
					{
						if(addNodeToSortedWordsList(swNode3))
							break;
						if(addNodeToSortedWordsList(swNode1))
							break;
					}else
					{
						if(addNodeToSortedWordsList(swNode1))
							break;
						if(addNodeToSortedWordsList(swNode3))
							break;
					}
							
				}else 
				{
					if(addNodeToSortedWordsList(swNode3))
						break;
					if(addNodeToSortedWordsList(swNode2))
						break;
					if(addNodeToSortedWordsList(swNode1))
						break;
				}
			}
			///////////////////////////////////////////////////////////////////////////////
			
		}
		//Collections.sort(lstSortedWords);
		/////////////////////Print Final Sorted list//////////////////
		//System.out.println();
		long endTime = System.nanoTime();
		long duration = (endTime - lTopK_TA_StartTime);
		System.out.println("============="+duration+"===============, Max ED=" + dMaxED);
		int iDisplayCount = Range.k;
		if(iDisplayCount > lstSortedWords.size())
			iDisplayCount = lstSortedWords.size();
		for(int i = 0; i < iDisplayCount; i++ )
			System.out.print("  (" + 
					trie.dictionary.get(lstSortedWords.get(i).iWordIndex) + ") " + 
					lstSortedWords.get(i).dValue);
		//System.out.print(lstSortedWords.get(i).iWordIndex +" ==> (" + 
		//		trie.dictionary.get(lstSortedWords.get(i).iWordIndex) + ") " + 
		//		lstSortedWords.get(i).dValue);
    }
    
    private void topK_TA_dash(List<Sorted_Words_Node> L1,double dL1Weight, List<Sorted_Words_Node> L2,double dL2Weight, 
	          List<Sorted_Words_Node> L3,double dL3Weight
	          ,Map<Integer,Double> mapL1,Map<Integer,Double> mapL2,Map<Integer,Double> mapL3)
  {
		Sorted_Words_Node swNode1, swNode2, swNode3 ;
		Map<Integer,Integer> buffer = new HashMap<>();
		lstSortedWords = new Vector<>();
		for(int iCurrentIndex = 0;iCurrentIndex < L1.size();iCurrentIndex++)
		{
			swNode1 = new Sorted_Words_Node();
			swNode2 = new Sorted_Words_Node();
			swNode3 = new Sorted_Words_Node();
			
			swNode1.dValue = swNode2.dValue = swNode3.dValue = 1000000;
			//////////////////////////////////////List 1////////////////////////////////
			if(!buffer.containsKey(L1.get(iCurrentIndex).iWordIndex))
			{
				buffer.put(L1.get(iCurrentIndex).iWordIndex,1);
				
				swNode1.iWordIndex = L1.get(iCurrentIndex).iWordIndex;
				swNode1.dValue = mapL1.get(swNode1.iWordIndex) *  dL1Weight +
						                    mapL2.get(swNode1.iWordIndex) *  dL2Weight + 
						                    mapL3.get(swNode1.iWordIndex) *  dL3Weight;
				//iCurrentElementsInSortedList++;
				//lstSortedWords.add(swNode1);
			}
			
			if(!buffer.containsKey(L2.get(iCurrentIndex).iWordIndex))
			{
				buffer.put(L2.get(iCurrentIndex).iWordIndex,1);
				
				swNode2.iWordIndex = L2.get(iCurrentIndex).iWordIndex;
				swNode2.dValue = mapL1.get(swNode2.iWordIndex) *  dL1Weight +
						         mapL2.get(swNode2.iWordIndex) *  dL2Weight + 
						         mapL3.get(swNode2.iWordIndex) *  dL3Weight;
				//iCurrentElementsInSortedList++;
				//lstSortedWords.add(swNode2);
			}
			if(!buffer.containsKey(L3.get(iCurrentIndex).iWordIndex))
			{
				buffer.put(L3.get(iCurrentIndex).iWordIndex,1);
				
				swNode3.iWordIndex = L3.get(iCurrentIndex).iWordIndex;
				swNode3.dValue = mapL1.get(swNode3.iWordIndex) *  dL1Weight +
						         mapL2.get(swNode3.iWordIndex) *  dL2Weight + 
						         mapL3.get(swNode3.iWordIndex) *  dL3Weight;
				//iCurrentElementsInSortedList++;
				//lstSortedWords.add(swNode3);
			}
			dTopK_TA_Threshold = L1.get(iCurrentIndex).dValue *  dL1Weight +
			         			 L2.get(iCurrentIndex).dValue *  dL2Weight + 
			                     L3.get(iCurrentIndex).dValue *  dL3Weight;
			if(swNode1.dValue <= swNode2.dValue)
			{
				if(swNode1.dValue <= swNode3.dValue)
				{
					if(addNodeToSortedWordsList(swNode1))
						break;
					if(swNode2.dValue <= swNode3.dValue)
					{
						if(addNodeToSortedWordsList(swNode2))
							break;
						if(addNodeToSortedWordsList(swNode3))
							break;
					}else
					{
						if(addNodeToSortedWordsList(swNode3))
							break;
						if(addNodeToSortedWordsList(swNode2))
							break;
					}
				}else
				{
					if(addNodeToSortedWordsList(swNode3))
						break;
					if(addNodeToSortedWordsList(swNode1))
						break;
					if(addNodeToSortedWordsList(swNode2))
						break;
				}
				
			}else
			{
				if(swNode2.dValue <= swNode3.dValue)
				{
					if(addNodeToSortedWordsList(swNode2))
						break;
					if(swNode3.dValue <= swNode1.dValue)
					{
						if(addNodeToSortedWordsList(swNode3))
							break;
						if(addNodeToSortedWordsList(swNode1))
							break;
					}else
					{
						if(addNodeToSortedWordsList(swNode1))
							break;
						if(addNodeToSortedWordsList(swNode3))
							break;
					}
							
				}else 
				{
					if(addNodeToSortedWordsList(swNode3))
						break;
					if(addNodeToSortedWordsList(swNode2))
						break;
					if(addNodeToSortedWordsList(swNode1))
						break;
				}
			}
			///////////////////////////////////////////////////////////////////////////////
			
		}
		//Collections.sort(lstSortedWords);
		/////////////////////Print Final Sorted list//////////////////
		//System.out.println();
		long endTime = System.nanoTime();
		long duration = (endTime - lTopK_TA_StartTime);
		System.out.println("============="+duration+"===============");
		int iDisplayCount = Range.k;
		if(iDisplayCount > lstSortedWords.size())
			iDisplayCount = lstSortedWords.size();
		for(int i = 0; i < iDisplayCount; i++ )
			System.out.print("  (" + 
					trie.dictionary.get(lstSortedWords.get(i).iWordIndex) + ") " + 
					lstSortedWords.get(i).dValue);
		//System.out.print(lstSortedWords.get(i).iWordIndex +" ==> (" + 
		//		trie.dictionary.get(lstSortedWords.get(i).iWordIndex) + ") " + 
		//		lstSortedWords.get(i).dValue);
  }
	private void initalize() {
		if (sorted_nodes == null || sorted_nodes.size() == 0) {
			active_it = max_active_it = -1;
			range_it = max_range_it = -1;
			record_it = max_record_it = -1;

		} else {
			active_it = 0;
			record_it = 0;
			n = (TrieNode) (sorted_nodes.get(active_it).node);
			range_it = n.rID.min;
			String word = trie.dictionary.get(range_it);
			keyword_records = trie.inverted_list.get(word);
			// Initialize maximum
			max_active_it = sorted_nodes.size();
			max_range_it = n.rID.max + 1;
			max_record_it = keyword_records.size();
		}
	}

	int active_it = -1;
	int max_active_it = 0;
	int range_it = -1;
	int max_range_it = 0;
	int record_it = -1;
	int max_record_it = 0;

	TrieNode n = null;

	List<Integer> keyword_records;

	@Override
	public boolean hasNext() {
		if ((record_it == max_record_it) && (range_it == max_range_it) && (active_it == max_active_it))
			return false;
		else
			return true;
	}

	void advance() {
		record_it++;

		if (record_it == max_record_it) {
			record_it = 0;
			range_it++;
			if (range_it == max_range_it) {
				active_it++;
				if (active_it == max_active_it) {
					active_it = max_active_it = -1;
					range_it = max_range_it = -1;
					record_it = max_record_it = -1;
					return;
				} else { // active
					n = (TrieNode) (sorted_nodes.get(active_it).node);
					range_it = n.rID.min;
					String word = trie.dictionary.get(range_it);
					keyword_records = trie.inverted_list.get(word);
					max_range_it = n.rID.max + 1;
					max_record_it = keyword_records.size();
				}

			} else {// range
				String word = trie.dictionary.get(range_it);
				keyword_records = trie.inverted_list.get(word);
				max_record_it = keyword_records.size();
			}
		}
	}

	@Override
	public Pair next() {
		if (!hasNext())
			throw new NoSuchElementException();
		Integer ret = keyword_records.get(record_it);
		float n = (sorted_nodes.get(active_it).tau());
		advance();
		Pair p = new Pair();
		p.id = ret;
		p.n=n;
		return p;
	}

	@Override
	public String toString() {
		return "[" + active_it + " " + max_active_it + "]";
	}

	public void reset() {
		initalize();
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
}
