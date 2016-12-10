package endless.blue.oneroom;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class Keyboard implements KeyListener {
	private static HashMap<Integer, Activator> activators = new HashMap<>();
	private static Keyboard instance;
	
	static {
		addActivator(new Activator("left",   KeyEvent.VK_LEFT));
		addActivator(new Activator("right",  KeyEvent.VK_RIGHT));
		addActivator(new Activator("up",     KeyEvent.VK_UP));
		addActivator(new Activator("down",   KeyEvent.VK_DOWN));
		addActivator(new Activator("action", KeyEvent.VK_X));
		addActivator(new Activator("cancel", KeyEvent.VK_C));
	}
	
	
	public static Activator getActivator(String key) {
		for(Activator a : activators.values()) {
			if (a.name.equalsIgnoreCase(key)) return a;
		}
		return null;
	}
	
	public static void addActivator(Activator a) {
		activators.put(a.binding, a);
	}
	
	public static boolean isPressed(String key) {
		Activator a = getActivator(key);
		if (a==null) return false;
		return a.pressed;
	}
	
	public static class Activator {
		/** scan code for this keybind */
		public String name;
		public int joypadBinding = -1;
		public int binding;
		public boolean pressed = false;
		
		public Activator(String name, int keybind) {
			this.name = name;
			this.binding = keybind;
		}
	}

	public static Keyboard getInstance() {
		if (instance==null) instance = new Keyboard();
		return instance;
	}
	
	

	@Override
	public void keyPressed(KeyEvent e) {
		Activator a = activators.get(e.getKeyCode());
		if (a!=null) {
			//if (!a.pressed) System.out.println("PRESS: "+a.name);
			a.pressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Activator a = activators.get(e.getKeyCode());
		if (a!=null) {
			//if (a.pressed) System.out.println("RELEASE: "+a.name);
			a.pressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
