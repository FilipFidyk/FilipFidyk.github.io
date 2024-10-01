#include <stdio.h>
#include <string.h>

int sumOfElements(int* A, int size){ //"int* A" is the same as "int A[]"", no pass by value for arrays
    int i, sum = 0;
    
       for (i = 0; i < size;i++){
        sum += A[i];
    }
    return sum;
}

int doubleNums(int* A, int size){
    int i;
    for (i = 0; i < size; i++){
        A[i] = 2*A[i];
    }
}

void print(char* C){
    while (*C != '\0'){
        printf("%c", *C);
        C++;
    }
    printf("\n");
}

//passing multidimensional arrays requires a "pointer to X" where X is what the array
//consists of.
//Example one: 3x2 int array, the argument passed is a pointer for an int array of size 2
//Example two 2x2x3 int array. the argument is a pointer of an int array of form 2x3
void Func1(int (*X)[2]){
    printf("hi");
}

void Func2(int (*Y)[2][3]){
    printf("Hi");
}

int main(){
    int A[] = {1,2,3,4,5};
    int size = sizeof(A)/sizeof(A[0]);
    int total = sumOfElements(A, size);
    doubleNums(A, size);
    printf("Sum = %d\n", total);
    for (int i = 0; i < 5; i++){
        printf("%d ", A[i]); 
    }

    //Array allocation method
    char C[20] = "Hello"; //String gets stored in the allocated space
    print(C);

    //ASSUMPTION - the pointer creates an empty pointer simply to find an empty address and
    //uses it to store data
    char* C2 = "Hello"; //String gets stored as a constant in compile time
    print(C2);

    //multidimension arrays
    int X[3][2] = {{1,2}, {3,4}, {5,6}};
    int Y[2][2][3] = {{{1,2,3}, {4,5,6}}, 
                      {{7,8,9}, {10,11,12}}};

    Func1(X);
    Func2(Y);

    return 0;
}