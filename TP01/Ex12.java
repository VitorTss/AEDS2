import java.util.Scanner;

public class Ex12 {

    public static void main(String[] args){
        
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        while(!s.equalsIgnoreCase("FIM")){
            if(senha(s)) System.out.println("SIM");
            else System.out.println("NAO");

            s = sc.nextLine();
        }

            sc.close(); 
    }
    
    public static boolean senha(String s){
        boolean senha = true;
        int contaMinusculo = 0;
        int contaMaiusculo = 0;
        int contaNumero = 0;
        int contaEspecial = 0;

        if(s.length() < 8){
            senha = false; 
        }

        for(int i = 0; i <= s.length()-1; i++){
            if(s.charAt(i) >= 97 && s.charAt(i) <= 122){
                contaMinusculo++;
            } 
            if(s.charAt(i) >= 65 && s.charAt(i) <= 90) {
                contaMaiusculo++;
            }
            if(s.charAt(i) >= 48 && s.charAt(i) <= 57 ){
                contaNumero++;
            }
            if((s.charAt(i) >= 33 && s.charAt(i) <= 47) || (s.charAt(i) >= 58 && s.charAt(i) <= 64) || (s.charAt(i) >= 91 && s.charAt(i) <= 96) || (s.charAt(i) >= 123 && s.charAt(i) <= 126)){
                contaEspecial++;
            }
        }
        
        if(contaMinusculo == 0 || contaMaiusculo == 0 || contaNumero == 0 || contaEspecial == 0){
            senha = false;
        }
        
        return senha;
    }

}
