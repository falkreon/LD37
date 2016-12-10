package endless.blue.oneroom;

import java.awt.Graphics2D;

public interface Screen {
	/** Called when the screen is activated */
	public void onActivate();
	public void onDeactivate();
	public void onPaint(Graphics2D g);
	public void onStep();
}
