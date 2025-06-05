import java.util.Scanner;

public class Program {

    // Método para verificar o fim do programa
    public static boolean isFIM(String s) {
        return s.length() == 3 &&
                s.charAt(0) == 'F' &&
                s.charAt(1) == 'I' &&
                s.charAt(2) == 'M';
    }

    // Função que verifica se duas strings são anagramas
    public static boolean saoAnagramas(String s1, String s2) {
        String str1 = "";
        String str2 = "";

        // Limpa e converte para minúsculo a primeira string
        for (int i = 0; i < s1.length(); i++) {
            char c = s1.charAt(i);
            if (c != ' ') {
                if (c >= 'A' && c <= 'Z') {
                    c += 32; // Transforma em minúscula
                }
                str1 += c;
            }
        }

        // Limpa e converte para minúsculo a segunda string
        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            if (c != ' ') {
                if (c >= 'A' && c <= 'Z') {
                    c += 32; // Transforma em minúscula
                }
                str2 += c;
            }
        }

        // Se o tamanho for diferente, não é anagrama
        if (str1.length() != str2.length()) {
            return false;
        }

        // Cria vetor para contar frequência dos caracteres
        int[] contador = new int[256];

        // Incrementa contador com caracteres da primeira string
        for (int i = 0; i < str1.length(); i++) {
            contador[str1.charAt(i)]++;
        }

        // Decrementa contador com caracteres da segunda string
        for (int i = 0; i < str2.length(); i++) {
            contador[str2.charAt(i)]--;
        }

        // Se algum contador for diferente de zero, não são anagramas
        for (int i = 0; i < 256; i++) {
            if (contador[i] != 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();

            // Condição de parada
            if (isFIM(linha)) {
                break;
            }

            // Separando as duas palavras na entrada com base no '-'
            int pos = linha.indexOf('-');
            String primeira = "";
            String segunda = "";

            // Pega a primeira palavra (antes do '-')
            for (int i = 0; i < pos; i++) {
                char c = linha.charAt(i);
                if (c != ' ' && c != '-') {
                    primeira += c;
                }
            }

            // Pega a segunda palavra (depois do '-')
            for (int i = pos + 1; i < linha.length(); i++) {
                char c = linha.charAt(i);
                if (c != ' ' && c != '-') {
                    segunda += c;
                }
            }

            boolean resultado = saoAnagramas(primeira, segunda);

            // Imprime "SIM" ou "NÃO" com acento corretamente
            if (resultado == true) {
                System.out.println("SIM");
            } else {
                System.out.println("NÃO");  // <-- Aqui tá certinho!
            }
        }

        sc.close();
    }
}