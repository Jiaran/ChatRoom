package model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import controller.Controller;

public class Model {
    
    private String myPort="10001";
    private String myName="";
    private MemberList myTotalList= null;
    private Member myChatter=null;
    private boolean isChatting = false;
    private boolean isClient=false;
    private TCPChatRoomServer myTCPServer= new TCPChatRoomServer(this);
    private TCPChatRoomClient myTCPClient= null;
    private List<String> myMessages= new ArrayList<String>();
    private Controller myController;
    
    public Model(String name){
        myName=name;
        new Thread(myTCPServer).start();
    }
    
    
    public void addClientToChatRoom (String clientIP) {
        
        myChatter=myTotalList.getMember(clientIP);
        
    }
    
    public void login(){
        UDPClient uc= new UDPClient();
        try{
            uc.logIn(myName, myPort);
            myTotalList= uc.getList();
            //return myTotalList.getIsValid();
        }
        catch (Exception e){
            System.out.println("Fail to Connect");
            System.exit(0);
        }
    }
    
    public void refresh(){
        UDPClient uc= new UDPClient();
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
        UDPClient uc= new UDPClient();
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

}
