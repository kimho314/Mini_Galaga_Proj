package mini_galaga;

import java.awt.Canvas;
import java.awt.Frame;

public class GameFrame extends Frame {
	private Canvas canvas;

	public GameFrame() {
		setBounds(0, 0, 496, 838);
		addWindowListener(new GameWindowListener());
		canvas = new GalagaCanvas();
		add(canvas);
		canvas.setFocusable(true);
		canvas.requestFocus();
		setVisible(true);
	}
}
