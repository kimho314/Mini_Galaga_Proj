package mini_galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Missile {
	private int x;
	private int y;
	private Image img;
	private int atk;
	private static final int DEFAULT_ATK = 10;
	
	public Missile() {
		x = 0;
		y = 0;
		atk = DEFAULT_ATK;
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
		return atk;
	}
	
	public void increaseAtk()
	{
		atk++;
	}
	
	public void resetAtk()
	{
		atk = DEFAULT_ATK;
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
