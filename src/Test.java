
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import eg.edu.alexu.ehr.*;

/**
 * @author Khalefa
 */
public class Test {

//	public Test() {
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JTextField f = new JTextField(10);
//	AutoSuggestor autoSuggestor = new AutoSuggestor(f, frame,  t, Color.WHITE.brighter(), Color.BLUE,
//				Color.RED, 0.75f);
//		JPanel p = new JPanel();
//		p.add(f);
//		frame.add(p);
//		frame.pack();
//		frame.setVisible(true);
//	}

	static FuzzyTrie t;

	public static void main(String[] args) {

		t = new FuzzyTrie("c:\\data\\author.data");
		for(String s:t.matchString("mohamed_e", 2))
			System.out.println(s);
	
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				new Test();
//			}
//		});
	}

}