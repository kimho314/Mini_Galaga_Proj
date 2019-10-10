package mini_galaga;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalagaCanvas extends Canvas implements KeyListener {

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
	private RankingManager rkm;

	int btxMove1;
	int btxMove2;

	private EnemyGroup[] egs; // su - �� ������ ���׷� �迭
	private int curEgsCnt; // su - egs �迭 index ī��Ʈ
	private int prevEgsCnt;
	private static final int maxEgsCnt = 100;

	private int gameTimer;

	private static int maxEgsNewTimer = 700;
	private static final int maxEbTimer = 10;

	private static final int maxKidTimer = 15;
	private static final int maxKidBulletTimer = 200;
	private static final int maxEndingTimer = 200;
	private static final int maxPrintRankingChartTimer = 700;


	public GalagaCanvas() {
		addKeyListener(this);

		/*
		 * initialization sequence
		 *         �� 
		 * setting init : initialize background, title,
		 * game leve of difficulty 
		 *          �� 
		 * playing init : character, missile, enemy group
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
						SoundManager.getInstance().loop("bgm", 2);
						/*
						 * ���� ���� �� ĳ���Ϳ� �ִ� ü�°� �ִ� �Ѿ� ������ ������ �ش�
						 */
						if (!kidInitFlag) {
							kid.setMaxHp(Difficulty.healthPoint);
							kid.setBulletNum(Difficulty.missileStack);
							kidInitFlag = true;
						}

						for (Missile m : missiles) {
							if (m != null) {
								m.update();
							}
						}

						for (int i = 0; i < missiles.size(); i++) {
							if (missiles.get(i) != null) {
								if (missiles.get(i).getY() < 100) {
									// �Ѿ� �������� ���ݷ� 10���� �ʱ�ȭ
									kid.resetAtk();
									missiles.remove(i);
								} else {
									if (curEgsCnt > 0) {
										for (int j = 0; j < egs.length; j++) {
											if (egs[j] != null) {
												boolean retCrush = false;

												retCrush = egs[j].isCrush(missiles.get(i), kid.getAtk());
												if (retCrush) {
													missiles.remove(i);
													//scDisp.scoreUp(this.score);
													// �Ѿ��� �� ���� �����ϸ�
													// �÷��̾� ���ݷ� 1�� ����
													kid.increaseAtk();
													break; // �̻����� �����Ǹ� �ٷ� egs�� �ִ� for���� ���� �������� �ؾ���
												}
											}
										}

									}
								}
							}
						}

						// suyoung maxEgsUpdateTimer ��� ����
						for (int i = 0; i < egs.length; i++) {
							if (egs[i] != null) {
								// suyoung �޼��� ����
								egs[i].timerUpdate();

								if (egs[i].getGy() >= 680) {
									// y�� �ٴڿ� ���� �� null �� �ֱ�
									egs[i] = null;
									kid.minusMaxHp();

									// sound
									if (kid.getHp() >= 1) {
										SoundManager.getInstance().play("life");
									}
								}

							}
						}
						
						
						// EnemyGroup�迭 egs�� broken update �ݺ�
						// �� ���� �Ѿ� ������ ������ ����Ʈ �����ְ�
						// ����Ʈ ������ ���� �� ��迭 ����
						if (gameTimer % maxEbTimer == 0) { // 70ms
							for (int i = 0; i < egs.length; i++) {
								if (egs[i] != null) {
									egs[i].brokenCheck();
									egs[i].brokenRemove(scDisp, score);
								}
							}
						}

						// su - egsTimerŸ�̸� ������� ���׷� ����
						if (gameTimer % maxEgsNewTimer == 0) { // 7000ms
							egs[curEgsCnt] = new EnemyGroup();// suyoung �Ű����� ����
							curEgsCnt++;

							if (curEgsCnt >= maxEgsCnt) {
								curEgsCnt = 0;
							}

						}

						// HP�� 0�� �Ǹ� ���� ������ ����
						// ���� ���ھ 99999999�� �ǵ� ���� ������ ����
						if ((kid.getHp() <= 0) || (scDisp.getScore() >= 99999999)) {
							windowsIndex = 3;
							SoundManager.getInstance().stop("bgm");
						}

						/*
						 * bullet timer�� 0�� �Ǹ� ���� ���� �Ѿ� ������ ������� �Ѿ� ���� 0���� �ʱ�ȭ �׸��� �Ѿ� reload�Ѵ�
						 */
						if ((gameTimer % maxKidBulletTimer == 0) && (kid.getBulletNum() <= 0)) {
							kid.reloadBullet();
						}

						/*
						 * ���� �ð��� ������ �� ���� �ð� ���� : 3�� �����Ǹ� 7ms���� �� ü�� ���� : 1�� �����Ǹ� 1�� ���� �� �̵� �ӵ� ���� :
						 * 5�� �����Ǹ� 7ms����
						 */
						difficultyUp();
					}

					gameTimer++;
					if (gameTimer >= Integer.MAX_VALUE) {
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

		SoundManager.getInstance().play("intro");
	}

	private void playingInit() {
		kid = new Character();
		missiles = Collections.synchronizedList(new ArrayList<Missile>());
		scDisp = new ScoreDisplay();
		endController = new EndingController();

		egs = new EnemyGroup[maxEgsCnt]; // su - EnemyGroup �迭
		curEgsCnt = 0; // su - �迭ī��Ʈ �ʱ�ȭ
		prevEgsCnt = 0;

		rkm = new RankingManager();
		rkm.loadRanking();
	}


	public void difficultyUp() {
		int diffEgsCnt = curEgsCnt - prevEgsCnt;

		if (diffEgsCnt < 0) {
			diffEgsCnt = maxEgsCnt - diffEgsCnt;
		}

		if (diffEgsCnt == 1) {
			System.out.println("Enemy Hp up!!!");
			Enemy.DEFAULT_ENEMY_HP++;
			
			if (curEgsCnt % 3 == 0) {
				if ((maxEgsNewTimer >= 400) && (maxEgsNewTimer <= 700)) {
					maxEgsNewTimer--;
				}
			} else if (curEgsCnt % 5 == 0) {// suyoung EnemyGroup �� Ÿ�̸� basicTime�� ����
				if ((EnemyGroup.basicTime >= 50) && (EnemyGroup.basicTime <= 60)) {
					EnemyGroup.basicTime--;
				}
			} else {}
			
			System.out.println(maxEgsNewTimer + ", " + EnemyGroup.basicTime);
		}

		prevEgsCnt = curEgsCnt;

	}

	@Override
	public void update(Graphics g) {
		paint(g);

		if ((windowsIndex == 2) && (gameTimer % maxKidTimer == 0)) { // ���� ���۽ÿ� ĳ������ �̵��� �۵���Ŵ
			if (leftPress == true & rightPress != true) {
				kid.move(Direction.LEFT);
			}

			if ((leftPress != true) && (rightPress == true)) {
				kid.move(Direction.RIGHT);
			}
		}

		if (windowsIndex == 3) {
			if (gameTimer % maxEndingTimer == 0) {
				if (rkm.isScoreHigh(scDisp.getScore())) {
					windowsIndex++;
				} else {
					endController.setRestartFlag(true);
					windowsIndex = 5;
				}
			}
		}

		if (windowsIndex == 4) {
			int rkmDrawSwitchState = rkm.getDrawSwitchState();
			if (rkmDrawSwitchState == 1) {
				if (gameTimer % maxPrintRankingChartTimer == 0) {
					rkm.setDrawSwitchState(2);

				}
			} else if (rkmDrawSwitchState == 2) {
				endController.setRestartFlag(true);
				windowsIndex++; // = 5
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
		} else if (windowsIndex == 3 || windowsIndex == 5) {
			endController.draw(g2, this);
		} else if (windowsIndex == 4) {
			rkm.draw(g2, this);
		} else {
		}

		g.drawImage(bufImage, 0, 0, this); // ��� ��ü�� �� �׸� �����̹����� ĵ������ �ѹ��� �����
	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.LEFT);
				SoundManager.getInstance().play("menu");
			} else if (windowsIndex == 2) { // ������ �̵�Ű
				leftPress = true;
			} else {
			}
			break;

		case KeyEvent.VK_UP:
			if (windowsIndex == 0) { // ����ȭ��
				title.move(Direction.UP);
			} else if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.UP);
				SoundManager.getInstance().play("menu");
			} else if (windowsIndex == 4) {
				rkm.move(Direction.UP);
			} else if (windowsIndex == 5) {
				endController.move(Direction.UP);
				SoundManager.getInstance().play("menu");
			} else {
			}
			break;

		case KeyEvent.VK_RIGHT:
			if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.RIGHT);
				SoundManager.getInstance().play("menu");
			} else if (windowsIndex == 2) { // ������ �̵�Ű
				rightPress = true;
			} else if (windowsIndex == 4) {
				rkm.move(Direction.RIGHT);
			} else {
			}
			break;

		case KeyEvent.VK_DOWN:
			if (windowsIndex == 0) { // ����ȭ��
				title.move(Direction.DOWN);
			} else if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.DOWN);
				SoundManager.getInstance().play("menu");
			} else if (windowsIndex == 5) {
				endController.move(Direction.DOWN);
				SoundManager.getInstance().play("menu");
			} else if (windowsIndex == 4) {
				rkm.move(Direction.DOWN);
			} else {
			}
			break;

		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			if (windowsIndex == 0) { // ����ȭ�� ���ù�ư
				if (title.nextMenu() == 0) { // ���ӽ��۽� ���̵�����â���ΰ�
					windowsIndex = 1;
					btxMove1 = 100;
					btxMove2 = 200;
					SoundManager.getInstance().stop("intro");
					break;
				} else // ��������� ������ ����
				{
					title.move(Direction.SELECT);
				}
				break;
			} else if (windowsIndex == 1) { // ���̵� ����â
				difficulty.move(Direction.SELECT);
				SoundManager.getInstance().play("menu");

				if (Difficulty.gameStart != true) {
					gameStart(); // ���ӽ��۽� ������ �����ϰԵ�
					btxMove2 = 200;
				}
				break;
			} else if (windowsIndex == 2) { // ���ݹ�ư
				// missile�߻� �� ĳ���Ͱ� ������ �ִ� �Ѿ� ���� �ϳ� ����
				if ((kid.getBulletNum()) > 0 && (kid.getBulletNum() <= Difficulty.missileStack)) {
					Missile m = kid.attack();
					missiles.add(m);
					SoundManager.getInstance().play("fire");
					kid.minusBulletNum();

					if (kid.getBulletNum() <= 0) {
						kid.setBulletNum(0);
					}
				}

				kid.move(Direction.SELECT);
			} else if (windowsIndex == 4) {
				if (rkm.getRankingNextIdx() >= 2) {
					if (rkm.getDrawSwitchState() == 0) {
						rkm.updateRanking();
						rkm.storeRanking();
						rkm.printRankingData();
					}
				}
			} else if (windowsIndex == 5) {
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
			} else {
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
