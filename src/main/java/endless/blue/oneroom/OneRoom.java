package endless.blue.oneroom;

import javax.swing.JFrame;

public class OneRoom {
	/**
	 * Dear god this year's theme is awful.
	 */
	public static void main(String... args) {
		Screen s = new FieldScreen();
		s.onActivate();
		
		
		Display display = new Display("The Chinese Room");
		display.setVisible(true);
		display.setExtendedState(JFrame.MAXIMIZED_BOTH);
		Display.screen = s;
	}
}
