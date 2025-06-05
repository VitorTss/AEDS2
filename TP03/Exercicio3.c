#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_SHOWS 10000
#define MAX_FIELD 512
#define MAX_LIST 50
#define MAXTAM 100

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
    char date[MAX_FIELD];
    int release_year;
    char rating[MAX_FIELD];
    char duration[MAX_FIELD];
    char listen_in[MAX_LIST][MAX_FIELD];
    int listen_count;
} Show;

Show shows[MAX_SHOWS];
int show_count = 0;

Show array[MAXTAM];
int n;

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

void print_show(const Show* s) {
    printf("=> %s ## %s ## %s ## ", s->show_id, s->title, s->type);

    for (int i = 0; i < s->director_count; i++) {
        printf("%s", s->director[i]);
        if (i < s->director_count - 1) printf(", ");
    }

    printf(" ## [");
    for (int i = 0; i < s->cast_count; i++) {
        printf("%s", s->cast[i]);
        if (i < s->cast_count - 1) printf(", ");
    }
    printf("] ## ");

    for (int i = 0; i < s->country_count; i++) {
        printf("%s", s->country[i]);
        if (i < s->country_count - 1) printf(", ");
    }

    printf(" ## %s ## %d ## %s ## %s ## [",
        strlen(s->date) > 0 ? s->date : "March 1, 1900",
        s->release_year != -1 ? s->release_year : 0,
        s->rating,
        s->duration);

    for (int i = 0; i < s->listen_count; i++) {
        printf("%s", s->listen_in[i]);
        if (i < s->listen_count - 1) printf(", ");
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
        int campo = 0, aspas = 0;
        char* ini = linha;

        for (char* p = linha; *p && campo < 13; p++) {
            if (*p == '"') {
                aspas = !aspas;
                memmove(p, p + 1, strlen(p));
                p--;
            } else if (*p == ',' && !aspas) {
                *p = '\0';
                campos[campo++] = ini;
                ini = p + 1;
            }
        }

        if (campo < 13) campos[campo++] = ini;
        while (campo < 13) campos[campo++] = "";

        Show* s = &shows[show_count];
        removeN_R(campos[11]);

        strcpy(s->show_id, strlen(campos[0]) ? campos[0] : "NaN");
        strcpy(s->type, strlen(campos[1]) ? campos[1] : "NaN");
        strcpy(s->title, strlen(campos[2]) ? campos[2] : "NaN");

        split_list(strlen(campos[3]) ? campos[3] : "NaN", s->director, &s->director_count);
        split_list(strlen(campos[4]) ? campos[4] : "NaN", s->cast, &s->cast_count);
        sort_list(s->cast, s->cast_count);

        split_list(strlen(campos[5]) ? campos[5] : "NaN", s->country, &s->country_count);

        strcpy(s->date, strlen(campos[6]) ? campos[6] : "March 1, 1900");
        s->release_year = strlen(campos[7]) ? atoi(campos[7]) : -1;
        strcpy(s->rating, strlen(campos[8]) ? campos[8] : "NaN");
        strcpy(s->duration, strlen(campos[9]) ? campos[9] : "NaN");

        split_list(strlen(campos[10]) ? campos[10] : "NaN", s->listen_in, &s->listen_count);
        sort_list(s->listen_in, s->listen_count);

        show_count++;
    }

    fclose(arquivo);
}

Show* buscar(const char* id) {
    for (int i = 0; i < show_count; i++) {
        if (strcmp(shows[i].show_id, id) == 0) {
            return &shows[i];
        }
    }
    return NULL;
}

void start() {
    n = 0;
}

void inserirFim(Show s) {
    if (n >= MAXTAM) {
        printf("Erro ao inserir!\n");
        exit(1);
    }
    array[n++] = s;
}

void inserirInicio(Show s) {
    if (n >= MAXTAM) {
        printf("Erro ao inserir!\n");
        exit(1);
    }
    for (int i = n; i > 0; i--)
        array[i] = array[i - 1];
    array[0] = s;
    n++;
}

void inserir(Show s, int pos) {
    if (n >= MAXTAM || pos < 0 || pos > n) {
        printf("Erro ao inserir!\n");
        exit(1);
    }
    for (int i = n; i > pos; i--)
        array[i] = array[i - 1];
    array[pos] = s;
    n++;
}

Show* removerInicio() {
    if (n == 0) exit(1);
    Show* removido = &array[0];
    for (int i = 0; i < n - 1; i++)
        array[i] = array[i + 1];
    n--;
    return removido;
}

Show* removerFim() {
    if (n == 0) exit(1);
    return &array[--n];
}

Show* remover(int pos) {
    if (n == 0 || pos < 0 || pos >= n) exit(1);
    Show* removido = &array[pos];
    for (int i = pos; i < n - 1; i++)
        array[i] = array[i + 1];
    n--;
    return removido;
}

void mostrar() {
    for (int i = 0; i < n; i++)
        print_show(&array[i]);
}

int main() {
    read_csv("/tmp/disneyplus.csv");
    start();

    char entrada[100];
    while (1) {
        scanf(" %[^\n]", entrada);
        removeN_R(entrada);
        if (strcmp(entrada, "FIM") == 0) break;
        Show* s = buscar(entrada);
        if (s) inserirFim(*s);
    }

    int qtd;
    scanf("%d", &qtd);
    for (int i = 0; i < qtd; i++) {
        char op[3], id[100];
        int pos;
        scanf(" %s", op);
        if (strcmp(op, "II") == 0) {
            scanf(" %[^\n]", id); removeN_R(id);
            Show* s = buscar(id); if (s) inserirInicio(*s);
        } else if (strcmp(op, "IF") == 0) {
            scanf(" %[^\n]", id); removeN_R(id);
            Show* s = buscar(id); if (s) inserirFim(*s);
        } else if (strcmp(op, "I*") == 0) {
            scanf("%d %[^\n]", &pos, id); removeN_R(id);
            Show* s = buscar(id); if (s) inserir(*s, pos);
        } else if (strcmp(op, "RI") == 0) {
            Show* s = removerInicio(); printf("(R) %s\n", s->title);
        } else if (strcmp(op, "RF") == 0) {
            Show* s = removerFim(); printf("(R) %s\n", s->title);
        } else if (strcmp(op, "R*") == 0) {
            scanf("%d", &pos);
            Show* s = remover(pos); printf("(R) %s\n", s->title);
        }
    }

    mostrar();
    return 0;
}
