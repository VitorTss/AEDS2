import java.util.Scanner;

public class Ex11{
    
    public static int comprimentoSubstringSemRepeticao(String s) {
        boolean[] visto = new boolean[128]; 
        int maxLength = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            
            while (visto[c]) {
                visto[s.charAt(left)] = false;
                left++;
            }
            
            visto[c] = true;
            int tamanhoAtual = right - left + 1;
            if (tamanhoAtual > maxLength) {
                maxLength = tamanhoAtual;
            }
        }
        
        return maxLength;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            if (linha.isEmpty()) break;
            System.out.println(comprimentoSubstringSemRepeticao(linha));
        }

        scanner.close();
    }
}
