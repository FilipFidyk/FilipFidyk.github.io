#include <stdlib.h>
#include <stdio.h>
#include "list.h"

//Initialise the list to a starting capacity of 25
void createList(struct List **self){
    *self = (struct List*)malloc(sizeof(struct List));
    (*self)->capacity = 25;
    (*self)->size = 0;
    (*self)->array = (int*)calloc((*self)->capacity, sizeof(int));
}

//A way to easily resize if capacity is at max 
void changeList(struct List **self, int inCapacity){
    (*self)->capacity = inCapacity;
    (*self)->array = (int*)realloc((*self)->capacity, sizeof(int));
}

//add to the end of the list, simple, resize if necessary
void add(struct List **self, int data){
    if ((*self)->size == (*self)->capacity){
        changeList(self, 2*(*self)->capacity);
    }

    (*self)->array[(*self)->size++] = data;

}

//Either append using previous method, or create temp array, copy and insert
//Free previous array, and change the pointer to the temp array
void insert(struct List **self, int data, int loc){
    if (loc == 0){
        add(self, data);
        return;
    }

    int *tempArray;
    if ((*self)->size == (*self)->capacity){
        changeList(self, 2*(*self)->capacity);
        *tempArray = (int*)calloc((*self)->capacity, sizeof(int));
    }
    else{
        *tempArray = (int*)calloc((*self)->capacity, sizeof(int));
    }

    int i;
    for (i = 0; i < loc; i++){
        tempArray[i] = (*self)->array[i];
    }
    tempArray[loc] = data;
    for (i = loc + 1; i < (*self)->size; i++){
        tempArray[i+1] = (*self)->array[i];
    }

    (*self)->size++;
    free((*self)->array);
    (*self)->array = tempArray;
    
}

int remove(struct List **self, int data){
    if ((*self)->size == 0){
        return INT_MIN;
    }
    
    int i, found = 0;
    for (i = 0; i < (*self)->size; i++){
        if ((*self)->array[i] == data){
            found = 1;
            break;
        }
    }

    if (!found){
        return INT_MIN;
    }
    else{
        for (i; i < (*self)->size - 1; i++){
            (*self)->array[i] = (*self)->array[i+1]; 
        }
        (*self)->array[i+1] = 0;
        (*self)->size--;
        return INT_MAX;
    }
}

int size(struct List *self){
    return self->size;
}

int isEmpty(struct List *self){
    return self->size == 0;
};

void toString(struct List *self, char **result){
    char buffer[12];
    
    for (int i = 0; i < self->size; i++){
        sprintf(buffer, "%d", self->array[i], " ");
        strcat(*result, buffer);
    }

}
