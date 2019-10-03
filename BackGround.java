package mini_galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class BackGround {
	private int x;
	private int y;

	private Image img;

	public BackGround() {
		x = 0;
		y = 0;

		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("res/bg.jpg");
	}

	public void draw(Graphics g, GalagaCanvas roleCanvas) {
		g.drawImage(img, x, y, roleCanvas);

	}

	public void update() {
		
	}

}
