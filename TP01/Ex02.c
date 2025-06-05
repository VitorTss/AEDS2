#include <stdio.h> 
#include <stdlib.h> 
#include <stdbool.h>
#include <string.h>

bool palindromo(char nome [500]){
 bool flag = true; 
    
    int i = 0;
    int j = strlen(nome)-1;

    while (i < j && flag)
    {
      if(nome[i] != nome[j]){
        flag = false;
      }
        i++;
        j--;
    }
    return flag;
}

int main(){
    char nome[500];

   // fgets (nome, 500, stdin);
   scanf(" %[^\n]",nome);

    while(strcmp(nome, "FIM" ) != 0){
        if(palindromo(nome)) printf("SIM\n");
            else printf("NAO\n");

    //fgets (nome, 500, stdin);
    scanf(" %[^\n]",nome);
    }
        return 0;
}