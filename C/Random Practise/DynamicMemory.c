#include <stdio.h>
#include <stdlib.h> 
#include <string.h>

int main(){
    int n;
    printf("Size of array: ");
    scanf("%d", &n);
    
    //malloc and calloc do the same and return a void pointer that has to be cast
    int* A = (int*)malloc(n*sizeof(int));
    int* B = (int*)calloc(n, sizeof(int));
    for (int i = 0; i < n; i++){
        A[i] = i+1;
        printf("%d ", A[i]);
    }
    free(A);
    free(B);
}