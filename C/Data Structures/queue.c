#include <stdlib.h>
#include <stdio.h>
#include "queue.h"

void createQueue(struct Queue **self, unsigned inCapacity){
    *self = (struct Queue*)malloc(sizeof(struct Queue));
    (*self)->capacity = inCapacity;
    (*self)->front = -1;
    (*self)->back = -1;
    (*self)->array = (int*)calloc(inCapacity, sizeof(int));
}

int enqueue(struct Queue **self, int item){
    if(isEmpty(self)){
        (*self)->front = 0;
        (*self)->back = 0;
    }
    else if (isFull(self)){
        return INT_MIN;
    }
    else{
        (*self)->back++;
        if ((*self)->back == (*self)->capacity){
            (*self)->back = (*self)->back % (*self)->capacity;
        }
    }

     (*self)->array[(*self)->back] = item; 
}

int dequeue(struct Queue **self){
    if (isEmpty(self)){
        return INT_MIN;
    }

    int tempOut = (*self)->array[(*self)->back];
    if ((*self)->back == 0){
        (*self)->back += (*self)->capacity-1;
    }
    else{
        (*self)->back--;
    }

    return tempOut;
}

int front(struct Queue **self){
    if (isEmpty(self)){
        return INT_MIN;
    }

    return (*self)->array[(*self)->back];
}

int isEmpty(struct Queue **self){
    return (*self)->front == -1 && (*self)->back == -1;
}

int isFull(struct Queue **self){
    return (*self)->back - (*self)->front == -1 || (*self)->back - (*self)->front == (*self)->capacity-1;
}