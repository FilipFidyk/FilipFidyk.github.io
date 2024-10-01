#include <stdlib.h>
#include <stdio.h>
#include "stack.h"

//array implementation of a stack, a linked list version simply replaces the array pointer with a linked list pointer
void createStack(struct Stack **self, unsigned inCapacity){
    *self = (struct Stack*)malloc(sizeof(struct Stack));
    (*self)->capacity = inCapacity;
    (*self)->top = -1;
    (*self)->array = (int*)calloc(inCapacity, sizeof(int));
}

void push(struct Stack **self, int item){
    if ((*self)->top == (*self)->capacity){
        return;
    }

    (*self)->array[++(*self)->top] = item;
}

int pop(struct Stack **self){
    if ((*self)->top == -1){
        return INT_MIN;
    }

    return (*self)->array[(*self)->top--];
}

int top(struct Stack **self){
    if ((*self)->top == -1){
        return INT_MIN;
    }

    return (*self)->array[(*self)->top];
}

void toString(struct Stack **self){
    for (int i = 0; i <= (*self)->top; i++){
        printf("%d ", (*self)->array[i]);
    }
}

int isEmpty(struct Stack **self){
    if ((*self)->top == -1){
        return (*self)->top == -1;
    }
}

void main(){
    struct Stack *stack;
    createStack(&stack, 10);
}