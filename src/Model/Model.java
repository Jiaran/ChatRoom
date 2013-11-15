package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Model {
    
    private String myPort="10001";
    private String myName="";
    private MemberList myTotalList= null;
    private MemberList myChatRoomList= new MemberList();
    private boolean isChatting = false;
    private boolean isClient=false;
    private TCPChatRoomServer myTCPServer= new TCPChatRoomServer(this);
    private List<TCPChatRoomClient> myTCPClients= new ArrayList<TCPChatRoomClient>();
    private List<String> myMessages= new ArrayList<String>();
    
    public Model(String name){
        myName=name;
        new Thread(myTCPServer).start();
    }
    
    
    public void addClientToChatRoom (String clientIP) {
        
        myChatRoomList.addMember(myTotalList.getMember(clientIP));
        
    }
    
    public void login(){
        UDPClient uc= new UDPClient();
        try{
            uc.logIn(myName, myPort);
            myTotalList= uc.getList();
        }
        catch (Exception e){
            System.out.println("Fail to Connect");
            System.exit(0);
        }
    }
    
    public void logout(){
        UDPClient uc= new UDPClient();
        try{
            uc.logOut();
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
        myChatRoomList.clearList();
    }
    
    public synchronized boolean isChatting(){
        return isChatting;
    }
    public synchronized void setIsChatting(boolean b){
        isChatting=b;
    }
    
    public void start(){
        Iterator<Member> it=myChatRoomList.getMembers().iterator();
        while(it.hasNext()){
            TCPChatRoomClient tc= new TCPChatRoomClient(it.next(),this);
            
            myTCPClients.add(tc);
        }
        myMessages.clear();
        myChatRoomList.clearList();
        isClient=true;
        
        
    }
    
    public void send(String fromUser){
        addMessage("I says: "+fromUser);
        fromUser=myName+" says: "+fromUser;
       
        if(isClient==true){
            for(int i=0; i< myTCPClients.size();i++){
                myTCPClients.get(i).send(fromUser);
            }
        }
        else{
            myTCPServer.send(fromUser);
        }
    }
    
    public void TCPdisconnect(){
        for(int i=0; i< myTCPClients.size();i++){
            myTCPClients.get(i).quit();
        }
        //myTCPServer.quit();
        myTCPClients.clear();
        System.out.println("quited");
    }
    
    public synchronized List<String> getMessages(){
        List<String> result= new ArrayList<String>(myMessages);
        myMessages.clear();
        return result;
    }

}
