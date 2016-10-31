
public class Client implements Runnable{

	String Ip  = "127.0.0.1" ;
	public Client(String ip) {
		// TODO Auto-generated constructor stub
		Ip = ip ;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ChessBoardOnline myclient = new ChessBoardOnline(2, Ip) ;
	}

}
