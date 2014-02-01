package GUIObject;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


@SuppressWarnings("serial")
public class HalfScreen extends JPanel {
    protected Component section1;
    protected Component section2;
    
    public HalfScreen (Component jc1, Component jc2) {
       
        section1 = jc1;
        section2 = jc2;
        initial();
    }
    
    protected void initial(){
        this.setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                              section1, section2);
        splitPane.setPreferredSize(new Dimension(400, 600));
        
        add(splitPane,BorderLayout.CENTER);
        setVisible(true);
    }
    
    public void addNewButton(JButton button){
        add(button,BorderLayout.SOUTH);
       
    }

}
