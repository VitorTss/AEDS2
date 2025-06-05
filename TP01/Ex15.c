#include <stdio.h>

int main() {
    int quantidade;
    double valor;
    FILE *arquivoBinario;

    scanf("%d", &quantidade);

    arquivoBinario = fopen("dados.bin", "wb");
    if (arquivoBinario == NULL) {
        printf("Erro ao criar o arquivo!\n");
        return 1;
    }

    for (int i = 0; i < quantidade; i++) {
        scanf("%lf", &valor);
        fwrite(&valor, sizeof(double), 1, arquivoBinario);
    }

    fclose(arquivoBinario);

    arquivoBinario = fopen("dados.bin", "rb");
    if (arquivoBinario == NULL) {
        printf("Erro ao abrir o arquivo!\n");
        return 1;
    }

    for (int i = quantidade - 1; i >= 0; i--) {
        fseek(arquivoBinario, i * sizeof(double), SEEK_SET);
        fread(&valor, sizeof(double), 1, arquivoBinario);

        if (valor == (long long)valor) {
            printf("%lld\n", (long long)valor);
        } else {
            printf("%.3lf\n", valor);
        }
    }

    fclose(arquivoBinario);

    return 0;
}
