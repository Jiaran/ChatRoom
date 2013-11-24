package model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

public class UDPprotocal {
    
    MemberList totalList=new MemberList();
    
    public MemberList getList(String list){
    	String[] receiveSet=list.split(String.valueOf("%"));
		if(receiveSet[0].isEmpty()){
			System.out.println("Fail to get the list.");
			totalList.clearList();
			return null;
		}
		else{
			int k=Integer.decode(receiveSet[0]);
			for(int i=1;i<k+1;i++){
				String[] member=receiveSet[i].split(" ");
				if(member.length!=3){
					System.out.println("Fail to get the list.");
					totalList.clearList();
					return null;
				}
				else{
					totalList.addMember(member[0], member[1], member[2]);
				}
			}
		}
		return totalList;

    }
    
    public String requeString(String type, String name, String port) throws UnknownHostException{
    	String giveBack="";
    	if(type.equals("login")){
    		giveBack=type+" "+name+" ";
	    	try {
				giveBack+=InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	giveBack+=" "+port;
	    	return giveBack;
    	}
    	else if(type.equals("logout")){
    		giveBack=type;
    		giveBack+=" ";
    		giveBack+=InetAddress.getLocalHost().getHostAddress();
    		return giveBack;
    	}
    	else{
    		System.out.println("fail to send request.");
    		return null;
    	}
   }
    
	public String inputAdd(String inputString){
		if(inputString.isEmpty()){
       	 System.out.println("Someone failed to join the community.1");
        }
        else{
       	 String[] memberData=inputString.split(String.valueOf(' '));
       	 if(memberData[0].compareTo("login")==0){
	       	 if(memberData.length==4){
	       		 String name=memberData[1];
	       		 String ipAddress =memberData[2];
	       		 String portNumber = memberData[3];
	       		 
	       		 totalList.addMember(name, ipAddress, portNumber);
	       		 
	       		 Set<String> list=totalList.getList();
			     Iterator<String> it=list.iterator();
			         
			     String sendString=String.valueOf(totalList.getNumber());
			     sendString+="%";
			         
			     while(it.hasNext()){
			    	 Member returnMember=totalList.getMember(it.next());
			    	 sendString+=returnMember.getName();
			    	 sendString+=String.valueOf(' ');
			    	 sendString+=returnMember.getIP();
			    	 sendString+=String.valueOf(' ');
			    	 sendString+=returnMember.getPort();
			    	 sendString+=String.valueOf('%');
			    	 }
			     return sendString;
	       	 }
	       	 else{
	       		 System.out.println("Someone failed to join the community.2");
		        	 
	       	 }
       	 }
       	 else if(memberData[0].compareTo("logout")==0){
       		 if(memberData.length!=2){
       			System.out.println("Someone failed to leave the community.");
       		 }
       		 else{
       			String name=totalList.getMember(memberData[1]).getName();
       			totalList.removeMember(memberData[1]);
       			return name;
       		 }
       	 }
       	 else{
       		System.out.println("Incorrect operation.");
       		 return "";
       	 }
       	 }
		return "";
		
	}

}
