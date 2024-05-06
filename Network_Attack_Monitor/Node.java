import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Node {

    String cityName;
    double X, Y;
    boolean hasFirewall;
    String status;
    boolean hasOutbreak;
    int alerts;

    List<Node> neighbors;
    HashMap<String, List<Attack>> attacks;// if firewall is present, this list represents the stopped attacks

    Node(String city, double X, double Y, boolean firewall) {
        this.cityName = city;
        this.X = X;
        this.Y = Y;
        this.hasFirewall = firewall;
        this.neighbors = new ArrayList<>();
        this.attacks = new HashMap<>();
        this.status = "active";
        this.hasOutbreak = false;
        this.alerts = 0;
    }

    public void addNeighbor(Node node) {
        neighbors.add(node);
    }

    public void removeNeighbor(Node node) {
        neighbors.remove(node);
    }

    @Override
    public String toString() {
        /*List<String> neighborNames = neighbors.stream().map(neighbor -> neighbor.cityName).collect(Collectors.toList());
        List<String> attackTypes = new ArrayList<>();
        for (String key : attacks.keySet()) {
            attackTypes.addAll(attacks.get(key).stream().map(val -> val.getType()).collect(Collectors.toList()));
        }*/

       /* String output = "Node{" +
                cityName + '\'' +
                // ",(" + X +
                //"," + Y +
                ", hasFirewall=" + hasFirewall +
                ", status=" + status +
                ", neighbors=" + neighborNames +
                ", attacks=" + attackTypes +
                "}";*/
        String output = cityName;
        return output;
    }

    public void addAttack(Attack attack) {
        String key = attack.getType();
        List<Attack> values = attacks.get(key);

        if(hasFirewall){
            // just add attack to list to keep track
            if (values == null) {
                values = new ArrayList<>();
            }
            values.add(attack);
            attacks.put(key, values);
        }

        if (status.equals("active")) {
            if (!hasFirewall) {

                if (values != null) {
                    // generate alert if more than 2 viruses of the same type occur within 2 minutes
                    int count = attack.getAttacksWithinTimeFrame(values, 2);
                    if (count > 2) {
                        System.out.println("ALERT!!! " + this.cityName + " has a " + key + " virus.");
                        this.alerts++;
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // outbreak occurs if 4 or more of the same type of virus occurs within 4 minutes
                    count = attack.getAttacksWithinTimeFrame(values, 4);
                    if (count >= 4) {
                        System.out.println("ALERT!!! Outbreak at " + this.cityName);
                        this.hasOutbreak = true;

                        // inject attack of same type (with current time) into all neighbors
                        Attack injectedAttack = new Attack(attack.getType(), Calendar.getInstance().getTime());
                        for (Node node : neighbors) {
                            node.addAttack(injectedAttack);
                        }

                        /*try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    }
                }

                if (values == null) {
                    values = new ArrayList<>();
                }
                values.add(attack);
                attacks.put(key, values);

                // node goes offline if 6 attacks (of at least two types) hit the node
                int allAttacks = 0;
                for (String s : attacks.keySet()) {
                    allAttacks = allAttacks + attacks.get(s).size();
                }
               
                if (attacks.keySet().size() >= 2 && allAttacks == 6) {
                    status = "inactive";
                    //remove connections to neighbors (bi-directional)
                    for (Node n : neighbors) {
                        n.removeNeighbor(this);
                    }
                    this.neighbors.clear();
                }
            }
        }
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }
}
