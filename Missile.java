package bomb;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Missile {
	private int x;
	private int y;
	private Image img;
	private final static int OFFSET_W = 10;
	private static final int OFFSET_H = 20;

	public Missile() {
		x = -500;
		y = -500;

		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("res/missile.png");
	}

	public Missile(int x, int y) {
		this();
		this.x = x;
		this.y = y;
	}

	public void update() {
		y -= 7;
	}

	public void draw(Graphics g, RoleCanvas roleCanvas) {
		g.drawImage(img, x-OFFSET_W, y-OFFSET_H, roleCanvas);
	}

	public int getY() {
		return y;
	}
}
