
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import eg.edu.alexu.*;

/**
 * @author Khalefa
 */
public class Test {

	public Test() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextField f = new JTextField(10);
		AutoSuggestor autoSuggestor = new AutoSuggestor(f, frame, (PivotalTrie) t, Color.WHITE.brighter(), Color.BLUE,
				Color.RED, 0.75f);
		JPanel p = new JPanel();
		p.add(f);
		frame.add(p);
		frame.pack();
		frame.setVisible(true);
	}

	static Trie t;

	public static void Serilaize(String file) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(t);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + file);
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	public static void writeWords(String file) {
		try {
			PrintWriter writer = new PrintWriter(file + ".words", "UTF-8");
			Random randomGenerator = new Random();
		    for (int idx = 0; idx < Trie.dictionary.size(); ++idx){
		      int randomInt = (int)(Math.random()* 1000);//randomGenerator.nextInt(Trie.dictionary.size() + 1);
		      writer.println(Trie.dictionary.get(idx) + "," + randomInt);
		    }
			writer.close();
			System.out.printf("Serialized data is saved in " + file);
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static void Desrialize(String file) {
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			t = (PivotalTrie) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("PivotalTrie class not found");
			c.printStackTrace();
			return;
		}
	}

	public static void main(String[] args) {

		String[] filename = { "icd10cmedited.txt", "test.txt", "words.txt", "test2.txt" };
		String file = "c:\\data\\Edit\\icd10cmedited.txt.words" ;
		//String file = "c:\\data\\Edit\\test.txt";
		t = new PivotalTrie(file);
        t.gstrQuery = "abc";
		Matcher m;
		m = new Matcher((PivotalTrie)t);
		m.getCandidate(t.gstrQuery);
//			Serilaize(file);
//			//writeWords(file);
//			 t=null;
//			Desrialize(file + ".ser");
//			SwingUtilities.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					new Test();
//				}
//			});
	}

}