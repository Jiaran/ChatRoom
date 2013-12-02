package model;

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

/**
 * @author Jiaran
 *      Model handles all the data management. It keeps one listening socket to receive 
 *      connection with other user . Also it can establish its own socket to communicate
 *      with others. So one model can be both accept others chat invitation or invite others
 *      to chat. To be simple, we this client is chatting with other peers, it will automatically
 *      turn down invitations. However, it's easy to modify it to handle multiple chat. But 
 *      there's no point as we don't have many clients online at the same time.
 */
public class Model {
    
    private String myPort="10001";
    private String myUDPServerAddress= "localhost";
    private String myName="";
    private MemberList myTotalList= null;
    private Member myChatter=null;
    private boolean isChatting = false;
    private boolean isClient=false;
    private TCPChatRoomServer myTCPServer= new TCPChatRoomServer(this);
    private TCPChatRoomClient myTCPClient= null;
    private List<String> myMessages= new ArrayList<String>();
    private Controller myController;
    private Map<Integer,String> myRTTMap= new HashMap<Integer,String>();
    public Model(String name){
        myName=name;
        myPort= myTCPServer.getPort().toString();
        new Thread(myTCPServer).start();
    }
    
    
    public void addClientToChatRoom (String clientName) {
        
        myChatter=myTotalList.getMember(clientName);
        
    }
    
    public void login(){
        UDPClient uc= new UDPClient(myUDPServerAddress);
        try{
            uc.logIn(myName, myPort);
            myTotalList= uc.getList();
            if(!myTotalList.getIsValid()){
                JOptionPane.showMessageDialog(null, "Name is occupied");
                System.exit(0);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Fail to Connect to the Server");
            System.exit(0);
           
        }
    }
    
    public void refresh(){
        UDPClient uc= new UDPClient(myUDPServerAddress);
        try{
            uc.logIn("", myPort);
            myTotalList= uc.getList();
            //return myTotalList.getIsValid();
        }
        catch (Exception e){
            System.out.println("Fail to Connect");
            System.exit(0);
        }
    }
    
    public void logout(){
        UDPClient uc= new UDPClient(myUDPServerAddress);
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
        myMessages.add(str);
        System.out.println(str);
        
    }
    
    public void clearChatRoomList(){
       myChatter=null;
    }
    
    public synchronized boolean isChatting(){
        return isChatting;
    }
    public synchronized void setIsChatting(boolean b){
        isChatting=b;
    }
    
    public boolean start(){
        if(myChatter==null){
            return false;
        }
        myTCPClient = new TCPChatRoomClient(myChatter, this);

        myMessages.clear();
        clearChatRoomList();
        isClient = true;
        return true;
        
        
    }
    
    public void send(String fromUser){
        fromUser=fromUser.replace('\n', ' ');
        addMessage("I says: "+fromUser);
        fromUser=myName+" says: "+fromUser;
       
        if(isClient==true){
           myTCPClient.send(fromUser);
        }
        else{
            myTCPServer.send(fromUser);
        }
    }
    
    public void TCPdisconnect(){
    	System.out.println(isClient);
    	if(isClient)
    		myTCPClient.quit();
    	
        myTCPServer.quit();
        myTCPClient = null;
        System.out.println("quited");
        isClient = false;
    }
    
    public synchronized List<String> getMessages(){
        List<String> result= new ArrayList<String>(myMessages);
        myMessages.clear();
        return result;
    }


    public int ask (String who) {
        AskThread at = new AskThread(who);

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
            myController.showChatRoom();
        }
        return at.getFlag();

    }
    
    private class AskThread implements Runnable {
        private String who=null;
        private int flag=-1;
        public AskThread(String w){
            who=w;
            
        }
        @Override
        public void run () {
            flag=JOptionPane.showConfirmDialog(null, who+" invite you to chat, accept? ");
            
        }
        
        public int getFlag(){
            return flag;
        }
    }

     public void setController (Controller controller) {
        myController = controller;
        
    }
     
     public String getName(){
         return myName;
     }
     
     public void addToRTTMap(int uniqueID, String content){
         myRTTMap.put(uniqueID, content);
     }
     public void addRTT(int uniqueID, long rtt){
         String content=myRTTMap.get(uniqueID);
         content= content + " ::::this message's round trip time is "+rtt+"ms";
         
         myRTTMap.put(uniqueID, content);
     }
     
     public List< String> getRTT(){
         List<String> result= new ArrayList<String>();
        
         Set<Entry<Integer,String>> set=myRTTMap.entrySet();
         Iterator<Entry<Integer,String>> it = set.iterator();
         while(it.hasNext()){
             Entry<Integer,String> entry= it.next();
             result.add(entry.getValue());
         }
         myRTTMap.clear();
         return result;
         
     }
     
     public void setUDPServerAddress(String serverAddress){
         myUDPServerAddress= serverAddress;
     }
}
