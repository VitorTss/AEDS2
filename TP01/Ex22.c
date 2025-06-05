#include <stdio.h>

int somaDigitos(int num) {
    if (num == 0) return 0;
    return (num % 10) + somaDigitos(num / 10);
}

int main() {
    int num;
    
    while (scanf("%d", &num) == 1) {
        printf("%d\n", somaDigitos(num));
    }
    
    return 0;
}
