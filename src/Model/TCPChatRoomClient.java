package model;

/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
 
public class TCPChatRoomClient {
    private String hostName="localhost";
    private String clientName="";
    private int portNumber = 10000;
    private Model myModel=null;
    PrintWriter out=null;
    BufferedReader in=null;
    Socket socket=null;
    public TCPChatRoomClient(Model model){
        myModel=model;
    }
    public TCPChatRoomClient(Member m, Model model){
        this(model);
        hostName=m.getIP();
        portNumber= Integer.parseInt(m.getPort());
        clientName= m.getName();
        establish();
    }
    public void send(String fromUser) {
        
        if(out==null)
            return;
        if (fromUser != null) {
            
            out.println(fromUser);
        }
    }
    
    public void establish () {
        try {
            
            socket= new Socket(hostName, portNumber);
            //socket.setSoTimeout(2000);
            
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream()));
            out.println(clientName);
            String isAccept="";
            isAccept= in.readLine();
            if (isAccept.equals("Yes")) {
                new Thread(new TCPListeningThread()).start();
            }
            else{
                quit();
            }
            
           
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                               hostName);
            e.printStackTrace();
            System.exit(1);

        }
    }
    
    public synchronized void quit () {
        try {
            System.out.println(in);
            if (socket != null) {
                socket.shutdownInput();
                socket.shutdownOutput();
            }
            if (in != null)
                in.close();
            
            if (out != null)
                out.close();
            
            
            out = null;
            in = null;
            socket = null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    class TCPListeningThread implements Runnable{

        @Override
        public void run () {
            String fromServer;
            try {
                while ((fromServer = in.readLine()) != null) {
                    myModel.addMessage(fromServer);

                }
                quit();
            }
            catch (IOException e) {
                System.out.println(in);
                e.printStackTrace();
            }

        }
    }
        
}
