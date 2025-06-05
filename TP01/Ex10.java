import java.util.Scanner;

public class Ex10 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in); 
        String s = sc.nextLine();

        while(!s.equalsIgnoreCase("FIM")){
           
            System.out.println(espaco(s));
            
            s = sc.nextLine();
        }
        
    }

    public static int espaco(String s){
        int conta = 0;
       
        for(int i = 0; i <= s.length()-1; i++){
            if(s.charAt(i) == 32) conta++;
        }
        
        return conta + 1;
    }

    
}
