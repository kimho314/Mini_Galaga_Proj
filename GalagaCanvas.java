package mini_galaga;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class GalagaCanvas extends Canvas implements KeyListener, MouseListener {

	private Character kid; // ĳ���͸� �ҷ���
	private BackGround bg; // ���ȭ���� �ҷ���
	private List<Missile> missiles; // �Ѿ˰��� �ҷ��� (�迭)

	private boolean leftPress; // ����Ű�� �������ִ��� Ȯ��
	private boolean rightPress; // ������Ű�� �������ִ��� Ȯ��

	private int scoreTotal; // ������ ǥ����
	private int score; // ���� ��������� ����
	private int windowsIndex; // â ��ȯ�� ������ ����

	private Title title; // ����ȭ��
	private Difficulty difficulty; // ���̵� ����â

	int btxMove1;
	int btxMove2;

	private int kidTimer;

	private EnemyGroup[] egs;// su - �� ������ ���׷� �迭
	private int egsCnt;// su - egs �迭 index ī��Ʈ
	private int egsTimer;// su - �������� Ÿ�̸�
	private int egsUpdateTimer;// su - �� �׷� ������Ʈ Ÿ�̸�

	public GalagaCanvas() {
		addMouseListener(this);
		addKeyListener(this);

		leftPress = false;
		rightPress = false;

		windowsIndex = 0;
		scoreTotal = 0;
		btxMove1 = 0;
		btxMove2 = 0;

		kid = new Character();
		bg = new BackGround();
		missiles = new ArrayList<Missile>();
		title = new Title();
		difficulty = new Difficulty();

		kidTimer = 0;

		egsTimer = 0; // su - Ÿ�̸� �ʱ�ȭ
		egsUpdateTimer = 0;// su - Ÿ�̸� �ʱ�ȭ

		egsCnt = 0; // su - �迭ī��Ʈ �ʱ�ȭ
		egs = new EnemyGroup[10]; // su - EnemyGroup �迭

		new Thread(() -> { // ���� ������
			while (true) {
				try {
					kid.update(); // ĳ���� ������Ʈ
					bg.update(); // ���ȭ�� ������Ʈ (�ʿ������ ���￹��)

					if (btxMove1 != 0) {
						title.update();
						btxMove1--;
					}
					if (btxMove2 != 0) {
						difficulty.update();
						btxMove2--;
					}

					if (windowsIndex == 2) { // ���� ���۽� �۵�

						for (Missile o : missiles) {
							o.update();
						}

						for (int i = 0; i < missiles.size(); i++) {
							if (missiles.get(i) != null) {

								if (missiles.get(i).getY() < 100) {
									missiles.remove(i);
								} else {
									// isCrush(missiles.get(i)); // su - �ش� �̻����� �浹���� Ȯ��
									if (egsCnt > 0) {
										for (int j = 0; j < egs.length; j++) {
											if (egs[j] != null) {
												boolean retCrush = egs[j].isCrush(missiles.get(i));

												if (retCrush) {
													System.out.println("Hit!!!");
													missiles.remove(i);
												}
											}
										}
									}
								}

							}
						}

						// su - EnemyGroup �迭 egs�� update�ݺ�
						if (egsUpdateTimer == 0) { // 700ms
							for (int i = 0; i < egs.length; i++) {

								if (egs[i] != null) {
									egs[i].update();

									if (egs[i].getGy() >= 680) {
										// y�� �ٴڿ� ���� �� null �� �ֱ�
										egs[i] = null;
										kid.minusMaxHp();
									}

								}
							}

							egsUpdateTimer = 100;
						}

						// su - egsTimerŸ�̸� ������� ���׷� ����
						if (egsTimer == 0) { // 7000ms
							egs[egsCnt] = new EnemyGroup(0, 1);
							egsCnt++;

							if (egsCnt > 9) {
								egsCnt = 0;
							}
							egsTimer = 2000;
						}

						// timer variables
						egsUpdateTimer--;
						egsTimer--;
						kidTimer--;

					} // windowsIndex ==2 end

					Thread.sleep(7); // �� 144������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}
		}).start();
	}

//	// su
//	public void isCrush(Missile o) { // �� �̻��ϸ��� üũ�ҰŶ� �Ű����� �̻��� �־���
//		int mx = o.getX() / 40;// �̻����� x��ǥ�� 40���� ������, ���� �̻����� �ִ� ĭ ��
//		int my = o.getY();// �̻����� y�߽���ǥ���� ���� 20�� ���༭ �̹��� ��ǥ ������ y�� ����
//
//		for (int i = 0; i < egs.length; i++) { // �ش� �̻����� ��ǥ�� ���� �� �׷�� ������� ����
//			if (egs[i] != null) {
//				if (my == egs[i].getGy() + 20) { // y��ǥ���� �ش� �� �׷��� yǥ�� ���ؼ� ������ �Ʒ�����
//
//					// egX�� �� �׷�ȿ� �ִ� ù��° ���� x��ǥ�� �� ���׷��� ��ĭ �з��ִ��� ����
//
//					int egX = egs[i].getEnemyGroup().get(0).getX() / 40;
//
//					// enemyHp�� ���׷�ȿ��� index�� x + egX�� ���� hp���� �����´�
//					/*
//					 * (0�����ͽ���) ���࿡ �̻����� x��ǥ�� 45 ��� �ϸ� 40���� �������� 1 ù��° ĭ�� �ִٰ� ����.(�̶� x : 1) ����
//					 * 4��¥���� ���׷��� �̹� �������� ��ĭ(0)�̵� �� ���¶�� (�����غ��� ���׷��� 0��° ���� ü���� �𿩾���) �� �� �׷���
//					 * 0��° �� x��ǥ�� 40�ϰŰ� �̰��� 40���� ������ 1�̴ϱ� egX �� 1�� ��. xĭ�� �ִ� ���� ���� egsIndex�� ���Ϸ���
//					 * x-egX�� ���ָ�ȴ� case 1 x-egX > 0 ũ����~ egsIndex = x-egX case 2 x=egX >> ũ����~
//					 * egsIndex = 0 case 3 x-egX < 0 ũ���� �ƴ�
//					 */
//					int egsIndex = mx - egX;
//
//					if (egsIndex >= 0 && egsIndex < egs[i].getEnemyGroup().size()) {
//						int enemyHp = egs[i].getEnemyGroup().get(egsIndex).getHp();
//						int enemyHpSum = enemyHp - o.getAtk();
//						egs[i].getEnemyGroup().get(egsIndex).setHp(enemyHpSum);
//					}
//
//				}
//			}
//
//		}
//	}

	@Override
	public void update(Graphics g) {
		paint(g);

		if (windowsIndex == 2 && kidTimer <= 0) { // ���� ���۽ÿ� ĳ������ �̵��� �۵���Ŵ
			if (leftPress == true & rightPress != true) {
				kid.move(Direction.LEFT);

			}

			if (leftPress != true & rightPress == true) {
				kid.move(Direction.RIGHT);
			}
//			if (attackPress == true)
//				fire(attackPress);
			kidTimer = 15;
		}
	}

	@Override
	public void paint(Graphics g) {
		Image bufImage = createImage(this.getWidth(), this.getHeight()); // ���� �̹����� ĵ������ ����
		Graphics g2 = bufImage.getGraphics(); // g2�� ���� �̹����� �׷��Ȱ��� �Է�

		bg.draw(g2, this); // g2�� �ִ� �����̹����� �׸� (���)
		title.draw(g2, this);
		difficulty.draw(g2, this);

		if (windowsIndex == 2) {
			kid.draw(g2, this); // g2�� �ִ� �����̹����� �׸�

			for (Missile o : missiles) {
				o.draw(g2, this);
			}

			for (int i = 0; i < egs.length; i++) {
				if (egs[i] != null) {
					egs[i].draw(g2, this);
				}
			}
		}

		g.drawImage(bufImage, 0, 0, this); // ��� ��ü�� �� �׸� �����̹����� ĵ������ �ѹ��� �����
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

		// System.out.println("windowsIndex : " + windowsIndex); // �׽�Ʈ��
		// System.out.println("hIndex : " + title.nextMenu()); // �׽�Ʈ��

		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:

			if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.LEFT);
			}
			if (windowsIndex == 2) { // ������ �̵�Ű
				leftPress = true;
			}
			break;

		case KeyEvent.VK_UP:

			if (windowsIndex == 0) { // ����ȭ��
				title.move(Direction.UP);
			}
			if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.UP);
			}
			break;

		case KeyEvent.VK_RIGHT:

			if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.RIGHT);
			}
			if (windowsIndex == 2) { // ������ �̵�Ű
				rightPress = true;
			}
			break;

		case KeyEvent.VK_DOWN:

			if (windowsIndex == 0) { // ����ȭ��
				title.move(Direction.DOWN);
			}
			if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.DOWN);
			}
			break;

		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			if (windowsIndex == 0) { // ����ȭ�� ���ù�ư
				if (title.nextMenu() == 0) { // ���ӽ��۽� ���̵�����â���ΰ�
					windowsIndex = 1;
					btxMove1 = 100;
					btxMove2 = 200;
					break;
				} else // ��������� ������ ����
				{
					title.move(Direction.SELECT);
				}
				break;
			}

			if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.SELECT);
				if (difficulty.gameStart != true) {
					gameStart(); // ���ӽ��۽� ������ �����ϰԵ�
					btxMove2 = 200;
				}
				break;
			}

			if (windowsIndex == 2) { // ���ݹ�ư
//				kid.attack(Direction.SELECT);
				Missile m = kid.attack();
				if (m != null) {
					missiles.add(m);
				}
				kid.move(Direction.SELECT);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { // Ű�� ���� �۵�
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			leftPress = false; // �̵�Ű�� ���� �̵��� ����
			break;

		case KeyEvent.VK_RIGHT:
			rightPress = false; // �̵�Ű�� ���� �̵��� ����
			break;

		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/*
	 * ���� �Լ��� ���� �����ߵ�
	 * 
	 * // private void fire(boolean sw) { // if (missiles.size() <
	 * difficulty.getMissileStack()) { // Missile m = kid.attack(5); // ��ȣ�� ���� ����
	 * ���ݱ��� �ʿ��� �����Ӽ� // if (m != null) { // missiles.add(m); // } // } // }
	 * 
	 */

	private void gameStart() {
		windowsIndex = 2;
		score = 2 * difficulty.scoreMagnification(); // ���� ���۽� ���̵����� ������ ����ŭ ���ھ� ������ ������
	}

	private void scoreUp() { // �̱���
	}
}
