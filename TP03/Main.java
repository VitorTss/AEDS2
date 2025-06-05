import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Show {
    //atributos
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


    public Show() { //contrutor padrão
        this.show_id = this.type = this.title = this.director = this.country = this.rating = this.duration = "NaN";
        this.cast = new String[0];
        this.listed_in = new String[0];
        this.date_added = null;
        this.release_year = -1;
    }

    // nos sets e nos gets e sets ja posso tratar o ID arrancando o "s" e tranformando o id em int.
    
    public String getShow_id(){
        return show_id;
    }
    public void setShow_id(String show_id){
        this.show_id = show_id;
    }
   
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }

    
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    
    public String getDirector(){
        return director;
    }
    public void setDirector(String director){
        this.director = director;
    }

   
    public String[] getCast(){
        return cast;
    }
    public void setCast(String[] cast){
        this.cast = cast;
    }

   
    public String getCountry(){
        return country;
    }
    public void setCountry(String country){
        this.country = country;
    }

   
    public Date getDate_added(){
        return date_added;
    }
    public void setDate_added(Date date_added){
        this.date_added = date_added;
    }

   
    public int getRelease_year(){
        return release_year;
    }
    public void setRelease_year(int release_year){
        this.release_year = release_year;
    }

   
    public String getRating(){
        return rating;
    }
    public void setRating(String rating){
        this.rating = rating;
    }

   
    public String getDuration(){
        return duration;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }

    
    public String[] getListed_in(){
        return listed_in;
    }
    public void setListed_in(String[] listed_in){
        this.listed_in = listed_in;
    }

  
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
            SimpleDateFormat formatoSaida = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
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

/**
 * Lista estatica
 * @author Max do Val Machado
 * @version 2 01/2015
 */
class Lista {
    private Show[] array;
    private int n;
 
 
    /**
     * Construtor da classe.
     */
    public Lista () {
       this(6);
    }
 
 
    /**
     * Construtor da classe.
     * @param tamanho Tamanho da lista.
     */
    public Lista (int tamanho){
       array = new Show[tamanho];
       n = 0;
    }
 
 
    /**
     * Insere um elemento na primeira posicao da lista e move os demais
     * elementos para o fim da lista.
     * @param x int elemento a ser inserido.
     * @throws Exception Se a lista estiver cheia.
     */
    public void inserirInicio(Show x) throws Exception {
 
       //validar insercao
       if(n >= array.length){
          throw new Exception("Erro ao inserir!");
       } 
 
       //levar elementos para o fim do array
       for(int i = n; i > 0; i--){
          array[i] = array[i-1];
       }
 
       array[0] = x;
       n++;
    }
 
 
    /**
     * Insere um elemento na ultima posicao da lista.
     * @param x int elemento a ser inserido.
     * @throws Exception Se a lista estiver cheia.
     */
    public void inserirFim(Show x) throws Exception {
 
       //validar insercao
       if(n >= array.length){
          throw new Exception("Erro ao inserir!");
       }
 
       array[n] = x;
       n++;
    }
 
 
    /**
     * Insere um elemento em uma posicao especifica e move os demais
     * elementos para o fim da lista.
     * @param x int elemento a ser inserido.
     * @param pos Posicao de insercao.
     * @throws Exception Se a lista estiver cheia ou a posicao invalida.
     */
    public void inserir(Show x, int pos) throws Exception {
 
       //validar insercao
       if(n >= array.length || pos < 0 || pos > n){
          throw new Exception("Erro ao inserir!");
       }
 
       //levar elementos para o fim do array
       for(int i = n; i > pos; i--){
          array[i] = array[i-1];
       }
 
       array[pos] = x;
       n++;
    }
 
 
    /**
     * Remove um elemento da primeira posicao da lista e movimenta 
     * os demais elementos para o inicio da mesma.
     * @return resp int elemento a ser removido.
     * @throws Exception Se a lista estiver vazia.
     */
    public Show removerInicio() throws Exception {
 
       //validar remocao
       if (n == 0) {
          throw new Exception("Erro ao remover!");
       }
 
       Show resp = array[0];
       n--;
 
       for(int i = 0; i < n; i++){
          array[i] = array[i+1];
       }
 
       return resp;
    }
 
 
    /**
     * Remove um elemento da ultima posicao da lista.
     * @return resp int elemento a ser removido.
     * @throws Exception Se a lista estiver vazia.
     */
    public Show removerFim() throws Exception {
 
       //validar remocao
       if (n == 0) {
          throw new Exception("Erro ao remover!");
       }
 
       return array[--n];
    }
 
 
    /**
     * Remove um elemento de uma posicao especifica da lista e 
     * movimenta os demais elementos para o inicio da mesma.
     * @param pos Posicao de remocao.
     * @return resp int elemento a ser removido.
     * @throws Exception Se a lista estiver vazia ou a posicao for invalida.
     */
    public Show remover(int pos) throws Exception {
 
       //validar remocao
       if (n == 0 || pos < 0 || pos >= n) {
          throw new Exception("Erro ao remover!");
       }
 
       Show resp = array[pos];
       n--;
 
       for(int i = pos; i < n; i++){
          array[i] = array[i+1];
       }
 
       return resp;
    }
 
 
    /**
     * Mostra os elementos da lista separados por espacos.
     */
    public void mostrar (){
       for(int i = 0; i < n; i++){
          array[i].imprimir();
       }
       
    }
 
 
    /**
     * Procura um elemento e retorna se ele existe.
     * @param x int elemento a ser pesquisado.
     * @return <code>true</code> se o array existir,
     * <code>false</code> em caso contrario.
     */
    public boolean pesquisar(Show x) {
       boolean retorno = false;
       for (int i = 0; i < n && retorno == false; i++) {
          retorno = (array[i] == x);
       }
       return retorno;
    }
 }


 //classe main
public class Main {
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

    //main principal
    public static void main(String[] args) {
        preencherCatalogo();
        Scanner sc = new Scanner(System.in);
        String entrada = sc.nextLine(); 
        Lista lista = new Lista(100);

        while (!isFim(entrada)) {
            for (int i = 0; i < catalogo.size(); i++) {
                Show s = catalogo.get(i);
                if (s.getShow_id().equals(entrada)) {
                    try {
                        lista.inserirFim(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            entrada = sc.nextLine();
        }
      
        int numero = sc.nextInt();

        for(int i = 0; i < numero; i++){
            entrada = sc.next();
            String id;
            if(entrada.equals("II")){
                id = sc.next();
                boolean achou = false;
                Show s = null;
                for (int j = 0; j < catalogo.size() && !achou; j++) {
                    s = catalogo.get(j);
                    if (s.getShow_id().equals(id)) {
                        achou = true;
                    }
                }
                try {
                    lista.inserirInicio(s);
                } catch (Exception e) {
                }
                
            }
            
            else if(entrada.equals("IF")){
                id = sc.next();
                boolean achou = false;
                Show s = null;
                for (int j = 0; j < catalogo.size() && !achou; j++) {
                    s = catalogo.get(j);
                    if (s.getShow_id().equals(id)) {
                        achou = true;
                    }
                }
                try {
                    lista.inserirFim(s);
                } catch (Exception e) {
                }
                
            }
            
            else if(entrada.equals("I*")){
               
                int posicao = sc.nextInt();
                id = sc.next();
                boolean achou = false;
                Show s = null;
                

                for (int j = 0; j < catalogo.size() && !achou; j++) {
                    s = catalogo.get(j);
                    if (s.getShow_id().equals(id)) {
                        achou = true;
                    }
                }
                try {
                    lista.inserir(s, posicao);

                } catch (Exception e) {
                }
                
            }
         
           else if(entrada.equals("RI")){
            Show s = null;
                try {
                     s = lista.removerInicio();
                } catch (Exception e) {
                }

                System.out.println("(R) " + s.getTitle());
                
            }
            
            else if(entrada.equals("RF")){
                Show s = null;
                    try {
                         s = lista.removerFim();
                    } catch (Exception e) {
                    }
    
                    System.out.println("(R) " + s.getTitle());
                    
                }
            
                else {
                    int posicao = sc.nextInt();

                    Show s = null;
                        try {
                             s = lista.remover(posicao);
                        } catch (Exception e) {
                        }
        
                        System.out.println("(R) " + s.getTitle());
                        
                    }
                
        }

        lista.mostrar();
        
        sc.close();



    }
}
