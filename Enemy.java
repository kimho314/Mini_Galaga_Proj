

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Enemy {
	private int x;// 블럭 현재 x좌표
	private int y;// 블럭 현재 y좌표
	private int hp; // 블럭 체력

	private Image img; // 이미지 객체
	private final static int E_OFFSET_W = 20; // 폭탄블럭 너비반(정사각이라 하나로씀)
	private final static int TOPHEIGHT = 100; // 상단 헤드 높이

	private int imgIdx;// 이미지 순서 index
	private boolean broken; // broken애니 실행을 위한 스위치
	private boolean brokenrm;// 애니가 끝나고 배열 remove실행시 필요한 스위치
	
	public static int DEFAULT_ENEMY_HP = 10;
	
	public Enemy() {// 기본생성자
		this(0, 0); // 초기화
	}

	public Enemy(int x, int y) {// 위치값 넣는 생성자
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("res/bomb2.png");

		hp = DEFAULT_ENEMY_HP; // 체력 기본 10 설정

		// 중심점을 가운데 기준, 시작점 0으로 시작하기 위해 너비반 더해줌
		this.x = x + E_OFFSET_W;
		// 중심점을 가운데 기준, 기본 좌표에서 너비반값 더하고, 시작점을 상단높이 제외하도록 더해줌
		this.y = y + TOPHEIGHT + E_OFFSET_W;

		imgIdx = 0;
		broken = false;// 계속 스레드에서 값 체크함
	}

	public void draw(Graphics g, GalagaCanvas galagaCanvas) {// 기준점 : 블럭 가운데
		int sw = 40 * imgIdx;

		g.drawImage(img, x - E_OFFSET_W, y - E_OFFSET_W, x - E_OFFSET_W + 40, y - E_OFFSET_W + 40, sw, 0, sw + 40, 40,
				galagaCanvas);
	}

	public int getX() { // 현재 x 좌표 값 가져오기 getter
		return this.x;
	}

	public void setX(int x) { // 현재 x 좌표 값 설정 setter
		this.x = x;
	}

	public int getY() { // 현재 y 좌표 값 가져오기 getter
		return this.y;
	}

	public void setY(int y) { // 현재 y 좌표 값 설정 setter
		this.y = y;
	}

	public int getHp() { // 현재 hp 값 가져오기 getter
		return this.hp;
	}

	public void setHp(int hp) { // 현재 hp 설정 setter
		this.hp = hp;
	}
	
	
	
	public boolean getBrokenrm() { // brokenrm 스위치값 getter
		return brokenrm;
	}
	
	public void setBrokenrm(boolean b)
	{
		this.brokenrm = b;
	}
	
	public void setBroken(boolean broken) { // brokenrm 스위치값 setter
		this.broken = broken;
	}

	public void isBroken() {// 스레드에서 계속 broken 체크 하는 함수
		if (broken) {
			imgIdx++;
			
			if (imgIdx > 7) {
				imgIdx = 7;
				brokenrm = true;
			} 
		}
	}
}
