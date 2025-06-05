import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

//class show
class Show {
    private String id;
    private String tipo;
    private String titulo;
    private String diretor;
    private String[] elenco;
    private String pais;
    private Date dataAdicao;
    private int anoLancamento;
    private String classificacao;
    private String duracao;
    private String[] categorias;

    public Show() {
        this.id = this.tipo = this.titulo = this.diretor = this.pais = this.classificacao = this.duracao = "NaN";
        this.elenco = new String[0];
        this.categorias = new String[0];
        this.dataAdicao = null;
        this.anoLancamento = -1;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDiretor() { return diretor; }
    public void setDiretor(String diretor) { this.diretor = diretor; }

    public String[] getElenco() { return elenco; }
    public void setElenco(String[] elenco) { this.elenco = elenco; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public Date getDataAdicao() { return dataAdicao; }
    public void setDataAdicao(Date dataAdicao) { this.dataAdicao = dataAdicao; }

    public int getAnoLancamento() { return anoLancamento; }
    public void setAnoLancamento(int anoLancamento) { this.anoLancamento = anoLancamento; }

    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }

    public String getDuracao() { return duracao; }
    public void setDuracao(String duracao) { this.duracao = duracao; }

    public String[] getCategorias() { return categorias; }
    public void setCategorias(String[] categorias) { this.categorias = categorias; }

    public void lerLinha(String linha) {
        try {
            String[] partes = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            this.id = partes[0];
            this.tipo = partes[1];
            this.titulo = partes[2].replace("\"", "").trim();
            this.diretor = partes[3].isEmpty() ? "NaN" : partes[3].replace("\"", "").trim();
            this.elenco = partes[4].isEmpty() ? new String[]{"NaN"} : partes[4].replace("\"", "").split(",\\s*");
            Arrays.sort(this.elenco);
            this.pais = partes[5].isEmpty() ? "NaN" : partes[5].trim().replace("\"", "");

            StringBuilder buffer = new StringBuilder();
            boolean dentroAspas = false;
            int campoPos = 0;

            for (int i = 0; i < linha.length(); i++) {
                char c = linha.charAt(i);
                if (c == '"') {
                    dentroAspas = !dentroAspas;
                } else if (c == ',' && !dentroAspas) {
                    if (campoPos == 6) break;
                    campoPos++;
                    buffer.setLength(0);
                } else {
                    buffer.append(c);
                }
            }

            String valorData = buffer.toString().trim();

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                this.dataAdicao = valorData.isEmpty() ? sdf.parse("March 1, 1900") : sdf.parse(valorData);
            } catch (Exception e) {
                this.dataAdicao = null;
            }

            this.anoLancamento = partes[7].isEmpty() ? -1 : Integer.parseInt(partes[7].trim());
            this.classificacao = partes[8].isEmpty() ? "NaN" : partes[8].trim();
            this.duracao = partes[9].isEmpty() ? "NaN" : partes[9].trim();
            this.categorias = partes[10].isEmpty() ? new String[]{"NaN"} : partes[10].replace("\"", "").split(",\\s*");
            Arrays.sort(this.categorias);

        } catch (Exception e) {
            System.out.println("Erro ao ler linha: " + e.getMessage());
        }
    }

    public void imprimir() {
        System.out.print("=> " + id + " ## " + titulo + " ## " + tipo + " ## " + diretor + " ## [");
        System.out.print(String.join(", ", elenco));
        System.out.print("] ## " + pais + " ## ");
        if (dataAdicao != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            System.out.print(sdf.format(dataAdicao));
        } else {
            System.out.print("NaN");
        }
        System.out.print(" ## " + anoLancamento + " ## " + classificacao + " ## " + duracao + " ## [");
        System.out.print(String.join(", ", categorias));
        System.out.println("] ##");
    }

    public Show clone() {
        Show copia = new Show();
        copia.id = this.id;
        copia.tipo = this.tipo;
        copia.titulo = this.titulo;
        copia.diretor = this.diretor;
        copia.elenco = this.elenco.clone();
        copia.pais = this.pais;
        copia.dataAdicao = this.dataAdicao;
        copia.anoLancamento = this.anoLancamento;
        copia.classificacao = this.classificacao;
        copia.duracao = this.duracao;
        copia.categorias = this.categorias.clone();
        return copia;
    }
}

public class exercicio18 {

    static Show[] baseDados = new Show[10000];
    static int quantidade = 0;
    static long inicioTempo;
    static int[] contComparacoes = {0};
    static int[] contTrocas = {0};

    public static void carregarDados() {
        String caminho = "/tmp/disneyplus.csv";
        try {
            Scanner leitor = new Scanner(new File(caminho));
            leitor.nextLine();
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                Show s = new Show();
                s.lerLinha(linha);
                baseDados[quantidade++] = s;
            }
            leitor.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    public static boolean isFim(String str) {
        return str.equals("FIM");
    }

    public static void quickSort(Show[] arr, int inicio, int fim) {
        if (inicio < fim) {
            int p = particionar(arr, inicio, fim);
            quickSort(arr, inicio, p - 1);
            quickSort(arr, p + 1, fim);
        }
    }

    public static int particionar(Show[] arr, int inicio, int fim) {
        Show pivo = arr[fim];
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (arr[j].getDataAdicao().compareTo(pivo.getDataAdicao()) < 0 ||
                (arr[j].getDataAdicao().compareTo(pivo.getDataAdicao()) == 0 &&
                 arr[j].getTitulo().compareToIgnoreCase(pivo.getTitulo()) < 0)) {
                i++;
                trocar(arr, i, j);
                contTrocas[0]++;
            }
            contComparacoes[0]++;
        }
        trocar(arr, i + 1, fim);
        contTrocas[0]++;
        return i + 1;
    }

    public static void trocar(Show[] arr, int i, int j) {
        Show aux = arr[i];
        arr[i] = arr[j];
        arr[j] = aux;
    }

    public static void main(String[] args) {
        inicioTempo = System.nanoTime();

        carregarDados();
        Scanner in = new Scanner(System.in);

        Show[] selecionados = new Show[10000];
        int contSel = 0;
        String entrada = in.nextLine();

        while (!isFim(entrada)) {
            for (int i = 0; i < quantidade; i++) {
                if (baseDados[i].getId().equals(entrada)) {
                    selecionados[contSel++] = baseDados[i];
                    break;
                }
            }
            entrada = in.nextLine();
        }

        quickSort(selecionados, 0, contSel - 1);

        for (int i = 0; i < Math.min(10, contSel); i++) {
            selecionados[i].imprimir();
        }

        long fimTempo = System.nanoTime();
        in.close();

        double tempoExec = (fimTempo - inicioTempo) / 1e6;

        try {
            FileWriter fw = new FileWriter("843309_quick.txt");
            fw.write("843309\t" + contComparacoes[0] + "\t" + contTrocas[0] + "\t" + tempoExec);
            fw.close();
        } catch (IOException e) {
            System.out.println("Erro ao gravar log: " + e.getMessage());
        }
    }
}