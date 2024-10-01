#include <stdio.h>
int main(){

    int A[] = {2,4,5,8,1};
    int i;
    for (i = 0; i < 5; i++){
       
        printf("address %d = %d\n", i, &A[i]);      //Prints address of element index i
        printf("address %d = %d\n", i, A+i);        //Prints address of element index i
        printf("value %d   = %d\n", i, A[i]);       //Prints value of element index i
        printf("value %d   = %d\n\n", i, *(A+1));       //Prints value of element index i
    }
        return 0;
}