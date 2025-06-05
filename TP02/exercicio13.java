import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Show {
    // Atributos
    private String show_id;
    private String type;
    private String title;
    private String director;
    private String[] cast;
    private String country;
    private Date date_added;
    private int release_year;
    private String rating;
    private String duration;
    private String[] listed_in;

    // Construtor padrão
    public Show() {
        this.show_id = this.type = this.title = this.director = this.country = this.rating = this.duration = "NaN";
        this.cast = new String[0];
        this.listed_in = new String[0];
        this.date_added = null;
        this.release_year = -1;
    }

    // Getters e Setters
    public String getShow_id() { return show_id; }
    public void setShow_id(String show_id) { this.show_id = show_id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String[] getCast() { return cast; }
    public void setCast(String[] cast) { this.cast = cast; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Date getDate_added() { return date_added; }
    public void setDate_added(Date date_added) { this.date_added = date_added; }

    public int getRelease_year() { return release_year; }
    public void setRelease_year(int release_year) { this.release_year = release_year; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String[] getListed_in() { return listed_in; }
    public void setListed_in(String[] listed_in) { this.listed_in = listed_in; }

    public void lerLinha(String linhaCSV) {
        try {
            List<String> camposSeparados = new ArrayList<>();
            StringBuilder bufferCampo = new StringBuilder();
            boolean dentroAspas = false;

            for (int i = 0; i < linhaCSV.length(); i++) {
                char caractere = linhaCSV.charAt(i);
                if (caractere == '"') {
                    dentroAspas = !dentroAspas;
                } else if (caractere == ',' && !dentroAspas) {
                    camposSeparados.add(bufferCampo.toString().trim());
                    bufferCampo.setLength(0);
                } else {
                    bufferCampo.append(caractere);
                }
            }

            camposSeparados.add(bufferCampo.toString().trim());

            String[] campos = camposSeparados.toArray(new String[0]);
            this.show_id = campos[0];
            this.type = campos[1];
            this.title = campos[2].replace("\"", "");
            this.director = campos[3].isEmpty() ? "NaN" : campos[3];
            this.cast = campos[4].isEmpty() ? new String[]{"NaN"} : campos[4].split(",\\s*");
            Arrays.sort(this.cast);
            this.country = campos[5].isEmpty() ? "NaN" : campos[5];

            try {
                SimpleDateFormat formatoData = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
                this.date_added = campos[6].isEmpty() ? null : formatoData.parse(campos[6]);
            } catch (Exception e) {
                this.date_added = null;
            }

            this.release_year = campos[7].isEmpty() ? -1 : Integer.parseInt(campos[7]);
            this.rating = campos[8].isEmpty() ? "NaN" : campos[8];
            this.duration = campos[9].isEmpty() ? "NaN" : campos[9];
            this.listed_in = campos[10].isEmpty() ? new String[]{"NaN"} : campos[10].split(",\\s*");
            Arrays.sort(this.listed_in);
        } catch (Exception e) {
            System.out.println("Erro ao ler linha: " + e.getMessage());
        }
    }

    public void imprimir() {
        System.out.print("=> " + show_id + " ## " + title + " ## " + type + " ## " + director + " ## [");
        System.out.print(String.join(", ", cast));
        System.out.print("] ## " + country + " ## ");
        if (date_added != null) {
            SimpleDateFormat formatoSaida = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            System.out.print(formatoSaida.format(date_added));
        } else {
            System.out.print("NaN");
        }
        System.out.print(" ## " + release_year + " ## " + rating + " ## " + duration + " ## [");
        System.out.print(String.join(", ", listed_in));
        System.out.println("] ##");
    }

    public Show clone() {
        Show copia = new Show();
        copia.show_id = this.show_id;
        copia.type = this.type;
        copia.title = this.title;
        copia.director = this.director;
        copia.cast = this.cast.clone();
        copia.country = this.country;
        copia.date_added = this.date_added;
        copia.release_year = this.release_year;
        copia.rating = this.rating;
        copia.duration = this.duration;
        copia.listed_in = this.listed_in.clone();
        return copia;
    }
}

public class exercicio13 {
    static List<Show> catalogo = new ArrayList<>();

    public static void preencherCatalogo() {
        String caminho = "/tmp/disneyplus.csv";
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminho));
            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                Show s = new Show();
                s.lerLinha(linha);
                catalogo.add(s);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    public static boolean isFim(String str) {
        return str.equals("FIM");
    }

    public static void mergeSort(List<Show> lista, int esq, int dir, int[] comp, int[] mov) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergeSort(lista, esq, meio, comp, mov);
            mergeSort(lista, meio + 1, dir, comp, mov);
            intercalar(lista, esq, meio, dir, comp, mov);
        }
    }

    public static void intercalar(List<Show> lista, int esq, int meio, int dir, int[] comp, int[] mov) {
        List<Show> left = new ArrayList<>(lista.subList(esq, meio + 1));
        List<Show> right = new ArrayList<>(lista.subList(meio + 1, dir + 1));
        int i = 0, j = 0, k = esq;

        while (i < left.size() && j < right.size()) {
            comp[0]++;
            Show a = left.get(i);
            Show b = right.get(j);

            boolean menor;
            if (a.getDuration().compareTo(b.getDuration()) < 0) {
                menor = true;
            } else if (a.getDuration().compareTo(b.getDuration()) > 0) {
                menor = false;
            } else {
                menor = a.getTitle().compareTo(b.getTitle()) <= 0;
                comp[0]++;
            }

            lista.set(k++, menor ? left.get(i++) : right.get(j++));
            mov[0]++;
        }

        while (i < left.size()) {
            lista.set(k++, left.get(i++));
            mov[0]++;
        }

        while (j < right.size()) {
            lista.set(k++, right.get(j++));
            mov[0]++;
        }
    }

    public static void main(String[] args) {
        preencherCatalogo();
        Scanner sc = new Scanner(System.in);
        List<Show> selecionados = new ArrayList<>();
        String entrada = sc.nextLine();

        while (!isFim(entrada)) {
            for (Show s : catalogo) {
                if (s.getShow_id().equals(entrada)) {
                    selecionados.add(s.clone());
                    break;
                }
            }
            entrada = sc.nextLine();
        }

        int[] comparacoes = { 0 }, movimentacoes = { 0 };
        long inicio = System.currentTimeMillis();

        mergeSort(selecionados, 0, selecionados.size() - 1, comparacoes, movimentacoes);

        for (Show s : selecionados) {
            s.imprimir();
        }

        long fim = System.currentTimeMillis();

        try {
            PrintWriter out = new PrintWriter("matricula_mergesort.txt", "UTF-8");
            out.printf("843309\t%d\t%d\t%dms\n", comparacoes[0], movimentacoes[0], (fim - inicio));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
