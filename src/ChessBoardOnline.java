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
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;

/*
 * 
 * created by shaoyq 16/8/3 javaEE
 */
public class ChessBoardOnline extends GrandManager implements MouseListener, ActionListener, MouseMotionListener{
	int port = 6789 ;
	String host = "127.0.0.1" ;
	int isSever ; // 1 client, 2 server ; client-black
	ServerSocket ssSocket ;
	Socket mySocket ;
	BufferedReader in ;
	DataOutputStream out ;
	Stack<Data> history = new Stack<Data>() ;
	Jiemian jonline = new Jiemian(myData, true) ;
	int timeLimit = -1 ;
	Timer myTimer = new Timer() ;
	TimerTask myTask = null ;
	boolean colorchange = false ;
	URL urlf ;
	URL urll ;
	AudioClip acf ;
	AudioClip acl ;
	int chatcnt = 0 ;
	
	public ChessBoardOnline(int server, String ip) {
		LoadMusic();
		jonline.addMouseListener(this);
		jonline.ngame.addActionListener(this);
		jonline.settime.addActionListener(this);
		jonline.undo.addActionListener(this);
		jonline.chat.addActionListener(this);
		jonline.addMouseMotionListener(this);
		jonline.ngame.addMouseMotionListener(this);
		jonline.settime.addMouseMotionListener(this);
		jonline.undo.addMouseMotionListener(this);
		jonline.chat.addMouseMotionListener(this);
		if(server == 2)
		{
			jonline.ngame.setEnabled(false);
			jonline.settime.setEnabled(false);
			jonline.undo.setEnabled(false);
		}
		isSever = server ;
		if(server == 2) host = ip ;
		setSocket() ;
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
	
	void changeAble()
	{
		if(myData.turn != isSever)
		{
			jonline.ngame.setEnabled(false);
			jonline.undo.setEnabled(false);
			jonline.settime.setEnabled(false);
		}
		else if(result == -1){
			jonline.ngame.setEnabled(true);
			jonline.undo.setEnabled(true);
			if(isSever == 1)
			{
				jonline.settime.setEnabled(true);
			}
		}
	}
	
	void setSocket()
	{
		if(isSever == 1)
		{
				ssSocket = null ;
				try {
					ssSocket = new ServerSocket(port) ;
				} catch (IOException e) {
					System.out.println("cannot listen to " + e);
				}
				mySocket = null ;
				try {
					mySocket = ssSocket.accept() ;
					JOptionPane.showMessageDialog(null, "Connected", "Connected", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {
					System.out.println("error:" + e);
				}
				try {
					in = new BufferedReader(new InputStreamReader(mySocket.getInputStream())) ;
				} catch (IOException e) {
					System.out.println("Error in socket reader " + e);
				}
				
				try {
					out = new DataOutputStream(mySocket.getOutputStream()) ;
				} catch (IOException e) {
					System.out.print("Error in socket writer " + e);
				}
				while (true) {
					String strlocal;
					try {
						strlocal = in.readLine();
						if(strlocal == null)
						{
							myClose();
							myData.turn = isSever ;
							break ;
						}
						deal(strlocal) ;
						//showMsg(result);
					} catch (IOException e) {
						System.out.println("error in read " + e);
						JOptionPane.showMessageDialog(null, "Connection error", "DisConnected", JOptionPane.ERROR_MESSAGE);
						break ;
					};
				}
			}
		else {
			ssSocket = null ;
			mySocket = null ;
			try {
				mySocket = new Socket(host, port) ;
				System.out.println("Connect to host");
				in = new BufferedReader(new InputStreamReader(mySocket.getInputStream())) ;
				out = new DataOutputStream(mySocket.getOutputStream()) ;
			} catch (Exception e) {
				System.out.println("Socket error " + e);
				JOptionPane.showMessageDialog(null, "Cannot Connect", "Error", JOptionPane.ERROR_MESSAGE);
			}
			while (true) {
				String strlocal;
				try {
					strlocal = in.readLine();
					if(strlocal == null)
					{
						myClose();
						break ;
					}
					deal(strlocal) ;
					//showMsg(result);
				} catch (IOException e) {
					System.out.println("error in read " + e);
					JOptionPane.showMessageDialog(null, "Connection error", "DisConnected", JOptionPane.ERROR_MESSAGE);
					break ;
				};
			}
		}
	}
	
	void myClose()
	{
		try {
			in.close();
			out.close();
			mySocket.close();
			System.out.println("myclose");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("cannot close " + e);
		}
	}
	
	void deal(String str){
		String[] words = str.split(":") ;
		if(words[0].charAt(0) == '4')
		{
			dealChat(str) ;
		}
		if(words[0].charAt(0) == '0' || words[0].charAt(0) == '3') // 放子
		{
			myData.turn = Integer.parseInt(words[1]) ;
			result = Integer.parseInt(words[2]) ;
			myData.cnt_black = Integer.parseInt(words[3]) ;
			myData.cnt_white = Integer.parseInt(words[4]) ;
			for(int i = 0 ; i < 8 ; i ++)
			{
				for(int j = 0 ; j < 8 ; j ++)
				{
					myData.board[i][j] = Integer.parseInt(words[i*8 + j + 5]) ;
				}
			}
			Data tmp = new Data() ;
			tmp.Copy(myData);
			history.push(tmp) ;
			jonline.repaint();
			changeAble();
			if(words[0].charAt(0) == '0') acf.play();
			if(words[0].charAt(0) == '3' && myData.turn == isSever)
			{
				jonline.textArea.setText("the opposite time out, your turn");
			}
			showMsg(result);
			if(result == -1 && myData.turn == isSever) {
				setClock();
			}
			/*else
			{
				//System.out.println("in deal");
				showMsg(result);
			}*/
		}
		if(words[0].charAt(0) == '1')
		{
			dealHuiqi(Integer.parseInt(words[1]));
		}
		if(words[0].charAt(0) == '2')
		{
			dealTimeSet(words);
		}
	}
	
	
	void dealChat(String str)
	{
		if(str.length() > 2)
		{
			String msg = "" ;
			msg = str.substring(2) ;
			++ chatcnt ;
			if(isSever == 1)
			{
				msg = "Client: " + msg + '\n';
			}
			else {
				msg = "Server: " + msg + '\n';
			}
			if(chatcnt > 4){
				jonline.chatArea.setText(msg);
				chatcnt = 0 ;
			}
			else {
				jonline.chatArea.append(msg);
			}
		}
	}
	
	void mywrite(int caozuo)
	{
		String strout = String.valueOf(caozuo) ;
		if(caozuo == 0 || caozuo == 3)
		{
			strout += ":" + myData.turn ;
			strout += ":" + result ;
			strout += ":" + myData.cnt_black ;
			strout += ":" + myData.cnt_white ;
			for(int i = 0 ; i < 8 ; i ++)
			{
				for(int j = 0 ; j < 8 ; j ++)
				{
					strout += ":" + myData.board[i][j] ;
				}
			}
			strout += '\n' ;
		}
		try {
			out.writeBytes(strout);
		} catch (IOException e) {
			System.out.println("write error" + e);
		}
		
		if(myData.turn != isSever) {
			stopClock();
		}
		else if(result == -1){
			setClock();
		}
	}
	
	void dealHuiqi(int w) 
	{
		if(w == 0) // 处理请求
		{
			int res ;
			res = JOptionPane.showConfirmDialog(null, "The play want to undo the move, yes or no?", "Confirm dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ;
			String strout ;
			if(res == JOptionPane.YES_OPTION)
			{
				strout = "1:1\n" ;
				try {
					out.writeBytes(strout);
					if(Huiqi()) {
						jonline.repaint();
						acl.play();
					}
					else {
						jonline.textArea.setText("cannot undo");
					}
				} catch (IOException e) {
					System.out.println("write error" + e);
				}
			}
			else
			{
				strout = "1:2\n" ;
				try {
					out.writeBytes(strout);
				} catch (IOException e) {
					System.out.println("write error" + e);
				}
			}	
		}
		else if(w == 1){
			myData.turn = isSever ;
			jonline.textArea.setText("agree your undo move");
			if(Huiqi())
			{
				jonline.repaint();
				acl.play();
			}
			else {
				jonline.textArea.setText("can't finish the undo move");
			}
			setClock();
		}
		else {
			myData.turn = isSever ;
			jonline.textArea.setText("the undo move require is denied");
			setClock();
		}
	}
	
	void dealTimeSet(String[] w)
	{
		if(w[1].charAt(0) == '0')
		{
			int res ;
			int timeset = Integer.parseInt(w[2]) ;
			res = JOptionPane.showConfirmDialog(null, "The server want to set the timelimt as " + w[2], "Agree or disagree", JOptionPane.YES_NO_OPTION) ;
			String strout = "" ;
			if(res == JOptionPane.YES_OPTION)
			{
				strout = "2:1\n" ;
				timeLimit = timeset ;
				jonline.textArea.setText("Agree to set the time limit: " + timeLimit + 's');
				jonline.time2.setText(String.valueOf(timeLimit) + "s");
			}
			else {
				strout = "2:2\n" ;
			}
			try {
				out.writeBytes(strout);
			} catch (IOException e) {
				System.out.println("can't write the message " + e);
			}
		}
		else if(w[1].charAt(0) == '1')
		{
			jonline.textArea.setText("Agree to set the time limit");
			jonline.time2.setText(String.valueOf(timeLimit) + "s");
			setClock();
		}
		else {
			jonline.textArea.setText("time set is denied");
			timeLimit = -1 ;
		}
	}
	
	
	void setClock()
	{
		myTask = null ;
		if(timeLimit > 0)
		{
			myTask = new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					TimeOut(); 
				}
			};
			myTimer.schedule(myTask, 1000*timeLimit, 1000*timeLimit);
		}
	}
	
	void stopClock()
	{
		if(timeLimit > 0 && myTask != null)
		{
			myTask.cancel() ;
			myTask = null ;
		}
	}
	
	void init()
	{
		stopClock();
		timeLimit = -1 ;
		
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
		mywrite(0);
	}

	
	void TimeOut()
	{
		jonline.textArea.setText("Time out, change turn");
		Data tmp = new Data() ;
		tmp.Copy(myData);
		if(findGray())
		{
			myData.turn = 3 - myData.turn ;
			jonline.repaint();
			changeAble();
			mywrite(3); ;
		}
		else {
			myData.Copy(tmp);
			jonline.textArea.setText("The opposite can't move, please continuse");
		}
	}
	
	
	public boolean Huiqi() {
		while (!history.empty()&&history.peek().turn == myData.turn) {
			history.pop() ;
		}
		if(history.empty()) return false ;
		history.pop() ;
		if(history.empty()) return false ;
		myData.Copy(history.peek());
		return true ;
	}
	
	void sendChat()
	{
		String msg = jonline.sendArea.getText() ;
		String strout = "" ;
		if(msg != null)
		{
			strout = "4:" + msg + '\n';
		}
		try {
			out.writeBytes(strout);
		} catch (IOException e) {
			jonline.textArea.setText("Can't send the message");
		}
		if(isSever == 1)
		{
			msg = "Server: " + msg + '\n';
		}
		else {
			msg = "client: " + msg + '\n';
		}
		++ chatcnt ;
		if(chatcnt > 4){
			jonline.chatArea.setText(msg) ;
			chatcnt = 0 ;
		}
		else {
			jonline.chatArea.append(msg);
		}
		jonline.sendArea.setText(null);
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		if(myData.turn != isSever) return ; // 当前不能落子
		if(result != -1) return ;
		int colv = event.getX() ;
		int rowv = event.getY() ;
		col = (colv - 10) / 50 ;
		row = (rowv - 10) / 50 ;
		if(canPut(row, col))
		{
			stopClock();
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
			if(result != -1){
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
			jonline.textArea.setText("");
			jonline.repaint();
			acf.play();
			changeAble();
			mywrite(0);
			//System.out.println("in mouseClicked event");
			showMsg(result) ;
		}
	}


	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == jonline.chat)
		{
			sendChat() ;
		}
		if(myData.turn != isSever) return ;
		if(event.getSource() == jonline.ngame)
		{
			init();
			jonline.repaint();
			jonline.time2.setText("No");
			changeAble();
		}
		if(event.getSource() == jonline.undo)
		{
			jonline.textArea.setText("Please wait to confirm");
			stopClock();
			String strout = "1:0\n" ;
			try {
				out.writeBytes(strout);
			} catch (IOException e) {
				System.out.println("can't send the message: " + e);
			}
		}
		if(event.getSource() == jonline.settime && isSever == 1)
		{
			String inputValue = JOptionPane.showInputDialog("Please enter the time limit (s)") ;
			try{
				timeLimit = Integer.parseInt(inputValue) ;
			}catch (NumberFormatException e) {
				// TODO: handle exception
				jonline.textArea.setText("illegal number");
				return ;
			}
			jonline.textArea.setText("Please wait to confirm");
			stopClock();
			String strout = "2:0:" + inputValue ;
			strout += '\n' ;
			try {
				out.writeBytes(strout);
			} catch (IOException e) {
				System.out.println("can't send the time message " + e) ;
			}
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jonline.chat)
		{
			jonline.chat.setForeground(Color.white);
			colorchange = true ;
			return ;
		}
		if(colorchange == true && e.getSource() == jonline && myData.turn != isSever)
		{
			jonline.chat.setForeground(jonline.brown);
			colorchange = false ;
			return ;
		}
		if(myData.turn != isSever) return ;
		if(e.getSource() == jonline && colorchange == false) return ;
		else if (e.getSource() == jonline) {
			jonline.ngame.setForeground(jonline.brown);
			jonline.settime.setForeground(jonline.brown);
			jonline.undo.setForeground(jonline.brown);
			jonline.chat.setForeground(jonline.brown);
			colorchange = false ;
		}
		if(e.getSource() == jonline.ngame)
		{
			jonline.ngame.setForeground(Color.white);
			colorchange = true ;
			return ;
		}
		if(e.getSource() == jonline.settime)
		{
			jonline.settime.setForeground(Color.white);
			colorchange = true ;
			return ;
		}
		if(e.getSource() == jonline.undo)
		{
			jonline.undo.setForeground(Color.white);
			colorchange = true ;
			return ;
		}
	}
}
