package mini_galaga;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class ScoreDisplay {
	private int score;
	
	private Toolkit tk;
	private Image scoreDisp;
	private Image scoreImg;
	
	private int scImgWidth;
	private int[] figureIndex;
	
	private static int scLen;
	
	static
	{
		scLen = 8;
	}
	
	public ScoreDisplay()
	{
		score = 0;
		//score = 12345678;
		
		scImgWidth = 22;
		figureIndex = new int[scLen];
		for(int i=0; i<figureIndex.length; i++)
		{
			figureIndex[i] = 0;
		}
		
		tk = Toolkit.getDefaultToolkit();
		scoreDisp = tk.getImage("res/score.png");
		scoreImg = tk.getImage("res/ic_num.png");
		
	}
	
	public void update()
	{
		// 0 : 1st figure
		// 1 : 2nd figure
		// 2 : 3rd figure
		// 3 : 4rd figure
		// 4 : 5th figure
		// 5 : 6th figure
		// 6 : 7th figure
		// 7 : 8th figure
		int[] numFigure = new int[scLen];
		int tmpNum = this.score;
		
		for(int i=0; i<scLen; i++)
		{
			tmpNum %= (int)Math.pow(10, scLen-i);
			numFigure[i] = (int)(tmpNum / (int)Math.pow(10, scLen-i-1));
		}
		tmpNum = 0;
		
		for(int i=0; i<scLen; i++)
		{
			figureIndex[i] = numFigure[i];
			//System.out.print("num[" + i + "] = " + figureIndex[i] + " ");
		}
		//System.out.println("");
	}
	
	public void draw(Graphics g, GalagaCanvas galagaCanvas) {
		
		// SCORE 이미지 출력
		g.drawImage(scoreDisp, 10, 10, galagaCanvas);
		
		// 숫자를 자릿수에 맞춰서 출력
		for(int i=0; i<scLen; i++)
		{
			int dx1 = 100 + (scImgWidth * i);
			int dy1 = 10;
			int dx2 = 100 + (scImgWidth * (i+1));
			int dy2 = 10 + scImgWidth;
			
			int sx1 = 0 + (scImgWidth * figureIndex[i]);
			int sy1 = 0;
			int sx2 = 0 + (scImgWidth * (figureIndex[i] + 1));
			int sy2 = 0 + scImgWidth;
			
			g.drawImage(scoreImg, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, galagaCanvas);
		}
		
	}
	
	public void scoreUp(int score)
	{
		this.score += score;
		
		if(this.score >= 99999999)
		{
			this.score = 99999999;
		}
	}
	
}
