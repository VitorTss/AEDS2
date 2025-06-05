
import java.util.*;

public class Ex01 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        
        while(!s.equalsIgnoreCase("FIM")){
            
            if( palindromo(s) ) System.out.println("SIM");
                
                else System.out.println("NAO");

                s = sc.nextLine();
        }
        sc.close();
    }

        public static boolean palindromo(String nome){
            boolean flag = true; 

            int i = 0;
            int j = nome.length()-1; // usado para pegar o tamanho da string
            
            while (i <= j && flag ) { 
                    if(nome.charAt(i) != nome.charAt(j)){ //charAt 
                        flag = false;
                    }
                    
                    i++;
                    j--;

            }
            return flag;

        }

}