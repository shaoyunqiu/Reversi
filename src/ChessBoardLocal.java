import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.spi.IIOServiceProvider;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * created by shaoyq 16/8/3 javaEE
 * 
 */
public class ChessBoardLocal extends GrandManager implements ActionListener, MouseListener, MouseMotionListener{
	Stack<Data> history = new Stack<Data>() ;
	int timeLimit = -1 ;
	Jiemian jlocal = new Jiemian(myData,false) ;
	boolean colorchange = false ;
	Timer myTimer = new Timer() ;
	TimerTask myTask = null;
	URL urlf ;
	URL urll ;
	AudioClip acf ;
	AudioClip acl ;
		
	public ChessBoardLocal() {
		jlocal.addMouseListener(this);
		jlocal.ngame.addActionListener(this);
		jlocal.sfile.addActionListener(this);
		jlocal.ofile.addActionListener(this);
		jlocal.settime.addActionListener(this);
		jlocal.undo.addActionListener(this);
		jlocal.addMouseMotionListener(this);
		jlocal.ngame.addMouseMotionListener(this);
		jlocal.sfile.addMouseMotionListener(this);
		jlocal.ofile.addMouseMotionListener(this);
		jlocal.settime.addMouseMotionListener(this);
		jlocal.undo.addMouseMotionListener(this);
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
		file = new File("wav/leak.wav") ;
		try {
			urll = file.toURL() ;
		} catch (MalformedURLException e) {
			System.out.println("Can't load the music");
		} ;
		acl = Applet.newAudioClip(urll) ;
	}
	
	void init()
	{
		if(timeLimit > 0)
		{
			myTask.cancel() ;
			timeLimit = -1 ;
		}
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
	
	public void SaveFile(String str) {
		String filename = "syq" + str + ".txt" ;
		File file = new File(filename) ;
		try {
			PrintStream out = new PrintStream(file) ;
			Iterator<Data> iter = history.iterator() ;
			while (iter.hasNext()) {
				Data d = iter.next() ;
				out.printf("%d:%d:%d:%d:%d\n", d.turn, d.cnt_black, d.cnt_white, d.huiqi[0], d.huiqi[1]) ;
				for(int i = 0 ; i < 8 ; i ++)
				{
					out.printf("%d:%d:%d:%d:%d:%d:%d:%d\n", d.board[i][0],d.board[i][1],d.board[i][2], d.board[i][3], d.board[i][4],d.board[i][5],d.board[i][6],d.board[i][7]) ;
				}
			}
		} catch (FileNotFoundException e) {
			jlocal.textArea.setText("Can't save the file");
		}
	}
	
	public void LoadFile(String str) {
		String filename = "syq" + str + ".txt" ;
		try {
			if(timeLimit > 0)
			{
				myTask.cancel() ;
				timeLimit = - 1 ;
			}
			jlocal.textArea.setText("LoadFile");
			jlocal.time2.setText("No");
			Scanner input = new Scanner(new File(filename)) ;
			history.clear();
			Data now = new Data();
			int r = 0 ;
			while (input.hasNextLine()) {
				String line = input.nextLine() ;
				if(line.length() <= 10 && line.length()>= 9)
				{
					now = new Data() ;
					String[] msg = line.split(":") ;
					now.turn = Integer.parseInt(msg[0]) ;
					now.cnt_black = Integer.parseInt(msg[1]) ;
					now.cnt_white = Integer.parseInt(msg[2]) ;
					now.huiqi[0] = Integer.parseInt(msg[3]) ;
					now.huiqi[1] = Integer.parseInt(msg[4]) ;
					r= 0 ;
				}
				else if(line.length() > 10){
					String[] msg = line.split(":") ;
					for(int i = 0 ; i < 8 ; i ++)
					{
						now.board[r][i] = Integer.parseInt(msg[i]) ;
					}
					++ r;
					if(r == 8)
					{
						Data tmp = new Data() ;
						tmp.Copy(now);
						history.push(tmp) ;
					}
				}
			}
			myData.Copy(history.peek());
		} catch (FileNotFoundException e) {
			jlocal.textArea.setText("Can't open the file");
		}
	}
	
	void TimeOut()
	{
		jlocal.textArea.setText("Time out, change turn");
		Data tmp = new Data() ;
		tmp.Copy(myData);
		if(findGray())
		{
			myData.turn = 3 - myData.turn ;
			jlocal.repaint();
		}
		else {
			myData.Copy(tmp);
			jlocal.textArea.setText("The opposite can't move, please continue");
		}
	}
	
	public void SetTime(String str) {
		try {
			timeLimit = Integer.parseInt(str) ;
		} catch (NumberFormatException e) {
			// TODO: handle exception
			jlocal.textArea.setText("the number is illegal");
			return ;
		}
		if(timeLimit > 0)
		{
			if(myTask != null) myTask.cancel() ;
			myTask = new TimerTask() {
				
				@Override
				public void run() {
					TimeOut();
				}
			};
			myTimer.schedule(myTask, timeLimit*1000, timeLimit*1000);
			jlocal.textArea.setText("Time Limit: " + timeLimit + "seconds");
			jlocal.time2.setText(String.valueOf(timeLimit) + "s");
		}
		else {
			jlocal.textArea.setText("fail to set time limit");
		}
	}
	
	void changeAble()
	{
		if(myData.huiqi[myData.turn-1] <= 0) jlocal.undo.setEnabled(false);
		else {
			jlocal.undo.setEnabled(true);
		}
	}

	
	public boolean Huiqi() {
		if(myData.huiqi[myData.turn-1] <= 0) return false ;
		int tmphui = myData.huiqi[myData.turn-1] ;
		while (!history.empty()&&history.peek().turn == myData.turn) {
			history.pop() ;
		}
		if(history.empty()) return false ;
		history.pop() ;
		if(history.empty()) return false ;
		if(timeLimit > 0) myTask.cancel() ;
		myData.Copy(history.peek());
		myData.huiqi[myData.turn-1] = tmphui - 1 ;
		//history.peek().huiqi[myData.turn-1] = myData.huiqi[myData.turn-1] ;
		Iterator<Data> iterator = history.iterator() ;
		while (iterator.hasNext()) {
			iterator.next().huiqi[myData.turn-1] = myData.huiqi[myData.turn-1] ;
		}
		return true ;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		if(result != -1) return ;
		int colv = event.getX() ;
		int rowv = event.getY() ;
		col = (colv - 10) / 50 ;
		row = (rowv - 10) / 50 ;
		if(canPut(row, col))
		{
			if(timeLimit > 0) myTask.cancel() ;
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
			changeAble();
			acf.play();
			showMsg(result) ;
			if(timeLimit > 0 && result == -1){
				myTask = new TimerTask() {
					public void run() {
						TimeOut();
					}
				};
				myTimer.schedule(myTask, timeLimit*1000, timeLimit*1000);
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
			changeAble();
		}
		if(event.getSource() == jlocal.undo)
		{
			if(!Huiqi())
			{
				jlocal.textArea.setText("Cannot undo!");
			}
			else{
				if(timeLimit > 0 && myTask != null) 
					myTask.cancel() ;
				jlocal.repaint(); 
				changeAble();
				acl.play();
				if(timeLimit > 0)
				{
					myTask = new TimerTask() {
						public void run() {
							// TODO Auto-generated method stub
							TimeOut(); 
						}
					};
					myTimer.schedule(myTask, timeLimit*1000, timeLimit*1000);
				}
			}
		}
		if(event.getSource() == jlocal.sfile)
		{
			String inputValue = JOptionPane.showInputDialog("Please enter a number as the filename") ;
			SaveFile(inputValue) ;
		}
		if(event.getSource() == jlocal.ofile)
		{
			String inputValue = JOptionPane.showInputDialog("Please enter the number of file you want to open") ;
			LoadFile(inputValue) ;
			jlocal.repaint();
			changeAble();
		}
		if(event.getSource() == jlocal.settime)
		{
			String inputValue = JOptionPane.showInputDialog("Please enter the time limit (s)") ;
			SetTime(inputValue) ;
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
		if(e.getSource() == jlocal.sfile)
		{
			jlocal.sfile.setForeground(Color.white);
			colorchange = true ;
			return ;
		}
		if(e.getSource() == jlocal.ofile)
		{
			jlocal.ofile.setForeground(Color.white);
			colorchange = true ;
			return ;
		}
		if(e.getSource() == jlocal.settime)
		{
			jlocal.settime.setForeground(Color.white);
			colorchange = true ;
			return ;
		}
		if(e.getSource() == jlocal.undo)
		{
			jlocal.undo.setForeground(Color.white);
			colorchange = true ;
			return ;
		}
	}
	
}
