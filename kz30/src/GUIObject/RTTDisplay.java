package GUIObject;


import controller.Controller;

public class RTTDisplay extends Display {

    public RTTDisplay (Controller c) {
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
