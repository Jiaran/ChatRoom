package controller;

import java.awt.BorderLayout;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
   
    int currentWorkSpaceIndex = 0;
    
   

    public MainFrame (String title) {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(title);
        
      
        setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
       
       
        pack();
        this.setVisible(false);

    }

       
    
}
