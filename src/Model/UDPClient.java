package Model;

import java.io.*;
import java.net.*;

public class UDPClient 
{
	private MemberList friends;
	
	public MemberList getList(){
		return friends;
	}
	
	public void addClient(String type, String inputName, String inputPort) throws IOException{
		if (type.compareTo("login")==0){
			UDPClientLog("login",inputName,inputPort);
		}
		else{
			System.out.println("fail to login");
		}
	}
	
	public void addClient(String type) throws IOException{
		if(type.compareTo("logout")==0){
			UDPClientLog("logout","null","null");
		}
		else{
			System.out.print("fail to logout");
		}
	}
	
	public void UDPClientLog(String type, String inputName, String inputPort) throws IOException{
		int ServerPort=9876;

		DatagramSocket clientSocket = new DatagramSocket();

		InetAddress IPAddress = InetAddress.getLocalHost();
		//replace it with true server name
		
		byte[] sendData;

		byte[] receiveData = new byte[1024];
		
		UDPprotocal pp=new UDPprotocal();
		
		String sentence=pp.requeString(type, inputName, inputPort);

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