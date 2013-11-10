package GUIObject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;



@SuppressWarnings("serial")
public class Display extends javax.swing.JPanel implements ActionListener,View {
    
    private final int DEFAULT_WIDTH = 400;
    private final int DEFAULT_HEIGHT = 450;
    private int DEFAULT_PERIOD = 60;
    private Timer timer = new Timer(DEFAULT_PERIOD, this);

    
    public Display () {
        timer.setInitialDelay(0);
        timer.start();
        this.setFocusable(true);
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setBackground(new Color(255, 255, 255));
        this.setVisible(true);

    }


    @Override
    public void actionPerformed (ActionEvent e) {
        repaint();
        
    }


    @Override
    public void updateView () {
       repaint();
        
    }

}
