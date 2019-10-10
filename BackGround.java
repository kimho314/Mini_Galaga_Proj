

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class BackGround {
	private int x;
	private int y;

	private Image bgImg;

	public BackGround() {
		x = 0;
		y = 0;

		Toolkit tk = Toolkit.getDefaultToolkit();

		int a = (int) (Math.random() * 5); // 배경화면 랜덤출력

		switch (a) {
		case 1:
			bgImg = tk.getImage("res/bg.jpg");
			break;
			
		case 2:
			bgImg = tk.getImage("res/bg2.jpg");
			break;
			
		case 3:
			bgImg = tk.getImage("res/bg3.jpg");
			break;
			
		case 4:
			bgImg = tk.getImage("res/bg4.jpg");
			break;
			 
		default:
			bgImg = tk.getImage("res/bg5.jpg");
			break;
		}
	}

	public void draw(Graphics g, GalagaCanvas roleCanvas) {
		g.drawImage(bgImg, x, y, roleCanvas);
	}

}
