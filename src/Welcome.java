import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * 
 * created by shaoyq 16/8/2 javaEE
 */

public class Welcome extends JFrame implements MouseMotionListener, MouseListener{

	JLabel offline = new JLabel("offline") ;
	JLabel online = new JLabel("Online") ;
	JLabel tiltle = new JLabel("Reversi") ;
	boolean colorchange = false ;
	MusicTread mt ;
	URL url ;
	AudioClip ac ;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Welcome myWelcome = new Welcome() ;
		myWelcome.work();
	}
	
	public void work() {
		setTitle("Reversi");
		setSize(new Dimension(410, 470));
		setLocation(200,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon bg = new ImageIcon("imag/welbg.jpg") ;
		JLabel bgLabel = new JLabel(bg) ;
		bgLabel.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
		getLayeredPane().add(bgLabel, new Integer(-30001)) ;
		JPanel contentPane = (JPanel)getContentPane() ;
		contentPane.setOpaque(false);
		tiltle.setFont(new Font("华文隶书", Font.ITALIC, 50));
		tiltle.setForeground(Color.black);
		tiltle.setHorizontalAlignment(JLabel.CENTER);
		offline.setFont(new Font("华文隶书", Font.ITALIC, 40));
		offline.setForeground(Color.black);
		offline.setHorizontalAlignment(JLabel.CENTER);
		online.setFont(new Font("华文隶书", Font.ITALIC, 40));
		online.setForeground(Color.black);
		online.setHorizontalAlignment(JLabel.CENTER);
		contentPane.setLayout(null);
		tiltle.setBounds(0, 30, 400, 50);
		online.setBounds(0, 220, 400, 40);
		offline.setBounds(0,150, 400, 40);
		contentPane.add(tiltle) ;
		contentPane.add(offline) ;
		contentPane.add(online) ;
		online.addMouseListener(this);
		offline.addMouseListener(this);
		online.addMouseMotionListener(this);
		offline.addMouseMotionListener(this);
		this.addMouseMotionListener(this);
		this.setResizable(false);
		this.setVisible(true);
		LoadBgm();
		mt.start();
		
	}

	void LoadBgm()
	{
		File file = new File("wav/bgm.wav") ;
		try {
			url = file.toURL() ;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't Load Music");
		}
		ac = Applet.newAudioClip(url) ;
		mt = new MusicTread(ac, 1) ;
	}
	
	void dealLocalChess(int n) {
		if(n == 0){
			ChessBoardLocal myLocal = new ChessBoardLocal() ;
		}
		else {
			Object[] ai = {"0", "1"} ;
			int r = JOptionPane.showOptionDialog(null, "Please choose the difficulty level", "Level", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, ai, ai[0]) ;
			ChessBoardAI myAi = new ChessBoardAI(r) ;
		}
		this.setEnabled(false);
		this.setVisible(false);
	}
	
	void dealOnlineChess(String ip)
	{
		if(ip == null) ip = "127.0.0.1" ;
		Client client = new Client(ip) ;
		Thread t = new Thread(client) ;
		t.start();
		this.setEnabled(false);
		this.setVisible(false);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == offline)
		{
			Object[] Options = {"PvsP", "PvsAI"} ;
			Icon icon = new ImageIcon("imag/black.png") ;
			int n = JOptionPane.showOptionDialog(null, "Choose a local mode", "Mode choosing", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon, Options, Options[0]) ;
			mt.interrupt();
			dealLocalChess(n);
		}
		if(e.getSource() == online)
		{
			mt.interrupt();
			String inputValue = JOptionPane.showInputDialog("Please enter the IP: ") ;
			dealOnlineChess(inputValue);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == online)
		{
			online.setForeground(Color.lightGray);
			colorchange = true ;
		}
		else if(e.getSource() == offline)
		{
			offline.setForeground(Color.lightGray) ;
			colorchange = true ;
		}
		else if(colorchange == true){
			online.setForeground(Color.black);
			offline.setForeground(Color.black);
			colorchange = false ;
		}
	}
}
