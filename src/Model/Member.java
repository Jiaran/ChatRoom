package Model;




public class Member {
	private String MemberName;
	private String MemberIpAddress;
	private String MemberPortNumber;
	
	public Member(){
		
	}
	
	public Member(String memberName, String memberIP, String memberPort){
		if(memberName.isEmpty()||memberIP.isEmpty()||memberPort.isEmpty()){
			return;
		}
		else{
			MemberName=memberName;
			MemberIpAddress=memberIP;
			MemberPortNumber=memberPort;
			}
	}
	
	public String getName(){
		return MemberName;
	}
	
	public String getIP(){
		return MemberIpAddress;
	}
	
	public String getPort(){
		return MemberPortNumber;
	}

}
