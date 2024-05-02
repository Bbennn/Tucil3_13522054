import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

class Perkakas {
    public static List<String> bacaInput(Scanner scanner) {
        System.out.println("===========INPUT===========");

        System.out.print("Start Word: ");
        String start = scanner.nextLine().toUpperCase();
        System.out.print("End Word: ");
        String end = scanner.nextLine().toUpperCase();
        return Arrays.asList(start, end);
    }

    public static int wordDiff(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return -1;
        }
        int result = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                result++;
            }
        }
        return result;
    }
}

class Rute {
    private List<String> nodes;
    private String tujuan;

    public Rute(String target) {
        nodes = new ArrayList<String>();
        tujuan = target;
    }

    public Rute(Rute other) {
        this.nodes = new ArrayList<String>();
        for (String string : other.nodes) {
            this.nodes.add(string);
        }
        this.tujuan = new String(other.tujuan);
    }

    public List<String> getRute() {
        return nodes;
    }

    public String getLastNode() {
        return nodes.getLast();
    }

    public List<String> searchRute(List<String> dict) {
        List<String> result = new ArrayList<String>();
        for (String kata : dict) {
            if (Perkakas.wordDiff(kata, this.getLastNode()) == 1) {
                result.add(kata);
            }
        }
        return result;
    }

    public void addRute(String baru) {
        nodes.add(baru);
    }

    public int gn() {
        return nodes.size();
    }

    public int hn() {
        return Perkakas.wordDiff(nodes.getLast(), tujuan);
    }

    public void printRute() {
        for (String kata : nodes) {
            System.out.println(kata);
        }
    }

    @Override
    public String toString() {
        return "Rute: \n" + String.join("\n", nodes);
    }

    // @Override
    public int compareUCS(Rute other) {
        if (Integer.compare(this.gn(), other.gn()) == 0) {
            return Integer.compare(this.hn(), other.hn());
        } else {
            return Integer.compare(this.gn(), other.gn());
        }
    }

    public int compareGBFS(Rute other) {
        if (Integer.compare(this.hn(), other.hn()) == 0) {
            return Integer.compare(this.gn(), other.gn());
        } else {
            return Integer.compare(this.hn(), other.hn());
        }
    }

    public int compareAStar(Rute other) {
        return Integer.compare(this.gn() + this.hn(), other.gn() + other.hn());
    }

}

public class Main {
    public static Rute UCS(String start, String target, List<String> dict) {
        Map<String, Integer> stringToInt = new HashMap<>();
        Map<Integer, String> intToString = new HashMap<>();
        for (int i = 0; i < dict.size(); i++) {
            stringToInt.put(dict.get(i), i);
            intToString.put(i, dict.get(i));
        }
        Rute r = new Rute(target);
        r.addRute(start);
        PriorityQueue<Rute> pq = new PriorityQueue<Rute>((r1, r2) -> r1.compareUCS(r2));
        pq.add(r);
        boolean stop = false;
        while (!stop) {
            Rute temp = pq.poll();
            if (temp.getLastNode().equals(target)) {
                stop = true;
                return temp;
            } else {
                List<String> newNodes = temp.searchRute(dict);
                for (String kata : newNodes) {
                    Rute newNode = new Rute(temp);
                    newNode.addRute(kata);
                    pq.add(newNode);
                }
            }
        }
        return null;
    }

    public static Rute GBFS(String start, String target, List<String> dict) {
        Map<String, Integer> stringToInt = new HashMap<>();
        Map<Integer, String> intToString = new HashMap<>();
        for (int i = 0; i < dict.size(); i++) {
            stringToInt.put(dict.get(i), i);
            intToString.put(i, dict.get(i));
        }
        Rute r = new Rute(target);
        r.addRute(start);
        PriorityQueue<Rute> pq = new PriorityQueue<Rute>((r1, r2) -> r1.compareGBFS(r2));
        pq.add(r);
        boolean stop = false;
        while (!stop) {
            Rute temp = pq.poll();
            if (temp.getLastNode().equals(target)) {
                stop = true;
                return temp;
            } else {
                List<String> newNodes = temp.searchRute(dict);
                for (String kata : newNodes) {
                    Rute newNode = new Rute(temp);
                    newNode.addRute(kata);
                    pq.add(newNode);
                }
            }
        }
        return null;
    }

    public static Rute AStar(String start, String target, List<String> dict) {
        Map<String, Integer> stringToInt = new HashMap<>();
        Map<Integer, String> intToString = new HashMap<>();
        for (int i = 0; i < dict.size(); i++) {
            stringToInt.put(dict.get(i), i);
            intToString.put(i, dict.get(i));
        }
        Rute r = new Rute(target);
        r.addRute(start);
        PriorityQueue<Rute> pq = new PriorityQueue<Rute>((r1, r2) -> r1.compareAStar(r2));
        pq.add(r);
        boolean stop = false;
        while (!stop) {
            Rute temp = pq.poll();
            if (temp.getLastNode().equals(target)) {
                stop = true;
                return temp;
            } else {
                List<String> newNodes = temp.searchRute(dict);
                for (String kata : newNodes) {
                    Rute newNode = new Rute(temp);
                    newNode.addRute(kata);
                    pq.add(newNode);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // proses baca dict
        List<String> dict = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("words.txt"))) {
            String word;
            while ((word = reader.readLine()) != null) {
                dict.add(word.toUpperCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Input
        Scanner scanner = new Scanner(System.in);
        List<String> input = Perkakas.bacaInput(scanner);
        String start = input.get(0);
        String end = input.get(1);

        // validasi input
        while (!dict.contains(start) || !dict.contains(end)) {
            System.out.println("Input kata salah! Pastikan setiap kata sudah benar!");
            input = Perkakas.bacaInput(scanner);
            start = input.get(0);
            end = input.get(1);
        }

        while (start.length() != end.length()) {
            System.out.println("Panjang kata tidak sama! Pastikan kedua kata memiliki panjang yang sama!");
            input = Perkakas.bacaInput(scanner);
            start = input.get(0);
            end = input.get(1);
        }

        boolean salah = true;
        String metode = "";
        while (salah) {
            System.out.println("===========================");
            System.out.println("Metode");
            System.out.println("1. Uniform Cost Search");
            System.out.println("2. Greedy Best First Search");
            System.out.println("3. A*");
            System.out.println("===========================");
            System.out.println("Pilih metode: ");
            metode = scanner.nextLine();
            if (!metode.equals("1") && !metode.equals("2") && !metode.equals("3")) {
                System.out.println("Input metode salah!");
            } else {
                salah = false;
            }
        }
        System.out.println("===========HASIL===========");
        System.out.println(start + " -> " + end);
        long startTime = System.currentTimeMillis();
        if (metode.equals("1")) {
            System.out.println("Uniform Cost Search");
            System.out.println(UCS(start, end, dict));
        } else if (metode.equals("2")) {
            System.out.println("Greedy Best First Search");
            System.out.println(GBFS(start, end, dict));
        } else {
            System.out.println(AStar(start, end, dict));
            System.out.println("A*");
        }
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("Waktu eksekusi: " + time + "ms");

        scanner.close();
    }
}
