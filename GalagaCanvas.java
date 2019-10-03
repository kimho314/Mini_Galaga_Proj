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

		kid = new Character();
		bg = new BackGround();
		missiles = new ArrayList<Missile>();
		title = new Title();
		difficulty = new Difficulty();

		kidTimer = 0;

		egsTimer = 0; // su - 타이머 초기화
		egsUpdateTimer = 0;// su - 타이머 초기화

		egsCnt = 0; // su - 배열카운트 초기화
		egs = new EnemyGroup[10]; // su - EnemyGroup 배열

		new Thread(() -> { // 서브 쓰레드
			while (true) {
				try {
					kid.update(); // 캐릭터 업데이트
					bg.update(); // 배경화면 업데이트 (필요없을시 지울예정)

					if (btxMove1 != 0) {
						title.update();
						btxMove1--;
					}
					if (btxMove2 != 0) {
						difficulty.update();
						btxMove2--;
					}

					if (windowsIndex == 2) { // 게임 시작시 작동

						for (Missile o : missiles) {
							o.update();
						}

						for (int i = 0; i < missiles.size(); i++) {
							if (missiles.get(i) != null) {

								if (missiles.get(i).getY() < 100) {
									missiles.remove(i);
								} else {
									// isCrush(missiles.get(i)); // su - 해당 미사일의 충돌여부 확인
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

						// timer variables
						egsUpdateTimer--;
						egsTimer--;
						kidTimer--;

					} // windowsIndex ==2 end

					Thread.sleep(7); // 약 144프레임
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}
		}).start();
	}

//	// su
//	public void isCrush(Missile o) { // 각 미사일마다 체크할거라 매개변수 미사일 넣어줌
//		int mx = o.getX() / 40;// 미사일의 x좌표를 40으로 나누어, 현재 미사일이 있는 칸 수
//		int my = o.getY();// 미사일의 y중심좌표에서 절반 20을 빼줘서 이미지 좌표 끝으로 y값 설정
//
//		for (int i = 0; i < egs.length; i++) { // 해당 미사일의 좌표랑 비교할 블럭 그룹들 갯수대로 실행
//			if (egs[i] != null) {
//				if (my == egs[i].getGy() + 20) { // y좌표값과 해당 블럭 그룹의 y표를 비교해서 같으면 아래실행
//
//					// egX는 블럭 그룹안에 있는 첫번째 블럭의 x좌표로 이 블럭그룹이 몇칸 밀려있는지 저장
//
//					int egX = egs[i].getEnemyGroup().get(0).getX() / 40;
//
//					// enemyHp는 블럭그룹안에서 index가 x + egX인 블럭의 hp값을 가져온다
//					/*
//					 * (0번부터시작) 만약에 미사일의 x좌표가 45 라고 하면 40으로 나눴을때 1 첫번째 칸에 있다고 생각.(이때 x : 1) 블럭이
//					 * 4개짜리인 블럭그룹이 이미 왼쪽으로 한칸(0)이동 된 상태라면 (생각해보면 블럭그룹의 0번째 블럭의 체력이 깎여야함) 이 블럭 그룹의
//					 * 0번째 블럭 x좌표가 40일거고 이것을 40으로 나누면 1이니까 egX 는 1이 됨. x칸에 있는 블럭의 순서 egsIndex를 구하려면
//					 * x-egX를 해주면된다 case 1 x-egX > 0 크러쉬~ egsIndex = x-egX case 2 x=egX >> 크러쉬~
//					 * egsIndex = 0 case 3 x-egX < 0 크러쉬 아님
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

		if (windowsIndex == 2 && kidTimer <= 0) { // 게임 시작시에 캐릭터의 이동을 작동시킴
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
		Image bufImage = createImage(this.getWidth(), this.getHeight()); // 버퍼 이미지에 캔버스를 생성
		Graphics g2 = bufImage.getGraphics(); // g2에 버퍼 이미지의 그래픽값을 입력

		bg.draw(g2, this); // g2에 있는 버퍼이미지에 그림 (배경)
		title.draw(g2, this);
		difficulty.draw(g2, this);

		if (windowsIndex == 2) {
			kid.draw(g2, this); // g2에 있는 버퍼이미지에 그림

			for (Missile o : missiles) {
				o.draw(g2, this);
			}

			for (int i = 0; i < egs.length; i++) {
				if (egs[i] != null) {
					egs[i].draw(g2, this);
				}
			}
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

		// System.out.println("windowsIndex : " + windowsIndex); // 테스트용
		// System.out.println("hIndex : " + title.nextMenu()); // 테스트용

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

	/*
	 * 공격 함수는 새로 만들어야됨
	 * 
	 * // private void fire(boolean sw) { // if (missiles.size() <
	 * difficulty.getMissileStack()) { // Missile m = kid.attack(5); // 괄호안 값은 다음
	 * 공격까지 필요한 프레임수 // if (m != null) { // missiles.add(m); // } // } // }
	 * 
	 */

	private void gameStart() {
		windowsIndex = 2;
		score = 2 * difficulty.scoreMagnification(); // 게임 시작시 난이도에서 설정한 값만큼 스코어 배율을 조절함
	}

	private void scoreUp() { // 미구현
	}
}
