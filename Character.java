package mini_galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Character {
	private int x; // 캐릭터의 x좌표
	private int y; // 캐릭터의 y좌표
	private int w; // 캐릭터 가로길이
	private int h; // 캐릭터 세로길이 
	
	private int hpx; // 하트 x좌표
	private int hpy; // 하트 y좌표
	private int hpw; // 하트이미지 가로길이
	private int hph; // 하트이미지 세로길이 
	private int hpI; // 하트이미지 사이 간격 
	private int maxHp;
	
	private int vx; // 캐릭터 한칸 이동값

	private Image img; // 캐릭터 이미지
	private Image hpImg; // 하트 이미지
	private Image BImg; // bae 추가
	private int imgIndex; // 캐릭터인덱스 
	
	private int attackSpeed;
	private int atk;
	private int bulletNum;
	
	private static final int DEFAULT_ATK = 10;
	
	public Character() {
		x = 496/2;
		y = 720;
		w = 40;
		h = 36;
		
		hpx = 45; // bae 수정
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
		BImg = tk.getImage("res/bullet2.png"); // bae 추가
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
		
		g.drawImage(img, x - 5 , y, x + w - 5, y + h, sx, 0, sx + w, h, galagaCanvas); // 캐릭터이미지 설정				
		
		for(int i=0; i<maxHp; i++)
		{
			g.drawImage(hpImg, hpI+hpx*i, hpy, hpI+hpx*(i+1), hpy+hph, 0, 0, hpw, hph, galagaCanvas); // 하트이미지 설정 
		}
		
		for(int i=0; i<bulletNum; i++) //// bae 추가
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
	         
	         x -= 40; // 가로 한칸씩 이동
	         if (x < 0) // 화면 프레임 아웃방지
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
