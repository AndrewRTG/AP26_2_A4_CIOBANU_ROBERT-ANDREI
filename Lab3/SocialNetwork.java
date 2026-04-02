import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
public class SocialNetwork {
    private List<Profile> profiles = new ArrayList<>();

    public SocialNetwork() {
    }


    public void addProfile(Profile profile) {
        profiles.add(profile);
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void sortNetwork() {
        profiles.sort(Comparator.comparing(Profile::getName));

    }
    public void sortNetworkImportance(){
        profiles.sort(Comparator.comparingInt(this::getImportance).reversed());
    }

    public int getEmployees(Company company) {
        int count = 0;
        for (Profile profile : profiles) {
            if (profile instanceof Person person) {
                if (person.getRelationships().containsKey(company))
                    count++;
            }

        }

        return count;
    }
    public int getImportance(Profile target)
    {
        int count=0;
        for (Profile profile : profiles) {
            if (profile instanceof Person person) {
                if(person.equals(target)){
                    count+=person.getRelationships().size();
                }
                else if (person.getRelationships().containsKey(target))
                    count++;

            }

        }
        return count;
    }


    public List<Profile> findArticulationPoints() {
        int n = profiles.size();
        if (n == 0) return new ArrayList<>();

        // 1. Mapăm Profile-urile la indecși (0 ... n-1)
        Map<Profile, Integer> profileToIndex = new HashMap<>();
        for (int i = 0; i < n; i++) {
            profileToIndex.put(profiles.get(i), i);
        }


        // Folosim Set în loc de List pentru a evita muchiile duplicate
        List<Set<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new HashSet<>());
        }

        for (Profile p : profiles) {
            if (p instanceof Person person) {
                int u = profileToIndex.get(p);
                for (Profile neighbor : person.getRelationships().keySet()) {
                    if (profileToIndex.containsKey(neighbor)) {
                        int v = profileToIndex.get(neighbor);
                        // Adăugăm muchia în ambele sensuri (pentru conectivitate, graful trebuie privit ca neorientat)
                        adj.get(u).add(v);
                        adj.get(v).add(u);
                    }
                }
            }
        }

        // 3. Inițializăm variabilele DFS
        boolean[] visited = new boolean[n];
        int[] tin = new int[n];
        int[] low = new int[n];
        int[] timer = {0};
        Set<Integer> cutPoints = new HashSet<>();

        // 4. Rulăm DFS-ul (exact logica ta din C++)
        for (int i = 0; i < n; ++i) {
            if (!visited[i]) {
                dfs(i, -1, visited, tin, low, timer, adj, cutPoints);
            }
        }


        List<Profile> result = new ArrayList<>();
        for (int index : cutPoints) {
            result.add(profiles.get(index));
        }

        return result;
    }


    private void dfs(int v, int p, boolean[] visited, int[] tin, int[] low, int[] timer, List<Set<Integer>> adj, Set<Integer> cutPoints) {
        visited[v] = true;
        tin[v] = low[v] = timer[0]++;
        int children = 0;

        for (int to : adj.get(v)) {
            if (to == p) continue;

            if (visited[to]) {
                low[v] = Math.min(low[v], tin[to]);
            } else {
                dfs(to, v, visited, tin, low, timer, adj, cutPoints);
                low[v] = Math.min(low[v], low[to]);


                if (low[to] >= tin[v] && p != -1) {
                    cutPoints.add(v);
                }
                children++;
            }
        }


        if (p == -1 && children > 1) {
            cutPoints.add(v);
        }
    }


    public List<Set<Profile>> findBiconnectedComponents() {
        int n = profiles.size();
        if (n == 0) return new ArrayList<>();


        Map<Profile, Integer> profileToIndex = new HashMap<>();
        for (int i = 0; i < n; i++) {
            profileToIndex.put(profiles.get(i), i);
        }


        List<Set<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new HashSet<>());
        }

        for (Profile p : profiles) {
            if (p instanceof Person person) {
                int u = profileToIndex.get(p);
                for (Profile neighbor : person.getRelationships().keySet()) {
                    if (profileToIndex.containsKey(neighbor)) {
                        int v = profileToIndex.get(neighbor);
                        adj.get(u).add(v);
                        adj.get(v).add(u);
                    }
                }
            }
        }

        boolean[] visited = new boolean[n];
        int[] tin = new int[n];
        int[] low = new int[n];
        int[] timer = {0};

        // Stiva în care ținem muchiile (un array de 2 elemente: u și v)
        Stack<int[]> edgeStack = new Stack<>();
        List<Set<Integer>> bccList = new ArrayList<>();

        for (int i = 0; i < n; ++i) {
            if (!visited[i]) {
                // Dacă nodul este izolat, formează o componentă singur
                if (adj.get(i).isEmpty()) {
                    Set<Integer> isolated = new HashSet<>();
                    isolated.add(i);
                    bccList.add(isolated);
                } else {
                    dfsBCC(i, -1, visited, tin, low, timer, adj, edgeStack, bccList);
                    if (!edgeStack.isEmpty()) {
                        Set<Integer> bcc = new HashSet<>();
                        while (!edgeStack.isEmpty()) {
                            int[] edge = edgeStack.pop();
                            bcc.add(edge[0]);
                            bcc.add(edge[1]);
                        }
                        bccList.add(bcc);
                    }
                }
            }
        }


        List<Set<Profile>> result = new ArrayList<>();
        for (Set<Integer> compIndices : bccList) {
            Set<Profile> bccProfiles = new HashSet<>();
            for (int idx : compIndices) {
                bccProfiles.add(profiles.get(idx));
            }
            result.add(bccProfiles);
        }

        return result;
    }

    private void dfsBCC(int u, int p, boolean[] visited, int[] tin, int[] low, int[] timer,
                        List<Set<Integer>> adj, Stack<int[]> edgeStack, List<Set<Integer>> bccList) {
        visited[u] = true;
        tin[u] = low[u] = timer[0]++;
        int children = 0;

        for (int v : adj.get(u)) {
            if (v == p) continue;

            if (visited[v]) {
                low[u] = Math.min(low[u], tin[v]);

                if (tin[v] < tin[u]) {
                    edgeStack.push(new int[]{u, v});
                }
            } else {
                children++;

                edgeStack.push(new int[]{u, v});

                dfsBCC(v, u, visited, tin, low, timer, adj, edgeStack, bccList);
                low[u] = Math.min(low[u], low[v]);


                if (low[v] >= tin[u]) {
                    Set<Integer> bcc = new HashSet<>();
                    while (true) {
                        int[] edge = edgeStack.pop();
                        bcc.add(edge[0]);
                        bcc.add(edge[1]);

                        if ((edge[0] == u && edge[1] == v) || (edge[0] == v && edge[1] == u)) {
                            break;
                        }
                    }
                    bccList.add(bcc);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Profile profile : profiles) {
            sb.append(profile.toString()).append(" Importance:").append(getImportance(profile));
            if(profile instanceof Company company){
            if(getEmployees(company)>0){
                sb.append(" | Number of Employees: ").append(getEmployees(company));
            }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
