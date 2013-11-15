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
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import controller.Controller;

public class TCPChatRoomServer implements Runnable{
    int portNumber = 10001;
    private Model myModel=null;
    private PrintWriter out=null;
    private BufferedReader in=null;
    private ServerSocket serverSocket=null;
    private Socket clientSocket=null;
    public TCPChatRoomServer(Model m){
        myModel=m;
        try {
            serverSocket = new ServerSocket(portNumber);
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void run()   {
        while(true){
            try {
                
                if (clientSocket == null) {
                    clientSocket = serverSocket.accept();
                    System.out.println("get Socket");
                    if (myModel.isChatting()) {
                        return;
                    }
                    else {
                        myModel.setIsChatting(true);
                        new Thread(new TCPListeningThread(clientSocket)).start();

                    }
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    
    public void send(String fromUser) {
        System.out.println(out);
        if(out==null)
            return;
        if (fromUser != null) {
            
            out.println(fromUser);
        }
    }
    
    public void quit () {
        try {
            if (out != null)
                out.close();
            if (in != null)
                in.close();
            if (clientSocket != null)
                clientSocket.close();
            clientSocket = null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    class AcceptThread implements Runnable{
        private int flag;
        private String name;
        private AcceptThread(String name){
            this.name=name;
        }
        @Override
        public void run () {

            flag =
                    JOptionPane
                            .showConfirmDialog(null, "name" +
                                                     " invites you to chat, accept?");

        }
        
        public int getFlag(){
            return flag;
        }
        
    }
    
    class TCPListeningThread implements Runnable{

        private Socket clientSocket= null;
        public TCPListeningThread(Socket s){
            clientSocket= s;
        }

        @Override
        public void run () {
            try {

                out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                in =
                        new BufferedReader(
                                           new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                inputLine = in.readLine();
                AcceptThread at = new AcceptThread(inputLine);
                Thread t = new Thread(at);
                SwingUtilities.invokeLater(t);
                t.join();

                if (at.getFlag() == JOptionPane.YES_OPTION) {
                    out.println("Yes");
                    while ((inputLine = in.readLine()) != null) {
                        myModel.addMessage(inputLine);
                    }
                    clientSocket.shutdownOutput();
                    out.close();
                    in.close();
                    clientSocket.close();
                    System.out.println("server down");
                    clientSocket = null;

                }
            }
            catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port "
                                   + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally {
                myModel.setIsChatting(false);
            }

        }
    }
}