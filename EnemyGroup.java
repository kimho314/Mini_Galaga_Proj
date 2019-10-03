package mini_galaga;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnemyGroup {
	private List<Enemy> enemies; // �� �迭 ����
	private int ran; // ������ ����
	private int vx; // �����̴� ����
	private int vy; // �����̴� ����

	private int attackSpeed; // ������ ���ǵ�
	private int gy; // �ش� �׷� y ��ǥ��

	private boolean drecSwi; // �� ���� ����Ű
	private boolean xSwi; // �� ���� ��� �ν� Ű

	public EnemyGroup() {
		this(0, 1);
	}

	public EnemyGroup(int y, int speed) {
		ran = (int) (Math.random() * 5) + 1; // �ּ� 1�� �̻� ������ ����

		enemies = Collections.synchronizedList(new ArrayList<Enemy>());

		// ����ȭ�Ǵ� ����Ʈ ����
		for (int i = 0; i < ran; i++) { // ��������ŭ �迭�� �� ����ֱ�
			enemies.add(new Enemy(i * 40, y)); // �� ��ü�������� �ʱ� x��ǥ�� �ʺ� 40�� ������ ���ؼ� ����
		}

		gy = enemies.get(0).getY();// �׷� y��ǥ�� ù��° �� y��ǥ�� �ʱ⼳��
		attackSpeed = speed; // ������ ���ǵ� ����
		vx = 40; // x��ǥ�� �����̴� ����
		vy = 40; // y��ǥ�� �����̴� ����
		xSwi = true; // ����� �� �ڵ� x��ǥ �̵�
	}

	public List<Enemy> getEnemyGroup() { // �� �迭 getter
		if (enemies.size() > 0) {
			return enemies;
		} else
			return null;
	}

	public int getGy() {// �׷� y��ǥ getter
		return this.gy; // ���� �׷� y��ǥ
	}

	public void draw(Graphics g, GalagaCanvas galagaCanvas) {
		for (Enemy e : enemies) {
			e.draw(g, galagaCanvas);
		}
	}

	public void update() { // �� �����Ӵ� ������Ʈ ȣ��

		// �׻� ���� broken ���ڰ� true���� Ȯ�� true�̸� ���߾׼�
		brokenCheck();		
		brokenRemove();
		brokenUpdate();
		
//		if (enemies.size() > 1) { // �迭�� 2���̻��� ��쿡�� �������ϵ��� ��
//			arrInit(); // �迭��� �ٽ� x��ǥ ������
//		} 
//		else if (enemies.size() == 1) {// �迭�� ���� 1���� ��� ���� ó��
//
//			if (enemies.get(0).getHp() <= 0) {
//				enemies.get(0).setBroken(true);// �ش� ���� hp�� 0�϶� broke������ true ����
//
//				// broken true�� ������ �ִ� ���� �� ���� brokenrm���� �ٲ� �� remove����
//				if (enemies.get(0).getBrokenrm())
//					enemies.remove(0);
//			} 
//		}
		
		move();

	}


	public boolean isCrush(Missile o) {
		int mx = o.getX() / 40;// �̻����� x��ǥ�� 40���� ������, ���� �̻����� �ִ� ĭ ��
		int my = o.getY();// �̻����� y�߽���ǥ���� ���� 20�� ���༭ �̹��� ��ǥ ������ y�� ����
		boolean ret = false;

		if (!enemies.isEmpty()) {
			for (int i = 0; i < enemies.size(); i++) {
				if (my == this.gy + 40) {
					int egX = enemies.get(0).getX() / 40;
					int egsIndex = mx - egX;

					if (egsIndex >= 0 && egsIndex < enemies.size()) {
						int enemyHp = enemies.get(egsIndex).getHp();
						int enemyHpSum = enemyHp - o.getAtk();
						enemies.get(egsIndex).setHp(enemyHpSum);
						ret = true;
						break;
					}
				}
			}
		}

		return ret;
	}

//	public void arrInit() { // �迭�� x��ǥ ��迭 �� �� ü�� üũ �Լ�
//		for (int i = 0; i < enemies.size(); i++) {
//			if (enemies.get(i).getHp() <= 0) {
//
//				// ���迭�� �ϳ��� ü��(hp)���� �����ͼ� 0���� �۰ų� ������ ���� broken�� true�� ����
//				enemies.get(i).setBroken(true);
//
//				if (enemies.get(i).getBrokenrm()) {
//					// broken �ִ� �Ŀ� ���� brokenrm ���� true�� ��� �迭 ����
//					enemies.remove(i);
//
//					for (int j = i + 1; j < enemies.size(); j++) {
//						// ���ŵ� �迭 index���� ���ʷ� x��ǥ ��ĭ(40) ����
//						enemies.get(j).setX(enemies.get(j).getX() - 40);
//					}
//				}
//			}
//		}
//	}
	
	public void brokenCheck()
	{
		for (Enemy e : enemies) {
			e.isBroken();
		}
	}
	
	
	public void brokenRemove()
	{
		for(int i=0; i<enemies.size(); i++)
		{
			if(enemies.get(i).getBrokenrm())
			{
				enemies.remove(i);
				for (int j = i + 1; j < enemies.size(); j++) {
					// ���ŵ� �迭 index���� ���ʷ� x��ǥ ��ĭ(40) ����
					enemies.get(j).setX(enemies.get(j).getX() - 40);
				}
			}
		}
	}
	
	public void brokenUpdate() {
		if (enemies.size() > 0) {
			for (int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				if (e.getHp() <= 0) {
					e.setBroken(true);
//					enemies.remove(i);
//
//					for (int j = i + 1; j < enemies.size(); j++) {
//						// ���ŵ� �迭 index���� ���ʷ� x��ǥ ��ĭ(40) ����
//						enemies.get(j).setX(enemies.get(j).getX() - 40);
//					}
				}
			}
		}
	}
	
	
	public void move() {
		if (xSwi) { // Ű���� true�� ��� �� x��ǥ �̵�

			xmove();// �ڵ� x��ǥ �̵� �Լ� ȣ��

			if (!enemies.isEmpty()) {
				if (enemies.get(0).getX() <= 20) {// ���� ���� ���� �ε��� ���
					xSwi = false;// y��ǥ �̵��� ���� Ű�� ����
					drecSwi = false;// x��ǥ ������ ������ �ٲ��ش�

				} else if (enemies.get((enemies.size() - 1)).getX() >= 460) {// ���� ������ ���� �ε��� ���
					xSwi = false;// y��ǥ �̵��� ���� Ű�� ����
					drecSwi = true;// x��ǥ ������ ������ �ٲ��ش�
				}
			}
		} else {
			ymove();// Ű���� false�� ��� �� y��ǥ �̵�
		}
	}

	public void xmove() { // �� x��ǥ �̵� �Լ�

		if (drecSwi) {// drecSwi���� true�ϰ��� ���� �������� ���� �ִ� ���

			for (Enemy e : enemies) {
				int dx = e.getX() - (vx * attackSpeed);// �� ��ü���� -vx��ŭ �̵�
				e.setX(dx);
			}
		} else {// drecSwi���� false�ϰ��� ���� ���������� ���� �ִ� ���

			for (Enemy e : enemies) {
				int dx = e.getX() + (vx * attackSpeed);// �� ��ü���� vx��ŭ �̵�
				e.setX(dx);
			}
		}
	}

	public void ymove() {// �� y��ǥ �̵� �Լ�
		for (Enemy e : enemies) {
			int dy = e.getY() + vy;
			e.setY(dy);
			gy = dy; // �� �迭�� y
		}

		xSwi = true;// �� y��ǥ �̵��� �ٽ� x��ǥ �̵��� ���� Ű�� ����
	}

}
