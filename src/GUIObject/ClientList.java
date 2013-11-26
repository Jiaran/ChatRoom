package GUIObject;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JTextField;
import model.Member;
import model.MemberList;
import controller.Controller;



@SuppressWarnings("serial")
public class ClientList extends GUISection implements View {

    private final int ITEM_HEIGHT = 30;
    private final int DEFAULT_WIDTH = 200;
    private final int DEFAULT_HEIGHT = 450;
    private String buttonName;
    private MemberList myOnlineClients = null;
    
    public ClientList ( Controller c ) {
        super("",c);
        this.buttonName = "Add to Chat";
        setLayout(new GridBagLayout());
        this.setAlignmentX(LEFT_ALIGNMENT);
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    public void setMembers (MemberList members) {
        myOnlineClients= members;
    }

    private void createGUI (Collection< Member> clients) {
        if (clients == null) { return; }
        this.setPreferredSize(new Dimension(300, ITEM_HEIGHT * clients.size()));
        Iterator<Member> it= clients.iterator();
        int i=0;
        while(it.hasNext()){
            Member m= it.next();
            JTextField tempText = new JTextField();
            tempText.setText(m.getName());
            tempText.setEditable(false);
            tempText.setPreferredSize(new Dimension(100, ITEM_HEIGHT));
            
            tempText.setMinimumSize(new Dimension(150, ITEM_HEIGHT));
            JButton button = new JButton(buttonName);
            button.addActionListener(new ActionAdd(m.getName()));
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
            i++;
 
        }
        
    }

    @Override
    public void updateView () {
        removeAll();
        createGUI(myOnlineClients.getMembers());
        revalidate();
        repaint();

    }
    
    private class ActionAdd implements ActionListener{
        private String clientName="";
        private ActionAdd(String name){
            clientName=name;
        }
            
        @Override
        public void actionPerformed (ActionEvent arg0) {
            System.out.println(clientName);
            myController.addClientToChatRoom(clientName);
            
        }
        
    }

}
