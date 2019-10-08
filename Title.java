package mini_galaga;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Title {
	private int hIndex; // 어떤 버튼을 선택중인지 확인할 변수
	
	private int btnX; // 이미지의 기본 x값
	private int btnY; // 이미지의 기본 y값
	
	private Toolkit tk;
	private Image start; // 게임시작 버튼
	private Image exit; // 게임종료 버튼
	private Image arrow; // 버튼 옆에 무엇을 선택했는지 알려주는 화살표

	public Title() {
		hIndex = 0;
		btnX = 189;
		btnY = 352;

		tk = Toolkit.getDefaultToolkit();
		start = tk.getImage("res/btn_start.png");
		exit = tk.getImage("res/btn_end.png");
		arrow = tk.getImage("res/ico_arr.png");
	}

	public void update() {// 창이 나오고 사라지는걸 애니메이션화
		btnX -= 4;
	}

	public void move(Direction direction) {
		switch (direction) {
		case UP:
			if (hIndex == 1) {
				hIndex--;
			}
			break;
			
		case DOWN:
			if (hIndex == 0) {
				hIndex++;
			}
			break;
			
		case SELECT:
			if (hIndex == 1) {
				gameExit();
			}
			break;
		}
	}

	public void draw(Graphics g, GalagaCanvas roleCanvas) {
		if (hIndex == 0) { // 게임시작
			g.drawImage(start, btnX, btnY, btnX + 102, btnY + 24, 0, 24, 102, 48, roleCanvas);
		} else {
			g.drawImage(start, btnX, btnY, btnX + 102, btnY + 24, 0, 0, 102, 24, roleCanvas);
		}
		
		if (hIndex == 1) { // 게임종료
			g.drawImage(exit, btnX, btnY + 30, btnX + 102, btnY + 54, 0, 24, 102, 48, roleCanvas);
		} else {
			g.drawImage(exit, btnX, btnY + 30, btnX + 102, btnY + 54, 0, 0, 102, 24, roleCanvas);
		}
		
		g.drawImage(arrow, btnX - 30, btnY + (hIndex * 30), btnX - 12, btnY + 24 + (hIndex * 30), 0, 0, 11, 17,
				roleCanvas);
	}

	private void gameExit() { // 게임종료 버튼
		System.exit(0);
	}

	public int nextMenu() { // 게임시작 버튼
		return hIndex;
	}
}
