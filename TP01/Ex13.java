import java.io.*;
import java.net.*; 

public class Ex13 {

    public static int[] contarCaracteres(String conteudo) {
        int contagem[] = new int[25];

        for (int i = 0; i < 25; i++) {
            contagem[i] = 0;
        }

        for (int i = 0; i < conteudo.length(); i++) {
            if (conteudo.charAt(i) == '\u0061') {
                contagem[0] += 1;
            } else if (conteudo.charAt(i) == '\u0065') {
                contagem[1] += 1;
            } else if (conteudo.charAt(i) == '\u0069') {
                contagem[2] += 1;
            } else if (conteudo.charAt(i) == '\u006f') {
                contagem[3] += 1;
            } else if (conteudo.charAt(i) == '\u0075') {
                contagem[4] += 1;
            } else if (conteudo.charAt(i) == '\u00e1') {
                contagem[5] += 1;
            } else if (conteudo.charAt(i) == '\u00e9') {
                contagem[6] += 1;
            } else if (conteudo.charAt(i) == '\u00ed') {
                contagem[7] += 1;
            } else if (conteudo.charAt(i) == '\u00f3') {
                contagem[8] += 1;
            } else if (conteudo.charAt(i) == '\u00fa') {
                contagem[9] += 1;
            } else if (conteudo.charAt(i) == '\u00e0') {
                contagem[10] += 1;
            } else if (conteudo.charAt(i) == '\u00e8') {
                contagem[11] += 1;
            } else if (conteudo.charAt(i) == '\u00ec') {
                contagem[12] += 1;
            } else if (conteudo.charAt(i) == '\u00f2') {
                contagem[13] += 1;
            } else if (conteudo.charAt(i) == '\u00f9') {
                contagem[14] += 1;
            } else if (conteudo.charAt(i) == '\u00e3') {
                contagem[15] += 1;
            } else if (conteudo.charAt(i) == '\u00f5') {
                contagem[16] += 1;
            } else if (conteudo.charAt(i) == '\u00e2') {
                contagem[17] += 1;
            } else if (conteudo.charAt(i) == '\u00ea') {
                contagem[18] += 1;
            } else if (conteudo.charAt(i) == '<' && conteudo.charAt(i+1) == 'b' && conteudo.charAt(i+2) == 'r' && conteudo.charAt(i+3) == '>') {
                contagem[20] += 1;
            } else if (conteudo.charAt(i) == '<' && conteudo.charAt(i+1) == 't' && conteudo.charAt(i+2) == 'a' && conteudo.charAt(i+3) == 'b' && conteudo.charAt(i+4) == 'l' && conteudo.charAt(i+5) == 'e' && conteudo.charAt(i+6) == '>') {
                contagem[21] += 1;
                contagem[19] -= 3;
                contagem[1] -= 1;
                contagem[0] -= 1;
            } else if (conteudo.charAt(i) == '\u00f4') {
                contagem[22] += 1;
            } else if (conteudo.charAt(i) == '\u00fb') {
                contagem[23] += 1;
            } else if (conteudo.charAt(i) == '\u00ee') {
                contagem[24] += 1;
            } else if ((conteudo.charAt(i) >= 'a' && conteudo.charAt(i) <= 'z')) {
                contagem[19] += 1;
            }
        }
        return contagem;
    }

    public static String obterHtml(String endereco) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String resposta = "", linha;

        try {
            url = new URL(endereco);
            is = url.openStream();
            br = new BufferedReader(new InputStreamReader(is));

            while ((linha = br.readLine()) != null) {
                resposta += linha + "\n";
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        try {
            is.close();
        } catch (IOException ioe) {
        }
        return resposta;
    }

    public static void main(String[] args) {
        String url, html;
        int[] contagem;

        while (true) {
            String nome = MyIO.readLine();

            if (nome.length() == 3 && nome.charAt(0) == 'F' && nome.charAt(1) == 'I' && nome.charAt(2) == 'M') {
                break;
            }

            url = MyIO.readLine();

            html = obterHtml(url);

            contagem = contarCaracteres(html);

            MyIO.println("a("+ contagem[0] +") e("+contagem[1]+") i("+contagem[2]+") o("+contagem[3]+") u("+contagem[4]+") á("+contagem[5]+") é("+contagem[6]+") í("+contagem[7]+") ó("+contagem[8]+") ú("+contagem[9]+") à("+contagem[10]+") è("+contagem[11]+") ì("+contagem[12]+") ò("+contagem[13]+") ù("+contagem[14]+") ã("+contagem[15]+") õ("+contagem[16]+") â("+contagem[17]+") ê("+contagem[18]+") î("+contagem[24]+") ô("+contagem[22]+") û("+contagem[23]+") consoante("+contagem[19]+") <br>("+contagem[20]+") <table>("+contagem[21]+") " + nome);
        }
    }
}
