
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import controller.Controller;
import model.Model;
import model.TCPChatRoomServer;


public class Test1 {

    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main (String[] args) throws InterruptedException {
    
        
       Model m= new Model("h");
       
      TCPChatRoomServer t = new TCPChatRoomServer(m);
      new Thread(t).start();
      

    }

}
