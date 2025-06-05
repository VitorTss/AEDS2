import java.util.*;

public class Ex18 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        while (!s.equalsIgnoreCase("FIM")) {
            System.out.println(ciframento(s, 0));
            s = sc.nextLine();
        }
        sc.close();
    }

    public static String ciframento(String s, int i) {
        if (i == s.length()) return "";
        char c = (char) (s.charAt(i) + 3);
        return c + ciframento(s, i + 1);
    }
}
