import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/*
 * 
 * created by shaoyq 16/8/2 javaEE
 */
public class MusicTread extends Thread{
	AudioClip ac ;
	int mode = 1 ;
	public MusicTread(AudioClip a, int m) {
		// TODO Auto-generated constructor stub
		mode = m ;
		ac = a ;
	}
	
	public void run() {
		if(mode == 1)
		{
			ac.loop();
		}
		else {
			ac.play();
		}
	}
	
	public void interrupt(){
		ac.stop();
	}
}
