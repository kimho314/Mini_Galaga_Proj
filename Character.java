package bomb;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Character {
	private int x;
	private int y;

	private int w;
	private int h;

	private Image img;
	private int imgIndex;
	private int imgDelayCount;

	private final static int OFFSET_W = 32;
	private static final int OFFSET_H = 32; // final static 두개의 순서가 관계없음

	private int attackSpeed;

	public Character() {
		x = 400;
		y = 450;

		w = h = 64;

		imgDelayCount = 0;
		attackSpeed = 0;

		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("res/fighter.png");
		imgIndex = 3;
	}

	public void update() {
		if (attackSpeed > 0)
			attackSpeed--;
	}

	public void draw(Graphics g, RoleCanvas roleCanvas) {
		int sx = imgIndex * w;
		g.drawImage(img, x - OFFSET_W, y - OFFSET_H, x + w - OFFSET_W, y + h - OFFSET_H, sx, 0, sx + w, h, roleCanvas);
	}

	public void move(Direction direction) {
		switch (direction) {
		case LEFT:
			if (x > 20) {
				if (imgDelayCount < 0)
					imgDelayCount = 1;
				if (imgIndex > 0 & ++imgDelayCount >= 5) {
					imgIndex--;
					imgDelayCount = 0;
				}
				x -= 3;
			} else {
				x = 16;
			}
			break;
		case RIGHT:
			if (x < 770) {
				if (imgDelayCount > 0)
					imgDelayCount = -1;
				if (imgIndex < 6 & --imgDelayCount <= -5) {
					imgIndex++;
					imgDelayCount = 0;
				}
				x += 3;
			} else {
				x = 770;
			}
			break;

		default:
			break;
		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Missile attack(int attackSpeed) {
		if (this.attackSpeed == 0) {
			Missile m = new Missile(x, y - 10);
			this.attackSpeed = attackSpeed;
			return m;
		}
		return null;
	}
}
