package mini_galaga;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalagaCanvas extends Canvas implements KeyListener, MouseListener {

	private Character kid; // ĳ���͸� �ҷ���
	private boolean kidInitFlag;
	
	private BackGround bg; // ���ȭ���� �ҷ���
	private List<Missile> missiles; // �Ѿ˰��� �ҷ��� (�迭)

	private boolean leftPress; // ����Ű�� �������ִ��� Ȯ��
	private boolean rightPress; // ������Ű�� �������ִ��� Ȯ��

	private int score; // ���� ��������� ����
	private int windowsIndex; // â ��ȯ�� ������ ����

	private Title title; // ����ȭ��
	private Difficulty difficulty; // ���̵� ����â
	private ScoreDisplay scDisp;

	private EndingController endController;

	int btxMove1;
	int btxMove2;


	private EnemyGroup[] egs; // su - �� ������ ���׷� �迭
	private int egsCnt; // su - egs �迭 index ī��Ʈ
	private static final int maxEgsCnt = 100;
	
	private int gameTimer;
	
	private static final int maxEgsUpdateTimer = 100;
	private static final int maxEbTimer = 10;
	private static final int maxEgsTimer = 1000;
	private static final int maxKidTimer = 15;
	private static final int maxKidBulletTimer = 200;	
	private static final int maxEcTimer = 100;
	

	public GalagaCanvas() {
		addMouseListener(this);
		addKeyListener(this);		
		
		/*
		 * * initialization sequence * *
		 *             ��
		 *   setting init : initialize background, title, game leve of difficulty
		 *             ��
		 *   playing init : character, missile, enemy group 
		 */
		settingInit();
		playingInit();

		new Thread(() -> { // ���� ������
			while (true) {
				try {
					kid.update(); // ĳ���� ������Ʈ
					scDisp.update(); // score ǥ��

					if (btxMove1 != 0) {
						title.update();
						btxMove1--;
					}
					if (btxMove2 != 0) {
						difficulty.update();
						btxMove2--;
					}

					if (windowsIndex == 2) { // ���� ���۽� �۵�
						
						/*
						 * ���� ���� �� ĳ���Ϳ� 
						 * �ִ� ü�°� �ִ� �Ѿ� ������
						 * ������ �ش�
						 */
						if(!kidInitFlag)
						{
							kid.setMaxHp(Difficulty.healthPoint);
							kid.setBulletNum(Difficulty.missileStack);
							kidInitFlag = true;
						}
						
						for(Missile m : missiles)
						{
							if(m != null)
							{
								m.update();
							}
						}
						
						
						for (int i = 0; i < missiles.size(); i++) {
							if (missiles.get(i) != null) {

								if (missiles.get(i).getY() < 100) {
									missiles.remove(i);
								} 
								else {
									if (egsCnt > 0) {
										for (int j = 0; j < egs.length; j++) {
											if (egs[j] != null) {
												boolean retCrush = false;
												
												retCrush = egs[j].isCrush(missiles.get(i));
												if (retCrush) {
													missiles.remove(i);
													scDisp.scoreUp(this.score);
													break; // �̻����� �����Ǹ� �ٷ� egs�� �ִ� for���� ���� �������� �ؾ���
												}
											}
										}
									}
								}

							}
						}
												
						
						// su - EnemyGroup �迭 egs�� move update�ݺ�
						if (gameTimer % maxEgsUpdateTimer == 0) { // 700ms
							for (int i = 0; i < egs.length; i++) {

								if (egs[i] != null) {
									egs[i].moveUpdate();

									if (egs[i].getGy() >= 680) {
										// y�� �ٴڿ� ���� �� null �� �ֱ�
										egs[i] = null;
										kid.minusMaxHp();
									}

								}
							}

						}
						
						
						// EnemyGroup�迭 egs�� broken update �ݺ�
						// �� ���� �Ѿ� ������ ������ ����Ʈ �����ְ�
						// ����Ʈ ������ ���� �� ��迭 ����
						if (gameTimer % maxEbTimer  == 0) { // 70ms
							for (int i = 0; i < egs.length; i++) {
								if (egs[i] != null) {
									egs[i].brokenUpdate();
								}
							}
						}

						// su - egsTimerŸ�̸� ������� ���׷� ����
						if (gameTimer % maxEgsTimer  == 0) { // 7000ms
							egs[egsCnt] = new EnemyGroup(0, 1);
							egsCnt++;

							if (egsCnt >= maxEgsCnt) {
								egsCnt = 0;
							}
						}
						
						// HP�� 0�� �Ǹ� ���� ������ ����
						if (kid.getHp() <= 0) {
							windowsIndex = 3;
						}

						
						/*
						 * bullet timer�� 0�� �Ǹ� ���� ���� �Ѿ� ������ �������
						 * �Ѿ� ���� 0���� �ʱ�ȭ
						 * �׸��� �Ѿ� reload�Ѵ�
						 */
						if(gameTimer % maxKidBulletTimer  == 0 && kid.getBulletNum() <= 0)
						{
							System.out.println("Reload!!!");
							kid.reloadBullet();
						}

					}
					
					gameTimer++;
					if(gameTimer >= Integer.MAX_VALUE)
					{
						gameTimer = 0;
					}
					
					Thread.sleep(7); // �� 144������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				repaint();
			}
		}).start();
	}

	private void settingInit() {
		leftPress = false;
		rightPress = false;
		
		kidInitFlag = false;

		windowsIndex = 0;
		btxMove1 = 0;
		btxMove2 = 0;
		
		gameTimer = 0;
		
		bg = new BackGround();
		title = new Title();
		difficulty = new Difficulty();
	}

	private void playingInit() {
		kid = new Character();
		missiles = Collections.synchronizedList(new ArrayList<Missile>());
		scDisp = new ScoreDisplay();
		endController = new EndingController();		

		egs = new EnemyGroup[maxEgsCnt]; // su - EnemyGroup �迭
		egsCnt = 0; // su - �迭ī��Ʈ �ʱ�ȭ		
	}

	@Override
	public void update(Graphics g) {
		paint(g);

		if ((windowsIndex == 2) && (gameTimer % maxKidTimer == 0)) { // ���� ���۽ÿ� ĳ������ �̵��� �۵���Ŵ
			if (leftPress == true & rightPress != true) {
				kid.move(Direction.LEFT);

			}

			if (leftPress != true & rightPress == true) {
				kid.move(Direction.RIGHT);
			}
		}

		if (windowsIndex == 3) {
			if (gameTimer % maxEcTimer == 0 ) {
				endController.setRestartFlag(true);
			}
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
			scDisp.draw(g2, this);

			for (Missile o : missiles) {
				o.draw(g2, this);
			}

			for (int i = 0; i < egs.length; i++) {
				if (egs[i] != null) {
					egs[i].draw(g2, this);
				}
			}
		} else if (windowsIndex == 3) {
			endController.draw(g2, this);
		} else {}

		g.drawImage(bufImage, 0, 0, this); // ��� ��ü�� �� �׸� �����̹����� ĵ������ �ѹ��� �����
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {

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
			if (windowsIndex == 3) {
				endController.move(Direction.UP);
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
			if (windowsIndex == 3) {
				endController.move(Direction.DOWN);
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
				
				// missile�߻� �� ĳ���Ͱ� ������ �ִ� �Ѿ� ���� �ϳ� ����
				if ((kid.getBulletNum()) > 0 && (kid.getBulletNum() <= Difficulty.missileStack)) {
					Missile m = kid.attack();
					missiles.add(m);
					
					kid.minusBulletNum();
					if(kid.getBulletNum() <= 0)
					{
						kid.setBulletNum(0);
					}
				}
				
				kid.move(Direction.SELECT);
			}

			if (windowsIndex == 3) {
				int endSel = endController.getEndSel();
				
				if (endSel == 0) {
					// restart
					windowsIndex = 2;
					kidInitFlag = false;
					playingInit();
				} else if (endSel == 1) {
					System.exit(0);
				} else {
					System.out.println("Wrong Ending Selection");
				}
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
	public void keyTyped(KeyEvent e) {}

	private void gameStart() {
		windowsIndex = 2;
		score = 2 * difficulty.scoreMagnification(); // ���� ���۽� ���̵����� ������ ����ŭ ���ھ� ������ ������
	}

}
