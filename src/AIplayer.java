import java.awt.Point;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.GroupLayout.SequentialGroup;

/*
 * 
 * created by shaoyq 16/8/3 javaEE
 */
public class AIplayer {
	int diff = 0 ;
	Data now = new Data() ;
	int[][] gu = new int[8][8] ;
	Point point = new Point() ;
	
	public AIplayer(int di) {
		// TODO Auto-generated constructor stub
		diff = di ;
		if(diff == 1)
		{
			Setgu() ;
		}
	}
	
	void Setgu()
	{
		gu[0][0] = 4 ; gu[0][7] = 4 ; gu[7][0] = 4 ; gu[7][7] = 4 ;
		gu[1][1] = 0 ; gu[1][6] = 0 ; gu[6][1] = 0 ; gu[6][6] = 0 ;
		gu[0][1] = 1 ; gu[0][6] = 1 ; gu[1][0] = 1 ; gu[1][6] = 1 ;
		gu[6][1] = 1 ; gu[6][6] = 1 ; gu[7][1] = 1 ; gu[7][6] = 1 ;
		for(int i = 2 ; i <= 5 ; i ++) 
		{
			gu[1][i] = 1 ;
			gu[0][i] = 3 ;
			gu[6][i] = 1 ;
			gu[7][i] = 3 ;
			gu[i][0] = 3 ;
			gu[i][1] = 1 ;
			gu[i][6] = 1 ;
			gu[i][7] = 3 ;
		}
		for(int i = 2 ; i <= 5 ; i ++)
		{
			for(int j = 2 ; j <= 5 ; j ++)
			{
				gu[i][j] = 2 ;
			}
		}
	}
	
	public Point makeMove(Data d) {
		now = d ;
		if(diff == 0)
		{
			ArrayList<Point> array = new ArrayList<Point>() ;
			for(int i = 0 ; i < 8 ; i ++)
			{
				for(int j = 0 ; j < 8 ; j ++)
				{
					if(now.board[i][j] == 3)
					{
						array.add(new Point(i, j)) ;
					}
				}
			}
			int size = array.size() ;
			Random random = new Random() ;
			int choose = random.nextInt(size) ;
			return array.get(choose) ;
		}
		else {
			HashMap<Integer, ArrayList<Point>> myMap = new HashMap<Integer, ArrayList<Point>>() ;
			for(int i = 0 ; i < 5 ; i ++)
			{
				myMap.put(i, new ArrayList<Point>()) ;
			}
			for(int i = 0 ; i < 8 ; i ++)
			{
				for(int j = 0 ; j < 8 ; j ++)
				{
					if(now.board[i][j] == 3)
					{
						int fen = gu[i][j] ;
						myMap.get(fen).add(new Point(i, j)) ;
					}
				}
			}
			int high = 0 ;
			for(int i = 4 ; i >= 0 ; i --)
			{
				if(myMap.get(i).isEmpty() == false)
				{
					high = i ;
					break ;
				}
			}
			int size = myMap.get(high).size() ;
			Random random = new Random() ;
			int choose = random.nextInt(size) ;
			return myMap.get(high).get(choose) ;
		}
	}
}
