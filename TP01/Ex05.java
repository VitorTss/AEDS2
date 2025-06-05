import java.util.Scanner;

public class Ex05 {

    public static boolean verificarFim(String entrada) {
        return entrada.length() == 3 &&
                entrada.charAt(0) == 'F' &&
                entrada.charAt(1) == 'I' &&
                entrada.charAt(2) == 'M';
    }

    public static String substituirVariaveisExpressao(String expressao, char[] valores) {
        String novaExpressao = "";

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                int indice = c - 'A';
                novaExpressao += valores[indice];
            } else {
                novaExpressao += c;
            }
        }

        return novaExpressao;
    }

    public static int avaliarExpressao(String expressao) {
        while (expressao.length() > 1) {
            int indexFechaParenteses = expressao.indexOf(')');
            if (indexFechaParenteses == -1) {
                break;
            }

            int indexAbreParenteses = indexFechaParenteses;
            while (indexAbreParenteses >= 0 && expressao.charAt(indexAbreParenteses) != '(') {
                indexAbreParenteses--;
            }

            if (indexAbreParenteses < 0) {
                break;
            }

            String conteudoParenteses = expressao.substring(indexAbreParenteses + 1, indexFechaParenteses);

            int inicioOperador = indexAbreParenteses - 1;

            while (inicioOperador >= 0 && expressao.charAt(inicioOperador) == ' ') {
                inicioOperador--;
            }

            int fimOperador = inicioOperador;

            while (inicioOperador >= 0 && expressao.charAt(inicioOperador) >= 'a' && expressao.charAt(inicioOperador) <= 'z') {
                inicioOperador--;
            }
            inicioOperador++;

            String operador = expressao.substring(inicioOperador, fimOperador + 1);

            char resultadoOperacao = '0';

            if (operador.equals("not")) {
                resultadoOperacao = avaliarNot(conteudoParenteses);
            } else if (operador.equals("and")) {
                resultadoOperacao = avaliarAnd(conteudoParenteses);
            } else if (operador.equals("or")) {
                resultadoOperacao = avaliarOr(conteudoParenteses);
            }

            String antesExpressao = expressao.substring(0, inicioOperador);
            String depoisExpressao = expressao.substring(indexFechaParenteses + 1);

            expressao = antesExpressao + resultadoOperacao + depoisExpressao;
        }

        return expressao.charAt(0) - '0';
    }

    public static char avaliarNot(String conteudo) {
        for (int i = 0; i < conteudo.length(); i++) {
            char c = conteudo.charAt(i);
            if (c == '0') {
                return '1';
            }
            if (c == '1') {
                return '0';
            }
        }
        return '0';
    }

    public static char avaliarAnd(String conteudo) {
        boolean resultado = true;
        for (int i = 0; i < conteudo.length(); i++) {
            char c = conteudo.charAt(i);
            if (c == '0') {
                resultado = false;
            }
        }
        return resultado ? '1' : '0';
    }

    public static char avaliarOr(String conteudo) {
        boolean resultado = false;
        for (int i = 0; i < conteudo.length(); i++) {
            char c = conteudo.charAt(i);
            if (c == '1') {
                resultado = true;
            }
        }
        return resultado ? '1' : '0';
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();

            if (verificarFim(linha)) {
                break;
            }

            if (linha.length() < 3) {
                continue;
            }

            int numeroVariaveis = linha.charAt(0) - '0';
            int posicao = 2;

            char[] valoresVariaveis = new char[26];

            for (int i = 0; i < numeroVariaveis; i++) {
                if (posicao >= linha.length()) {
                    continue;
                }
                valoresVariaveis[i] = linha.charAt(posicao);
                posicao += 2;
            }

            String expressao = "";
            for (int i = posicao; i < linha.length(); i++) {
                expressao += linha.charAt(i);
            }

            if (expressao.length() == 0) {
                System.out.println(0);
                continue;
            }

            String expressaoSubstituida = substituirVariaveisExpressao(expressao, valoresVariaveis);
            int resultado = avaliarExpressao(expressaoSubstituida);
            System.out.println(resultado);
        }

        scanner.close();
    }
}
