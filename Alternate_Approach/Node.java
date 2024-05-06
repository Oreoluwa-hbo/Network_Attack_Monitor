import java.util.ArrayList;

public class Node {

    private final String name;
    private final double[] coordinates;
    private final boolean firewall;
    private boolean status;
    private ArrayList<Virus> virusAttacks;

    public Node(String name, double[] coordinates, boolean firewall) {
        this.name = name;
        this.coordinates = coordinates;
        this.firewall = firewall;
        status = true;
        virusAttacks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean getFirewall() {
        return firewall;
    }

    public ArrayList<Virus> getVirusAttacks() {
        return virusAttacks;
    }

    public boolean getStatus() {
        return status;
    }

    public void getAttacked(Virus virus, Map map) {
        if(virus.getAttackNode().equals(name) && status) {
            virusAttacks.add(virus);

            boolean multipleVirusTypes = false;
            for (int i = 0; i < (virusAttacks.size() - 1); i++) {
                if(!virusAttacks.get(i).getAttackType().equals(virusAttacks.get(i+1).getAttackType())) {
                    multipleVirusTypes = true;
                }
            }

            /* accumulated  a  total  of  6  virus injections  (with  a  minimum  of  2  types  of  viruses) */
            if (multipleVirusTypes && (virusAttacks.size() == 6) && !firewall) {
                status = false;
                map.nodeGoneInactive(this);
            }
        }
    }
}
