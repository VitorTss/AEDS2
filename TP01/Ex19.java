import java.util.Scanner;

public class Ex19 {

    public static boolean verificarFim(String entrada) {
        return entrada.length() == 3 &&
                entrada.charAt(0) == 'F' &&
                entrada.charAt(1) == 'I' &&
                entrada.charAt(2) == 'M';
    }

    public static String substituirVariaveisExpressao(String expressao, char[] valores, int indice) {
        if (indice >= expressao.length()) {
            return "";
        }

        char c = expressao.charAt(indice);

        if (c >= 'A' && c <= 'Z') {
            int pos = c - 'A';
            return valores[pos] + substituirVariaveisExpressao(expressao, valores, indice + 1);
        } else {
            return c + substituirVariaveisExpressao(expressao, valores, indice + 1);
        }
    }

    public static int avaliarExpressao(String expressao) {
        return avaliarExpressaoRecursivo(expressao, 0);
    }

    public static int avaliarExpressaoRecursivo(String expressao, int index) {
        if (expressao.length() == 1) {
            return expressao.charAt(index) - '0';
        }

        int indexFechaParenteses = expressao.indexOf(')', index);
        if (indexFechaParenteses == -1) {
            return expressao.charAt(index) - '0';
        }

        int indexAbreParenteses = indexFechaParenteses;
        while (indexAbreParenteses >= 0 && expressao.charAt(indexAbreParenteses) != '(') {
            indexAbreParenteses--;
        }

        if (indexAbreParenteses < 0) {
            return expressao.charAt(index) - '0';
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

        return avaliarExpressaoRecursivo(antesExpressao + resultadoOperacao + depoisExpressao, 0);
    }

    public static char avaliarNot(String conteudo) {
        if (conteudo.length() == 0) {
            return '0';
        }

        char c = conteudo.charAt(0);
        return (c == '0') ? '1' : '0';
    }

    public static char avaliarAnd(String conteudo) {
        if (conteudo.length() == 0) {
            return '1';
        }

        char c = conteudo.charAt(0);
        return (c == '0') ? '0' : avaliarAnd(conteudo.substring(1));
    }

    public static char avaliarOr(String conteudo) {
        if (conteudo.length() == 0) {
            return '0';
        }

        char c = conteudo.charAt(0);
        return (c == '1') ? '1' : avaliarOr(conteudo.substring(1));
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

            String expressaoSubstituida = substituirVariaveisExpressao(expressao, valoresVariaveis, 0);
            int resultado = avaliarExpressao(expressaoSubstituida);
            System.out.println(resultado);
        }

        scanner.close();
    }
}
