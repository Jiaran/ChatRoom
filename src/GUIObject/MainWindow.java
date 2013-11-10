package GUIObject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


@SuppressWarnings("serial")
public class MainWindow extends JPanel {

    public MainWindow (HalfScreen hs1, HalfScreen hs2) {

        this.setLayout(new BorderLayout());
        initial(hs1, hs2);
        this.setVisible(true);

    }

    private void initial (HalfScreen hs1, HalfScreen hs2) {

        createAndShowGUI(hs2, hs1);
    }

    private void createAndShowGUI (JPanel jp1, JPanel jp2) {

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                              jp1, jp2);
        splitPane.setPreferredSize(new Dimension(1200, 650));

        add(splitPane);

    }

}
