#include <stdio.h>
#include <stdlib.h>

/*
list struct with all information, the size and a pointer to the array in the heap
Done so for easier swapping between lists when resizing
*/
//In order to not have to use the struct keyword everywhere declare the structure in a typedef
typedef struct listStruct {
    int *list;
    int size;
} intList;

//An initialising function
void init(intList **list){
    (*list)->list = (int*)calloc(1,sizeof(int));
    (*list)->size = 0;
}

void append(intList **list, int var){
    //Temporary array declared in the stack 
    int tempArray[(*list)->size + 1];

    //Copy array elements into temporary array and add the new one
    int i;
    for(i = 0; i < (*list)->size; i++){
        tempArray[i] = (*list)->list[i];
    }
    tempArray[(*list)->size] = var;

    //increment the size, clear out the previous array in the heap and redeclare it
    (*list)->size++;
    free((*list)->list); 
    (*list)->list = calloc((*list)->size, sizeof(int));

    //Copy temporary array back into heap array
    for(i = 0; i < (*list)->size; i++){
        (*list)->list[i] = tempArray[i];
    }
}

//if you try to add an element to an index that doesn't exist, nothing will happen
void add(intList **list, int var, int loc){
    if (loc >= 0 && loc <= (*list)->size){
        append(list, var);
        int i, temp;
        for (i = (*list)->size-1; i > loc; i--){
            temp = (*list)->list[i];
            (*list)->list[i] = (*list)->list[i-1];
            (*list)->list[i-1] = temp;
        }

    }
    else{}
}

int size(intList **list){
    return (*list)->size;
}

void delete(intList **list, int var){
    int i, j = 0, found = 0;
    int tempArray[(*list)->size];

    //iterate through list and find the element we want to delete and set a found flag
    //Make sure to shift back with j when the element is removed
    for (i = 0; i < (*list)->size;i++){
        if ((*list)->list[i] != var || found == 1){
            tempArray[j] = (*list)->list[i];
            j++;
        }
        else {    
            found = 1;
        }
    }

    if (found == 1){
        (*list)->size--;
        free((*list)->list);
        (*list)->list = (int *)calloc((*list)->size, sizeof(int));

        for(i = 0; i<(*list)->size;i++){
            (*list)->list[i] = tempArray[i];
        }
    }

}

int isEmpty(intList **list){
    if ((*list)->size == 0){
        return 1;
    }
    return 0;
}

void printList(intList **list){
    int i;
    for (i = 0; i<(*list)->size;i++){
        printf("%d ", (*list)->list[i]);
    }
    printf("\n");
}

int main(){

    intList *list;
    init(&list);
    append(&list, 2);
    append(&list, 2);
    append(&list, 2);
    add(&list, 3, 2);
    printList(&list);
    delete(&list, 2);
    delete(&list, 4);
    printList(&list);
    printf("%d", size(&list));
    return 0;
}