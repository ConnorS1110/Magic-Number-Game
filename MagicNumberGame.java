import javax.swing.SwingUtilities;

/**
 * Test the NumbersGame class
 * 
 * @author Connor
 * 
 * 10-26-2020
 *
 */
public class MagicNumberGame {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new NumbersGameLogic();
			}
		});
	}
}
