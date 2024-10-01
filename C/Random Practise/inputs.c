#include <stdio.h>
#include <stdlib.h>

int main(){
    
    //Simple method - random size allocation
    char x[12] = " ";
    char y[12] = " ";
    
    scanf("%s", x);
    scanf("%s", y);
    printf("Hello %s %s\n", x, y);

    //Improved method - pointer allocation
    char *x2;
    char *y2;
    printf("Enter your first name: ");
    scanf("%s", &x2);
    printf("Enter your last name: ");
    scanf("%s", &y2);



}