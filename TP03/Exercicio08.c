#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>
#include <time.h>

#define MAX_LINE 1024
#define MAX_SHOWS 10000
#define MAX_ITEMS 50

typedef struct Date {
    char month[20];
    int day;
    int year;
} Date;

typedef struct Show {
    char* id;
    char* category;
    char* name;
    char* director;
    char** cast;
    int castCount;
    char* country;
    Date date_added;
    int release_year;
    char* rating;
    char* duration;
    char** categories;
    int categoryCount;
    char* description;
} Show;

typedef struct Node {
    Show* show;
    struct Node* prev;
    struct Node* next;
} Node;

Show* catalog[MAX_SHOWS];
int showCount = 0;

int comparisons = 0;
int movements = 0;

char* limparEspacos(char* str) {
    if (!str) return strdup("NaN");
    while (isspace(*str)) str++;
    char* end = str + strlen(str) - 1;
    while (end > str && isspace(*end)) end--;
    *(end + 1) = '\0';
    return strdup(str);
}
char* removerAspas(char* str) {
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
char* tratarCampo(char* field) {
    if (!field || strlen(field) == 0) return strdup("NaN");
    char* semAspas = removerAspas(field);
    char* resultado = limparEspacos(semAspas);
    free(semAspas);
    return resultado;
}
char** tratarLista(char* str, int* size) {
    char** items = malloc(MAX_ITEMS * sizeof(char*));
    *size = 0;
    if (!str || strlen(str) == 0) {
        items[(*size)++] = strdup("NaN");
        return items;
    }
    char* limpo = removerAspas(str);
    char* token = strtok(limpo, ",");
    while (token != NULL && *size < MAX_ITEMS) {
        char* item = tratarCampo(token);
        items[(*size)++] = item;
        token = strtok(NULL, ",");
    }
    free(limpo);
    for (int i = 0; i < *size - 1; i++) {
        for (int j = 0; j < *size - i - 1; j++) {
            if (strcasecmp(items[j], items[j + 1]) > 0) {
                char* temp = items[j];
                items[j] = items[j + 1];
                items[j + 1] = temp;
            }
        }
    }
    if (*size == 0) {
        items[(*size)++] = strdup("NaN");
    }
    return items;
}
int dividirCampos(char* line, char* fields[], int maxFields) {
    int i = 0;
    char* ptr = line;
    while (*ptr && i < maxFields) {
        if (*ptr == '"') {
            ptr++;
            char* start = ptr;
            while (*ptr && !(*ptr == '"' && (*(ptr + 1) == ',' || *(ptr + 1) == '\0'))) {
                ptr++;
            }
            int len = ptr - start;
            fields[i] = malloc(len + 1);
            strncpy(fields[i], start, len);
            fields[i][len] = '\0';
            if (*ptr == '"') ptr++;
            if (*ptr == ',') ptr++;
        } else {
            char* start = ptr;
            while (*ptr && *ptr != ',') ptr++;
            int len = ptr - start;
            fields[i] = malloc(len + 1);
            strncpy(fields[i], start, len);
            fields[i][len] = '\0';
            if (*ptr == ',') ptr++;
        }
        i++;
    }
    return i;
}
void carregarCatalogo() {
    FILE* file = fopen("/tmp/disneyplus.csv", "r");
    if (file == NULL) {
        printf("Erro ao abrir o arquivo!\n");
        return;
    }
    char line[MAX_LINE];
    fgets(line, MAX_LINE, file);
    while (fgets(line, MAX_LINE, file)) {
        line[strcspn(line, "\n")] = 0;
        char* campos[15];
        int totalCampos = dividirCampos(line, campos, 15);
        Show* novoShow = malloc(sizeof(Show));
        novoShow->id = tratarCampo(campos[0]);
        novoShow->category = tratarCampo(campos[1]);
        novoShow->name = tratarCampo(campos[2]);
        novoShow->director = tratarCampo(campos[3]);
        novoShow->cast = tratarLista(campos[4], &novoShow->castCount);
        novoShow->country = tratarCampo(campos[5]);
        char* dataStr = tratarCampo(campos[6]);
        if (strcmp(dataStr, "NaN") != 0) {
            char* parte = strtok(dataStr, " ");
            strcpy(novoShow->date_added.month, parte ? parte : "NaN");
            parte = strtok(NULL, ",");
            novoShow->date_added.day = parte ? atoi(parte) : -1;
            parte = strtok(NULL, "");
            novoShow->date_added.year = parte ? atoi(parte) : -1;
        } else {
            strcpy(novoShow->date_added.month, "March");
            novoShow->date_added.day = 1;
            novoShow->date_added.year = 1900;
        }
        free(dataStr);
        novoShow->release_year = (totalCampos > 7 && strlen(campos[7]) > 0) ? atoi(campos[7]) : -1;
        novoShow->rating = tratarCampo(campos[8]);
        novoShow->duration = tratarCampo(campos[9]);
        novoShow->categories = tratarLista(campos[10], &novoShow->categoryCount);
        novoShow->description = tratarCampo(campos[11]);
        for (int i = 0; i < totalCampos; i++) free(campos[i]);
        catalog[showCount++] = novoShow;
    }
    fclose(file);
}
void imprimirShow(Show* s) {
    printf("=> %s ## %s ## %s ## %s ## [", s->id, s->name, s->category, s->director);
    for (int i = 0; i < s->castCount; i++) {
        printf("%s%s", s->cast[i], (i < s->castCount - 1) ? ", " : "");
    }
    printf("] ## %s ## %s %d, %d ## %d ## %s ## %s ## [", s->country, s->date_added.month, s->date_added.day,
           s->date_added.year, s->release_year, s->rating, s->duration);
    for (int i = 0; i < s->categoryCount; i++) {
        printf("%s%s", s->categories[i], (i < s->categoryCount - 1) ? ", " : "");
    }
    printf("] ##\n");
}
bool isEnd(char* str) {
    return strcasecmp(str, "FIM") == 0;
}
int compararShows(Show* a, Show* b) {
    char* ordemMes[] = {"January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"};
    int mesA = -1, mesB = -1;
    for (int i = 0; i < 12; i++) {
        if (strcasecmp(a->date_added.month, ordemMes[i]) == 0) mesA = i;
        if (strcasecmp(b->date_added.month, ordemMes[i]) == 0) mesB = i;
    }
    if (a->date_added.year != b->date_added.year) return a->date_added.year - b->date_added.year;
    if (mesA != mesB) return mesA - mesB;
    if (a->date_added.day != b->date_added.day) return a->date_added.day - b->date_added.day;
    return strcasecmp(a->name, b->name);
}
Node* criarNo(Show* show) {
    Node* node = malloc(sizeof(Node));
    node->show = show;
    node->prev = NULL;
    node->next = NULL;
    return node;
}
void adicionarNo(Node** head, Node** tail, Node* node) {
    if (*head == NULL) {
        *head = *tail = node;
    } else {
        (*tail)->next = node;
        node->prev = *tail;
        *tail = node;
    }
}
void trocarNos(Node* a, Node* b) {
    Show* temp = a->show;
    a->show = b->show;
    b->show = temp;
    movements++;
}
Node* particionarLista(Node* left, Node* right, Node** newLeft, Node** newRight) {
    Show* pivo = right->show;
    Node* i = left->prev;
    for (Node* j = left; j != right; j = j->next) {
        comparisons++;
        if (compararShows(j->show, pivo) < 0) {
            i = (i == NULL) ? left : i->next;
            trocarNos(i, j);
        }
    }
    i = (i == NULL) ? left : i->next;
    trocarNos(i, right);
    *newLeft = left;
    *newRight = right;
    return i;
}
void quicksortLista(Node* left, Node* right) {
    if (!left || left == right || left == right->next) return;
    Node *newLeft, *newRight;
    Node* p = particionarLista(left, right, &newLeft, &newRight);
    if (p && p != left) quicksortLista(left, p->prev);
    if (p && p != right) quicksortLista(p->next, right);
}
void liberarLista(Node* head) {
    Node* curr = head;
    while (curr) {
        Node* temp = curr;
        curr = curr->next;
        free(temp);
    }
}
int main() {
    carregarCatalogo();
    char entrada[50];
    fgets(entrada, sizeof(entrada), stdin);
    entrada[strcspn(entrada, "\n")] = 0;
    Node* head = NULL;
    Node* tail = NULL;
    while (!isEnd(entrada)) {
        for (int i = 0; i < showCount; i++) {
            if (strcasecmp(catalog[i]->id, entrada) == 0) {
                Node* node = criarNo(catalog[i]);
                adicionarNo(&head, &tail, node);
                break;
            }
        }
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = 0;
    }
    clock_t inicio = clock();
    if (head && tail) quicksortLista(head, tail);
    clock_t fim = clock();
    Node* atual = head;
    while (atual) {
        imprimirShow(atual->show);
        atual = atual->next;
    }
    double tempoExec = ((double)(fim - inicio)) / CLOCKS_PER_SEC;
    FILE* log = fopen("843309_quicksort2.txt", "w");
    if (log) {
        fprintf(log, "843309\t%d\t%d\t%lf\n", comparisons, movements, tempoExec);
        fclose(log);
    }
    liberarLista(head);
    return 0;
}
