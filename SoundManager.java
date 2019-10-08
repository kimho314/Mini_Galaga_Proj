package mini_galaga;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
	private Clip clip;
	private AudioInputStream stream;
	private File file;

	public SoundManager() {
		this("");
	}

	public SoundManager(String menu) {
		String soundUrl = "";

		switch (menu) {
		case "intro" : soundUrl = "sound/intro.wav"; 
					break;
		case "menu" : soundUrl = "sound/menu.wav";
					break;
		case "life" : soundUrl = "sound/life.wav"; 
					break;
		case "fire" : soundUrl = "sound/fire.wav"; 
					break;
		case "over" : soundUrl = "sound/over.wav"; 
					break;
		case "bgm" : soundUrl = "sound/bgm.wav"; 
					break;
		default: soundUrl = ""; break;
		}
		
		file = new File(soundUrl);
		if(file.exists()) {
			try {  				
				 stream = AudioSystem.getAudioInputStream(file);
		         clip = AudioSystem.getClip();
		         clip.open(stream);
		        //clip.start();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
		}
	}

	public void play() {
		try { 
			clip.setFramePosition(0);
			clip.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
	}

	public void loop() {
		try { 
			clip.loop(0);
        } catch(Exception e) {
            e.printStackTrace();
        }
	}

	public void loop(int cnt) {
		try { 
			clip.loop(cnt);
        } catch(Exception e) {
            e.printStackTrace();
        }
	}

	public void stop() {
		try { 
			clip.stop();
        } catch(Exception e) {
            e.printStackTrace();
        }	 
	}
}
