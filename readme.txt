Zakee Jabbar UIC


"Networked Chat with RSA Encryption"


This is a GUI version of a chat room. In order for the chatroom to work, a server needs to be started first. To start the server execute ChatServer.java and click Start Listening. Once the server has started listening, then multiple clients can connect. For the clients to connect to the server, click on Connect -> Start Connection, enter the IP address and port number of the server, p and q which should be any prime numbers over 16411 (if you would like the program to pick the numbers put any number between 1 - 10), and a name. Once each user is connected they can start sending messages to specific people that are in the chat room which are encrypted via RSA. Users can chose to exit at any time.



Supported Functions:

Server Program:
Start Listening


Client Program:
Quit -> Quit : Closes the connection and exits the program.
Connect -> Start Connection: Opens a dialog box to enter IP address, port number, p, q and name to connect to the server.




To run this program on command line:


Server:
javac ChatServer.java
java ChatServer


Client(s):
javac ChatClient.java
java ChatClient
