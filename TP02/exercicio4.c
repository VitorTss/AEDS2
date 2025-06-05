#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>

#define MAX_LINHA 1024
#define MAX_PROGRAMAS 10000
#define MAX_ITENS 50

// Struct Data
typedef struct Data {
    char mes[20];
    int dia;
    int ano;
} Data;

// Struct Programa
typedef struct Show {
    char* id_programa;
    char* tipo;
    char* titulo;
    char* diretor;
    char** elenco;       
    int tamanhoElenco;      
    char* pais;
    Data data_adicionado;   
    int ano_lancamento;
    char* classificacao;
    char* duracao;
    char** categorias; 
    int tamanhoCategorias;
    char* descricao;
} Show;

Show* catalogo[MAX_PROGRAMAS];
int totalProgramas = 0;
int comparacoes = 0; // Contador para log

char* limparString(char* str);
char* removerAspas(char* str);
char* processarCampo(char* campo);
char** processarLista(char* str, int* tamanho);
int dividirLinha(char* linha, char* campos[], int maxCampos);
void lerCSV();
bool pesquisaBinaria(Show* array[], int n, char* chave);
int compararTitulos(const void* a, const void* b);
bool isFim(char* str);

char* limparString(char* str) {
    if (!str) return strdup("NaN");
    while (isspace(*str)) str++;
    char* fim = str + strlen(str) - 1;
    while (fim > str && isspace(*fim)) fim--;
    *(fim + 1) = '\0';
    return strdup(str);
}

char* removerAspas(char* str) {
    if (!str) return NULL;
    char* resultado = malloc(strlen(str) + 1);
    int j = 0;
    for (int i = 0; str[i]; i++) {
        if (str[i] != '"') {
            resultado[j++] = str[i];
        }
    }
    resultado[j] = '\0';
    return resultado;
}

char* processarCampo(char* campo) {
    if (!campo || strlen(campo) == 0) return strdup("NaN");
    char* semAspas = removerAspas(campo);
    char* resultado = limparString(semAspas);
    free(semAspas);
    return resultado;
}

char** processarLista(char* str, int* tamanho) {
    char** itens = malloc(MAX_ITENS * sizeof(char*));
    *tamanho = 0;
    if (!str || strlen(str) == 0) {
        itens[(*tamanho)++] = strdup("NaN");
        return itens;
    }
    char* limpo = removerAspas(str);
    char* token = strtok(limpo, ",");
    while (token != NULL && *tamanho < MAX_ITENS) {
        itens[(*tamanho)++] = processarCampo(token);
        token = strtok(NULL, ",");
    }
    free(limpo);
    return itens;
}

int dividirLinha(char* linha, char* campos[], int maxCampos) {
    int i = 0;
    char* ptr = linha;
    while (*ptr && i < maxCampos) {
        if (*ptr == '"') {
            ptr++;
            char* inicio = ptr;
            while (*ptr && !(*ptr == '"' && (*(ptr + 1) == ',' || *(ptr + 1) == '\0'))) ptr++;
            int len = ptr - inicio;
            campos[i] = malloc(len + 1);
            strncpy(campos[i], inicio, len);
            campos[i][len] = '\0';
            if (*ptr == '"') ptr++;
            if (*ptr == ',') ptr++;
        } else {
            char* inicio = ptr;
            while (*ptr && *ptr != ',') ptr++;
            int len = ptr - inicio;
            campos[i] = malloc(len + 1);
            strncpy(campos[i], inicio, len);
            campos[i][len] = '\0';
            if (*ptr == ',') ptr++;
        }
        i++;
    }
    return i;
}

void lerCSV() {
    FILE* arquivo = fopen("/tmp/disneyplus.csv", "r");
    if (!arquivo) return;
    char linha[MAX_LINHA];
    fgets(linha, MAX_LINHA, arquivo);
    while (fgets(linha, MAX_LINHA, arquivo)) {
        linha[strcspn(linha, "\n")] = 0;
        char* campos[15];
        int qtd = dividirLinha(linha, campos, 15);
        Show* novo = malloc(sizeof(Show));
        novo->id_programa = processarCampo(campos[0]);
        novo->tipo = processarCampo(campos[1]);
        novo->titulo = processarCampo(campos[2]);
        novo->diretor = processarCampo(campos[3]);
        novo->elenco = processarLista(campos[4], &novo->tamanhoElenco);
        novo->pais = processarCampo(campos[5]);
        char* dataStr = processarCampo(campos[6]);
        if (strcmp(dataStr, "NaN") != 0) {
            char* parte = strtok(dataStr, " ");
            strcpy(novo->data_adicionado.mes, parte ? parte : "NaN");
            parte = strtok(NULL, ",");
            novo->data_adicionado.dia = parte ? atoi(parte) : -1;
            parte = strtok(NULL, "");
            novo->data_adicionado.ano = parte ? atoi(parte) : -1;
        } else {
            strcpy(novo->data_adicionado.mes, "March");
            novo->data_adicionado.dia = 1;
            novo->data_adicionado.ano = 1900;
        }
        free(dataStr);
        novo->ano_lancamento = (qtd > 7 && strlen(campos[7]) > 0) ? atoi(campos[7]) : -1;
        novo->classificacao = processarCampo(campos[8]);
        novo->duracao = processarCampo(campos[9]);
        novo->categorias = processarLista(campos[10], &novo->tamanhoCategorias);
        novo->descricao = processarCampo(campos[11]);
        for (int i = 0; i < qtd; i++) free(campos[i]);
        catalogo[totalProgramas++] = novo;
    }
    fclose(arquivo);
}

int compararTitulos(const void* a, const void* b) {
    Show* s1 = *(Show**)a;
    Show* s2 = *(Show**)b;
    return strcasecmp(s1->titulo, s2->titulo);
}

bool pesquisaBinaria(Show* array[], int n, char* chave) {
    int esquerda = 0, direita = n - 1;
    while (esquerda <= direita) {
        int meio = (esquerda + direita) / 2;
        int cmp = strcasecmp(array[meio]->titulo, chave);
        comparacoes++;
        if (cmp == 0) return true;
        else if (cmp < 0) esquerda = meio + 1;
        else direita = meio - 1;
    }
    return false;
}

bool isFim(char* str) {
    return strcasecmp(str, "FIM") == 0;
}

int main() {
    lerCSV();
    char entrada[50];
    Show* resultados[MAX_PROGRAMAS];
    int tamResultados = 0;

    fgets(entrada, sizeof(entrada), stdin);
    entrada[strcspn(entrada, "\n")] = 0;
    while (!isFim(entrada)) {
        for (int i = 0; i < totalProgramas; i++) {
            if (strcasecmp(catalogo[i]->id_programa, entrada) == 0) {
                resultados[tamResultados++] = catalogo[i];
                break;
            }
        }
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = 0;
    }

    qsort(resultados, tamResultados, sizeof(Show*), compararTitulos);

    clock_t inicio = clock();

    char busca[50];
    fgets(busca, sizeof(busca), stdin);
    busca[strcspn(busca, "\n")] = 0;
    while (!isFim(busca)) {
        if (pesquisaBinaria(resultados, tamResultados, busca)) printf("SIM\n");
        else printf("NAO\n");
        fgets(busca, sizeof(busca), stdin);
        busca[strcspn(busca, "\n")] = 0;
    }

    clock_t fim = clock();
    double tempo = ((double)(fim - inicio)) / CLOCKS_PER_SEC;

    FILE* log = fopen("matricula_pesquisa_binaria.txt", "w");
    if (log) {
        fprintf(log, "843309\t%d\t%lf\n", comparacoes, tempo);
        fclose(log);
    }

    return 0;
}