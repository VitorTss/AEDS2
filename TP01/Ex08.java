import java.util.Scanner;

public class Ex08 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        while (sc.hasNextInt()) {
            int num = sc.nextInt();
            System.out.println(somaDigitos(num));
        }
        
        sc.close();
    }

    public static int somaDigitos(int num) {
        if (num == 0) return 0;
        return (num % 10) + somaDigitos(num / 10);
    }
}
