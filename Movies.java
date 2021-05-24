import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/** from Professor Brizan **/

/*
 * Downloaded JAR files from:
 *   http://commons.apache.org/proper/commons-csv/user-guide.html (Apache Commons CSV)
 *   http://www.java2s.com/Code/Jar/j/Downloadjsonsimple11jar.htm (JSON-Simple)
 *
 * Got them in my build path using:
 *   https://www.wikihow.com/Add-JARs-to-Project-Build-Paths-in-Eclipse-%28Java%29
 */

public class Movies {
    public Graph graph; /** call Graph **/
    ArrayList<String> actors; /** string of actors **/
    ArrayList<Integer> actorsPerMovie; /** number of actors per movie **/
    ArrayList<String> allActors; /**  string of all actors**/

    public void buildGraph() { /** got help online **/
        for (int i = 0; i < actorsPerMovie.size(); i++) {
            if (i == 0) {
                for (int j = 0; j < actorsPerMovie.get(i); j++) {
                    for (int k = j + 1; k < actorsPerMovie.get(i); k++) {
                        graph.add(find(allActors.get(j)), find(allActors.get(k)));
                    }
                }
            } else {
                int start = previous(i);
                int end = sum(i);
                for (int l = start; l < end; l++) {
                    for (int k = l + 1; k < end; k++) {
                        graph.add(find(allActors.get(l)),find(allActors.get(k)));
                    }
                }
            }
        }
    }

    public Movies(int size, ArrayList<String> actors, ArrayList<Integer> actorsPerMovie, ArrayList<String> allActors) { /** **/
        this.actors = actors;
        graph = new Graph(size, actors);
        this.actorsPerMovie = actorsPerMovie;
        this.allActors = allActors;
        buildGraph(); /** calls buildGraph **/
    }

    private int sum(int total) { /** sum of all the actors recorded **/
        int sum = 0;
        for (int i = 0; i <= total; i++) {
            sum += actorsPerMovie.get(i);
        }
        return sum; /** returns sum, used in main to see the total amount **/
    }

    private int previous(int prev) { /** previous function **/
        int sum = 0;
        for (int i = 0; i < prev; i++) {
            sum += actorsPerMovie.get(i);
        }
        return sum;
    }

    public int find(String name) { /** find function to detect a name in the graph **/
        for (int i = 0; i < actors.size(); i++) {
            if (actors.get(i).equals(name)) {
                return i;
            }
        }
        return -1; /** if not detected it prints -1 which ends the program **/
    }

    public void findPath(String first, String last) { /** detects the shortest path between two names, if no names it prints none detected **/
        ArrayList<Integer> path = graph.shortestPath(find(first),find(last));
        if (path != null) {
            System.out.print("Shortest path between " + first + " and " + last + " is : \n");
            System.out.print(first);
            for (int inside : path) {
                System.out.print("->->->");
                System.out.print(actors.get(inside));
            }
        } else {
            System.out.println("*** NOT PATH DETECTED ***");
        }
    }

    public static void main(String[] args) { /** main function **/
        try {
            /** from Professor Brizan **/
            Reader reader = new FileReader(args[0]);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            JSONParser jsonParser = new JSONParser();
            ArrayList<String> actors = new ArrayList<>();
            ArrayList<Integer> movieSizes = new ArrayList<>();
            ArrayList<Integer> movieSizesDup = new ArrayList<>();
            ArrayList<String> allActors = new ArrayList<>();

            int movies = 0;

            for (CSVRecord csvRecord : csvParser) {
                if (movies >= 1) {
                    int size = 0;
                    int numActors = 0;
                    String castJSON = csvRecord.get(2);
                    Object object = jsonParser.parse(castJSON);

                    JSONArray jsonArray = (JSONArray) object;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String name = jsonObject.get("name").toString().toLowerCase(Locale.ROOT);
                        if (!actors.contains(name)) {
                            actors.add(name);
                            numActors++;
                        }
                        allActors.add(name);
                        size++;
                    }
                    movieSizes.add(numActors);
                    movieSizesDup.add(size);
                }
                ++movies;
            }

            Movies movies2 = new Movies(actors.size(),actors,movieSizesDup,allActors);
            Scanner scanner = new Scanner(System.in);

            System.out.println("-1 TO TERMINATE PROGRAM: ");
            String a1 = "";
            String a2 = "";
            boolean Compiling = true;
            while (Compiling) { /** while function to test the two names for and find the shortest path or not **/
                while (Compiling && a1 != "-1" && movies2.find(a1) == -1) { /** for first name **/
                    System.out.print("\n1st ACTOR NAME: ");
                    a1 = scanner.nextLine().toLowerCase(Locale.ROOT);
                    if (a1.equals("-1")) {
                        Compiling = false;
                    }
                }

                while (Compiling && a2 != "-1" && movies2.find(a2) == -1) { /** for second name **/
                    System.out.print("2nd ACTOR NAME: ");
                    a2 = scanner.nextLine().toLowerCase(Locale.ROOT);
                    if (a2.equals("-1")) {
                        Compiling = false;
                    }
                }
                if (Compiling) {
                    movies2.findPath(a1, a2);
                    a1 = "";
                    a2 = "";
                }
            }
            csvParser.close();
        } catch (Exception e) {
            System.out.println("File " + args[0] + " is invalid or is in the wrong format.");
        }
    }
}

