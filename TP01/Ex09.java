import java.util.*;

public class Ex09 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            
            if (s.equalsIgnoreCase("FIM")) break;

            boolean resultado = anagrama(s);
            
            if (resultado) System.out.println("SIM");
            else System.out.println("NAO");
        }

        sc.close();
    }

    public static boolean anagrama(String s) {
        boolean eAnagrama = true;  // Variável de controle

        String[] palavras = s.split(" - "); 
        
        if (palavras.length != 2) {
            eAnagrama = false;
        } else {
            String palavra1 = palavras[0].toLowerCase();
            String palavra2 = palavras[1].toLowerCase();

            if (palavra1.length() != palavra2.length()) {
                eAnagrama = false;
            } else {
                char[] array1 = palavra1.toCharArray();
                char[] array2 = palavra2.toCharArray();

                Arrays.sort(array1);
                Arrays.sort(array2);

                if (!Arrays.equals(array1, array2)) {
                    eAnagrama = false;
                }
            }
        }
        
        return eAnagrama;
    }
}
