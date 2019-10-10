package mini_galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Character {
	private int x; // ĳ������ x��ǥ
	private int y; // ĳ������ y��ǥ
	private int w; // ĳ���� ���α���
	private int h; // ĳ���� ���α��� 
	
	private int hpx; // ��Ʈ x��ǥ
	private int hpy; // ��Ʈ y��ǥ
	private int hpw; // ��Ʈ�̹��� ���α���
	private int hph; // ��Ʈ�̹��� ���α��� 
	private int hpI; // ��Ʈ�̹��� ���� ���� 
	private int maxHp;
	
	private int vx; // ĳ���� ��ĭ �̵���

	private Image img; // ĳ���� �̹���
	private Image hpImg; // ��Ʈ �̹���
	private Image BImg; // bae �߰�
	private int imgIndex; // ĳ�����ε��� 
	
	private int attackSpeed;
	private int atk;
	private int bulletNum;
	
	private static final int DEFAULT_ATK = 10;
	
	public Character() {
		x = 496/2;
		y = 720;
		w = 40;
		h = 36;
		
		hpx = 45; // bae ����
		hpy = 60;
		hpw = 48;
		hph = 36;
		maxHp = 0;		
		hpI = 5;
		
		atk = DEFAULT_ATK;
		attackSpeed = 0;
		bulletNum = 0;
		imgIndex = 3;
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("res/man.png");
		hpImg = tk.getImage("res/heart.png");
		BImg = tk.getImage("res/bullet2.png"); // bae �߰�
	}
	
	public void increaseAtk()
	{
		this.atk++;
		System.out.println("increase atk " + atk);
	}
	
	public void resetAtk()
	{
		atk = DEFAULT_ATK;
		System.out.println("atk reset " + atk);
	}
	
	public int getAtk()
	{
		return atk;
	}
	
	public void setBulletNum(int bulletNum)
	{
		this.bulletNum = bulletNum;
	}
	
	public int getBulletNum()
	{
		return this.bulletNum;
	}
	
	public void minusBulletNum()
	{
		this.bulletNum--;
		if(this.bulletNum <= 0)
		{
			this.bulletNum = 0;
		}
	}
	
	public void reloadBullet()
	{
		this.bulletNum = Difficulty.missileStack;
	}
	
	public void minusMaxHp()
	{
		this.maxHp--;
		if(this.maxHp <= 0)
		{
			this.maxHp = 0;
		}
	}
	
	public void setMaxHp(int hp)
	{
		this.maxHp = hp;
	}
	
	public int getHp()
	{
		return maxHp;
	}
	
	public int imgIndex() {
		imgIndex = 4;
		return imgIndex;
	}

	public void update() {
		if (attackSpeed > 0)
		{
			attackSpeed--;
		}
		x += vx;
	}

	public void draw(Graphics g, GalagaCanvas galagaCanvas) { 
		int sx = imgIndex * w;
		
		g.drawImage(img, x - 5 , y, x + w - 5, y + h, sx, 0, sx + w, h, galagaCanvas); // ĳ�����̹��� ����				
		
		for(int i=0; i<maxHp; i++)
		{
			g.drawImage(hpImg, hpI+hpx*i, hpy, hpI+hpx*(i+1), hpy+hph, 0, 0, hpw, hph, galagaCanvas); // ��Ʈ�̹��� ���� 
		}
		
		for(int i=0; i<bulletNum; i++) //// bae �߰�
		{
			g.drawImage(BImg, hpI+hpx*i, hpy-40, hpI+hpx*(i+1), hpy+hph-40, 0, 0, hpw, hph, galagaCanvas); 
		}
	}

	public void move(Direction direction) {
		switch (direction) {
	      case LEFT:
	         if (imgIndex == 0) {
	            imgIndex = 1;
	         }else {
	            imgIndex = 0;
	         }
	         
	         x -= 40; // ���� ��ĭ�� �̵�
	         if (x < 0) // ȭ�� ������ �ƿ�����
	            x = 0;
	         break;

	      case RIGHT:
	         if (imgIndex == 7) {
	            imgIndex = 8;
	         }else {
	            imgIndex = 7;
	         }
	         
	         x += 40;
	         if (x > 450)
	            x = 450;
	         break;
	         
	      case SELECT:
	         imgIndex = 4;
	         break;
	      }
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Missile attack() {
		Missile m = new Missile(x, y);
		return m;
	}
}
