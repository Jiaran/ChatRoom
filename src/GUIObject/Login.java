package GUIObject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import controller.Controller;

@SuppressWarnings("serial")
public class Login extends GUISection {
    private int myWidth=400;
    private int myHeight=200;
    private JTextField myNameField=new JTextField();
    
    public Login(Controller c){
        super("Please Enter Your Name",c);
        
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(myWidth,myHeight));
        myNameField.setPreferredSize(new Dimension(200,50));
        this.add(myNameField,BorderLayout.CENTER);
        JButton button= new JButton("Login");
        button.addActionListener(new ActionLogin());
        this.add(button,BorderLayout.EAST);
        
    }
    
    private class ActionLogin implements ActionListener{

        @Override
        public void actionPerformed (ActionEvent arg0) {
            String name=myNameField.getText();
            System.out.println(name);
            myController.login(name);
            
        }
        
    }
}
