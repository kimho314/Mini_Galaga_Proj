

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnemyGroup {
	private List<Enemy> enemies; // �� �迭 ����
	private int rand; // ������ ����
	private int vx; // �����̴� ����
	private int vy; // �����̴� ����

	private int gy; // �ش� �׷� y ��ǥ��

	private boolean drecSwi; // �� ���� ����Ű
	private boolean xSwi; // �� ���� ��� �ν� Ű
	
	private int egTimer; // suyoung �߰�
	public static int basicTime = 60; // suyoung �߰�
	

	public EnemyGroup() {// suyoung �⺻�����ڷ� ����
		rand = (int) (Math.random() * 5) + 1; // �ּ� 1�� �̻� ������ ����
		enemies = Collections.synchronizedList(new ArrayList<Enemy>());

		// ����ȭ�Ǵ� ����Ʈ ����
		for (int i = 0; i < rand; i++) { // ��������ŭ �迭�� �� ����ֱ�
			enemies.add(new Enemy(i * 40, 0)); // suyoung y�� 0���� ����
		}

		gy = enemies.get(0).getY();// �׷� y��ǥ�� ù��° �� y��ǥ�� �ʱ⼳��
		vx = 40; // x��ǥ�� �����̴� ����
		vy = 40; // y��ǥ�� �����̴� ����
		xSwi = true; // ����� �� �ڵ� x��ǥ �̵�
		egTimer = (enemies.size()*9)+basicTime; // suyoung �߰� 
	}
	
	public void timerUpdate() {// suyoung timerUpdate �޼��� �߰�
		if(egTimer == 0) {
			move();
			egTimer = (enemies.size()*9)+basicTime;// timerReset();
		}
		egTimer--;
	}
	

	public int getGy() {// �׷� y��ǥ getter
		return this.gy; // ���� �׷� y��ǥ
	}
	
	public void printHp()
	{
		for(Enemy e : enemies)
		{
			if(e != null)
			{
				System.out.println(e.getHp());
			}
		}
	}
	
	public void draw(Graphics g, GalagaCanvas galagaCanvas) {
		for (Enemy e : enemies) {
			e.draw(g, galagaCanvas);
		}
	}
	

	public boolean isCrush(Missile o, int atk) {		
		boolean ret = false;

		if (o != null) {			
			int mx = o.getX() / 40; // �̻����� x��ǥ�� 40���� ������, ���� �̻����� �ִ� ĭ ��
			int my = o.getY();
			
			for (int i = 0; i < enemies.size(); i++) {
				if ((my <= this.gy + 80) && (my >= this.gy - 80)) {
					int egX = enemies.get(0).getX() / 40;
					int egsIndex = mx - egX;

					if ((egsIndex >= 0) && (egsIndex < enemies.size())) {
						int enemyHp = enemies.get(egsIndex).getHp();
						int enemyHpSum = enemyHp - atk;
						
						enemies.get(egsIndex).setHp(enemyHpSum);
						System.out.println(enemies.get(egsIndex).getHp());
						ret = true;
						break;
					}
				}
			}
		}

		return ret;
	}

	
	public void brokenCheck()
	{
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			
			// hp�� 0�� enemy�� broken�� true�� set
			if (e.getHp() <= 0) {
				e.setBroken(true);
			}
			// enemy�� broken���� Ȯ���ϸ鼭 ���� true�̸��� 
			// �μ����� �ִϸ��̼� ����
			e.isBroken();
		}
	}
	
	
	public void brokenRemove(ScoreDisplay scDisp, int score) {
		for (int i = 0; i < enemies.size(); i++) {
			// enemy�� �μ����� �ִϸ��̼��� �������� Ȯ��
			if (enemies.get(i).getBrokenrm()) {
				// ���⼭ ���ھ� ���� �Ͼ����
				scDisp.scoreUp(score);
				// ���ھ� �� �Ŀ� enemies ����
				enemies.remove(i);

				// ��迭�� �� ��� ������ 2�� �̻��̰� enemy group�� ù��° ����� �ƴҶ���
				if ((enemies.size() > 1) && (i != 0)) {
					for (int j = i; j < enemies.size(); j++) {
						// ���ŵ� �迭 index���� ���ʷ� x��ǥ ��ĭ(40) ����
						enemies.get(j).setX(enemies.get(j).getX() - 40);
					}
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
				int dx = e.getX() - vx;// suyoung attackSpeed���� 
				e.setX(dx);
			}
		} else {// drecSwi���� false�ϰ��� ���� ���������� ���� �ִ� ���

			for (Enemy e : enemies) {
				int dx = e.getX() + vx;// suyoung attackSpeed���� 
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
