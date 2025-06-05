import java.util.Scanner;
import java.util.Random;

public class Ex04 {

    public static boolean verificarFIM(String entrada) {
        return entrada.length() == 3 &&
                entrada.charAt(0) == 'F' &&
                entrada.charAt(1) == 'I' &&
                entrada.charAt(2) == 'M';
    }

    public static String substituirLetras(String texto, char antiga, char nova) {
        String resultado = "";

        for (int i = 0; i < texto.length(); i++) {
            char caractere = texto.charAt(i);

            if (caractere == antiga) {
                resultado += nova;
            } else {
                resultado += caractere;
            }
        }

        return resultado;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random randomizador = new Random();
        randomizador.setSeed(4);

        while (true) {
            String entrada = scanner.nextLine();

            if (verificarFIM(entrada)) {
                // Remove o println("FIM");
                break;
            }

            char letraAntiga = (char) ('a' + Math.abs(randomizador.nextInt()) % 26);
            char letraNova = (char) ('a' + Math.abs(randomizador.nextInt()) % 26);

            String entradaAlterada = substituirLetras(entrada, letraAntiga, letraNova);

            System.out.println(entradaAlterada);
        }

        scanner.close();
    }
}
