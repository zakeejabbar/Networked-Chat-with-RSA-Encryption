import java.net.*;
import java.util.HashMap;
import java.util.Vector;
import java.io.*;
import java.math.BigInteger;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CentralServer extends JFrame implements ActionListener{
	JButton serverButton;
	JLabel machineInfo;
	JLabel portInfo;
	JTextArea history;
	private boolean running;
	HashMap<String, ClientInfo> clientInfo;
	
	Vector<ObjectOutputStream> outStreamList;

	boolean serverContinue;
	ServerSocket serverSocket;
	
	public CentralServer() {
		super("Central Server");
		
		running = false;
		
		Container container = getContentPane();
		container.setLayout(new FlowLayout());
		
		serverButton = new JButton("Start Listening");
		serverButton.addActionListener(this);
		outStreamList = new Vector<>();
		clientInfo = new HashMap<String, ClientInfo>();
		
		container.add(serverButton);
		String machineAddress = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			machineAddress = addr.getHostAddress();
		}
		catch(UnknownHostException e) {
			machineAddress = "127.0.0.1";
		}
		machineInfo = new JLabel(machineAddress);
		container.add(machineInfo);
		portInfo = new JLabel("Not Listening");
		container.add(portInfo);
		
		history = new JTextArea(10, 40);
		history.setEditable(false);
		container.add(new JScrollPane(history));
		
		setSize(500, 250);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(running == false) {
			new ConnectionThread(this);
		}
		else {
			serverContinue = false;
			serverButton.setText("Start Listening");
			portInfo.setText("Not Listening");
		}
	}

}

class ConnectionThread extends Thread
{
  CentralServer centralServer;
  
  public ConnectionThread (CentralServer cs)
  {
	centralServer = cs;
    start();
  }
  
  public void run()
  {
	  centralServer.serverContinue = true;
    
    try 
    { 
    	  centralServer.serverSocket = new ServerSocket(0); 
    	  centralServer.portInfo.setText("Listening on Port: " + centralServer.serverSocket.getLocalPort());
      System.out.println ("Connection Socket Created");
      try { 
        while (centralServer.serverContinue)
        {
          System.out.println ("Waiting for Connection");
          centralServer.serverButton.setText("Stop Listening");
          new CommunicationThread (centralServer.serverSocket.accept(), centralServer, centralServer.outStreamList, centralServer.clientInfo); 
        }
      } 
      catch (IOException e) 
      { 
        System.err.println("Accept failed."); 
        System.exit(1); 
      } 
    } 
    catch (IOException e) 
    { 
      System.err.println("Could not listen on port: 10008."); 
      System.exit(1); 
    } 
    finally
    {
      try {
    	  	centralServer.serverSocket.close(); 
      }
      catch (IOException e)
      { 
        System.err.println("Could not close port: 10008."); 
        System.exit(1); 
      } 
    }
  }
}


class CommunicationThread extends Thread
{ 

    private Socket clientSocket;
    private CentralServer centralServer;
    private Vector<ObjectOutputStream> outStreamList;
    private HashMap<String, ClientInfo> clientInfo;



    public CommunicationThread (Socket clientSoc, CentralServer cs, Vector<ObjectOutputStream> osl, HashMap<String, ClientInfo> info)
    {
        clientSocket = clientSoc;
        centralServer = cs;
        centralServer.history.insert("Communicating with Port " + clientSocket.getLocalPort() + "\n", 0);
        outStreamList = osl;
        clientInfo = info;
        start();
    }

    public void run()
    {
       System.out.println ("New Communication Thread Started");

       try
       {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            outStreamList.add(out);

            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            MessageObject inputObject;
            Vector<BigInteger> encryptedMessage;

            while ((inputObject = (MessageObject) in.readObject()) != null)
            {
                 char messageType = inputObject.getType();
                 String name;
                 switch(messageType)
                 {
                 case 'A':
                     if(clientInfo.containsKey(inputObject.getName()))
                     {
                         MessageObject quit = new MessageObject('Q');
                         out.writeObject(quit);
                         //out.flush();
                         out.reset();
                     }
                     else
                     {
                	 /*==================IMMEDIATELY ADD EXISTING CLIENTS TO NEW CLIENT===================================*/
                         for (String key_name : clientInfo.keySet()) {
                             ClientInfo c = clientInfo.get(key_name);
                             MessageObject update = new MessageObject('U', c.getPublicKey(), key_name);
                             out.writeObject(update);
                             //out.flush();
                             out.reset();
                         }

                         name = inputObject.getName();

                         Pair publicKey = inputObject.getPublicKey();
                         clientInfo.put(name, new ClientInfo(publicKey, name, out));

                         centralServer.history.insert("User added: " + name + "\n", 0);
                     /*==================INFORM OTHER USERS THAT NEW CLIENT HAS BEEN ADDED=================================*/
                         for (String key_name : clientInfo.keySet()) {
                             if (key_name != name) {
                                 ClientInfo c = clientInfo.get(key_name);
                                 System.out.println("Sending Message");
                                 c.getOut().writeObject(inputObject);
                                 //c.getOut().flush();
                                 c.getOut().reset();
                             }
                         }
                     }
                        break;

                     case 'D':
                    	 	name = inputObject.getName();
                    	 	clientInfo.remove(name);
                    	 	for(String key_name: clientInfo.keySet()) {
                    	 		if(key_name != name)
                    	 		{
                                    ClientInfo c = clientInfo.get(key_name);
                                    MessageObject update = new MessageObject('D', name);
                                    c.getOut().writeObject(update);
                                    c.getOut().flush();
                    	 		}
     	             	 }
                         

                         break;


                     case 'M':
                         System.out.println("Received message!");
                         name = inputObject.getName();
                         encryptedMessage = inputObject.getEncryptedValues();
                         System.out.println("in server received");
                         for(BigInteger b: encryptedMessage)
                         {
                             System.out.println(b);
                         }

                         for (String key_name : clientInfo.keySet())
                         {
                        	 	ClientInfo c = clientInfo.get(key_name);
                        	 	if(key_name.equals(name))
                        	 	{
                                     System.out.println ("Sending Message");
                                     c.getOut().writeObject (inputObject);
                                     c.getOut().reset();
                        	 	}
                         }
                         encryptedMessage.removeAllElements();
                         break;
                 }
            }

            outStreamList.remove(out);
            out.close();
            in.close();
            clientSocket.close();
       }
       catch (IOException e)
       {
            System.err.println("Problem with Communication Server");
            e.printStackTrace();
       }
       catch(ClassNotFoundException cE)
       {
           System.err.println("Object class not found!");
       }
    }
} 







