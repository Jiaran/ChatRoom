import model.Member;
import model.Model;
import model.TCPChatRoomClient;


public class Test {

    /**
     * @param args
     */
    public static void main (String[] args) {
       Model m= new Model("h");
       Member me= new Member("h", "localhost", "10000");
       TCPChatRoomClient t= new TCPChatRoomClient(me,m);
       t.establish();
       t.send("nima");

    }

}
