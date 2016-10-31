
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * created by shaoyq 16/8/3 JavaEE
 */
public class Jiemian extends JPanel{
	static int chess_width = 620 ;
	static int chess_height = 470 ;
	static int each_size = 50 ;
	static int chess_size = 40 ;
	Color brown = new Color(160, 82, 45) ;
	Color back = new Color(210, 180, 140) ;
	Data myData = new Data() ;
	JFrame frame = new JFrame("ChessBoard offline") ;
	JButton ngame = new JButton() ;
	JButton sfile = new JButton() ;
	JButton ofile = new JButton() ;
	JButton settime = new JButton() ;
	JButton undo = new JButton() ;
	JButton chat = new JButton() ;
	JTextArea textArea = new JTextArea() ;
	JTextArea chatArea = new JTextArea() ;
	JTextArea sendArea = new JTextArea() ;
	JLabel b_num = new JLabel() ;
	JLabel w_num = new JLabel() ;
	JLabel b_cnt = new JLabel() ;
	JLabel w_cnt = new JLabel() ;
	JLabel now_move = new JLabel() ;
	JLabel move = new JLabel() ;
	JLabel time1 = new JLabel() ;
	JLabel time2 = new JLabel() ;
	String fontname = "Comic Sans MS" ;
	
	public Jiemian(Data d, boolean ifOn) {
		// TODO Auto-generated constructor stub
		myData = d ;
		setBackground(back);
		frame = new JFrame("ChessBoardLocal") ;
		frame.setSize(new Dimension(chess_width, chess_height));
		frame.setContentPane(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		setMyButton(ngame, "New Game");
		setMyButton(sfile, "Save File");
		setMyButton(ofile, "Open File");
		setMyButton(undo, "Undo");
		setMyButton(settime, "Set Time");
		setMyButton(chat, "Chat");
		ngame.setBounds(440, 200, 150, 18);
		add(ngame) ;
		if(ifOn == false)
		{
			sfile.setBounds(440, 240, 150, 18);
			ofile.setBounds(440, 280, 150, 18);
			settime.setBounds(440, 320, 150, 18);
			undo.setBounds(440, 360, 150, 18);
			add(sfile) ;
			add(ofile) ;
			add(settime) ;
			add(undo) ;
		}
		else {
			frame.setSize(new Dimension(chess_width, chess_height + 50));
			settime.setBounds(440, 240, 150, 18);
			undo.setBounds(440, 280, 150, 18);
			chatArea.setFont(new Font(fontname, Font.PLAIN, 12));
			chatArea.setBounds(440, 320, 150, 90);
			sendArea.setFont(new Font(fontname, Font.PLAIN, 12));
			sendArea.setBounds(10, 430, 400, 30);
			chat.setBounds(440, 440, 150, 18);
			add(settime) ;
			add(undo) ;
			add(chatArea) ;
			add(sendArea) ;
			add(chat) ;
			frame.setTitle("ChessBoardOnline");
		}
		textArea.setFont(new Font(fontname, Font.PLAIN, 12));
		textArea.setBounds(440, 140, 150, 40);
		textArea.setLineWrap(true);
		add(textArea) ;
		Font labelFont = new Font(fontname, Font.PLAIN, 14) ;
		b_num.setFont(labelFont);
		b_num.setForeground(Color.black);
		b_num.setText("Black Num: ");
		b_num.setHorizontalAlignment(JLabel.CENTER);
		w_num.setFont(labelFont);
		w_num.setForeground(Color.white);
		w_num.setHorizontalAlignment(JLabel.CENTER);
		w_num.setText("White Num: ");
		b_num.setBounds(440, 15, 100, 15);
		w_num.setBounds(440, 45, 100, 15);
		now_move.setFont(labelFont);
		now_move.setForeground(brown);
		now_move.setHorizontalAlignment(JLabel.CENTER);
		now_move.setText("Now Move: ");
		now_move.setBounds(440, 75, 100, 15);
		time1.setFont(labelFont);
		time1.setForeground(brown);
		time1.setHorizontalAlignment(JLabel.CENTER);
		time1.setText("Time Limit: ");
		time1.setBounds(440, 105, 100, 15);
		b_cnt.setFont(labelFont);
		b_cnt.setForeground(Color.black);
		b_cnt.setHorizontalAlignment(JLabel.CENTER);
		b_cnt.setText(String.valueOf(0));
		b_cnt.setBounds(540, 15, 50, 15);
		w_cnt.setFont(labelFont);
		w_cnt.setForeground(Color.white);
		w_cnt.setHorizontalAlignment(JLabel.CENTER);
		w_cnt.setText(String.valueOf(0));
		w_cnt.setBounds(540, 45, 50, 15);
		move.setFont(labelFont);
		move.setForeground(brown);
		move.setHorizontalAlignment(JLabel.CENTER);
		move.setText("Black");
		move.setBounds(540, 75, 50, 15);
		time2.setFont(labelFont);
		time2.setForeground(brown);
		time2.setHorizontalAlignment(JLabel.CENTER);
		time2.setText("No");
		time2.setBounds(540, 105, 50, 15);
		add(b_num) ;
		add(w_num) ;
		add(now_move) ;
		add(time1) ;
		add(b_cnt) ;
		add(w_cnt) ;
		add(move) ;
		add(time2) ;
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	
	void ChangeData(Data d)
	{
		myData = d ;
	}
	
	void setMyButton(JButton bnt, String text) 
	{
		Font myFont = new Font(fontname, Font.PLAIN, 16) ;
		bnt.setFont(myFont);
		bnt.setForeground(brown);
		bnt.setText(text);
		bnt.setBorderPainted(false);
		bnt.setFocusPainted(false);
		bnt.setContentAreaFilled(false);
		bnt.setAlignmentX(JButton.CENTER_ALIGNMENT);
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		paintBg(graphics) ;
		paintQz(graphics) ;
		b_cnt.setText(String.valueOf(myData.cnt_black));
		w_cnt.setText(String.valueOf(myData.cnt_white));
		if(myData.turn == 1) move.setText("black");
		else move.setText("white") ;
	}
	
	public void paintBg(Graphics graphics) {
		graphics.setColor(brown);
		graphics.fillRect(10, 10, 400, 400);
		graphics.setColor(back);
		for(int i = 10 ; i <= 410 ; i += 50)
		{
			graphics.drawLine(i, 10, i, 410);
			graphics.drawLine(10, i, 410, i);
		}
	}
	
	public void paintQz(Graphics graphics) {
		Graphics2D graphics2d = (Graphics2D)graphics ;
		RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
		graphics2d.setRenderingHints(renderingHints);
		for(int i = 0 ; i < 8 ; i ++)
		{
			for(int j = 0 ; j < 8 ; j ++)
			{
				if(myData.board[i][j] == 1)
				{
					graphics2d.setColor(Color.BLACK);
					graphics2d.fillOval(j*50+14, i*50+14, chess_size, chess_size);
				}
				else if(myData.board[i][j] == 2){
					graphics2d.setColor(Color.WHITE);
					graphics2d.fillOval(j*50+14, i*50+14, chess_size, chess_size);
				}
				else if (myData.board[i][j] == 3) {
					graphics2d.setColor(Color.GRAY);
					graphics2d.fillOval(j*50+29, i*50+29, 10, 10);
				}
			}
		}
	}

}
