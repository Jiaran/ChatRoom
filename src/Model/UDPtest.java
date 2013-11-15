package model;

import java.io.IOException;

public class UDPtest {
	public static void main(String[] args) throws Exception{
		UDPClient tom=new UDPClient();
	
		tom.logIn("tom","9999");

		tom.logOut();
		
		UDPClient bell=new UDPClient();
		
		bell.logIn("bell", "1234");
		bell.logOut();
		
	}
}
