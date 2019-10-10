

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class RankingManager {
	private FileOutputStream fos;
	private FileInputStream fin;
	private PrintStream fout;

	private int score;
	private String name;
	private String[][] data;

	private Scanner scan;
	
	private int[] alphabetIdx;
	private char[] alphabetArr;
	
	private static final int ALPHABET_WIDTH = 40;
	private static final int NUM_WIDTH = 16;
	private static final int MAX_ALPHABET_NUM = 26;
	
	private int nextIdx;
	private int changeAlphabetIdx;
	
	
	Toolkit tk;
	private Image rankingAlphabetImg;
	private Image cursorImg;
	private Image endImg;
	private int drawSwitchState;

	public RankingManager() {
		score = 0;
		data = new String[5][3];
		name = "name"; // ��ŷ �̸��� �ҷ��;ߵ�
		
		nextIdx = 0;
		changeAlphabetIdx = 0;
		
		alphabetIdx = new int[3];
		for(int i=0; i<alphabetIdx.length; i++)
		{
			alphabetIdx[i] = 0;
		}
		alphabetArr = new char[26];
		for(int i=0; i<alphabetArr.length; i++)
		{
			alphabetArr[i] = (char) ('A' + i);
		}
		
		tk = Toolkit.getDefaultToolkit();
		endImg = tk.getImage("res/ending_title.png");
		rankingAlphabetImg = tk.getImage("res/eng.png");
		cursorImg = tk.getImage("res/ico_arr_l.png");
		drawSwitchState = 0;
	}
	
	public int getRankingNextIdx()
	{
		return nextIdx;
	}
	
	
	public int getDrawSwitchState()
	{
		return drawSwitchState;
	}
	
	public void setDrawSwitchState(int val)
	{
		drawSwitchState = val;
	}
	
	public void printRankingData()
	{
		drawSwitchState = 1;
		
		for(int i=0; i<data.length; i++)
		{
			for(int j=0; j<data[i].length; j++)
			{
				System.out.print(data[i][j] + " ");
			}
			System.out.println("");
		}
	}
		
	
	
	public boolean isScoreHigh(int score)
	{
		boolean ret = false;
		this.score = score;
		
		if(Integer.valueOf(data[4][2]) < this.score) {
			ret = true;
		}		
		return ret;
	}
	
	public void storeRanking()
	{
		try { // ����
			fos = new FileOutputStream("res/ScoreData.txt");
			PrintStream fout = new PrintStream(fos);
			
			// ���� �̸� ���ھ�
			// ������ ������
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					fout.print(data[i][j] + " ");
				}
				fout.println();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
	}
	
	public void loadRanking() {
		/*
		 * data[0][i] -> 1st
		 * data[1][i] -> 2nd
		 * data[2][i] -> 3rd
		 * data[3][i] -> 4th
		 * data[4][i] -> 5th
		 */
		
		/*
		 *    ���                   �̸�                ���ھ�
		 * data[0][0]  data[0][1]  data[0][2]
		 * data[1][0]  data[1][1]  data[1][2]
		 * data[2][0]  data[2][1]  data[2][2]
		 * data[3][0]  data[3][1]  data[3][2]
		 * data[4][0]  data[4][1]  data[4][2]
		 */
		
		try { // �Է�
			fin = new FileInputStream("res/ScoreData.txt");
			scan = new Scanner(fin);
			
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					if (scan.hasNext()) {
						data[i][j] = scan.next();
					}
				}
				
				if (scan.hasNextLine())
					scan.nextLine();
			}			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
	}
	
	public void updateRanking()
	{
		saveName();
		
		boolean sw = true;
		
		for (int i = 0; i < data.length; i++) {
			if (Integer.valueOf(data[i][2]) < score) {
				for (int j = 1; j < data.length-i; j++) {
					data[data.length - j][1] = data[data.length - j - 1][1];
					data[data.length - j][2] = data[data.length - j - 1][2];
				}
				
				if (sw == true) {
					data[i][1] = name;
					data[i][2] = String.valueOf(score);
					sw = false;
					break;
				}
			}
		}
	}
	
	public void saveName()
	{
		String tmpStr = "";
		
		tmpStr = String.valueOf(alphabetArr[alphabetIdx[0]]);
		tmpStr = tmpStr.concat(String.valueOf(alphabetArr[alphabetIdx[1]]));
		tmpStr = tmpStr.concat(String.valueOf(alphabetArr[alphabetIdx[2]]));
		//System.out.println(tmpStr);
		name = tmpStr;
	}

	public void draw(Graphics g, GalagaCanvas galagaCanvas) {	
		if(drawSwitchState == 0)
		{
			drawTypeName(g,galagaCanvas);
		}
		else if(drawSwitchState == 1)
		{
			drawRankingChart(g,galagaCanvas);
		}
		else {}
	}
	
	public void drawTypeName(Graphics g, GalagaCanvas galagaCanvas)
	{
		g.drawImage(endImg, 109, 281, galagaCanvas);
		g.drawImage(cursorImg, 190 + (ALPHABET_WIDTH * nextIdx), 360 + 160 + 40, 229 + (ALPHABET_WIDTH * nextIdx), 400 + 160 + 40, 
				0, 0, 19, 13, galagaCanvas);
		
		g.drawImage(rankingAlphabetImg, 180, 360 + 160, 240, 400 + 160, 
				0 + (ALPHABET_WIDTH * alphabetIdx[0]), 0, 
				40 + (ALPHABET_WIDTH * alphabetIdx[0]), 40, galagaCanvas);
		
		g.drawImage(rankingAlphabetImg, 180 + ALPHABET_WIDTH, 360 + 160, 240 + ALPHABET_WIDTH, 400 + 160, 
				0 + (ALPHABET_WIDTH * alphabetIdx[1]), 0, 
				40 + (ALPHABET_WIDTH * alphabetIdx[1]), 40, galagaCanvas);
		
		g.drawImage(rankingAlphabetImg, 180 + ALPHABET_WIDTH * 2, 360 + 160, 240 + ALPHABET_WIDTH * 2, 400 + 160, 
				0 + (ALPHABET_WIDTH * alphabetIdx[2]), 0, 
				40 + (ALPHABET_WIDTH * alphabetIdx[2]), 40, galagaCanvas);
	}
	
	public void drawRankingChart(Graphics g, GalagaCanvas galagaCanvas)
	{
		int fontSize = 30;
		
		g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
		g.setColor(Color.white);
		
		for(int i=0; i<data.length; i++)
		{
			g.drawString(data[i][0], 150, 250 + (50*i));
			g.drawString(data[i][1], 150 + fontSize + 10, 250 + (50*i));
			g.drawString(data[i][2], 150 + fontSize*3 + 10*3, 250 + (50*i));
		}
	}

	public void move(Direction dir) {

		switch (dir) {
		case UP:
			changeAlphabetIdx++;
			break;

		case DOWN:
			changeAlphabetIdx--;
			break;

		case RIGHT:
			nextIdx++;
			changeAlphabetIdx = 0;
			break;
		}

		if (changeAlphabetIdx >= MAX_ALPHABET_NUM - 1) {
			changeAlphabetIdx = MAX_ALPHABET_NUM - 1;
		}
		if (changeAlphabetIdx <= 0) {
			changeAlphabetIdx = 0;
		}

		if (nextIdx >= 2) {
			nextIdx = 2;
		}

		alphabetIdx[nextIdx] = changeAlphabetIdx;

	}

}
