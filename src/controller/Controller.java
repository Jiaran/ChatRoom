package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import GUIObject.ClientList;
import GUIObject.Display;
import GUIObject.HalfScreen;
import GUIObject.Login;
import GUIObject.RunCode;
import GUIObject.Settings;
public class Controller {
    
    private MainFrame myLogin=new MainFrame("Login");
    private MainFrame myClientWindow=new MainFrame("Client Window");
    private MainFrame myChatRoom=new MainFrame("ChatRoom");
    private Settings myButtons= new Settings("Control", this);
    private ClientList myClientList= new ClientList(this);
    private Display myDisplay=null;
    public Controller(){
        myLogin.getContentPane().add(new Login(this));
        myLogin.pack();
        myLogin.setVisible(true);
        initialButtons();
        myClientWindow.getContentPane().add(new HalfScreen(myClientList,myButtons));
        myClientWindow.setLocation(200, 0);
    }
    
    private void initialButtons () {
        JButton b1= new JButton("Start");
        b1.addActionListener(new ActionStart());
        myButtons.addButton(b1);
        
        JButton b2= new JButton("Disconnect");
        b2.addActionListener(new ActionDisconnect());
        myButtons.addButton(b2);
        
        JButton b3= new JButton("Refresh");
        b3.addActionListener(new ActionRefresh());
        myButtons.addButton(b3);
        
    }

    public void initial(){
        
    }
    
    public static void main (String[] args) {
        
      
        
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run () {

                new Controller();
            }
        });

    }

    public void login (String name) {
       myLogin.setVisible(false);
       myClientWindow.pack();
       //get names;
       
        List<String> strings = new ArrayList<String>();
        strings.add("hahaha");
        strings.add("jr");
        strings.add("karl");

        myClientList.setStrings(strings);
        myClientList.updateView();
        myClientWindow.setVisible(true);
        
    }
    
    public void start (){
        myClientWindow.setVisible(false);
        myChatRoom=new MainFrame("ChatRoom");
        myChatRoom.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        myChatRoom.addWindowListener(new BackToList());
        RunCode rc=new RunCode(this);
        myDisplay= new Display();
        HalfScreen screen= new HalfScreen(myDisplay, rc);
        myChatRoom.getContentPane().add(screen);
        myChatRoom.pack();
        myChatRoom.setLocation(200, 0);
        myChatRoom.setVisible(true);
    }
    
    private class  BackToList extends WindowAdapter{

        @Override
        public void windowClosing (WindowEvent e) {
            e.getWindow().dispose();
            myClientWindow.setVisible(true);
            //disconnet
        }       
        
    }
    
    private class  ActionStart implements ActionListener{

        @Override
        public void actionPerformed (ActionEvent arg0) {
           start();
        }

        
        
    }
    
    private class  ActionRefresh implements ActionListener{

        @Override
        public void actionPerformed (ActionEvent arg0) {
           refresh();
        }

        
        
    }
    private class  ActionDisconnect implements ActionListener{

        @Override
        public void actionPerformed (ActionEvent arg0) {
           disconnect();
        }

        
        
    }

    public void send (String text) {
        
        
    }

    public void disconnect () {
        
        System.exit(0);
    }

    public void refresh () {
        
        
    }

    public void addClientToChatRoom () {
        
        
    }
    
}






