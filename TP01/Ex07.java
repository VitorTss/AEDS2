import java.util.*;

public class Ex07 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        while(!s.equalsIgnoreCase("FIM")){
            System.out.println(inverte(s));

            s = sc.nextLine();
        }

    }

    public static String inverte(String s){
        char[] palavra = s.toCharArray();
        
        int esquerda = 0, direita = s.length()-1;

        while(esquerda < direita){
            char tmp = palavra[esquerda];
            palavra [esquerda] = palavra[direita];
            palavra[direita] = tmp;

            esquerda++;
            direita--;
        }
        return new String(palavra);

    }
}

