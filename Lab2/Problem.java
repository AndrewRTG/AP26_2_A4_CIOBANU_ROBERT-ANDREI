import java.util.*;
import java.util.Random;
import java.util.Arrays;
/**
 * Main class describing an instance of the Best Route Problem.
 * Handles the logic for adding elements, validation, and connectivity.
 */
public class Problem {
    private Location[] locations=new Location[10000];
    private Road[] roads= new Road[30000];
    private int locCurr=0; // cate locatii am adaugat pana acum
    private int roadCurr=0; // cate strazi am adaugat pana acum
    /**
     * Adds a location if it doesn't already exist in the problem instance.
     * @param location The location to add.
     */
    public void addLocation (Location location)
    {
        for (int i=0; i< locCurr;i++)
        {
            if(locations[i].equals(location))
            {
                System.out.println("Locatia: " + location.getName() + " exista deja. Adauga alta!");
                return;
            }
        }
        if(locCurr<10000)
        {
            location.setId(locCurr);
            locations[locCurr++]=location;

        }
        else System.out.println("Nu mai exista spatiu in vectorul de locatii");
    }
    public Location[] getLocations() {
        return locations;
    }
    /**
     * Adds a road if it doesn't already exist in the problem instance.
     * @param road The road to add.
     */
    public void addRoad (Road road)
    {
        for (int i=0; i< roadCurr;i++)
        {
            if(roads[i].equals(road))
            {
                System.out.println("Strada: " + road + " exista deja. Adauga alta!");
                return;
            }
        }
        if(roadCurr<30000)
        {
            roads[roadCurr++]=road;

        }
        else System.out.println("Nu mai exista spatiu in vectorul de strazi");
    }
    /**
     * Validates the instance.
     * Rule: Road length >= Euclidean distance between start and finish.
     * @return true if instance is valid.
     */
    public boolean valid() {
        for (int i = 0; i < roadCurr; i++) {
            Road road = roads[i];
            Location s = road.getStart();
            Location f = road.getFinish();


            if (s == null || f == null) {
                System.out.println("Eroare: Drumul " + i + " nu are locații de start/final!");
                return false;
            }
            double distance = Math.sqrt(Math.pow(s.getX() - f.getX(), 2) + Math.pow(s.getY() - f.getY(), 2));
            if (road.getLength() < distance) {
                System.out.println("Instanta invalida. Drumul între " + s.getName() + " și " + f.getName() + " este mai scurt decât distanța minimă posibilă(distanta euclidiana)");
                return false;
            }
        }
        return locCurr >= 2;

    }
    /**
     * Determines if a path exists between two locations using DFS.
     * @param start The origin location.
     * @param finish The destination location.
     * @return true if reachable.
     */
    public boolean canGo(Location start, Location finish)
    {
        int[] visited= new int[100];
        return dfs (start,finish,visited);
    }
    /**
     * Internal DFS implementation using an integer array for tracking visited nodes.
     * @param current Current node.
     * @param target Goal node.
     * @param visited Array where 1 means visited, 0 means not visited.
     * @return true if goal reached.
     */
    public boolean dfs(Location current, Location target, int[] visited)
    {
        if (current.equals(target))
        {
            return true;
        }
        visited[current.getId()]=1;
        for (int i=0; i<roadCurr;i++)
        {
            Road road=roads[i];
            Location vecin=null;
            if(road.getStart().equals(current))
            {
                vecin=road.getFinish();
            }
            else if (road.getFinish().equals(current))
            {
                vecin=road.getStart();
            }
            if (vecin!=null && visited[vecin.getId()]==0)
            {
                if(dfs(vecin,target,visited))
                    return true;
            }

        }
        return false;

    }


    public Solution getBestRoute(Location start, Location finish, boolean fastest) {
        int n = locCurr;
        double[] dist = new double[n];
        int[] parentLoc = new int[n];   //  Salvam indexul locatiei parinte
        Road[] parentRoad = new Road[n]; //  Salvam drumul folosit
        boolean[] settled = new boolean[n];

        PriorityQueue<Node> pq = new PriorityQueue<>(n, new Node());

        for (int i = 0; i < n; i++) {
            dist[i] = Double.MAX_VALUE;
            parentLoc[i] = -1;
        }

        dist[start.getId()] = 0;
        pq.add(new Node(start.getId(), 0));

        while (!pq.isEmpty()) {
            int u = pq.remove().node;
            if (settled[u]) continue;
            settled[u] = true;

            if (u == finish.getId()) break; // Am gasit tinta


            for (int i = 0; i < roadCurr; i++) {
                Road road = roads[i];
                int v = -1;

                if (road.getStart().getId() == u) v = road.getFinish().getId();
                else if (road.getFinish().getId() == u) v = road.getStart().getId();

                if (v != -1 && !settled[v]) {

                    double cost = fastest ? (double) road.getLength() / road.getSpeedLimit() : road.getLength();

                    if (dist[u] + cost < dist[v]) {
                        dist[v] = dist[u] + cost;
                        parentLoc[v] = u;      // Tinem minte de unde am venit
                        parentRoad[v] = road;  // Tinem minte pe ce drum am venit
                        pq.add(new Node(v, dist[v]));
                    }
                }
            }
        }


        return buildSolution(finish.getId(), parentLoc, parentRoad, dist[finish.getId()], fastest);
    }

    private Solution buildSolution(int target, int[] parentLoc, Road[] parentRoad, double cost, boolean fastest) {
        if (parentLoc[target] == -1) return new Solution(null, 0, "None");


        int steps = 0;
        int temp = target;
        while (parentLoc[temp] != -1) {
            steps++;
            temp = parentLoc[temp];
        }

        Road[] path = new Road[steps];
        temp = target;
        for (int i = steps - 1; i >= 0; i--) {
            path[i] = parentRoad[temp];
            temp = parentLoc[temp];
        }

        return new Solution(path, cost, fastest ? "Fastest" : "Shortest");
    }

    @Override
    public String toString() {
        Location[] activeLocations = Arrays.copyOfRange(locations, 0, locCurr);
        Road[] activeRoads = Arrays.copyOfRange(roads, 0, roadCurr);
        StringBuilder sb = new StringBuilder();
        sb.append("--- Problem Instance ---\n");
        sb.append("Locations (").append(locCurr).append("): ").append(Arrays.toString(activeLocations)).append("\n");
        sb.append("Roads (").append(roadCurr).append("): ").append(Arrays.toString(activeRoads));

        return sb.toString();
    }
    public void generateRandomInstance(int numLocs, int numRoads) {
        Random rand = new Random();


        for (int i = 0; i < numLocs; i++) {
            this.addLocation(new City("City " + i, rand.nextDouble() * 1000, rand.nextDouble() * 1000, rand.nextInt(100000)));
        }


        for (int i = 0; i < numRoads; i++) {
            Location s = locations[rand.nextInt(locCurr)];
            Location f = locations[rand.nextInt(locCurr)];
            if (s.equals(f)) continue;


            float dist = (float) Math.sqrt(Math.pow(s.getX() - f.getX(), 2) + Math.pow(s.getY() - f.getY(), 2));
            this.addRoad(new Road(RoadTypes.HIGHWAY, dist + rand.nextFloat() * 10, 130, s, f));
        }
    }
}
class Node implements Comparator<Node> {
    public int node;
    public double cost;
    public Node() {}
    public Node(int node, double cost) { this.node = node; this.cost = cost; }
    @Override public int compare(Node n1, Node n2) { return Double.compare(n1.cost, n2.cost); }
}

