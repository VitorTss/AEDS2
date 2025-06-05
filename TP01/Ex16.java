import java.util.*;

public class Ex16 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        
        while (!s.equalsIgnoreCase("FIM")) {
            if (palindromo(s, 0, s.length() - 1))
                System.out.println("SIM");
            else
                System.out.println("NAO");
            
            s = sc.nextLine();
        }
        sc.close();
    }

    public static boolean palindromo(String nome, int i, int j) {
        if (i >= j) return true;
        if (nome.charAt(i) != nome.charAt(j)) return false;
        return palindromo(nome, i + 1, j - 1);
    }
}
