import javax.swing.JFrame;

public class ChatServer
{
	public static void main(String[] args) {
		CentralServer server  = new CentralServer();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
