

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Enemy {
	private int x;// �� ���� x��ǥ
	private int y;// �� ���� y��ǥ
	private int hp; // �� ü��

	private Image img; // �̹��� ��ü
	private final static int E_OFFSET_W = 20; // ��ź�� �ʺ��(���簢�̶� �ϳ��ξ�)
	private final static int TOPHEIGHT = 100; // ��� ��� ����

	private int imgIdx;// �̹��� ���� index
	private boolean broken; // broken�ִ� ������ ���� ����ġ
	private boolean brokenrm;// �ִϰ� ������ �迭 remove����� �ʿ��� ����ġ
	
	public static int DEFAULT_ENEMY_HP = 10;
	
	public Enemy() {// �⺻������
		this(0, 0); // �ʱ�ȭ
	}

	public Enemy(int x, int y) {// ��ġ�� �ִ� ������
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("res/bomb2.png");

		hp = DEFAULT_ENEMY_HP; // ü�� �⺻ 10 ����

		// �߽����� ��� ����, ������ 0���� �����ϱ� ���� �ʺ�� ������
		this.x = x + E_OFFSET_W;
		// �߽����� ��� ����, �⺻ ��ǥ���� �ʺ�ݰ� ���ϰ�, �������� ��ܳ��� �����ϵ��� ������
		this.y = y + TOPHEIGHT + E_OFFSET_W;

		imgIdx = 0;
		broken = false;// ��� �����忡�� �� üũ��
	}

	public void draw(Graphics g, GalagaCanvas galagaCanvas) {// ������ : �� ���
		int sw = 40 * imgIdx;

		g.drawImage(img, x - E_OFFSET_W, y - E_OFFSET_W, x - E_OFFSET_W + 40, y - E_OFFSET_W + 40, sw, 0, sw + 40, 40,
				galagaCanvas);
	}

	public int getX() { // ���� x ��ǥ �� �������� getter
		return this.x;
	}

	public void setX(int x) { // ���� x ��ǥ �� ���� setter
		this.x = x;
	}

	public int getY() { // ���� y ��ǥ �� �������� getter
		return this.y;
	}

	public void setY(int y) { // ���� y ��ǥ �� ���� setter
		this.y = y;
	}

	public int getHp() { // ���� hp �� �������� getter
		return this.hp;
	}

	public void setHp(int hp) { // ���� hp ���� setter
		this.hp = hp;
	}
	
	
	
	public boolean getBrokenrm() { // brokenrm ����ġ�� getter
		return brokenrm;
	}
	
	public void setBrokenrm(boolean b)
	{
		this.brokenrm = b;
	}
	
	public void setBroken(boolean broken) { // brokenrm ����ġ�� setter
		this.broken = broken;
	}

	public void isBroken() {// �����忡�� ��� broken üũ �ϴ� �Լ�
		if (broken) {
			imgIdx++;
			
			if (imgIdx > 7) {
				imgIdx = 7;
				brokenrm = true;
			} 
		}
	}
}
