package mini_galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Missile {
	private int x;
	private int y;
	private Image img;
	private int atk;
	private int stack;
	
	public Missile() {
		x = 0;
		y = 0;
		atk = 10;
		stack = 0;
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("res/bullet.png");
	}
	
	
	public Missile(int x, int y) {
		this();
		this.x = x;
		this.y = y;
	}
	
	
	public int getAtk()
	{
		return atk+stack;
	}
	
	public void update() {
		y -= 7;
	}

	public void draw(Graphics g, GalagaCanvas Galagacanvas) {
		g.drawImage(img, x + 4, y + 4, Galagacanvas);
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
