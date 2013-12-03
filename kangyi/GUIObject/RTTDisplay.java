package GUIObject;


import starter.ClientStart;

public class RTTDisplay extends Display {

    public RTTDisplay (ClientStart c) {
        super(c);
       
    }
    
    public void getMessagesAndDisplay () {
        myMessages = myStarter.getRTT();
        appendAll(myMessages);
    }
    
    public void clear(){
        setText("");
    }


  
}
