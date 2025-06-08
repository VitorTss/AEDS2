import java.io.*;
import java.util.*;

class DatePlus {
    String month;
    int day;
    int year;
    DatePlus(String month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }
}

class Show {
    String id, category, name, director, country, rating, duration, description;
    List<String> cast;
    List<String> categories;
    DatePlus date_added;
    int release_year;

    Show(String id, String category, String name, String director, List<String> cast,
         String country, DatePlus date_added, int release_year, String rating,
         String duration, List<String> categories, String description) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.director = director;
        this.cast = cast;
        this.country = country;
        this.date_added = date_added;
        this.release_year = release_year;
        this.rating = rating;
        this.duration = duration;
        this.categories = categories;
        this.description = description;
    }

    Show(Show o) {
        this(o.id, o.category, o.name, o.director, new ArrayList<>(o.cast), o.country,
                new DatePlus(o.date_added.month, o.date_added.day, o.date_added.year),
                o.release_year, o.rating, o.duration, new ArrayList<>(o.categories), o.description);
    }
}

class Node {
    Show show;
    Node prev, next;
    Node(Show show) {
        this.show = show;
        this.prev = null;
        this.next = null;
    }
}

public class Exercicio10 {
    static List<Show> catalogo = new ArrayList<>();
    static int comparacoes = 0, movimentacoes = 0;

    static String removerEspacos(String str) {
        return (str == null) ? "NaN" : str.strip();
    }
    static String tirarAspas(String str) {
        return (str == null) ? null : str.replace("\"", "");
    }
    static String tratarCampo(String campo) {
        if (campo == null || campo.length() == 0) return "NaN";
        return removerEspacos(tirarAspas(campo));
    }
    static List<String> tratarLista(String str) {
        List<String> itens = new ArrayList<>();
        if (str == null || str.length() == 0) {
            itens.add("NaN");
            return itens;
        }
        String[] partes = tirarAspas(str).split(",");
        for (String s : partes) {
            itens.add(tratarCampo(s));
        }
        itens.sort(String.CASE_INSENSITIVE_ORDER);
        return itens;
    }
    static DatePlus analisarData(String campo) {
        if (campo == null || campo.trim().equals("NaN") || campo.trim().isEmpty())
            return new DatePlus("March", 1, 1900);
        try {
            String[] partes = campo.split(" ");
            String mes = partes[0];
            int dia = Integer.parseInt(partes[1].replace(",", ""));
            int ano = Integer.parseInt(partes[2]);
            return new DatePlus(mes, dia, ano);
        } catch (Exception e) {
            return new DatePlus("March", 1, 1900);
        }
    }
    static int indiceMes(String mes) {
        String[] meses = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < meses.length; i++) {
            if (meses[i].equalsIgnoreCase(mes)) return i;
        }
        return -1;
    }
    static int compararShows(Show a, Show b) {
        if (a.date_added.year != b.date_added.year) return a.date_added.year - b.date_added.year;
        int mA = indiceMes(a.date_added.month), mB = indiceMes(b.date_added.month);
        if (mA != mB) return mA - mB;
        if (a.date_added.day != b.date_added.day) return a.date_added.day - b.date_added.day;
        return a.name.compareToIgnoreCase(b.name);
    }

    static void carregarCatalogo() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("/tmp/disneyplus.csv"));
        String linha = br.readLine(); // Cabeçalho
        while ((linha = br.readLine()) != null) {
            String[] campos = new String[15];
            int idx = 0, i = 0, entreAspas = 0;
            StringBuilder atual = new StringBuilder();
            while (i < linha.length()) {
                char c = linha.charAt(i);
                if (c == '"') entreAspas ^= 1;
                else if (c == ',' && entreAspas == 0) {
                    campos[idx++] = atual.toString();
                    atual.setLength(0);
                } else atual.append(c);
                i++;
            }
            campos[idx++] = atual.toString();
            String id = tratarCampo(campos[0]);
            String categoria = tratarCampo(campos[1]);
            String nome = tratarCampo(campos[2]);
            String diretor = tratarCampo(campos[3]);
            List<String> elenco = tratarLista(campos[4]);
            String pais = tratarCampo(campos[5]);
            DatePlus dataAdicao = analisarData(tratarCampo(campos[6]));
            int anoLancamento = -1;
            try { if (campos[7] != null && !campos[7].isEmpty()) anoLancamento = Integer.parseInt(campos[7]); } catch(Exception ignored) {}
            String classificacao = tratarCampo(campos[8]);
            String duracao = tratarCampo(campos[9]);
            List<String> generos = tratarLista(campos[10]);
            String descricao = tratarCampo(campos[11]);
            catalogo.add(new Show(id, categoria, nome, diretor, elenco, pais, dataAdicao,
                    anoLancamento, classificacao, duracao, generos, descricao));
        }
        br.close();
    }

    static Node adicionarNo(Node cauda, Show show) {
        Node novo = new Node(new Show(show));
        if (cauda != null) {
            cauda.next = novo;
            novo.prev = cauda;
        }
        return novo;
    }

    static Node particionar(Node esquerda, Node direita) {
        Show pivo = direita.show;
        Node i = esquerda.prev;
        for (Node j = esquerda; j != direita; j = j.next) {
            comparacoes++;
            if (compararShows(j.show, pivo) < 0) {
                i = (i == null) ? esquerda : i.next;
                Show temp = i.show;
                i.show = j.show;
                j.show = temp;
                movimentacoes++;
            }
        }
        i = (i == null) ? esquerda : i.next;
        Show temp = i.show;
        i.show = direita.show;
        direita.show = temp;
        movimentacoes++;
        return i;
    }

    static void ordenarLista(Node esquerda, Node direita) {
        if (esquerda == null || direita == null || esquerda == direita || esquerda == direita.next) return;
        Node p = particionar(esquerda, direita);
        if (p != null && p.prev != null) ordenarLista(esquerda, p.prev);
        if (p != null && p.next != null) ordenarLista(p.next, direita);
    }

    static void imprimirShow(Show s) {
        System.out.print("=> " + s.id + " ## " + s.name + " ## " + s.category + " ## " + s.director + " ## [");
        for (int i = 0; i < s.cast.size(); i++) {
            System.out.print(s.cast.get(i));
            if (i < s.cast.size() - 1) System.out.print(", ");
        }
        System.out.print("] ## " + s.country + " ## " + s.date_added.month + " " + s.date_added.day + ", " + s.date_added.year +
                " ## " + s.release_year + " ## " + s.rating + " ## " + s.duration + " ## [");
        for (int i = 0; i < s.categories.size(); i++) {
            System.out.print(s.categories.get(i));
            if (i < s.categories.size() - 1) System.out.print(", ");
        }
        System.out.println("] ##");
    }

    public static void main(String[] args) throws Exception {
        carregarCatalogo();
        Scanner sc = new Scanner(System.in);
        Node inicio = null, fim = null;

        while (sc.hasNextLine()) {
            String entrada = sc.nextLine().trim();
            if (entrada.equalsIgnoreCase("FIM")) break;
            for (Show s : catalogo) {
                if (s.id.equalsIgnoreCase(entrada)) {
                    if (inicio == null) {
                        inicio = fim = new Node(new Show(s));
                    } else {
                        fim = adicionarNo(fim, s);
                    }
                    break;
                }
            }
        }

        Node aux = fim;
        while (aux != null && aux.prev != null) aux = aux.prev;
        inicio = aux;

        long ini = System.currentTimeMillis();
        if (inicio != null && fim != null) ordenarLista(inicio, fim);
        long fimTempo = System.currentTimeMillis();

        for (Node n = inicio; n != null; n = n.next) {
            imprimirShow(n.show);
        }

        double tempoExecucao = (fimTempo - ini) / 1000.0;
        PrintWriter log = new PrintWriter("843309_quicksort3.txt");
        log.printf("843309\t%d\t%d\t%.6f\n", comparacoes, movimentacoes, tempoExecucao);
        log.close();
    }
}
