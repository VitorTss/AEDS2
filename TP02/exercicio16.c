#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>

char* meses[] = {"January", "February", "March", "April", "May", "June", 
    "July", "August", "September", "October", "November", "December"};

#define MAX_LINE 1024
#define MAX_SHOWS 10000
#define MAX_ITEMS 50

typedef struct Data {
    char mes[20];
    int dia;
    int ano;
} Data;

typedef struct Show {
    char* show_id;
    char* type;
    char* title;
    char* director;
    char** cast;       
    int castSize;      
    char* country;
    Data date_added;
    int release_year;
    char* rating;
    char* duration;
    char** listed_in; 
    int listedInSize;
    char* description;
} Show;

Show* catalogo[MAX_SHOWS];
int totalShows = 0;

// Correção aqui ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
int total_comparacoes = 0;
int total_movimentacoes = 0;
// Correção acima ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

char* limparTexto(char* str) {
    if (!str) return strdup("NaN");
    while (isspace(*str)) str++;
    char* end = str + strlen(str) - 1;
    while (end > str && isspace(*end)) end--;
    *(end + 1) = '\0';
    return strdup(str);
}

char* removerAspasTexto(char* str) {
    if (!str) return NULL;
    char* result = malloc(strlen(str) + 1);
    int j = 0;
    for (int i = 0; str[i]; i++) {
        if (str[i] != '"') {
            result[j++] = str[i];
        }
    }
    result[j] = '\0';
    return result;
}

char* removerAspasEspaco(char* field) {
    if (!field || strlen(field) == 0) return strdup("NaN");
    char* noQuotes = removerAspasTexto(field);
    char* result = limparTexto(noQuotes);
    free(noQuotes);
    return result;
}

char** tratarLista(char* str, int* tamanho) {
    char** items = malloc(MAX_ITEMS * sizeof(char*));
    *tamanho = 0;

    if (!str || strlen(str) == 0) {
        items[(*tamanho)++] = strdup("NaN");
        return items;
    }

    char* clear = removerAspasTexto(str);
    char* token = strtok(clear, ",");
    while (token != NULL && *tamanho < MAX_ITEMS) {
        char* item = removerAspasEspaco(token);
        items[(*tamanho)++] = item;
        token = strtok(NULL, ",");
    }

    free(clear);

    for (int i = 0; i < *tamanho - 1; i++) {
        for (int j = 0; j < *tamanho - i - 1; j++) {
            if (strcasecmp(items[j], items[j + 1]) > 0) {
                char* temp = items[j];
                items[j] = items[j + 1];
                items[j + 1] = temp;
            }
        }
    }

    if (*tamanho == 0) {
        items[(*tamanho)++] = strdup("NaN");
    }
    return items;
}

int dividirCampos(char* linha, char* campos[], int maxCampos) {
    int i = 0;
    char* ptr = linha;
    while (*ptr && i < maxCampos) {
        if (*ptr == '"') {
            ptr++;
            char* start = ptr;
            while (*ptr && !(*ptr == '"' && (*(ptr + 1) == ',' || *(ptr + 1) == '\0'))) {
                ptr++;
            }
            int len = ptr - start;
            campos[i] = malloc(len + 1);
            strncpy(campos[i], start, len);
            campos[i][len] = '\0';
            if (*ptr == '"') ptr++;
            if (*ptr == ',') ptr++;
        } else {
            char* start = ptr;
            while (*ptr && *ptr != ',') ptr++;
            int len = ptr - start;
            campos[i] = malloc(len + 1);
            strncpy(campos[i], start, len);
            campos[i][len] = '\0';
            if (*ptr == ',') ptr++;
        }
        i++;
    }
    return i;
}

void readCSV() {
    FILE* file = fopen("/tmp/disneyplus.csv", "r");   
    if (file == NULL) return;

    char linha[MAX_LINE];
    fgets(linha, MAX_LINE, file);

    while (fgets(linha, MAX_LINE, file)) {
        linha[strcspn(linha, "\n")] = 0;

        char* campos[15];
        int fieldCount = dividirCampos(linha, campos, 15);

        Show* novoShow = malloc(sizeof(Show));

        novoShow->show_id = removerAspasEspaco(campos[0]);
        novoShow->type = removerAspasEspaco(campos[1]);
        novoShow->title = removerAspasEspaco(campos[2]);
        novoShow->director = removerAspasEspaco(campos[3]);
        novoShow->cast = tratarLista(campos[4], &novoShow->castSize);
        novoShow->country = removerAspasEspaco(campos[5]);

        char* dateStr = removerAspasEspaco(campos[6]);
        if (strcmp(dateStr, "NaN") != 0) {
            char* parte = strtok(dateStr, " ");
            strcpy(novoShow->date_added.mes, parte ? parte : "NaN");
            parte = strtok(NULL, ",");
            novoShow->date_added.dia = parte ? atoi(parte) : -1;
            parte = strtok(NULL, "");
            novoShow->date_added.ano = parte ? atoi(parte) : -1;
        } else {
            strcpy(novoShow->date_added.mes, "March");
            novoShow->date_added.dia = 1;
            novoShow->date_added.ano = 1900;
        }
        free(dateStr);

        novoShow->release_year = (fieldCount > 7 && strlen(campos[7]) > 0) ? atoi(campos[7]) : -1;
        novoShow->rating = removerAspasEspaco(campos[8]);
        novoShow->duration = removerAspasEspaco(campos[9]);
        novoShow->listed_in = tratarLista(campos[10], &novoShow->listedInSize);
        novoShow->description = removerAspasEspaco(campos[11]);

        for (int i = 0; i < fieldCount; i++) free(campos[i]);
        catalogo[totalShows++] = novoShow;
    }

    fclose(file);
}

void printShow(Show* showAtual) {
    printf("=> %s ## %s ## %s ## %s ## [", showAtual->show_id, showAtual->title, showAtual->type, showAtual->director);
    for (int i = 0; i < showAtual->castSize; i++) {
        printf("%s%s", showAtual->cast[i], (i < showAtual->castSize - 1) ? ", " : "");
    }
    printf("] ## %s ## %s %d, %d ## %d ## %s ## %s ## [", showAtual->country, showAtual->date_added.mes, showAtual->date_added.dia,
           showAtual->date_added.ano, showAtual->release_year, showAtual->rating, showAtual->duration);
    for (int i = 0; i < showAtual->listedInSize; i++) {
        printf("%s%s", showAtual->listed_in[i], (i < showAtual->listedInSize - 1) ? ", " : "");
    }
    printf("] ##\n");
}

bool isFim(char* str) {
    return strcasecmp(str, "FIM") == 0;
}

// Algoritmo de ordenação por inserção
void insertionSort(Show* array[], int n) {
    for (int i = 1; i < n; i++) {
        Show* temp = array[i];
        int j = i - 1;

        // Compara primeiro por type, se empatar pega o title
        while (j >= 0 && (strcasecmp(array[j]->type, temp->type) > 0 || 
                          (strcasecmp(array[j]->type, temp->type) == 0 &&
                           strcasecmp(array[j]->title, temp->title) > 0))) {
            array[j + 1] = array[j];
            j--;
        }
        array[j + 1] = temp;
    }
}


      
int main() {
    readCSV(); 

    char entrada[50];
    fgets(entrada, sizeof(entrada), stdin);
    entrada[strcspn(entrada, "\n")] = 0;

    Show* filmeSalvo[MAX_SHOWS];
    int controle = 0;

    while (!isFim(entrada)) {
        for (int i = 0; i < totalShows; i++) {
            if (strcasecmp(catalogo[i]->show_id, entrada) == 0) {
                filmeSalvo[controle++] = catalogo[i];
                break;
            }
        }
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = 0;
    }

    clock_t inicio = clock();
    // Ordena usando Inserção
    insertionSort(filmeSalvo, controle);
    clock_t fim = clock();

    int limite = 10;

    for (int i = 0; i < limite; i++) {
        printShow(filmeSalvo[i]);
    }

    return 0;
}
