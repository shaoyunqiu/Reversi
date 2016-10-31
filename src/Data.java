//created by shaoyq 16/7/27 javaEE
public class Data {
	int[][] board = new int[8][8] ;
	int turn = 1; // 1 black, 2 white ;
	int cnt_black = 0;
	int cnt_white = 0;
	int[] huiqi = new int[2] ;
	public void Copy(Data d)
	{
		for(int i = 0 ; i < 8 ; i ++)
		{
			for(int j = 0 ; j < 8 ; j ++)
			{
				board[i][j] = d.board[i][j] ;
			}
		}
		turn = d.turn ;
		cnt_black = d.cnt_black ;
		cnt_white = d.cnt_white ;
		huiqi[0] = d.huiqi[0] ;
		huiqi[1] = d.huiqi[1] ;
	}
}
