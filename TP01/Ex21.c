#include <stdio.h>
#include <string.h>

void inverte(char *s, int esquerda, int direita) {
    if (esquerda >= direita) return;
    
    char tmp = s[esquerda];
    s[esquerda] = s[direita];
    s[direita] = tmp;
    
    inverte(s, esquerda + 1, direita - 1);
}

int main() {
    char s[500];
    
    fgets(s, sizeof(s), stdin);
    s[strcspn(s, "\n")] = '\0'; // Remover quebra de linha
    
    while (strcmp(s, "FIM") != 0) {
        inverte(s, 0, strlen(s) - 1);
        printf("%s\n", s);
        
        fgets(s, sizeof(s), stdin);
        s[strcspn(s, "\n")] = '\0';
    }
    
    return 0;
}