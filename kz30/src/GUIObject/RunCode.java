package GUIObject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.Controller;


@SuppressWarnings("serial")
public class RunCode extends GUISection {
    private JTextArea myInput= new JTextArea();
    private JButton myButton= new JButton("Send");
    
    public RunCode (Controller c) {
        super("Input",c);

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(M_WIDTH, M_HEIGHT));
        this.setMinimumSize(new Dimension(M_WIDTH, M_HEIGHT));
       
        bindInput();
        bindButton();

    }

    private void bindInput () {
        
        JScrollPane paneScrollPane = new JScrollPane(myInput);
        myInput.addKeyListener(new ActionEnter());
        paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        paneScrollPane.setPreferredSize(new Dimension(250, 100));
        this.add(paneScrollPane, BorderLayout.CENTER);
    }

    private void bindButton () {
        myButton.addActionListener(new ActionSend());
        myButton.setPreferredSize(new Dimension(80, 80));
        this.add(myButton, BorderLayout.EAST);

    }
    
    private void send(){
        String text=myInput.getText();
        myInput.setText("");
        System.out.println(text);
        myStarter.send(text);
    }
    private class ActionSend implements ActionListener{

        @Override
        public void actionPerformed (ActionEvent arg0) {
           send();
            
        }
        
    }

    private class ActionEnter extends KeyAdapter {

        public void keyPressed (KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                send();
            }
        }
    }

}
