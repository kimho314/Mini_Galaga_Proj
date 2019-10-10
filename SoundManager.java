

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
	
	private String[] urlArr;
	private Sound[] sound;
	
	private static SoundManager sm;
	
	private SoundManager()
	{
		urlArr = new String[6];
		urlArr[0] =  "sound/intro.wav"; 
		urlArr[1] =  "sound/menu.wav";
		urlArr[2] =  "sound/life.wav"; 
		urlArr[3] =  "sound/fire.wav"; 
		urlArr[4] =  "sound/over.wav"; 
		urlArr[5] =  "sound/bgm.wav";
		
		sound = new Sound[6];
		for(int i=0; i<sound.length; i++)
		{
			sound[i] = new Sound(urlArr[i]);
		}
	}
	
	public static SoundManager getInstance()
	{
		if(sm == null)
		{
			sm = new SoundManager();
		}
		return sm;
	}
	
	private int soundSelection(String sound)
	{
		int ret = -1;
		
		if(sound.equals("intro"))
		{
			ret = 0;
		}
		else if(sound.equals("menu"))
		{
			ret = 1;
		}
		else if(sound.equals("life"))
		{
			ret = 2;
		}
		else if(sound.equals("fire"))
		{
			ret = 3;
		}
		else if(sound.equals("over"))
		{
			ret = 4;
		}
		else if(sound.equals("bgm"))
		{
			ret = 5;
		}
		else 
		{
			System.out.println("Wrong Sound!!!");
		}
		
		return ret;
	}
	
	
	public void play(String sound)
	{
		int ret = soundSelection(sound);
		this.sound[ret].play();
	}
	
	public void loop(String sound) {
		int ret = soundSelection(sound);
		this.sound[ret].loop();
	}
	
	public void loop(String sound, int cnt) {
		int ret = soundSelection(sound);
		this.sound[ret].loop(cnt);
	}
	
	public void stop(String sound) {
		int ret = soundSelection(sound);
		this.sound[ret].stop();
	}
}
