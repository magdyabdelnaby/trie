package eg.edu.alexu;


public class Sorted_Words_Node implements Comparable<Sorted_Words_Node>
{
	public int iWordIndex;
	public double dValue;
	Sorted_Words_Node()
	{
	}
	Sorted_Words_Node(int iWordIndex, double dValue)
	{
		this.iWordIndex = iWordIndex;
		this.dValue = dValue;
	}
	@Override
	public int compareTo(Sorted_Words_Node o) 
	{
		if(dValue == o.dValue)
			return Integer.compare(iWordIndex, o.iWordIndex);
		return Double.compare(dValue, o.dValue);
	}
}
