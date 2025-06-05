import java.util.Scanner;

public class Ex06 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); 
        String s = sc.nextLine();

        while(!s.equalsIgnoreCase("FIM")){

            if(vogais(s)) System.out.print("SIM ");
            else System.out.print("NAO ");
            
            if(consoante(s)) System.out.print("SIM ");
            else System.out.print("NAO ");

            if(inteiro(s)) System.out.print("SIM ");
            else System.out.print("NAO ");

            if(real(s)) System.out.print("SIM ");
            else System.out.print("NAO ");
            System.out.println("");
          
            s = sc.nextLine();
        }
        
      
    }

    public static boolean vogais(String s){
        boolean flag = true; 
       
        for(int i = 0; i <= s.length()-1 && flag; i++){
            if(s.charAt(i) != 'A' && s.charAt(i) != 'a' && s.charAt(i) != 'E' && s.charAt(i) != 'e' && s.charAt(i) != 'I' && s.charAt(i) != 'i' && s.charAt(i) != 'O' && s.charAt(i) != 'o' && s.charAt(i) != 'U' && s.charAt(i) != 'u'){

                flag = false;
            }
        }

        return flag;
    }

    public static boolean consoante(String s){

        boolean ehconsoante = true;
        
        for(int i = 0; i <= s.length()-1 && ehconsoante; i++){
            if(s.charAt(i) == 'A' || s.charAt(i) == 'a' || s.charAt(i) == 'E' || s.charAt(i) == 'e' || s.charAt(i) == 'I' || s.charAt(i) == 'i' || s.charAt(i) == 'O' || s.charAt(i) == 'o' || s.charAt(i) == 'U' || s.charAt(i) == 'u'){
                
                ehconsoante = false;
            }

            if(s.charAt(i) > 47 && s.charAt(i) < 58) ehconsoante = false;
        }
        
        return ehconsoante;
    }
    
    
    public static boolean inteiro(String s){
        boolean ehinteiro = true;
        
        for(int i = 0; i <= s.length()-1 && ehinteiro; i++){
            
            if(s.charAt(i) <= 47 || s.charAt(i) >= 58) ehinteiro = false;
    
        }    
        
        return ehinteiro;
    }
   
    public static boolean real(String s){
        boolean ehreal = true;
        int contador = 0;
       
        for(int i = 0; i <= s.length()-1 && ehreal; i++){

        if(s.charAt(i) <= 47 || s.charAt(i) >= 58){
            if(s.charAt(i) == 44 || s.charAt(i) == 46) contador++;
            else ehreal = false;
        }
       }
        if(contador > 1) ehreal = false;

        return ehreal;
    }
}