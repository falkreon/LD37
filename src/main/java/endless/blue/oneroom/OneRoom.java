package endless.blue.oneroom;

import javax.swing.JFrame;

public class OneRoom {
	/**
	 * Dear god this year's theme is awful.
	 */
	public static void main(String... args) {
		Display display = new Display("One Room Draft");
		display.setVisible(true);
		display.setExtendedState(JFrame.MAXIMIZED_BOTH);
		display.screen = new FieldScreen();
	}
}
