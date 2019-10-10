

import java.awt.Canvas;
import java.awt.Frame;

public class GameFrame extends Frame {
	private GalagaCanvas canvas;
	
	private static GameFrame frame;
	
	public static GameFrame getInstance()
	{
		if(frame == null)
		{
			frame = new GameFrame();
		}
		return frame;
	}
	
	
	private GameFrame() {
		setBounds(0, 0, 496, 838);
		addWindowListener(new GameWindowListener());		
		canvas = new GalagaCanvas();
		canvas.setFocusable(true);
		canvas.requestFocus();
		add(canvas);
		
		setVisible(true);
	}
}
