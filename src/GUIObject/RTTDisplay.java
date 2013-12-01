package GUIObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import controller.Controller;

public class RTTDisplay extends Display {

    public RTTDisplay (Controller c) {
        super(c);
       
    }
    
    public void getMessagesAndDisplay () {
        myMessages = myController.getRTT();
        appendAll(myMessages);
    }
    
    public void clear(){
        setText("");
    }


  
}
