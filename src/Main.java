import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// class perkakas untuk utilitas umum
class Perkakas {
    public static List<String> bacaInput(Scanner scanner) {
        // untuk input kata
        System.out.println("===========INPUT===========");
        System.out.print("Start Word: ");
        String start = scanner.nextLine().toUpperCase();
        System.out.print("End Word: ");
        String end = scanner.nextLine().toUpperCase();
        return Arrays.asList(start, end);
    }

    public static int wordDiff(String s1, String s2) {
        // cari jumlah perbedaan huruf antara 2 kata
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

// class rute untuk merepresetasikan rute dari node
// dan menyelesaikan permasalahan
class Rute {
    private List<String> nodes;
    private String tujuan;

    public Rute(String target) {
        // ctor
        nodes = new ArrayList<String>();
        tujuan = target;
    }

    public Rute(Rute other) {
        // cctor
        this.nodes = new ArrayList<String>();
        for (String string : other.nodes) {
            this.nodes.add(string);
        }
        this.tujuan = (other.tujuan);
    }

    public List<String> getRute() {
        return nodes;
    }

    public String getLastNode() {
        return nodes.getLast();
    }

    public List<String> searchRute(List<String> dict) {
        // mencari node2 tetangga dari node terakhir di rute
        List<String> result = new ArrayList<String>();
        for (String kata : dict) {
            if (Perkakas.wordDiff(kata, this.getLastNode()) == 1) {
                result.add(kata);
            }
        }
        return result;
    }

    public void addRute(String baru) {
        // menambahkan node baru ke rute
        nodes.add(baru);
    }

    public int gn() {
        // mengembalikan panjang rute
        return nodes.size();
    }

    public int hn() {
        // mengembalikan jumlah perbedaan huruf antara node terakhir dengan tujuan
        return Perkakas.wordDiff(nodes.getLast(), tujuan);
    }

    public void printRute() {
        // print rute
        for (String kata : nodes) {
            System.out.println(kata);
        }
    }

    @Override
    public String toString() {
        // print rute untuk println
        return "Rute: \n" + String.join("\n", nodes) + "\n" + "Panjang rute: " + nodes.size();
    }

    // @Override
    public int compareUCS(Rute other) {
        // untuk menentukan prioritas ketika UCS
        if (Integer.compare(this.gn(), other.gn()) == 0) {
            return Integer.compare(this.hn(), other.hn());
        } else {
            return Integer.compare(this.gn(), other.gn());
        }
    }

    public int compareGBFS(Rute other) {
        // untuk menentukan prioritas ketika GBFS
        if (Integer.compare(this.hn(), other.hn()) == 0) {
            return Integer.compare(this.gn(), other.gn());
        } else {
            return Integer.compare(this.hn(), other.hn());
        }
    }

    public int compareAStar(Rute other) {
        // untuk menentukan prioritas ketika AStar
        return Integer.compare(this.gn() + this.hn(), other.gn() + other.hn());
    }

    // Algoritma searching
    public static Rute UCS(String start, String target, List<String> dict) {
        // algoritma UCS
        // inisialisasi
        Rute r = new Rute(target);
        r.addRute(start);
        PriorityQueue<Rute> pq = new PriorityQueue<Rute>((r1, r2) -> r1.compareUCS(r2));
        pq.add(r);
        Map<String, Boolean> visited = new HashMap<String, Boolean>();
        boolean stop = false;
        // proses
        while (!stop) {
            Rute temp = pq.poll();
            visited.put(temp.getLastNode(), true);
            if (temp.getLastNode().equals(target)) { // solusi tercapai?
                stop = true;
                System.out.println("Jumlah node yang dikunjungi: " + visited.size());
                return temp;
            } else {
                List<String> newNodes = temp.searchRute(dict); // ambil node baru
                for (String kata : newNodes) { // kata adalah node
                    Rute newNode = new Rute(temp); // duplikasi rute
                    if (!(visited.get(kata) != null)) { // node belum pernah divisit
                        newNode.addRute(kata); // tambah node baru
                        pq.add(newNode); // tambah rute baru
                    }
                }
            }
        }
        return null;
    }

    public static Rute GBFS(String start, String target, List<String> dict) {
        // algoritma GBFS
        // inisialisasi
        Rute r = new Rute(target);
        r.addRute(start);
        PriorityQueue<Rute> pq = new PriorityQueue<Rute>((r1, r2) -> r1.compareGBFS(r2));
        pq.add(r);
        Map<String, Boolean> visited = new HashMap<String, Boolean>();
        boolean stop = false;
        // proses
        while (!stop) {
            Rute temp = pq.poll();
            if (temp == null) {
                System.out.println("Tidak ada solusi");
                return null;
            }
            visited.put(temp.getLastNode(), true);
            while (!pq.isEmpty()) {
                pq.poll();
            }
            if (temp.getLastNode().equals(target)) { // solusi tercapai?
                System.out.println("Jumlah node yang dikunjungi: " + visited.size());
                stop = true;
                return temp;
            } else {
                List<String> newNodes = temp.searchRute(dict); // ambil node baru
                for (String kata : newNodes) { // kata adalah node
                    Rute newNode = new Rute(temp); // duplikasi rute
                    if (!(visited.get(kata) != null)) { // node belum pernah divisit
                        newNode.addRute(kata); // tambah node baru
                        pq.add(newNode); // tambah rute baru
                    }
                }
            }
        }
        return null;
    }

    public static Rute AStar(String start, String target, List<String> dict) {
        // algoritma AStar
        // inisialisasi
        Rute r = new Rute(target);
        r.addRute(start);
        PriorityQueue<Rute> pq = new PriorityQueue<Rute>((r1, r2) -> r1.compareAStar(r2));
        pq.add(r);
        Map<String, Boolean> visited = new HashMap<String, Boolean>();
        boolean stop = false;
        // proses
        while (!stop) {
            Rute temp = pq.poll();
            visited.put(temp.getLastNode(), true);
            if (temp.getLastNode().equals(target)) { // solusi tercapai?
                System.out.println("Jumlah node yang dikunjungi: " + visited.size());
                stop = true;
                return temp;
            } else {
                List<String> newNodes = temp.searchRute(dict); // ambil node baru
                for (String kata : newNodes) { // kata adalah node
                    Rute newNode = new Rute(temp); // duplikasi rute
                    if (!(visited.get(kata) != null)) { // node belum pernah divisit
                        newNode.addRute(kata); // tambah node baru
                        pq.add(newNode); // tambah rute baru
                    }
                }
            }
        }

        return null;
    }

}

public class Main {
    public static void main(String[] args) {
        // proses baca kamus
        List<String> dict = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("../src/words.txt"))) {
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
        while (!dict.contains(start) || !dict.contains(end) || start.length() != end.length()) {
            if (!dict.contains(start) || !dict.contains(end)) {
                System.out.println("Input kata salah! Pastikan setiap kata sudah benar!");
                input = Perkakas.bacaInput(scanner);
                start = input.get(0);
                end = input.get(1);
            } else {
                System.out.println("Panjang kata tidak sama! Pastikan kedua kata memiliki panjang yang sama!");
                input = Perkakas.bacaInput(scanner);
                start = input.get(0);
                end = input.get(1);

            }
        }

        // mereduksi kamus
        int sz = start.length();
        dict.stream().filter(s -> s.length() == sz);
        // System.out.println("sz: " + dict.size());

        // penentuan metode
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
        // eksekusi
        System.out.println("===========HASIL===========");
        System.out.println(start + " -> " + end);
        long startTime = System.currentTimeMillis();
        if (metode.equals("1")) {
            System.out.println("Uniform Cost Search");
            System.out.println(Rute.UCS(start, end, dict));
        } else if (metode.equals("2")) {
            System.out.println("Greedy Best First Search");
            System.out.println(Rute.GBFS(start, end, dict));
        } else {
            System.out.println("A*");
            System.out.println(Rute.AStar(start, end, dict));
        }
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("Waktu eksekusi: " + time + "ms");

        scanner.close();
    }
}