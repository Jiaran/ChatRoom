package Model;

import java.io.IOException;

public class UDPtest {
	public static void main(String[] args) throws Exception{
		UDPClient tom=new UDPClient();
	
		tom.logIn("tom","9999");

		tom.logOut();
		
	}
}
