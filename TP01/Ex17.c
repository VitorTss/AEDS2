#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

bool palindromo(char nome[], int i, int j) {
    if (i >= j) return true;
    if (nome[i] != nome[j]) return false;
    return palindromo(nome, i + 1, j - 1);
}

int main() {
    char nome[500];

    fgets(nome, sizeof(nome), stdin);
    nome[strcspn(nome, "\n")] = '\0';  // Remove a quebra de linha

    while (strcmp(nome, "FIM") != 0) {
        if (palindromo(nome, 0, strlen(nome) - 1))
            printf("SIM\n");
        else
            printf("NAO\n");

        fgets(nome, sizeof(nome), stdin);
        nome[strcspn(nome, "\n")] = '\0';  // Remove a quebra de linha
    }

    return 0;
}
