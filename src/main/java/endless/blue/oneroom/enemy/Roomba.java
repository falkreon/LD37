package endless.blue.oneroom.enemy;

public class Roomba extends Enemy {

	public Roomba() {
		super(null, Enemies.SOUND_ROOMBA_HURT);
		this.curSprite = Enemies.SPRITE_ROOMBA.clone();
	}

}
