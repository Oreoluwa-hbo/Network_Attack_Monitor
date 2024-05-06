import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    final static String locationOfGraphFile = "Graph.txt";
    final static String locationOfAttackFile = "Attack.txt";

    static HashMap<String, Node> map;

    private static void readGraphFile() throws FileNotFoundException {
        System.out.println("Loading map...");
        map = new HashMap<>();
        File file = new File(locationOfGraphFile);
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            // check type of line values (if node and coordinates || node and neighbor)

            int numberOfCommas = 0;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == ',') {
                    numberOfCommas++;
                }
            }
            if (numberOfCommas > 1) {
                //if (line.contains("(") && line.contains(")")) {
                // if node and coordinates, then we create a new node and set its details (name, coordinates, hasFirewall, etc)
                String[] lineParts = line.split(",");
                String city = lineParts[0].trim();
                String x = lineParts[1].trim().replace("(", "");
                String y = lineParts[2].trim().replace(")", "");
                boolean firewall = false;
                if (lineParts.length > 3) {
                    firewall = true;
                }

                Node node = new Node(city, Double.parseDouble(x), Double.parseDouble(y), firewall);
                map.put(city, node);
            } else if (numberOfCommas == 1) {
                // if node and neighbor, we just add to the neighbor list for the nodes involved in the line
                Node cityA, cityB;
                String[] lineParts = line.split(",");

                // find associated node object from our node list
                cityA = map.get(lineParts[0].trim());
                cityB = map.get(lineParts[1].trim());

                // make the 2 cities neighbors of each other
                cityA.addNeighbor(cityB);
                cityB.addNeighbor(cityA);
            }
        }
        System.out.println("Completed loading map...\n");
    }

    private static void readAttackFile() throws FileNotFoundException, ParseException {

        System.out.println("Loading attacks...");
        File file = new File(locationOfAttackFile);
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] lineParts = line.split(",");
            String city = lineParts[0].trim();
            String type = lineParts[1].trim();
            String date = lineParts[2].trim();
            String time = lineParts[3].trim();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss"); //2021-01-30, 10:18:00
            Date parsedDate = formatter.parse(date + ", " + time);
            // create an attack object and link to the appropriate node (based on city name)
            Node node = map.get(city);
            Attack attack = new Attack(type, parsedDate);
            node.addAttack(attack);
        }
        System.out.println("Completed loading attacks...\n");
    }

    private static void generateInteractiveReport() {
        while (true) {
            System.out.println("\n\nWelcome to the Network Attack Monitoring Tool. Select an option from the list below");
            System.out.println("1. Generate list of infected nodes");
            System.out.println("2. Generate list of nodes with firewalls");
            System.out.println("3. Generate list of nodes with firewalls and have been attacked");
            System.out.println("4. Generate list of nodes with outbreaks");
            System.out.println("5. Generate list of inactive nodes");
            System.out.println("6. Get details of a particular node");
            System.out.println("7. Generate a list of ‘safe’ routes available between two specified nodes");
            System.out.println("8. Generate adjacency graph");
            System.out.println("9. Exit");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.equals("9")) {
                System.out.println("Goodbye");
                return;
            }

            try {
                int choice = Integer.valueOf(input);
                switch (choice) {
                    case 1:
                        getInfectedNodes();
                        break;
                    case 2:
                        getNodesWithFirewalls();
                        break;
                    case 3:
                        getAttackedNodesWithFirewalls();
                        break;
                    case 4:
                        getNodesWithOutbreaks();
                        break;
                    case 5:
                        getInactiveNodes();
                        break;
                    case 6:
                        getNodeDetails();
                        break;
                    case 7:
                        generateSafeRoutes();
                        break;
                    case 8:
                        generateAdjacencyGraph();
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong input! Try again.");
            }
        }
    }


    private static void getInfectedNodes() {
        // get nodes without firewalls and have attacks
        List<Node> result = new ArrayList<>();
        for (String key : map.keySet()) {
            Node n = map.get(key);
            if (n.hasFirewall == false && n.attacks.size() > 0) {
                result.add(n);
            }
        }
        System.out.println("There are " + result.size() + " infected nodes: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.println("\t" + result.get(i).cityName);
        }
    }

    private static void getNodesWithFirewalls() {
        // get nodes with firewalls
        List<Node> result = new ArrayList<>();
        for (String key : map.keySet()) {
            Node n = map.get(key);
            if (n.hasFirewall == true) {
                result.add(n);
            }
        }
        System.out.println("There are " + result.size() + " with firewalls: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.println("\t" + result.get(i).cityName);
        }
    }

    private static void getAttackedNodesWithFirewalls() {
        // get nodes with firewalls and have attacks
        List<Node> result = new ArrayList<>();
        for (String key : map.keySet()) {
            Node n = map.get(key);
            if (n.hasFirewall == true && n.attacks.size() > 0) {
                result.add(n);
            }
        }
        System.out.println("There are " + result.size() + " with firewalls and have been attacked: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.println("\t" + result.get(i).cityName);
        }
    }

    private static void getNodesWithOutbreaks() {
        // get nodes with outbreak
        List<Node> result = new ArrayList<>();
        for (String key : map.keySet()) {
            Node n = map.get(key);
            if (n.hasOutbreak == true) {
                result.add(n);
            }
        }
        System.out.println("There are " + result.size() + " nodes with outbreaks: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.println("\t" + result.get(i).cityName);
        }
    }

    private static void getInactiveNodes() {
        // get nodes with status = inactive
        List<Node> result = new ArrayList<>();
        for (String key : map.keySet()) {
            Node n = map.get(key);
            if (n.status.equals("inactive")) {
                result.add(n);
            }
        }
        System.out.println("There are " + result.size() + " inactive nodes: ");
        for (int i = 0; i < result.size(); i++) {
            System.out.println("\t" + result.get(i).cityName);
        }
    }

    private static void getNodeDetails() {
        System.out.println("Enter the name of the city (node): ");
        Scanner sc = new Scanner(System.in);
        String city = sc.nextLine();

        Node cityNode = map.get(city);
        if (cityNode == null) {
            System.out.println("City not on list");
        } else {
            System.out.println("Details for: " + cityNode.cityName);
            System.out.println("\tStatus: " + cityNode.status);
            if (cityNode.hasFirewall) {
                System.out.println("\tFirewall present: True");
            }
            System.out.println("\tNumber of alerts: " + cityNode.alerts);
            if (cityNode.attacks.size() == 0)
                System.out.println("\tViruses: None");
            else
                System.out.println("\tViruses:");
            List<Attack> sortedAttacks = sortAttacks(cityNode.attacks);
            for (int i = 0; i < sortedAttacks.size(); i++) {
                System.out.println("\t\tAttack color = " + sortedAttacks.get(i).getType() + ", Date/Time = " + sortedAttacks.get(i).getDate().toString());
            }
        }
    }

    private static void generateSafeRoutes() {
        System.out.println("Enter the name of the first city (node): ");
        Scanner sc = new Scanner(System.in);
        String city1 = sc.nextLine();
        Node start = map.get(city1);

        System.out.println("Enter the name of the second city (node): ");
        String city2 = sc.nextLine();
        Node end = map.get(city2);

        //look for all possible routes to city2
        RouteSearch routeSearch = new RouteSearch();
        routeSearch.search(new ArrayList<Node>(), start, end);
        List<List<Node>> allPath = routeSearch.getAllPaths();

        if (allPath.size() > 0) {
            System.out.println("List of all safe routes:");
            for (List<Node> path : allPath) {
                System.out.println("\t" + path + ": Cost = " +RouteSearch.calculatePathDistance(path));
            }

            //sort paths by distance
            List<List<Node>> sortedPaths =  sortPathsByDistance(allPath);
            System.out.println("\nThe shortest safe route is " + sortedPaths.get(0) + " with a cost of " + routeSearch.calculatePathDistance(sortedPaths.get(0)));
        } else {
            System.out.println("There is no safe route between " + city1 + " and " + city2);
        }
    }

    private static List<List<Node>> sortPathsByDistance(List<List<Node>> allPath) {
        Comparator<List<Node>> comparingType = Comparator.comparing(path -> RouteSearch.calculatePathDistance(path));

        List<List<Node>> sortedPaths = allPath.stream().sorted(comparingType).collect(Collectors.toList());
        return sortedPaths;
    }

    private static void generateAdjacencyGraph() {
        for (String key : map.keySet()) {
            Node city = map.get(key);
            List<String> neighborNames = city.neighbors.stream().map(neighbor -> neighbor.cityName).collect(Collectors.toList());
            System.out.println(city.cityName + ": \t" + neighborNames);
        }
    }

    private static List<Attack> sortAttacks(Map<String, List<Attack>> attackMap) {
        List<Attack> unsortedAttacks = new ArrayList<>();
        for (String key : attackMap.keySet()) {
            unsortedAttacks.addAll(attackMap.get(key));
        }

        Comparator<Attack> comparingType = Comparator.comparing(Attack::getType).thenComparing(Attack::getDate);

        List<Attack> sortedAttacks = unsortedAttacks.stream().sorted(comparingType).collect(Collectors.toList());
        return sortedAttacks;
    }

    public static void main(String[] args) {
        try {
            // load the graph.txt and generate our map (nodes + connections)
            readGraphFile();
            // load attack.txt and apply each attack to the associated node
            readAttackFile();
            generateAdjacencyGraph();
            generateInteractiveReport();
        } catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
