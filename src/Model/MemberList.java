package Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


public class MemberList {
    private HashMap<String, Member> totalList;

    public MemberList () {
        totalList = new HashMap<String, Member>();
    }

    public void addMember (String memberName, String memberIP, String memberPort) {
        if (memberName.isEmpty() || memberIP.isEmpty() || memberPort.isEmpty()) {
            System.out.println("Someone failed to join the community.");
            return;
        }
        else {
            Member add = new Member(memberName, memberIP, memberPort);
            totalList.put(memberIP, add);

            System.out.println(memberName + " has joined.");
        }
    }

    public void addMember (Member m) {

        totalList.put(m.getIP(), m);

    }

    public void clearList () {
        totalList.clear();
    }

    public int getNumber () {
        return totalList.size();
    }

    public Set<String> getList () {
        return totalList.keySet();
    }

    public Member getMember (String ipString) {
        return totalList.get(ipString);
    }

    public void removeMember (String member) {
        totalList.remove(member);

    }

    public Collection<Member> getMembers () {
        return totalList.values();
    }
}
