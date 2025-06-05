#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>

#define MAX_SHOWS 10000
#define MAX_FIELD 512
#define MAX_LIST 50

typedef struct {
    char mes[20];
    int dia;
    int ano;
} Data;

typedef struct {
    char show_id[MAX_FIELD];
    char type[MAX_FIELD];
    char title[MAX_FIELD];
    char director[MAX_LIST][MAX_FIELD];
    int director_count;
    char cast[MAX_LIST][MAX_FIELD];
    int cast_count;
    char country[MAX_LIST][MAX_FIELD];
    int country_count;
    Data date_added;
    int release_year;
    char rating[MAX_FIELD];
    char duration[MAX_FIELD];
    char listen_in[MAX_LIST][MAX_FIELD];
    int listen_count;
} Show;

Show shows[MAX_SHOWS];
int show_count = 0;
int totalComparacoes = 0;
int totalMovimentacoes = 0;

void removeN_R(char* texto) {
    int tamanho = strlen(texto);
    if (tamanho > 0 && (texto[tamanho - 1] == '\n' || texto[tamanho - 1] == '\r'))
        texto[tamanho - 1] = '\0';
}

void split_list(char* entrada, char lista[MAX_LIST][MAX_FIELD], int* total_itens) {
    *total_itens = 0;
    char* parte = strtok(entrada, ",");
    while (parte != NULL && *total_itens < MAX_LIST) {
        while (*parte == ' ') parte++;
        strcpy(lista[*total_itens], parte);
        (*total_itens)++;
        parte = strtok(NULL, ",");
    }
}

void sort_list(char lista[MAX_LIST][MAX_FIELD], int quantidade) {
    for (int i = 0; i < quantidade - 1; i++) {
        int menor = i;
        for (int j = i + 1; j < quantidade; j++) {
            if (strcmp(lista[j], lista[menor]) < 0) {
                menor = j;
            }
        }
        if (menor != i) {
            char temp[MAX_FIELD];
            strcpy(temp, lista[i]);
            strcpy(lista[i], lista[menor]);
            strcpy(lista[menor], temp);
        }
    }
}

void print_show(const Show* registro) {
    printf("=> %s ## %s ## %s ## ", registro->show_id, registro->title, registro->type);

    for (int i = 0; i < registro->director_count; i++) {
        printf("%s", registro->director[i]);
        if (i < registro->director_count - 1) printf(", ");
    }

    printf(" ## [");
    for (int i = 0; i < registro->cast_count; i++) {
        printf("%s", registro->cast[i]);
        if (i < registro->cast_count - 1) printf(", ");
    }
    printf("] ## ");

    for (int i = 0; i < registro->country_count; i++) {
        printf("%s", registro->country[i]);
        if (i < registro->country_count - 1) printf(", ");
    }

    printf(" ## %s %d, %d ## %d ## %s ## %s ## [", 
        registro->date_added.mes,
        registro->date_added.dia,
        registro->date_added.ano,
        registro->release_year != -1 ? registro->release_year : 0,
        registro->rating,
        registro->duration);

    for (int i = 0; i < registro->listen_count; i++) {
        printf("%s", registro->listen_in[i]);
        if (i < registro->listen_count - 1) printf(", ");
    }
    printf("] ##\n");
}

void read_csv(const char* caminho) {
    FILE* arquivo = fopen(caminho, "r");
    if (!arquivo) {
        perror("Erro ao abrir o arquivo");
        exit(1);
    }

    char linha[4000];
    fgets(linha, sizeof(linha), arquivo);

    while (fgets(linha, sizeof(linha), arquivo) && show_count < MAX_SHOWS) {
        char* campos[13] = { NULL };
        int indice_campo = 0;
        int entre_aspas = 0;
        char* inicio_token = linha;

        for (char* ptr = linha; *ptr && indice_campo < 13; ptr++) {
            if (*ptr == '"') {
                entre_aspas = !entre_aspas;
                memmove(ptr, ptr + 1, strlen(ptr));
                ptr--;
            } else if (*ptr == ',' && !entre_aspas) {
                *ptr = '\0';
                campos[indice_campo++] = inicio_token;
                inicio_token = ptr + 1;
            }
        }
        if (indice_campo < 13) campos[indice_campo++] = inicio_token;
        while (indice_campo < 13) campos[indice_campo++] = "";

        Show* registro = &shows[show_count];
        removeN_R(campos[11]);

        strcpy(registro->show_id, campos[0]);
        strcpy(registro->type, campos[1]);
        strcpy(registro->title, campos[2]);

        split_list(campos[3], registro->director, &registro->director_count);
        sort_list(registro->director, registro->director_count);

        split_list(campos[4], registro->cast, &registro->cast_count);
        sort_list(registro->cast, registro->cast_count);

        split_list(campos[5], registro->country, &registro->country_count);
        sort_list(registro->country, registro->country_count);

        if (strlen(campos[6]) > 0) {
            char* data = strdup(campos[6]);
            char* token = strtok(data, " ");
            strcpy(registro->date_added.mes, token ? token : "March");
            token = strtok(NULL, ",");
            registro->date_added.dia = token ? atoi(token) : 1;
            token = strtok(NULL, "");
            registro->date_added.ano = token ? atoi(token) : 1900;
            free(data);
        } else {
            strcpy(registro->date_added.mes, "March");
            registro->date_added.dia = 1;
            registro->date_added.ano = 1900;
        }

        registro->release_year = strlen(campos[7]) > 0 ? atoi(campos[7]) : -1;
        strcpy(registro->rating, campos[8]);
        strcpy(registro->duration, campos[9]);

        split_list(campos[10], registro->listen_in, &registro->listen_count);
        sort_list(registro->listen_in, registro->listen_count);

        show_count++;
    }
    fclose(arquivo);
}

int mesParaNumero(const char* mes) {
    const char* meses[] = {"January", "February", "March", "April", "May", "June",
                           "July", "August", "September", "October", "November", "December"};
    for (int i = 0; i < 12; i++) {
        if (strcasecmp(mes, meses[i]) == 0) return i;
    }
    return -1;
}

int compareShows(const Show* a, const Show* b) {
    if (a->date_added.ano != b->date_added.ano)
        return a->date_added.ano - b->date_added.ano;

    int mesA = mesParaNumero(a->date_added.mes);
    int mesB = mesParaNumero(b->date_added.mes);
    if (mesA != mesB)
        return mesA - mesB;

    if (a->date_added.dia != b->date_added.dia)
        return a->date_added.dia - b->date_added.dia;

    return strcasecmp(a->title, b->title);
}

void quicksort(Show* array[], int esq, int dir) {
    if (esq < dir) {
        Show* pivo = array[dir];
        int i = esq - 1;
        for (int j = esq; j < dir; j++) {
            totalComparacoes++;
            if (compareShows(array[j], pivo) < 0) {
                i++;
                Show* temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                totalMovimentacoes++;
            }
        }
        Show* temp = array[i + 1];
        array[i + 1] = array[dir];
        array[dir] = temp;
        totalMovimentacoes++;
        int pi = i + 1;
        quicksort(array, esq, pi - 1);
        quicksort(array, pi + 1, dir);
    }
}

int main() {
    read_csv("/tmp/disneyplus.csv");
    char entrada[100];
    Show* resultados[MAX_SHOWS];
    int tamanhoResultados = 0;

    while (1) {
        fgets(entrada, sizeof(entrada), stdin);
        removeN_R(entrada);
        if (strcmp(entrada, "FIM") == 0) break;

        for (int i = 0; i < show_count; i++) {
            if (strcmp(shows[i].show_id, entrada) == 0) {
                resultados[tamanhoResultados++] = &shows[i];
                break;
            }
        }
    }

    clock_t inicio = clock();
    quicksort(resultados, 0, tamanhoResultados - 1);
    clock_t fim = clock();

    for (int i = 0; i < tamanhoResultados; i++) {
        print_show(resultados[i]);
    }

    double tempoExecucao = ((double)(fim - inicio)) / CLOCKS_PER_SEC;
    FILE* log = fopen("matricula_quicksort.txt", "w");
    if (log) {
        fprintf(log, "843309\t%d\t%d\t%lf\n", totalComparacoes, totalMovimentacoes, tempoExecucao);
        fclose(log);
    }
    return 0;
}