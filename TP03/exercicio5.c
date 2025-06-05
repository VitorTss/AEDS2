#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_SHOWS 10000
#define MAX_FIELD 512
#define MAX_LIST 50

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

    printf(" ## %s ## %d ## %s ## %s ## [", 
        registro->date[0] ? registro->date : "March 1, 1900",
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

        if (indice_campo < 13) {
            campos[indice_campo++] = inicio_token;
        }

        while (indice_campo < 13) {
            campos[indice_campo++] = "";
        }

        Show* registro = &shows[show_count];
        removeN_R(campos[11]);

        strcpy(registro->show_id, strlen(campos[0]) > 0 ? campos[0] : "NaN");
        strcpy(registro->type, strlen(campos[1]) > 0 ? campos[1] : "NaN");
        strcpy(registro->title, strlen(campos[2]) > 0 ? campos[2] : "NaN");

        split_list(strlen(campos[3]) > 0 ? campos[3] : "NaN", registro->director, &registro->director_count);
        //sort_list(registro->director, registro->director_count); a saida o verde não esta em ordem alfabetica

        split_list(strlen(campos[4]) > 0 ? campos[4] : "NaN", registro->cast, &registro->cast_count);
        sort_list(registro->cast, registro->cast_count);

        split_list(strlen(campos[5]) > 0 ? campos[5] : "NaN", registro->country, &registro->country_count);
       // sort_list(registro->country, registro->country_count); a saida o verde não esta em ordem alfabetica

        strcpy(registro->date, strlen(campos[6]) > 0 ? campos[6] : "March 1, 1900");
        registro->release_year = strlen(campos[7]) > 0 ? atoi(campos[7]) : -1;

        strcpy(registro->rating, strlen(campos[8]) > 0 ? campos[8] : "NaN");
        strcpy(registro->duration, strlen(campos[9]) > 0 ? campos[9] : "NaN");

        split_list(strlen(campos[10]) > 0 ? campos[10] : "NaN", registro->listen_in, &registro->listen_count);
        sort_list(registro->listen_in, registro->listen_count);

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
/**
 * Lista simples dinamica
 * @author Max do Val Machado
 * @version 2 01/2015
 */
#include <stdio.h>
#include <stdlib.h>
//#include <err.h>
#define bool   short
#define true   1
#define false  0

//TIPO CELULA ===================================================================
typedef struct Celula {
	Show elemento;        // Elemento inserido na celula.
	struct Celula* prox; // Aponta a celula prox.
} Celula;

Celula* novaCelula(Show elemento) {
   Celula* nova = (Celula*) malloc(sizeof(Celula));
   nova->elemento = elemento;
   nova->prox = NULL;
   return nova;
}

//LISTA PROPRIAMENTE DITA =======================================================
Celula* primeiro;
Celula* ultimo;


/**
 * Cria uma lista sem elementos (somente no cabeca).
 */
void start () {
   Show vazio; // pode ser qualquer valor, não será usado
   primeiro = novaCelula(vazio); // célula "sentinela"
   ultimo = primeiro;
}


/**
 * Insere um elemento na primeira posicao da lista.
 * @param x int elemento a ser inserido.
 */
void inserirInicio(Show x) {
   Celula* tmp = novaCelula(x);
   tmp->prox = primeiro->prox;
   primeiro->prox = tmp;
   if (primeiro == ultimo) {                    
      ultimo = tmp;
   }
   tmp = NULL;
}

/**
 * Insere um elemento na ultima posicao da lista.
 * @param x int elemento a ser inserido.
 */
void inserirFim(Show x) {
   ultimo->prox = novaCelula(x);
   ultimo = ultimo->prox;
}


/**
 * Remove um elemento da primeira posicao da lista.
 * @return resp int elemento a ser removido.
 * @throws Exception Se a lista nao contiver elementos.
 */
Show removerInicio() {
   if (primeiro == ultimo) {
      errx(1, "Erro ao remover!");
   }

   Celula* tmp = primeiro;
   primeiro = primeiro->prox;
   Show resp = primeiro->elemento;
   tmp->prox = NULL;
   free(tmp);
   tmp = NULL;
   return resp;
}


/**
 * Remove um elemento da ultima posicao da lista.
 * @return resp int elemento a ser removido.
 */
Show removerFim() {
   if (primeiro == ultimo) {
      errx(1, "Erro ao remover!");
   } 

   // Caminhar ate a penultima celula:
   Celula* i;
   for(i = primeiro; i->prox != ultimo; i = i->prox);

   Show resp = ultimo->elemento;
   ultimo = i;
   free(ultimo->prox);
   i = ultimo->prox = NULL;

   return resp;
}


/**
 * Calcula e retorna o tamanho, em numero de elementos, da lista.
 * @return resp int tamanho
 */
int tamanho() {
   int tamanho = 0;
   Celula* i;
   for(i = primeiro; i != ultimo; i = i->prox, tamanho++);
   return tamanho;
}


/**
 * Insere um elemento em uma posicao especifica considerando que o 
 * primeiro elemento valido esta na posicao 0.
 * @param x int elemento a ser inserido.
 * @param pos int posicao da insercao.
 * @throws Exception Se <code>posicao</code> invalida.
 */
void inserir(Show x, int pos) {

   int tam = tamanho();

   if(pos < 0 || pos > tam){
      errx(1, "Erro ao inserir posicao (%d/ tamanho = %d) invalida!", pos, tam);
   } else if (pos == 0){
      inserirInicio(x);
   } else if (pos == tam){
      inserirFim(x);
   } else {
      // Caminhar ate a posicao anterior a insercao
      int j;
      Celula* i = primeiro;
      for(j = 0; j < pos; j++, i = i->prox);

      Celula* tmp = novaCelula(x);
      tmp->prox = i->prox;
      i->prox = tmp;
      tmp = i = NULL;
   }
}


/**
 * Remove um elemento de uma posicao especifica da lista
 * considerando que o primeiro elemento valido esta na posicao 0.
 * @param posicao Meio da remocao.
 * @return resp int elemento a ser removido.
 * @throws Exception Se <code>posicao</code> invalida.
 */
Show remover (int pos) {
   Show resp;
   int tam = tamanho();

   if (primeiro == ultimo){
      errx(1, "Erro ao remover (vazia)!");
   } else if(pos < 0 || pos >= tam){
      errx(1, "Erro ao remover posicao (%d/ tamanho = %d) invalida!", pos, tam);
   } else if (pos == 0){
      resp = removerInicio();
   } else if (pos == tam - 1){
      resp = removerFim();
   } else {
      // Caminhar ate a posicao anterior a insercao
      Celula* i = primeiro;
      int j;
      for(j = 0; j < pos; j++, i = i->prox);

      Celula* tmp = i->prox;
      resp = tmp->elemento;
      i->prox = tmp->prox;
      tmp->prox = NULL;
      free(tmp);
      i = tmp = NULL;
   }
   return resp;
}


/**
 * Mostra os elementos da lista separados por espacos.
 */
void mostrar() {
   Celula* i;
   for (i = primeiro->prox; i != NULL; i = i->prox) {
      print_show(&i->elemento);
   }
}


int main() {
    read_csv("/tmp/disneyplus.csv");

    start();

    char entrada[100];
    while (1) {
        scanf(" %[^\n]", entrada);
        removeN_R(entrada);
        if (strcmp(entrada, "FIM") == 0) break;

        Show* resultado = buscar(entrada);
        if (resultado != NULL) {
           inserirFim(*resultado);
        }
    }
   
    int numero;
    scanf("%d", &numero);
    char operacao[2];
    char id[100]; 
    int posicao;
    for(int i = 0; i < numero; i++){
        scanf("%s", operacao);
        if(operacao[0] == 'I'){
            if(operacao[1] == 'I'){
                scanf(" %[^\n]", id);
                removeN_R(id);
                Show* resultado = buscar(id);
                if (resultado != NULL) {
                inserirInicio(*resultado);
            }
            }

             else if(operacao[1] == 'F'){
                scanf(" %[^\n]", id);
                removeN_R(id);
                Show* resultado = buscar(id);
                if (resultado != NULL) {
                inserirFim(*resultado);
            }
            }

            else if(operacao[1] == '*'){
                scanf("%d", &posicao);
                scanf(" %[^\n]", id);
                removeN_R(id);
                Show* resultado = buscar(id);
                if (resultado != NULL) {
                inserir(*resultado, posicao);
            }
            }
        } 
        
        
        
        else if(operacao[0] == 'R') {
            if(operacao[1] == 'I'){
                
                Show s = removerInicio();
                printf("(R) %s\n", s.title);

            }
            else if(operacao[1] == 'F'){
                
                Show s = removerFim();
                printf("(R) %s\n", s.title);

            }
            else if(operacao[1] == '*'){
                scanf("%d", &posicao);
                Show s = remover(posicao);
                printf("(R) %s\n", s.title);

            }
        }

    }
    mostrar();
    return 0;
}