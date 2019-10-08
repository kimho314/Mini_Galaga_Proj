package mini_galaga;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Title {
	private int hIndex; // � ��ư�� ���������� Ȯ���� ����
	
	private int btnX; // �̹����� �⺻ x��
	private int btnY; // �̹����� �⺻ y��
	
	private Toolkit tk;
	private Image start; // ���ӽ��� ��ư
	private Image exit; // �������� ��ư
	private Image arrow; // ��ư ���� ������ �����ߴ��� �˷��ִ� ȭ��ǥ

	public Title() {
		hIndex = 0;
		btnX = 189;
		btnY = 352;

		tk = Toolkit.getDefaultToolkit();
		start = tk.getImage("res/btn_start.png");
		exit = tk.getImage("res/btn_end.png");
		arrow = tk.getImage("res/ico_arr.png");
	}

	public void update() {// â�� ������ ������°� �ִϸ��̼�ȭ
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
		if (hIndex == 0) { // ���ӽ���
			g.drawImage(start, btnX, btnY, btnX + 102, btnY + 24, 0, 24, 102, 48, roleCanvas);
		} else {
			g.drawImage(start, btnX, btnY, btnX + 102, btnY + 24, 0, 0, 102, 24, roleCanvas);
		}
		
		if (hIndex == 1) { // ��������
			g.drawImage(exit, btnX, btnY + 30, btnX + 102, btnY + 54, 0, 24, 102, 48, roleCanvas);
		} else {
			g.drawImage(exit, btnX, btnY + 30, btnX + 102, btnY + 54, 0, 0, 102, 24, roleCanvas);
		}
		
		g.drawImage(arrow, btnX - 30, btnY + (hIndex * 30), btnX - 12, btnY + 24 + (hIndex * 30), 0, 0, 11, 17,
				roleCanvas);
	}

	private void gameExit() { // �������� ��ư
		System.exit(0);
	}

	public int nextMenu() { // ���ӽ��� ��ư
		return hIndex;
	}
}
