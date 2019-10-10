package mini_galaga;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	private Clip clip;
	private AudioInputStream stream;
	private File file;
	
	public Sound(String soundUrl) {
		file = new File(soundUrl);
		if (file.exists()) {
			try {
				stream = AudioSystem.getAudioInputStream(file);
				clip = AudioSystem.getClip();
				clip.open(stream);
			} catch (Exception e) {
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
