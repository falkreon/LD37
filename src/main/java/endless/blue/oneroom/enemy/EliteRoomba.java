package endless.blue.oneroom.enemy;

import endless.blue.oneroom.Cardinal;
import endless.blue.oneroom.Room;

public class EliteRoomba extends Enemy {

	int interest = 40;
	
	public EliteRoomba() {
		super(Enemies.IMAGE_ROOMBA_ELITE, Enemies.SOUND_ROOMBA_HURT);
		points = 200;
	}

	@Override
	public void wander(Room room) {
		interest--;
		if (interest>0) {
			if (!nudgeForward(room)) {
				this.facing = Cardinal.values()[(int)(Math.random()*4)];
				interest = 80;
			}
		} else {
			this.facing = Cardinal.values()[(int)(Math.random()*4)];
			interest = 80;
		}
	}
}
