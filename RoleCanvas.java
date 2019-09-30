package bomb;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class RoleCanvas extends Canvas implements KeyListener, MouseListener {

	private ĳ���� kid;
	private ���ȭ�� bg;
	private List<����ü> missiles;
	private boolean fireSw;
	private boolean leftPress;
	private boolean rightPress;
	private boolean attackPress;

	public RoleCanvas() {
		addMouseListener(this);
		addKeyListener(this);
		fireSw = false;
		leftPress = false;
		rightPress = false;
		attackPress = false;

		kid = new ĳ����();
		bg = new ���ȭ��();
		missiles = new ArrayList<����ü>();

		new Thread(() -> {
			while (true) {
				try {
					kid.update();
					bg.update();
					for (����ü o : missiles)
						o.update();
					for (int i = 0; i < missiles.size(); i++) {
						if (missiles.get(i) != null) {
							if (missiles.get(i).getY() < -45) {
								missiles.remove(i);
							}
						}
					}
					Thread.sleep(7); // �� 144������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}
		}).start();
	}

	@Override
	public void update(Graphics g) {
		paint(g);
		if (leftPress == true & rightPress != true)
			kid.move(Direction.LEFT);
		if (leftPress != true & rightPress == true)
			kid.move(Direction.RIGHT);
		if (attackPress == true)
			fireSw = true;
	}

	@Override
	public void paint(Graphics g) {
		Image bufImage = createImage(this.getWidth(), this.getHeight()); // ���� �̹����� ĵ������ ����
		Graphics g2 = bufImage.getGraphics(); // g2�� ���� �̹����� �׷��Ȱ��� �Է�

		bg.draw(g2, this); // g2�� �ִ� �����̹����� �׸� (���)
		for (����ü o : missiles)
			o.draw(g2, this);
		kid.draw(g2, this); // g2�� �ִ� �����̹����� �׸�
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

		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
		case KeyEvent.VK_NUMPAD4:
			leftPress = true;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
		case KeyEvent.VK_NUMPAD6:
			rightPress = true;
			break;
		case KeyEvent.VK_Z:
		case KeyEvent.VK_X:
		case KeyEvent.VK_C:
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_NUMPAD5:
		case KeyEvent.VK_SPACE:
			attackPress = true;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { // Ű�� ���� �۵�
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
		case KeyEvent.VK_NUMPAD4:
			leftPress = false;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
		case KeyEvent.VK_NUMPAD6:
			rightPress = false;
			break;
		case KeyEvent.VK_SPACE:
			attackPress = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void fire(boolean sw) {
		if (missiles.size() < 5) {
			����ü m = kid.attack(7); // ��ȣ�� ���� ���� ���ݱ��� �ʿ��� �����Ӽ�
			if (m != null) {
				missiles.add(m);
				fireSw = true;
			}
		}
	}
}
