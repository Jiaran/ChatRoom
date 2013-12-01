package GUIObject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import controller.Controller;

@SuppressWarnings("serial")
public class Login extends GUISection {
    private int myWidth=400;
    private int myHeight=400;
    private JTextField myNameField=new JTextField();
    private JTextField myServerIPAddress= new JTextField();
    
    public Login(Controller c){
        super("Please Enter Your Name",c);
        
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(myWidth,myHeight));
        
        JPanel temp= new JPanel();
        temp.setLayout(new BorderLayout());
        JLabel l=new JLabel("Server Address");
        l.setPreferredSize(new Dimension(100,50));
        temp.add(l,BorderLayout.WEST);
        myServerIPAddress.setPreferredSize(new Dimension(150,50));
        temp.add(myServerIPAddress,BorderLayout.CENTER);
        this.add(temp,BorderLayout.NORTH);
        
        JPanel temp1= new JPanel();
        temp1.setLayout(new BorderLayout());
        JLabel l1=new JLabel("Login Name");
        l1.setPreferredSize(new Dimension(100,50));
        temp1.add(l1,BorderLayout.WEST);
        myNameField.setPreferredSize(new Dimension(150,50));
        temp1.add(myNameField,BorderLayout.CENTER);
        this.add(temp1,BorderLayout.CENTER);
        
        
        JButton button= new JButton("Login");
        button.addActionListener(new ActionLogin());
        this.add(button,BorderLayout.SOUTH);
        
    }
    
    private class ActionLogin implements ActionListener{

        @Override
        public void actionPerformed (ActionEvent arg0) {
            String name=myNameField.getText();
            if(name.equals("")){
                JOptionPane.showMessageDialog(null, "Please Input Sever Address and Name");
                return;
            }
            System.out.println(name);
            myController.login(name);
            
        }
        
    }
}
