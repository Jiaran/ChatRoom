package Model;

public class Model {
    
    private String myPort="10000";
    private String myName="";
    MemberList totalList= null;
    MemberList chatRoomList= new MemberList();
    public Model(String name){
        myName=name;
    }
    
    
    public void addClientToChatRoom (String clientIP) {
        
        chatRoomList.addMember(totalList.getMember(clientIP));
        
    }
    
    public void login(){
        UDPClient uc= new UDPClient();
        uc.addClient(myName);
    }
    
    public void logout(){
        UDPClient uc= new UDPClient();
        uc.addClient(myName);
    }
    
    public MemberList getTotalList(){
        return null;
    }
    
    

}
