package Model;

import java.net.*;

public class UDPServer 
{
   public static void main(String args[]) throws Exception
   {
	DatagramSocket serverSocket =new DatagramSocket(9876);

      byte[] receiveData = new byte[1024];
      byte[] sendData;

      
      while(true)
      {
         DatagramPacket receivePacket =
            new DatagramPacket(receiveData, receiveData.length);

         serverSocket.receive(receivePacket);

         String receiveMember = new String(receivePacket.getData(),0, receivePacket.getLength());
         
         //UDPprotocal pp=new UDPprotocal();
         
         String sendString=pp.inputAdd(receiveMember);
         
         if(sendString.isEmpty()){
        	 
         }
         else{
	         InetAddress IPAddress = receivePacket.getAddress();
	 		
	         int port = receivePacket.getPort();
	         
		
			 sendData = sendString.getBytes();
			
			 DatagramPacket sendPacket =
			 new DatagramPacket(sendData, sendData.length,
			                               IPAddress, port);
		
		     serverSocket.send(sendPacket);
         }
        	 
      }
   }
}