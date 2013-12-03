package background;

import java.io.*;
import java.net.*;

public class UDPClient 
{
	private static final int TIMEOUT=5000;
	
	private MemberList friends;
	
	private String serverAddress;
	
	public UDPClient(String serverIP){
		serverAddress=serverIP;
	}
	
	public MemberList getList(){
		return friends;
	}
	
	public void logIn(String inputName, String inputPort) throws IOException{
		if(inputName.equals(""))
			UDPClientLog("refresh","null",inputPort);
		else{
			UDPClientLog("login",inputName,inputPort);
		}
	}
	
	public void logOut(String inputName) throws IOException{
			UDPClientLog("logout",inputName,"null");
	}
	
	public void UDPClientLog(String type, String inputName, String inputPort) throws IOException{
		int ServerPort=9876;

		DatagramSocket clientSocket = new DatagramSocket();
		clientSocket.setSoTimeout(TIMEOUT); 
		
		//InetAddress IPAddress=InetAddress.getByName("152.3.43.164");

		InetAddress IPAddress = InetAddress.getByName(serverAddress);
		//IPAddress = InetAddress.getByName("10.190.144.128");
		//replace it with true server name
		
		byte[] sendData;

		byte[] receiveData = new byte[1024];
		
		UDPprotocal pp=new UDPprotocal();
		
		String sentence="";
		
		sentence=pp.requeString(type, inputName, inputPort);

		if(sentence.isEmpty()){
			System.out.println("fail to send request.");
		}
		else{
			
			sendData = sentence.getBytes();
	
			DatagramPacket sendPacket =new DatagramPacket(sendData, sendData.length,
			                            IPAddress, ServerPort);
			
			long beginTime = System.currentTimeMillis();
			
			clientSocket.send(sendPacket);
			if(!type.equals("logout")){
				//System.out.println("sending request");
		
				DatagramPacket receivePacket =new DatagramPacket(receiveData, receiveData.length);
		
				clientSocket.receive(receivePacket);
				
				long endTime=System.currentTimeMillis();

				long costTime = (endTime - beginTime);
				System.out.print("The RRT is "+String.valueOf(costTime)+" ms");
		
				String receiveList = new String(receivePacket.getData(),0, receivePacket.getLength());
				
				friends=pp.getList(receiveList);
				
				//System.out.println("get friends list.");
			}
			clientSocket.close();
		}
		
	}
	
}