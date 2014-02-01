package controller;
/**
 * @author Jiaran, KanyYi
 * Controller class control the flow of the program. It acts as a mediator between
 * View and Model. 
 */
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import background.TCPProcessor;
import GUIObject.ClientList;
import GUIObject.Display;
import GUIObject.HalfScreen;
import GUIObject.Login;
import GUIObject.MainFrame;
import GUIObject.RTTDisplay;
import GUIObject.RunCode;
import GUIObject.Settings;


public class Controller {
    private MainFrame myLogin = new MainFrame("Login");
    private MainFrame myClientWindow = new MainFrame("Client Window");
    private MainFrame myChatRoom = new MainFrame("ChatRoom");
    private MainFrame myRTT = new MainFrame("Round Trip Time List");    
    private Settings myButtons = new Settings("Control", this);
    private ClientList myClientList = new ClientList(this);
    private TCPProcessor myProcessor = null;
    private RTTDisplay myRTTDisplay=null;
    private Display myDisplay = null;

    public Controller () {
        // set Login Window
        myLogin.getContentPane().add(new Login(this));
        myLogin.pack();
        myLogin.setVisible(true);
        myLogin.addWindowListener(new CloseChatRoom());
        initialButtons();
        
        // set Client Window
        myClientWindow.getContentPane().add(new HalfScreen(myClientList, myButtons));
        myClientWindow.setLocation(200, 0);
        myClientWindow.addWindowListener(new CloseChatRoom());
        
        // set RTT display window
        myRTTDisplay=new RTTDisplay(this);
        JScrollPane paneScrollPane = new JScrollPane(myRTTDisplay);
        paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        paneScrollPane.setPreferredSize(new Dimension(400,450));
        myRTT.getContentPane().add(paneScrollPane);
        myRTT.pack();
        myRTT.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void initialButtons () {
        JButton b1 = new JButton("Start");
        b1.addActionListener(new ActionStart());
        myButtons.addButton(b1);

        JButton b2 = new JButton("Disconnect");
        b2.addActionListener(new ActionDisconnect());
        myButtons.addButton(b2);

        JButton b3 = new JButton("Refresh");
        b3.addActionListener(new ActionRefresh());
        myButtons.addButton(b3);
    }

    public static void main (String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                new Controller();
            }
        });

    }

    /**
     * @param name : name get from user input. Cannot be empty;
     * This is the login process;
     */
    public void login (String name, String address) {
        myProcessor = new TCPProcessor(name);
        myProcessor.setUDPServerAddress(address);
        myProcessor.setStarter(this);
        myLogin.setVisible(false);
        myProcessor.login();
        myClientList.setMembers(myProcessor.getTotalList());
        myClientList.updateView();
        myClientWindow.pack();
        myClientWindow.setVisible(true);
    }

    /**
     * start establish TCP connection between peers
     */
    public void start () {
        if(myProcessor.begin()){
            showChatRoom();
        }
        else {
            JOptionPane.showMessageDialog(null, "Select who you want to chat");
            return;
        } 
    }

    /**
     * this method hides the client list and show chat room interface for the 
     * user to chat with others.
     */
    public void showChatRoom(){
        myClientWindow.setVisible(false);
        myChatRoom = new MainFrame("ChatRoom");
        myChatRoom.addWindowListener(new BackToList());
        RunCode rc = new RunCode(this);
        myDisplay = new Display(this);
        JScrollPane paneScrollPane = new JScrollPane(myDisplay);
        paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        paneScrollPane.setPreferredSize(new Dimension(400,450));
        HalfScreen screen = new HalfScreen(paneScrollPane, rc);
        JButton showRTT= new JButton("View RTT");
        showRTT.addActionListener(new ActionShowRTT());
        screen.addNewButton(showRTT);
        myChatRoom.getContentPane().add(screen);
        myChatRoom.pack();
        myChatRoom.setLocation(200, 0);
        myChatRoom.setVisible(true);
    }

    /**
     * This is the class whose method is called when window is closed.
     * It guarantees our TCP connection ends properly, all the TCP connection
     * should be closed. And our program is ready for next connection.
     * It also refreshes the client list. 
     *
     */
    private class BackToList extends WindowAdapter {
        public void windowClosing (WindowEvent e) {
            e.getWindow().dispose();
            myProcessor.disconnect();
            myProcessor.setIsChatting(false);
            myDisplay.stop();
            refresh(); 
            myClientWindow.setVisible(true);
            myRTTDisplay.clear();
        }
    }
    
    public class CloseChatRoom extends WindowAdapter{
        public void windowClosing (WindowEvent e) {
            e.getWindow().dispose();
            disconnect();
            System.exit(0);
        }
    }

    private class ActionStart implements ActionListener {
        public void actionPerformed (ActionEvent arg0) {
            start();
        }
    }
    
    private class ActionShowRTT implements ActionListener {
        public void actionPerformed (ActionEvent arg0) {
            myRTT.setVisible(true);
        }
    }

    private class ActionDisconnect implements ActionListener {
        public void actionPerformed (ActionEvent arg0) {
            disconnect();
            System.exit(0);
        }

    }

    private class ActionRefresh implements ActionListener {
        public void actionPerformed (ActionEvent arg0) {
            refresh();
        }

    }

    /**
     * @param text : text to send
     * Called when user input text from the view.
     */
    public void send (String text) {
        myProcessor.send(text);
    }

    /**
     * disconnect informs UDP server that this user is leaving
     */
    public void disconnect () {
        if(myProcessor!=null){
            myProcessor.logout();
        }
    }

    /**
     * updates client lists from the UDP server
     */
    public void refresh () {
        myProcessor.refresh();
        myClientList.setMembers(myProcessor.getTotalList());
        myClientList.updateView();
    }

    /**
     * called when user and a chatter to the chat room
     */
    public void addClientToChatRoom (String clientName) {
        myProcessor.addClientToChatRoom(clientName);
    }

    /**
     * @return Messages in the model so that view can present it to the user
     * about what they said and what their chatters said
     */
    public List<String> getMessages(){
        return myProcessor.getMessageList();
    }
    
    /**
     * @return RTT information
     */
    public List<String> getRTT(){
        if(myProcessor==null)
            return null;
        else
            return myProcessor.getRTTList();
    }
    
}
