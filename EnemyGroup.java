package mini_galaga;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnemyGroup {
	private List<Enemy> enemies; // 블럭 배열 생성
	private int rand; // 랜덤값 설정
	private int vx; // 움직이는 단위
	private int vy; // 움직이는 단위

	private int attackSpeed; // 움직임 스피드
	private int gy; // 해당 그룹 y 좌표값

	private boolean drecSwi; // 블럭 방향 조절키
	private boolean xSwi; // 블럭 벽에 닿는 인식 키

	public EnemyGroup() {
		this(0, 1);
	}

	public EnemyGroup(int y, int speed) {
		rand = (int) (Math.random() * 5) + 1; // 최소 1개 이상 랜덤값 설정

		enemies = Collections.synchronizedList(new ArrayList<Enemy>());

		// 동기화되는 리스트 설정
		for (int i = 0; i < rand; i++) { // 랜덤값만큼 배열에 블럭 담아주기
			enemies.add(new Enemy(i * 40, y)); // 각 객체생성마다 초기 x좌표를 너비 40를 갯수에 곱해서 설정
		}

		gy = enemies.get(0).getY();// 그룹 y좌표를 첫번째 블럭 y좌표로 초기설정
		attackSpeed = speed; // 움직임 스피드 설정
		vx = 40; // x좌표로 움직이는 단위
		vy = 40; // y좌표로 움직이는 단위
		xSwi = true; // 실행시 블럭 자동 x좌표 이동
	}

	public List<Enemy> getEnemyGroup() { // 블럭 배열 getter
		if (enemies.size() > 0) {
			return enemies;
		} else
			return null;
	}

	public int getGy() {// 그룹 y좌표 getter
		return this.gy; // 현재 그룹 y좌표
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

	public void moveUpdate() { // 매 프레임당 업데이트 호출		
		move();
	}
	
	
	public void brokenUpdate() {
		brokenCheck();
		brokenRemove();
	}
	

	public boolean isCrush(Missile o) {		
		boolean ret = false;

		if ((!enemies.isEmpty()) && (o != null)) {
			
			int mx = o.getX() / 40; // 미사일의 x좌표를 40으로 나누어, 현재 미사일이 있는 칸 수
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
			
			// hp가 0인 enemy의 broken을 true로 set
			if (e.getHp() <= 0) {
				e.setBroken(true);
			}
			// enemy의 broken값을 확인하면서 만약 true이면은 
			// 부서지는 애니메이션 실행
			e.isBroken();
		}
	}
	
	
	public void brokenRemove() {
		for (int i = 0; i < enemies.size(); i++) {
			// enemy의 부서지는 애니메이션이 끝났는지 확인
			if (enemies.get(i).getBrokenrm()) {
				enemies.remove(i);

				// 재배열은 적 블록 갯수가 2개 이상이고 enemy group의 첫번째 블록이 아닐때만
				if ((enemies.size() > 1) && (i != 0)) {
					for (int j = i; j < enemies.size(); j++) {
						// 제거된 배열 index부터 차례로 x좌표 한칸(40) 당기기
						enemies.get(j).setX(enemies.get(j).getX() - 40);
					}
				}
			}
		}
	}
	
	
	
	public void move() {
		if (xSwi) { // 키값이 true일 경우 블럭 x좌표 이동

			xmove();// 자동 x좌표 이동 함수 호출

			if (!enemies.isEmpty()) {
				if (enemies.get(0).getX() <= 20) {// 블럭이 왼쪽 벽에 부딪힐 경우
					xSwi = false;// y좌표 이동을 위해 키값 변경
					drecSwi = false;// x좌표 움직임 방향을 바꿔준다

				} else if (enemies.get((enemies.size() - 1)).getX() >= 460) {// 블럭이 오른쪽 벽에 부딪힐 경우
					xSwi = false;// y좌표 이동을 위해 키값 변경
					drecSwi = true;// x좌표 움직임 방향을 바꿔준다
				}
			}
		} else {
			ymove();// 키값이 false일 경우 블럭 y좌표 이동
		}
	}

	public void xmove() { // 블럭 x좌표 이동 함수

		if (drecSwi) {// drecSwi값이 true일경우는 블럭이 왼쪽으로 가고 있는 경우

			for (Enemy e : enemies) {
				int dx = e.getX() - (vx * attackSpeed);// 블럭 객체들을 -vx만큼 이동
				e.setX(dx);
			}
		} else {// drecSwi값이 false일경우는 블럭이 오른쪽으로 가고 있는 경우

			for (Enemy e : enemies) {
				int dx = e.getX() + (vx * attackSpeed);// 블럭 객체들을 vx만큼 이동
				e.setX(dx);
			}
		}
	}

	public void ymove() {// 블럭 y좌표 이동 함수
		for (Enemy e : enemies) {
			int dy = e.getY() + vy;
			e.setY(dy);
			gy = dy; // 블럭 배열의 y
		}

		xSwi = true;// 블럭 y좌표 이동후 다시 x좌표 이동을 위해 키값 변경
	}

}
