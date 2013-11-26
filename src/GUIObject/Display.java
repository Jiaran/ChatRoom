package GUIObject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.Timer;
import controller.Controller;



@SuppressWarnings("serial")
public class Display extends JTextArea implements ActionListener,View {
    
    private final int DEFAULT_WIDTH = 400;
    private final int DEFAULT_HEIGHT = 450;
    private int DEFAULT_PERIOD = 60;
    private Timer timer = new Timer(DEFAULT_PERIOD, this);
    private Controller myController=null;
    private List<String> myMessages=null;
    
    public Display (Controller c) {
        myController=c;
        timer.setInitialDelay(0);
        timer.start();
        this.setFocusable(true);
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setBackground(new Color(255, 255, 255));
        this.setVisible(true);

    }
    
    

    @Override
    public void actionPerformed (ActionEvent e) {
        System.out.println("called");
        getMessagesAndDisplay();
        repaint();
        
    }


    @Override
    public void updateView () {
       repaint();
        
    }
    
    public void getMessagesAndDisplay(){
        myMessages=myController.getMessages();
        appendAll(myMessages);
    }



    private void appendAll (List<String> m) {
        for(int i=0; i<m.size();i++){
            append(m.get(i));
            append("\n");
        }
        
    }
    
    public void stop(){
        timer.stop();
        
    }

}
