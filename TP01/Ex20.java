import java.util.Scanner;

public class Ex20 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        while (!s.equalsIgnoreCase("FIM")) {
            System.out.print(vogais(s, 0) ? "SIM " : "NAO ");
            System.out.print(consoante(s, 0) ? "SIM " : "NAO ");
            System.out.print(inteiro(s, 0) ? "SIM " : "NAO ");
            System.out.print(real(s, 0, 0) ? "SIM " : "NAO ");
            System.out.println();
            s = sc.nextLine();
        }
        sc.close();
    }

    public static boolean vogais(String s, int i) {
        if (i == s.length()) return true;
        char c = Character.toLowerCase(s.charAt(i));
        if ("aeiou".indexOf(c) == -1) return false;
        return vogais(s, i + 1);
    }

    public static boolean consoante(String s, int i) {
        if (i == s.length()) return true;
        char c = Character.toLowerCase(s.charAt(i));
        if (!Character.isLetter(c) || "aeiou".indexOf(c) != -1) return false;
        return consoante(s, i + 1);
    }

    public static boolean inteiro(String s, int i) {
        if (i == s.length()) return true;
        if (!Character.isDigit(s.charAt(i))) return false;
        return inteiro(s, i + 1);
    }

    public static boolean real(String s, int i, int count) {
        if (i == s.length()) return count <= 1;
        char c = s.charAt(i);
        if (!Character.isDigit(c)) {
            if (c == '.' || c == ',') count++;
            else return false;
        }
        return real(s, i + 1, count);
    }
}