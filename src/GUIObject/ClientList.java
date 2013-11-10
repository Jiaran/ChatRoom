package GUIObject;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import controller.Controller;




@SuppressWarnings("serial")
public class ClientList extends GUISection implements View {

    private final int ITEM_HEIGHT = 30;
    private final int DEFAULT_WIDTH = 200;
    private final int DEFAULT_HEIGHT = 450;
    private String buttonName;
    private List<String> myStrings = null;
    
    public ClientList ( Controller c ) {
        super("",c);
        this.buttonName = "Add to Chat";
        setLayout(new GridBagLayout());
        this.setAlignmentX(LEFT_ALIGNMENT);
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    public void setStrings (List<String> strings) {
        myStrings = strings;
    }

    private void createGUI (List<String> strings) {
        if (strings == null) { return; }
        this.setPreferredSize(new Dimension(300, ITEM_HEIGHT * strings.size()));

        for (int i = 0; i < strings.size(); i++) {
            
            JTextField tempText = new JTextField();
            tempText.setText(strings.get(i));
            tempText.setEditable(false);
            tempText.setPreferredSize(new Dimension(100, ITEM_HEIGHT));

            tempText.setMinimumSize(new Dimension(150, ITEM_HEIGHT));
            JButton button = new JButton(buttonName);
            button.addActionListener(new ActionAdd(strings.get(i)));
            button.setPreferredSize(new Dimension(80, ITEM_HEIGHT));
            button.setMinimumSize(new Dimension(80, ITEM_HEIGHT));
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = i;
            c.weightx = 0.6;
            c.anchor = GridBagConstraints.PAGE_START;
            add(tempText, c);
            c.gridx = 1;
            c.weightx = 0.4;
            add(button, c);

        }
    }

    @Override
    public void updateView () {
        removeAll();
        createGUI(myStrings);
        revalidate();

    }
    
    private class ActionAdd implements ActionListener{
        private String clientsName="";
        private ActionAdd(String name){
            clientsName=name;
        }
            
        @Override
        public void actionPerformed (ActionEvent arg0) {
            System.out.println(clientsName);
            myController.addClientToChatRoom();
            
        }
        
    }

}
