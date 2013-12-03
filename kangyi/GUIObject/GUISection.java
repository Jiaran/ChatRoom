package GUIObject;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import starter.ClientStart;


@SuppressWarnings("serial")
public abstract class GUISection extends JPanel {
    protected String myTitle;
    protected final int M_WIDTH = 100;
    protected final int M_HEIGHT = 100;
    protected ClientStart myStarter=null;
    public GUISection (String title,ClientStart c) {
        this.myTitle = title;
        myStarter=c;
        initialBorder();
    }

    private void initialBorder () {
        setBorder(BorderFactory
                .createCompoundBorder(
                                      BorderFactory.createCompoundBorder(
                                                                         BorderFactory
                                                                                 .createTitledBorder(myTitle),
                                                                         BorderFactory
                                                                                 .createEmptyBorder(5,
                                                                                                    5,
                                                                                                    5,
                                                                                                    5)),
                                      BorderFactory.createLineBorder(Color.BLACK, 1)));
    }

    public void setmyTitle (String s) {
        myTitle = s;
        initialBorder();
    }
    // public void processData(Data data){}

}
