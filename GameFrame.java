package bomb;

import java.awt.Canvas;
import java.awt.Frame;

public class GameFrame extends Frame {
	private Canvas canvas;

	public GameFrame() {
		setBounds(500, 20, 496, 838);
		addWindowListener(new GameWindowListener());
		canvas = new RoleCanvas();
		add(canvas);
		canvas.setFocusable(true);
		canvas.requestFocus();
		setVisible(true);
	}
}
