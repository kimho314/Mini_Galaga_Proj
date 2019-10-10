

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
		int tmpNum = score;
		
		for(int i=0; i<scLen; i++)
		{
			tmpNum %= (int)Math.pow(10, scLen-i);
			numFigure[i] = (int)(tmpNum / (int)Math.pow(10, scLen-i-1));
		}
		tmpNum = 0;
		
		for(int i=0; i<scLen; i++)
		{
			figureIndex[i] = numFigure[i];
		}
	}
	
	public void draw(Graphics g, GalagaCanvas galagaCanvas) {
		
		// SCORE 이미지 출력
		g.drawImage(scoreDisp, 350, 30, galagaCanvas); // bae 수정
		
		// 숫자를 자릿수에 맞춰서 출력
		for(int i=0; i<scLen; i++)
		{
			int dx1 = 300 + (scImgWidth * i);// bae 수정
			int dy1 = 60;// bae 수정
			int dx2 = 300 + (scImgWidth * (i+1));// bae 수정
			int dy2 = 60 + scImgWidth;// bae 수정
			
			int sx1 = 0 + (scImgWidth * figureIndex[i]);
			int sy1 = 0;
			int sx2 = 0 + (scImgWidth * (figureIndex[i] + 1));
			int sy2 = 0 + scImgWidth;
			
			g.drawImage(scoreImg, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, galagaCanvas);
		}
		
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void scoreUp(int _score)
	{
		score += _score;
		
		if(score >= 99999999)
		{
			score = 99999999;
		}
	}
	
}
