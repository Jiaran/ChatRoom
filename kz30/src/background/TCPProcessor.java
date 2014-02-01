package background;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller.Controller;





//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 *@author Kangyi Zhang
 *This background processor handles all data management. It generate a listening socket to 
 *wait for others users to start a conversation. It also could build a socket to start an 
 *conversation with another user. During chatting, the listening socket cannot start another 
 *conversation with else, which means the application cannot accept invitation from others.  
 */

public class TCPProcessor {
    private String myPortNumber="9999";
    private String UDPServerIP="";
    private String myName="";
    private Member mySelf=null;
    private MemberList myTotalList= null;
    private boolean ifChatting = false;
    private boolean ifClient=false;
    private List<String> myRecords= new ArrayList<String>();
    private TCPlistening runListening= new TCPlistening(this);
    private Controller myStarter;
    private Map<Integer,String> myRTTMap= new HashMap<Integer,String>();
    
    //Client
    private int clientPortNumber = 9998;
    private TCPProcessor myTCPProcessor=this;
    private String myIPAddress="localhost";
    private String clientName="";
    private PrintWriter clientOut=null;
    private BufferedReader clientIn=null;
    private Socket clientSocket=null;
    private int clientMessageID=0;
    private Map<Integer, Long> clientMessageSendTime= new HashMap<Integer, Long>();
    
    /**
     * This Class is a thread, which is always running in the background, 
     * and always listening to the network, waiting for others user's to
     * connect and start chat 
     */
    public class TCPlistening implements Runnable{
	    private TCPProcessor listenModel=null;
	    int listenPortNumber =0;
	    private ServerSocket serverSocket=null;
	    private PrintWriter serverOut=null;
	    private BufferedReader serverIn=null;
	    private Socket newSocket=null;
	    private int listenID=0;
	    private Map<Integer, Long> mySendTimeList= new HashMap<Integer, Long>();
	    
	    public TCPlistening(TCPProcessor processorS){
	        listenModel=processorS;
	        try {
	            serverSocket = new ServerSocket(listenPortNumber);
	        }
	        catch(Exception e){
	            e.printStackTrace();
	        }
	    }
	    
	    public void run()   {
	        while(true){
	            try {
	            	Socket tempSocket=serverSocket.accept();
	            	if(ifChatting){
	            		System.out.print("xxxxxxxxxxxxxxxxxxxx");
	            		System.out.print(ifChatting+"xxxxxxxxxxxxxxxxxxxxxxxx");
	            		PrintWriter tempOut =new PrintWriter(tempSocket.getOutputStream(), true);
	                    tempOut.println("NO");
	                    System.out.println("no accept xxxxxxxxx");
	                    tempSocket.close();
	            	}
	            	else if (newSocket == null|| newSocket.isClosed()) {
	                    newSocket = tempSocket;
	                    new Thread(new TCPPassiveListeningThread(newSocket)).start();
	                }
	            }
	            catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    public void sendMessage(String fromWhom) {
	        if(serverOut==null){
	            listenModel.addMessage("Sorry, your friend left");
	            return;
	        }
	        if (fromWhom != null) {
	        	serverOut.println(listenID+"%"+fromWhom);
	            listenModel.addToMessageMap(listenID, fromWhom);
	            mySendTimeList.put(listenID, System.currentTimeMillis());
	            listenID++;
	        }
	    }
	    
	    public void quit () {
	        if (serverOut != null) {
	            serverOut.println("NOREPLY%" + listenModel.getName() + " left the chat room");
	            serverOut.println("EXIT");
	        }
	    }
	    
	    private void exit(){
	        try {
	            if (serverOut != null){
	                serverOut.close();
	            }
	            if (serverIn != null)
	                serverIn.close();
	            if (newSocket != null && !newSocket.isClosed()){
	                newSocket.shutdownOutput();
	                newSocket.close();
	            }
	            serverOut=null;
	            serverIn=null;
	            newSocket = null;
	            mySendTimeList.clear();
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    class TCPPassiveListeningThread implements Runnable{
	        private Socket clientSocket= null;
	        public TCPPassiveListeningThread(Socket s){
	            clientSocket= s;
	        }
	        public void run () {
	            try {
	                serverOut =new PrintWriter(clientSocket.getOutputStream(), true);
	                serverIn =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	                String keyInput;
	                keyInput = serverIn.readLine();
	                if (listenModel.query(keyInput) == JOptionPane.YES_OPTION) {
	                    serverOut.println("Yes");
	                    ifChatting=true;
	                    while ((keyInput = serverIn.readLine()) != null) {
	                        if(keyInput.equals("EXIT")){
	                            serverOut.println("EXIT");
	                            break;
	                        }
	                        if(keyInput.matches("RECEIVE\\d*")){
	                            String uniqueIDString=keyInput.substring(7);
	                            int uniqueID= Integer.parseInt(uniqueIDString);
	                            long sendTime= mySendTimeList.get(uniqueID);
	                            long rtTime= System.currentTimeMillis()-sendTime;
	                            listenModel.addMessage(uniqueID, rtTime);
	                            continue;
	                        }
	                        if(keyInput.matches("NOREPLY.*")){
	                            String[] temp=keyInput.split("%", 2);
	                            keyInput=temp[1];
	                            listenModel.addMessage(keyInput);
	                            continue;
	                        }
	                        System.out.println(keyInput);
	                        String[] temp=keyInput.split("%", 2);
	                        serverOut.println("RECEIVE"+temp[0]);
	                        keyInput=temp[1];
	                        listenModel.addMessage(keyInput);
	                    }
	                }
	                exit();
	                System.out.println("server down");
	                clientSocket = null;
	            }
	            catch (IOException e) {
	                System.out.println("Exception caught when trying to listen on port "
	                                   + listenPortNumber + " or listening for a connection");
	                System.out.println(e.getMessage());
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    public Integer getPort(){
	        return serverSocket.getLocalPort();
	    }
	}
    
    
    public TCPProcessor(String name){
        myName=name;
        myPortNumber= runListening.getPort().toString();
        new Thread(runListening).start();
    }
    
    public void addClientToChatRoom (String clientName) {
        mySelf=myTotalList.getMember(clientName);   
    }
    
    public void login(){
        UDPClient udpClient= new UDPClient(UDPServerIP);
        try{
            udpClient.logIn(myName, myPortNumber);
            myTotalList= udpClient.getList();
            if(!myTotalList.getIsValid()){
                JOptionPane.showMessageDialog(null, "Name has been used");
                System.exit(0);
            }
        }
        catch (Exception e){
            System.out.println("Fail to Connect");
            JOptionPane.showMessageDialog(null, "Fail to Connect to the Server");
            System.exit(0);
        }
    }
    
    public void refresh(){
        UDPClient uc= new UDPClient(UDPServerIP);
        try{
            uc.logIn("", myPortNumber);
            myTotalList= uc.getList();
        }
        catch (Exception e){
            System.out.println("Fail to Connect");
            System.exit(0);
        }
    }
    
    public void logout(){
        UDPClient uc= new UDPClient(UDPServerIP);
        try{
            uc.logOut(myName);
        }
        catch (Exception e){
            System.out.println("Fail to Connect");  
        }
        
    }
    
    public MemberList getTotalList(){
        return myTotalList;
    }


    public synchronized void addMessage (String str) {
        myRecords.add(str);
        System.out.println(str);
    }
    
    public void clearChatRoomList(){
       mySelf=null;
    }
    
    public synchronized boolean whetherIsChatting(){
        return ifChatting;
    }
    public synchronized void setIsChatting(boolean a){
        ifChatting=a;
    }
    
    public boolean begin(){
        if(mySelf==null){
            return false;
        }
        TCPClientBuild(mySelf);
        myRecords.clear();
        clearChatRoomList();
        ifClient = true;
        return true;
    }
    
    public void send(String fromUser){
        fromUser=fromUser.replace('\n', ' ');
        addMessage("I says: "+fromUser);
        fromUser=myName+" says: "+fromUser;
        if(ifClient==true){
           sendMessage(fromUser,clientMessageID);
        }
        else{
        	runListening.sendMessage(fromUser);
        }
    }
    
    public void disconnect(){
    	if(ifClient)
    		quit();
    	runListening.quit();
    	clientOut=null;
    	clientIn=null;
    	clientSocket=null;
        System.out.println("quited");
        ifClient = false;
    }
    
    public synchronized List<String> getMessageList(){
        List<String> messageList= new ArrayList<String>(myRecords);
        myRecords.clear();
        return messageList;
    }


    public int query(String who) {
        queryThread at = new queryThread(who);
        try {
            SwingUtilities.invokeAndWait(at);
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(at.getFlag()==JOptionPane.YES_OPTION){
            myStarter.showChatRoom();
        }
        return at.getFlag();

    }
    
    private class queryThread implements Runnable {
        private String queryResultName=null;
        private int judgeValue=-1;
        public queryThread(String w){
            queryResultName=w;
        }
        public void run () {
            judgeValue=JOptionPane.showConfirmDialog(null, queryResultName+" invite you to chat, accept? ");
        }
        
        public int getFlag(){
            return judgeValue;
        }
    }

     public void setStarter (Controller starter) {
        myStarter = starter;
    }
     
     public String getName(){
         return myName;
     }
     
     public void addToMessageMap(int uniqueID, String content){
         myRTTMap.put(uniqueID, content);
     }
     
     public void addMessage(int uniqueID, long rtt){
         String message=myRTTMap.get(uniqueID);
         message= message + " /////this message's round trip time is "+rtt+" ms";
         myRTTMap.put(uniqueID, message);
     }
     
     public void setUDPServerAddress(String uspServerAddress){
         UDPServerIP= uspServerAddress;
     }
     
     public List< String> getRTTList(){
         List<String> returnedList= new ArrayList<String>();
         Set<Entry<Integer,String>> set=myRTTMap.entrySet();
         Iterator<Entry<Integer,String>> it = set.iterator();
         while(it.hasNext()){
             Entry<Integer,String> entry= it.next();
             returnedList.add(entry.getValue());
         }
         myRTTMap.clear();
         return returnedList;
     }
     
     /////////////////////////////////client
     public void TCPClientBuild(Member member){
         myIPAddress=member.getIP();
         clientPortNumber= Integer.parseInt(member.getPort());
         clientName= member.getName();
         establishClient(clientSocket);
     }
     
     public void sendMessage(String fromWho,int messageID) {
    	 System.out.print(clientOut);
         if(clientOut==null){
             this.addMessage("Sorry, your friend left");
             return;
         }
         if (fromWho != null) {    
             clientOut.println(messageID+"%"+fromWho);
             System.out.println(messageID+"%"+fromWho);
             this.addToMessageMap(messageID, fromWho);
             clientMessageSendTime.put(messageID, System.currentTimeMillis());
             messageID++;
         }
     }
     
     public synchronized void quit () {
         if(clientOut!=null){
        	 clientOut.println("NOREPLY%"+clientName+" left the chat room");
        	 clientOut.println("EXIT");
         }
     }
     
     public void exit(){
         try {
             if (clientSocket != null) {
            	 clientSocket.shutdownOutput();
             }
             if (clientIn != null)
            	 clientIn.close();
             if (clientOut != null){
            	 clientOut.close();
             }
             clientOut = null;
             clientIn = null;
             clientSocket = null;
         }
         catch (IOException e) {
             e.printStackTrace();
         }
     }
     
     public void establishClient (Socket thisSocket) {
         try {
        	 thisSocket= new Socket(myIPAddress, clientPortNumber);
             System.out.println(myIPAddress+" "+clientPortNumber);
             clientOut = new PrintWriter(thisSocket.getOutputStream(), true);
             clientIn = new BufferedReader(new InputStreamReader(thisSocket.getInputStream()));
             clientOut.println(myTCPProcessor.getName());
             new Thread(new TCPactiveListeningThread()).start();
         }
         catch (UnknownHostException e) {
             System.err.println("Don't know about host " + myIPAddress);
             System.exit(1);
         }
         catch (IOException e) {
             System.err.println("Couldn't get I/O for the connection to " + myIPAddress);
             e.printStackTrace();
             System.exit(1);
         }
     }

     class TCPactiveListeningThread implements Runnable{
         public void run () {
             String receivedMessage;
             try {
                 String ifAccept="";
                 ifAccept= clientIn.readLine();
                 System.out.print(ifAccept);
                 if(ifAccept==null ||!ifAccept.equals("Yes") ){
                     exit();
                     return;
                 }
                 myTCPProcessor.setIsChatting(true);
				while ((receivedMessage = clientIn.readLine()) != null) {
					if (receivedMessage.equals("EXIT")) {
						clientOut.println("EXIT");
						break;
					}
					if (receivedMessage.matches("RECEIVE\\d*")) {
						String uniqueIDString = receivedMessage.substring(7);
						int uniqueID = Integer.parseInt(uniqueIDString);
						long sendTime = clientMessageSendTime.get(uniqueID);
						long rtt = System.currentTimeMillis() - sendTime;
						myTCPProcessor.addMessage(uniqueID, rtt);
						continue;
					}
					if (receivedMessage.matches("NOREPLY.*")) {
						String[] temp = receivedMessage.split("%", 2);
						receivedMessage = temp[1];
						myTCPProcessor.addMessage(receivedMessage);
						continue;
					}
					String[] temp = receivedMessage.split("%", 2);
					System.out.println(temp[0]);
					clientOut.println("RECEIVE" + temp[0]);
					receivedMessage = temp[1];
					myTCPProcessor.addMessage(receivedMessage);
				}
				System.out.print("yyyyyyyyyyyyyyyyyyyy");
				exit();
			}
             catch (IOException e) {
                 System.out.println(clientIn);
                 e.printStackTrace();
             }
         }
     }


}
