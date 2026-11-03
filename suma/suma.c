#include <stdio.h>

int suma(int  num1, int num2) {
    return num1 + num2;
}

void main() {

    int result = 0;
    int a,b;

    scanf("%d", &a);
    scanf("%d",&b);

    result = suma(a,b);

    printf("%d",result);

}