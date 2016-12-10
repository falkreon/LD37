package endless.blue.oneroom;

public class Light {
	public int x;
	public int y;
	public float intensity;
	public float radius;
	
	public Light() {
		x = 20;
		y = 10;
		intensity = 0.6f;
		radius = 10;
	}
	
	public Light(int x, int y, float radius) {
		this.x = x;
		this.y = y;
		this.intensity = 0.6f;
		this.radius = radius;
	}
}
