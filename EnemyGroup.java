package mini_galaga;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnemyGroup {
	private List<Enemy> enemies; // �� �迭 ����
	private int rand; // ������ ����
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
		rand = (int) (Math.random() * 5) + 1; // �ּ� 1�� �̻� ������ ����

		enemies = Collections.synchronizedList(new ArrayList<Enemy>());

		// ����ȭ�Ǵ� ����Ʈ ����
		for (int i = 0; i < rand; i++) { // ��������ŭ �迭�� �� ����ֱ�
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
	
	public void hpUp()
	{
		for(Enemy e : enemies)
		{
			if(e != null)
			{
				int eHp = e.getHp();
				if(eHp < Integer.MAX_VALUE)
				{
					e.setHp(eHp++);
				}
			}
		}
	}
	
	public void printEnemyGroupHp()
	{
		for(int i=0; i<enemies.size(); i++)
		{
			if(enemies.get(i) != null)
			{
				System.out.print(" E index : " + i + " HP : " + enemies.get(i).getHp());
			}
		}
	}
	
	public void draw(Graphics g, GalagaCanvas galagaCanvas) {
		for (Enemy e : enemies) {
			e.draw(g, galagaCanvas);
		}
	}

	public void moveUpdate() { // �� �����Ӵ� ������Ʈ ȣ��		
		move();
	}
	
	
	public void brokenUpdate() {
		brokenCheck();
		brokenRemove();
	}
	

	public boolean isCrush(Missile o) {		
		boolean ret = false;

		if ((!enemies.isEmpty()) && (o != null)) {
			
			int mx = o.getX() / 40; // �̻����� x��ǥ�� 40���� ������, ���� �̻����� �ִ� ĭ ��
			int my = o.getY();
			
			for (int i = 0; i < enemies.size(); i++) {
				if ((my <= this.gy + 40) && (my >= this.gy)) {
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
	
	
	public void brokenRemove() {
		for (int i = 0; i < enemies.size(); i++) {
			// enemy�� �μ����� �ִϸ��̼��� �������� Ȯ��
			if (enemies.get(i).getBrokenrm()) {
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
