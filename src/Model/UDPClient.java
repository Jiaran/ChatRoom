package Model;

import java.io.*;
import java.net.*;

public class UDPClient 
{
	private MemberList friends;
	
	public MemberList getList(){
		return friends;
	}
	
	public void logIn(String inputName, String inputPort) throws IOException{
		UDPClientLog("login",inputName,inputPort);
	}
	
	public void logOut() throws IOException{
			UDPClientLog("logout","null","null");
	}
	
	public void UDPClientLog(String type, String inputName, String inputPort) throws IOException{
		int ServerPort=9876;

		DatagramSocket clientSocket = new DatagramSocket();

		InetAddress IPAddress = InetAddress.getLocalHost();
		//replace it with true server name
		
		byte[] sendData;

		byte[] receiveData = new byte[1024];
		
		UDPprotocal pp=new UDPprotocal();
		
		String sentence="";
		
		sentence=pp.requeString(type, inputName, inputPort);
		System.out.println(sentence);

		if(sentence.isEmpty()){
			System.out.println("fail to send request.");
		}
		else{
			
			sendData = sentence.getBytes();
	
			DatagramPacket sendPacket =new DatagramPacket(sendData, sendData.length,
			                            IPAddress, ServerPort);
	
			clientSocket.send(sendPacket);
			if(type.equals("login")){
				System.out.println("sending request");
		
				DatagramPacket receivePacket =new DatagramPacket(receiveData, receiveData.length);
		
				clientSocket.receive(receivePacket);
		
				String receiveList = new String(receivePacket.getData(),0, receivePacket.getLength());
				
				friends=pp.getList(receiveList);
				
				System.out.println("get friends list.");
			}
			clientSocket.close();
		}
		
	}
	
}