package bomb;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class 배경화면 {
	private int x;
	private int y;

	private Image img;

	public 배경화면() {
		x = 0;
		y = 0;

		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("res/test.png");
	}

	public void draw(Graphics g, RoleCanvas roleCanvas) {
		g.drawImage(img, x, y, roleCanvas);
//		g.drawImage(img, x, y - 800, roleCanvas);
//		g.drawImage(img, x + 480, y, roleCanvas);
//		g.drawImage(img, x + 480, y - 800, roleCanvas);
	}

	public void update() {
//		y += 1;
//
//		if (y >= 799) {
//			y = 0;
//		}
	}
}
