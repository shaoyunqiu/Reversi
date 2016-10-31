

import javax.swing.JOptionPane;
/*
 * 
 * created by shaoyq 16/8/3 javaEE
 */
public class GrandManager {
	public Data myData = new Data();
	public int result = -1 ; //-1 unfinished, 1:black win, 2 white, 3 tie 
	public int row, col ;
	
	public boolean canPut(int i, int j) 
	{
		if(i > 8 || j > 8 || i < 0 || j < 0) return false ;
		if(myData.board[i][j] != 3) return false ;
		return true ;
	}
	
	public void Reverse()
	{
		int stop = col ;
		stop = D1(row, col, myData.turn) ;
		for(int i = col + 1 ; i<stop ; i ++)
		{
			myData.board[row][i] = myData.turn ;
		}
		stop = col ;
		stop = D2(row, col, myData.turn) ;
		for(int i = col - 1; i > stop ; i --)
		{
			myData.board[row][i] = myData.turn ;
		}
		stop = row ;
		stop = D3(row, col, myData.turn) ;
		for(int i = row + 1 ; i < stop ; i ++)
		{
			myData.board[i][col] = myData.turn ;
		}
		stop = row ;
		stop = D4(row, col, myData.turn) ;
		for(int i = row - 1 ; i > stop ; i --)
		{
			myData.board[i][col] = myData.turn ;
		}
		stop = 0 ;
		stop = D5(row, col, myData.turn) ;
		for(int i = 1 ; i < stop ; i ++)
		{
			myData.board[row-i][col-i] = myData.turn ;
		}
		stop = 0 ;
		stop = D6(row, col, myData.turn) ;
		for(int i = 1 ; i < stop ; i ++)
		{
			myData.board[row+i][col+i] = myData.turn ;
		}
		stop = 0 ;
		stop = D7(row, col, myData.turn) ;
		for(int i = 1 ; i < stop ; i ++)
		{
			myData.board[row-i][col+i] = myData.turn ;
		}
		stop = 0 ;
		stop = D8(row, col, myData.turn) ;
		for(int i = 1 ; i < stop ; i ++)
		{
			myData.board[row+i][col-i] = myData.turn ;
		}
	}
	
	public void reCount()
	{
		myData.cnt_black = 0 ;
		myData.cnt_white = 0 ;
		for(int i = 0 ; i < 8 ; i ++)
		{
			for(int j = 0 ; j < 8 ; j ++)
			{
				if(myData.board[i][j] == 1)
				{
					++ myData.cnt_black ;
				}
				else if (myData.board[i][j] == 2) {
					++ myData.cnt_white ;
				}
				else {
					myData.board[i][j] = 0 ;
				}
			}
		}
	}
	
	public int Winner()
	{
		if(myData.cnt_black == 0 && myData.cnt_white > 0) return 2; 
		if(myData.cnt_white == 0 && myData.cnt_black > 0) return 1 ;
		if(myData.cnt_white + myData.cnt_black == 64)
		{
			if(myData.cnt_white == myData.cnt_black) return 3 ;
			else if (myData.cnt_white > myData.cnt_black) {
				return 2 ;
			}
			else return 1 ;
		}
		return -1 ;
	}
	
	public boolean findGray()
	{
		int mygray = 0 ;
		for(int i = 0 ; i < 8 ; i ++)
		{
			for(int j = 0 ; j < 8 ; j ++)
			{
				if(myData.board[i][j] == 1 || myData.board[i][j] == 2) continue ;
				if(exist(i,j,3-myData.turn)) {
					++ mygray ;
					myData.board[i][j] = 3 ;
				}
				else {
					myData.board[i][j] = 0 ;
					continue ;
				}
			}
		}
		if(mygray > 0) return true ;
		else return false ;
	}
	
	public boolean exist(int r, int c, int t)
	{
		int stop = c;
		stop = D1(r, c, t) ;
		if(stop > c+1 ) return true ;
		stop = c ;
		stop = D2(r, c, t) ;
		if(stop < c - 1) return true ;
		stop = r ; 
		stop = D3(r, c, t) ;
		if(stop > r + 1) return true ;
		stop = r ;
		stop = D4(r, c, t) ;
		if(stop < r - 1) return true ;
		stop = 0 ;
		stop = D5(r, c, t) ;
		if(stop > 1) return true ;
		stop = 0 ;
		stop = D6(r, c, t) ;
		if(stop > 1) return true ;
		stop = 0 ;
		stop = D7(r, c, t) ;
		if(stop > 1) return true ;
		stop = 0 ;
		stop = D8(r, c, t) ;
		if(stop > 1) return true ;
		return false ;
	}
	
	int D1(int r, int c, int t)
	{
		int ans = c ;
		int re = 3 - t ;
		for(int i = c + 1 ; i < 8 ; i ++)
		{
			if(myData.board[r][i] == re) continue ;
			else if (myData.board[r][i] == t) {
				ans = i ;
				break ;
			}
			else break ;
		}
		return ans ;
	}
	
	int D2(int r, int c, int t)
	{
		int ans = c ;
		int re = 3 - t ;
		for(int i = c - 1 ; i >= 0 ; i --)
		{
			if(myData.board[r][i] == re) continue ;
			else if (myData.board[r][i] == t) {
				ans = i ;
				break ;
			}
			else break ;		
		}
		return ans ;
	}
	
	int D3(int r, int c, int t)
	{
		int ans = r ;
		int re = 3 - t ;
		for(int i = r + 1 ; i < 8 ; i ++)
		{
			if(myData.board[i][c] == re) continue ;
			else if (myData.board[i][c] == t) {
				ans = i ;
				break ;
			}
			else break ;
		}
		return ans ;
	}
	
	int D4(int r, int c, int t)
	{
		int ans = r ;
		int re = 3 - t ;
		for(int i = r -1 ; i >= 0 ; i --)
		{
			if(myData.board[i][c] == re) continue ;
			else if (myData.board[i][c] == t) {
				ans = i ;
				break ;
			}
			else break ;
		}
		return ans ;
	}
	
	int D5(int r, int c, int t)
	{
		int ans = 0 ;
		int re = 3 - t ;
		for(int i = 1 ; i < 8 ; i ++)
		{
			int x = r - i ;
			int y = c - i ;
			if(x < 0 || y < 0) break ;
			if(myData.board[x][y] == re) continue ;
			else if (myData.board[x][y] == t) {
				ans = i ;
				break ;
			}
			else break ;
		}
		return ans ;
	}
	
	int D6(int r, int c, int t)
	{
		int ans = 0 ;
		int re = 3 - t ;
		for(int i = 1 ; i < 8 ; i ++)
		{
			int x = r + i ;
			int y = c + i ;
			if(x >= 8 || y >= 8) break ;
			if(myData.board[x][y] == re) continue ;
			else if (myData.board[x][y] == t) {
				ans = i ;
				break ;
			}
			else break ;
		}
		return ans ;
	}
	
	int D7(int r, int c, int t)
	{
		int ans = 0 ;
		int re = 3 - t ;
		for(int i = 1 ; i < 8 ; i ++)
		{
			int x = r - i ;
			int y = c + i ;
			if(x < 0 || y >= 8) break ;
			if(myData.board[x][y] == re) continue ;
			else if (myData.board[x][y] == t) {
				ans = i ;
				break ;
			}
			else break ;
		}
		return ans ;
	}
	
	int D8(int r, int c, int t)
	{
		int ans = 0 ;
		int re = 3 - t ;
		for(int i = 1 ; i < 8 ; i ++)
		{
			int x = r + i ;
			int y = c - i ;
			if(x >= 8 || y < 0) break ;
			if(myData.board[x][y] == re) continue ;
			else if (myData.board[x][y] == t) {
				ans = i ;
				break ;
			}
			else break ;
		}
		return ans ;
	}
	
	public void showMsg(int r) {
		if(r== -1) return ;
	    else if(r == 1)
		{
			JOptionPane.showMessageDialog(null, "Black Win", "End", JOptionPane.INFORMATION_MESSAGE) ; 
		}
		else if(r== 2){
			JOptionPane.showMessageDialog(null, "White Win", "End", JOptionPane.INFORMATION_MESSAGE) ;
		}
		else if(r== 3){
			JOptionPane.showMessageDialog(null, "Tie", "End", JOptionPane.INFORMATION_MESSAGE) ;
		}
		else if (r == 4) {
			if(myData.cnt_black > myData.cnt_white) JOptionPane.showMessageDialog(null, "Black Win", "End", JOptionPane.INFORMATION_MESSAGE) ;
			else if (myData.cnt_black < myData.cnt_white) {
				JOptionPane.showMessageDialog(null, "White Win", "End", JOptionPane.INFORMATION_MESSAGE) ;
			}
			else {
				JOptionPane.showMessageDialog(null, "Tie", "End", JOptionPane.INFORMATION_MESSAGE) ;
			}
		}
	}
}
