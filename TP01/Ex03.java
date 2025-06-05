import java.util.*;

public class Ex03 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        while(!s.equalsIgnoreCase ("FIM")){
            System.out.println(ciframento(s));
            s = sc.nextLine();
        }
    }

        public static String ciframento(String s){

            char[] cifra = new char[s.length()]; 
            
            for(int i=0; i < s.length(); i++){
                char c = s.charAt(i);

                if(c >= 1 && c <= 127){
                    cifra[i] = (char) (c + 3);
                }
                else cifra[i] = c; 
            }
            return new String(cifra);
        }
}
