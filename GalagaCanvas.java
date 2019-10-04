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

	private Character kid; // 캐릭터를 불러옴
	private BackGround bg; // 배경화면을 불러옴
	private List<Missile> missiles; // 총알값을 불러옴 (배열)

	private boolean leftPress; // 왼쪽키를 누르고있는지 확인
	private boolean rightPress; // 오른쪽키를 누르고있는지 확인

	private int scoreTotal; // 총점을 표기함
	private int score; // 점수 계산을위한 변수
	private int windowsIndex; // 창 전환을 실행할 변수

	private Title title; // 메인화면
	private Difficulty difficulty; // 난이도 설정창
	private ScoreDisplay scDisp;

	private EndingController endController;
	private int ecTimer;

	int btxMove1;
	int btxMove2;

	private int kidTimer;

	private EnemyGroup[] egs;// su - 블럭 적군들 블럭그룹 배열
	private int egsCnt;// su - egs 배열 index 카운트
	private int egsTimer;// su - 적군생성 타이머
	private int egsUpdateTimer;// su - 블럭 그룹 업데이트 타이머

	public GalagaCanvas() {
		addMouseListener(this);
		addKeyListener(this);

		leftPress = false;
		rightPress = false;

		windowsIndex = 0;
		scoreTotal = 0;
		btxMove1 = 0;
		btxMove2 = 0;

		bg = new BackGround();
		title = new Title();
		difficulty = new Difficulty();

		init();

		new Thread(() -> { // 서브 쓰레드
			while (true) {
				try {
					kid.update(); // 캐릭터 업데이트
					scDisp.update(); // score 표시

					if (btxMove1 != 0) {
						title.update();
						btxMove1--;
					}
					if (btxMove2 != 0) {
						difficulty.update();
						btxMove2--;
					}

					if (windowsIndex == 2) { // 게임 시작시 작동
						
						for(Missile m : missiles)
						{
							m.update();
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
													// System.out.println("egs : " + j + " bullet : " + i + " Hit!!!");
													missiles.remove(i);
													scDisp.scoreUp(this.score);
													break; // 미사일이 삭제되면 바로 egs가 있는 for문을 빠져 나가도록 해야함
												}
											}
										}
									}
								}

							}
						}

						// su - EnemyGroup 배열 egs의 update반복
						if (egsUpdateTimer == 0) { // 700ms
							for (int i = 0; i < egs.length; i++) {

								if (egs[i] != null) {
									egs[i].update();

									if (egs[i].getGy() >= 680) {
										// y값 바닥에 닿을 때 null 값 주기
										egs[i] = null;
										kid.minusMaxHp();
									}

								}
							}

							egsUpdateTimer = 100;
						}

						// su - egsTimer타이머 설정대로 블럭그룹 생성
						if (egsTimer == 0) { // 7000ms
							egs[egsCnt] = new EnemyGroup(0, 1);
							egsCnt++;

							if (egsCnt > 9) {
								egsCnt = 0;
							}
							egsTimer = 2000;
						}

						if (kid.getHp() <= 0) {
							windowsIndex = 3;
							ecTimer = 100;
						}

						// update timer variables
						egsUpdateTimer--;
						egsTimer--;
						kidTimer--;

					} // windowsIndex ==2 end
					else if (windowsIndex == 3) {
						ecTimer--;
					} else {
					}

					Thread.sleep(7); // 약 144프레임

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}
		}).start();
	}

	private void init() {
		kid = new Character();
		kidTimer = 0;

		missiles = Collections.synchronizedList(new ArrayList<Missile>());
		scDisp = new ScoreDisplay();

		endController = new EndingController();
		ecTimer = 0;

		egs = new EnemyGroup[10]; // su - EnemyGroup 배열
		egsCnt = 0; // su - 배열카운트 초기화

		egsTimer = 0; // su - 타이머 초기화
		egsUpdateTimer = 0;// su - 타이머 초기화
	}

	@Override
	public void update(Graphics g) {
		paint(g);

		if (windowsIndex == 2 && kidTimer <= 0) { // 게임 시작시에 캐릭터의 이동을 작동시킴
			if (leftPress == true & rightPress != true) {
				kid.move(Direction.LEFT);

			}

			if (leftPress != true & rightPress == true) {
				kid.move(Direction.RIGHT);
			}

			kidTimer = 15;
		}

		if (windowsIndex == 3) {
			if (ecTimer <= 0) {
				ecTimer = 0;
				endController.setRestartFlag(true);
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Image bufImage = createImage(this.getWidth(), this.getHeight()); // 버퍼 이미지에 캔버스를 생성
		Graphics g2 = bufImage.getGraphics(); // g2에 버퍼 이미지의 그래픽값을 입력

		bg.draw(g2, this); // g2에 있는 버퍼이미지에 그림 (배경)
		title.draw(g2, this);
		difficulty.draw(g2, this);

		if (windowsIndex == 2) {
			kid.draw(g2, this); // g2에 있는 버퍼이미지에 그림
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
		} else {
		}

		g.drawImage(bufImage, 0, 0, this); // 모든 객체를 다 그린 버퍼이미지를 캔버스에 한번에 출력함
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

		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:

			if (windowsIndex == 1) { // 난이도 설정창
				difficulty.move(Direction.LEFT);
			}
			if (windowsIndex == 2) { // 게임중 이동키
				leftPress = true;
			}
			break;

		case KeyEvent.VK_UP:

			if (windowsIndex == 0) { // 메인화면
				title.move(Direction.UP);
			}
			if (windowsIndex == 1) { // 난이도 설정창
				difficulty.move(Direction.UP);
			}
			if (windowsIndex == 3) {
				endController.move(Direction.UP);
			}
			break;

		case KeyEvent.VK_RIGHT:

			if (windowsIndex == 1) { // 난이도 설정창
				difficulty.move(Direction.RIGHT);
			}
			if (windowsIndex == 2) { // 게임중 이동키
				rightPress = true;
			}
			break;

		case KeyEvent.VK_DOWN:

			if (windowsIndex == 0) { // 메인화면
				title.move(Direction.DOWN);
			}
			if (windowsIndex == 1) { // 난이도 설정창
				difficulty.move(Direction.DOWN);
			}
			if (windowsIndex == 3) {
				endController.move(Direction.DOWN);
			}
			break;

		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			if (windowsIndex == 0) { // 메인화면 선택버튼
				if (title.nextMenu() == 0) { // 게임시작시 난이도설정창으로감
					windowsIndex = 1;
					btxMove1 = 100;
					btxMove2 = 200;
					break;
				} else // 게임종료시 게임이 꺼짐
				{
					title.move(Direction.SELECT);
				}
				break;
			}

			if (windowsIndex == 1) { // 난이도 설정창
				difficulty.move(Direction.SELECT);
				if (difficulty.gameStart != true) {
					gameStart(); // 게임시작시 게임이 시작하게됨
					btxMove2 = 200;
				}
				break;
			}

			if (windowsIndex == 2) { // 공격버튼
				Missile m = kid.attack();
				if (m != null) {
					missiles.add(m);
				}
				kid.move(Direction.SELECT);
			}

			if (windowsIndex == 3) {
				int endSel = endController.getEndSel();
				if (endSel == 0) {
					// restart
					windowsIndex = 2;
					init();
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
	public void keyReleased(KeyEvent e) { // 키를 땔시 작동
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			leftPress = false; // 이동키를 떼면 이동이 멈춤
			break;

		case KeyEvent.VK_RIGHT:
			rightPress = false; // 이동키를 떼면 이동이 멈춤
			break;

		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void gameStart() {
		windowsIndex = 2;
		score = 2 * difficulty.scoreMagnification(); // 게임 시작시 난이도에서 설정한 값만큼 스코어 배율을 조절함
	}

}
