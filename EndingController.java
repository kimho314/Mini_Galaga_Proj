package mini_galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class EndingController {
	
	Toolkit tk;
	private Image endImg;
	private Image restartImg;
	private Image btnEndImg;
	private Image blueArrowImg;
	
	private boolean restartFlag;
	private int selIndex;
	private SoundManager overS; // sound
	public EndingController()
	{
		overS = new SoundManager("over"); // sound
		restartFlag = false;
		selIndex = 0;
		
		tk = Toolkit.getDefaultToolkit();
		endImg = tk.getImage("res/ending_title.png");
		restartImg = tk.getImage("res/btn_restart.png");
		btnEndImg = tk.getImage("res/btn_end.png");
		blueArrowImg = tk.getImage("res/ico_arr.png");
	}
	
	public void setRestartFlag(boolean restartFlag)
	{
		this.restartFlag = restartFlag;
	}
	
	public int getEndSel()
	{
		return selIndex;
	}
	
	public void draw(Graphics g, GalagaCanvas galagaCanvas) {
		
		if(restartFlag)
		{ 
			g.drawImage(blueArrowImg, 179, 376 + (24 * selIndex), 190, 400 + (24 * selIndex), 0, 0, 11, 17, galagaCanvas);			
			g.drawImage(restartImg, 202, 376, 278, 400, 0, (24 * (1 - selIndex)), 76, 24 + (24 * (1 - selIndex)), galagaCanvas);
			g.drawImage(btnEndImg, 190, 400, 291, 424, 0, (24 * selIndex), 101, 24 + (24 * selIndex), galagaCanvas);
		}
		else
		{
			g.drawImage(endImg, 109, 281, galagaCanvas);
			overS.loop(); // sound
		}
		
	}

	public void move(Direction dir) {
		
		switch(dir)
		{
		case UP:
			selIndex--;
			break;
			
		case DOWN:
			selIndex++;
			break;
		}
		
		if(selIndex <= 0)
			selIndex = 0;
		if(selIndex >= 1)
			selIndex = 1;
	}
	
	
}
