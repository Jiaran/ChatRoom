package controller;

import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import model.Model;
import GUIObject.ClientList;
import GUIObject.Display;
import GUIObject.HalfScreen;
import GUIObject.Login;
import GUIObject.RTTDisplay;
import GUIObject.RunCode;
import GUIObject.Settings;


/**
 * @author Jiaran, KanyYi
 * Controller class control the flow of the program. It acts as a mediator between
 * View and Model. 
 */
public class Controller {
    //model is the object manage data
    private Model myModel = null;
    
    /* all these windows are view. In MVC design pattern, model only handles data
     * how these data are presented is the view's job. Our project contains two people
     * and we share same View and Controller. But thanks to MVC pattern, we can design
     * our own Model separately. As long as model provides interface to give data, our
     * view can be used to display these data.
    */
    private MainFrame myLogin = new MainFrame("Login");
    private MainFrame myClientWindow = new MainFrame("Client Window");
    private MainFrame myChatRoom = new MainFrame("ChatRoom");
    private MainFrame myRTT = new MainFrame("RTT");    
    private Settings myButtons = new Settings("Control", this);
    private ClientList myClientList = new ClientList(this);
    private Display myDisplay = null;
    private RTTDisplay myRTTDisplay=null;
    
    
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

    public void initial () {

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
        myModel = new Model(name);
        myModel.setUDPServerAddress(address);
        myModel.setController(this);
        myLogin.setVisible(false);
        
   
        
        myModel.login();
        myClientList.setMembers(myModel.getTotalList());
        myClientList.updateView();
        myClientWindow.pack();
        myClientWindow.setVisible(true);

    }

   
    /**
     * start establish TCP connection between peers
     */
    public void start () {
       
        if(myModel.start()){
            showChatRoom();
        }
        else {
            JOptionPane.showMessageDialog(null, "Please choose a member to chat");
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

        @Override
        public void windowClosing (WindowEvent e) {
            
            e.getWindow().dispose();
            
            myModel.TCPdisconnect();
            myModel.setIsChatting(false);
            myDisplay.stop();
            refresh(); 
            myClientWindow.setVisible(true);
            myRTTDisplay.clear();
           
        }

    }
    
    private class CloseChatRoom extends WindowAdapter{
        @Override
        public void windowClosing (WindowEvent e) {
            e.getWindow().dispose();
           
            disconnect();
           
            System.exit(0);
           
        }
    }

    private class ActionStart implements ActionListener {

        @Override
        public void actionPerformed (ActionEvent arg0) {
            start();
        }

    }

    private class ActionRefresh implements ActionListener {

        @Override
        public void actionPerformed (ActionEvent arg0) {
            refresh();
        }

    }
    
    private class ActionShowRTT implements ActionListener {

        @Override
        public void actionPerformed (ActionEvent arg0) {
            myRTT.setVisible(true);
        }

    }
    
    private class ActionDisconnect implements ActionListener {

        @Override
        public void actionPerformed (ActionEvent arg0) {
            disconnect();
            System.exit(0);
        }

    }

    /**
     * @param text : text to send
     * Called when user input text from the view.
     */
    public void send (String text) {
        myModel.send(text);
    }

    /**
     * disconnect informs UDP server that this user is leaving
     */
    public void disconnect () {
        if(myModel!=null){
            myModel.logout();
        }
        //System.exit(0);
    }

    /**
     * updates client lists from the UDP server
     */
    public void refresh () {
        myModel.refresh();
        myClientList.setMembers(myModel.getTotalList());
        myClientList.updateView();
    }

    /**
     * called when user and a chatter to the chat room
     */
    public void addClientToChatRoom (String clientName) {
        myModel.addClientToChatRoom(clientName);

    }
    
    /**
     * @return Messages in the model so that view can present it to the user
     * about what they said and what their chatters said
     */
    public List<String> getMessages(){
        return myModel.getMessages();
       
    }
    
    /**
     * @return RTT information
     */
    public List<String> getRTT(){
        if(myModel==null)
            return null;
        else
            return myModel.getRTT();
    }
    
}
