package endless.blue.oneroom;

import java.awt.Color;
import java.awt.Graphics2D;

public class FieldScreen implements Screen {
	@Override
	public void onActivate() {
		
	}

	@Override
	public void onDeactivate() {
		
	}

	@Override
	public void onPaint(Graphics2D g) {
		g.setColor(new Color((int)(Math.random()*255.0f), (int)(Math.random()*255.0f), (int)(Math.random()*255.0f)));
		g.fillRect(1, 1, 638, 358);
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		
	}

}
