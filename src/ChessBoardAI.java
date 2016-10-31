import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.AtomicMoveNotSupportedException;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
/*
 * 
 * created by shaoyq 16/8/3 javaEE
 */

public class ChessBoardAI extends GrandManager implements MouseListener, MouseMotionListener, ActionListener{
	Stack<Data> history = new Stack<Data>() ;
	int timeLimit = -1 ;
	Jiemian jlocal = new Jiemian(myData,false) ;
	boolean colorchange = false ;
	URL urlf ;
	URL urll ;
	AudioClip acf ;
	AudioClip acl ;
	int diff = 0 ;
	AIplayer myplayer ;
		
	public ChessBoardAI(int di) {
		diff = di ;
		myplayer = new AIplayer(diff) ;
		jlocal.addMouseListener(this);
		jlocal.ngame.addActionListener(this);
		jlocal.addMouseMotionListener(this);
		jlocal.ngame.addMouseMotionListener(this);
		jlocal.settime.setEnabled(false);
		jlocal.sfile.setEnabled(false);
		jlocal.ofile.setEnabled(false);
		jlocal.undo.setEnabled(false);
		LoadMusic();
	}
	
	void LoadMusic()
	{
		File file = new File("wav/forward.wav") ;
		try {
			urlf = file.toURL() ;
		} catch (MalformedURLException e) {
			System.out.println("Can't load the music");
		}
		acf = Applet.newAudioClip(urlf) ;
		//forward = new MusicTread(acf, 2) ;
		file = new File("wav/leak.wav") ;
		try {
			urll = file.toURL() ;
		} catch (MalformedURLException e) {
			System.out.println("Can't load the music");
		} ;
		acl = Applet.newAudioClip(urll) ;
		//leak = new MusicTread(acl, 2) ;
	}
	
	void init()
	{
		history.clear();
		for(int i = 0 ; i < 8 ; i ++)
		{
			for(int j = 0 ; j < 8 ; j ++)
			{
				myData.board[i][j] = 0 ;
			}
		}
		myData.board[3][3] = 2 ;
		myData.board[3][4] = 1 ;
		myData.board[4][3] = 1 ;
		myData.board[4][4] = 2 ;
		myData.board[3][2] = 3 ;
		myData.board[2][3] = 3 ;
		myData.board[4][5] = 3 ;
		myData.board[5][4] = 3 ;
		myData.huiqi[0] = 2 ;
		myData.huiqi[1] = 2 ;
		result = -1 ;
		myData.cnt_black = 2 ;
		myData.cnt_white = 2 ;
		myData.turn = 1 ;
		Data tmp = new Data();
		tmp.Copy(myData);
		history.push(tmp) ;
	}
	
	void AImove()
	{
		while (myData.turn == 2 && result == -1) {
			Point point = myplayer.makeMove(myData) ;
			int row = point.x ;
			int col = point.y ;
			myData.board[row][col] = myData.turn ;
			Reverse(); 
			reCount();
			result = Winner() ;
			if(result == -1)
			{
				if(findGray()) 
				{
					myData.turn = 3 - myData.turn ;
				}
				else {
					myData.turn = 3 - myData.turn ;
					if(findGray())
					{
						myData.turn = 3 - myData.turn ;
					}
					else {
						myData.turn = 3 - myData.turn ;
						result = 4 ;
					}
				}
			}
			if(result != -1)
			{
				for(int i = 0 ; i < 8 ; i ++)
				{
					for(int j = 0 ; j < 8 ; j ++)
					{
						if(myData.board[i][j] == 3) myData.board[i][j] = 0 ;
					}
				}
			}
			Data tmp = new Data() ;
			tmp.Copy(myData);
			history.push(tmp) ;
			jlocal.textArea.setText("");
			jlocal.repaint();
			acf.play();
			showMsg(result) ;
		}
		jlocal.ngame.setEnabled(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		if(myData.turn != 1 || result != -1) return ;
		int colv = event.getX() ;
		int rowv = event.getY() ;
		col = (colv - 10) / 50 ;
		row = (rowv - 10) / 50 ;
		if(canPut(row, col))
		{
			myData.board[row][col] = myData.turn ;
			Reverse() ;
			reCount() ;
			result = Winner() ;
			if(result == -1)
			{
				if(findGray()) 
				{
					myData.turn = 3 - myData.turn ;
				}
				else {
					myData.turn = 3 - myData.turn ;
					if(findGray())
					{
						myData.turn = 3 - myData.turn ;
					}
					else {
						myData.turn = 3 - myData.turn ;
						result = 4 ;
					}
				}
			}
			if(result != -1)
			{
				for(int i = 0 ; i < 8 ; i ++)
				{
					for(int j = 0 ; j < 8 ; j ++)
					{
						if(myData.board[i][j] == 3) myData.board[i][j] = 0 ;
					}
				}
			}
			Data tmp = new Data() ;
			tmp.Copy(myData);
			history.push(tmp) ;
			jlocal.textArea.setText("");
			jlocal.repaint();
			acf.play();
			showMsg(result) ;
			if(result == -1 && myData.turn == 2) 
			{
				jlocal.ngame.setEnabled(false);
				AImove() ;
			}
		}
	}


	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getSource() == jlocal.ngame)
		{
			init();
			jlocal.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jlocal && colorchange == false) return ;
		else if (e.getSource() == jlocal) {
			jlocal.ngame.setForeground(jlocal.brown);
			jlocal.sfile.setForeground(jlocal.brown);
			jlocal.ofile.setForeground(jlocal.brown);
			jlocal.settime.setForeground(jlocal.brown);
			jlocal.undo.setForeground(jlocal.brown);
			colorchange = false ;
		}
		if(e.getSource() == jlocal.ngame)
		{
			jlocal.ngame.setForeground(Color.white);
			colorchange = true ;
			return ;
		}
	}	
}

