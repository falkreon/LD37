package endless.blue.oneroom;

public class OneRoom {
	/**
	 * Dear god this year's theme is awful.
	 */
	public static void main(String... args) {
		Screen s = new FieldScreen();
		s.onActivate();
		
		
		Display display = new Display("One Room Draft");
		display.setVisible(true);
		//display.setExtendedState(JFrame.MAXIMIZED_BOTH);
		Display.screen = s;
	}
}
