#include <stdlib.h>
#include <stdio.h>
#include "DLinkedList.h"

void initNode(struct Node **self, int inData){
    *self = (struct Node*)calloc(1, sizeof(struct Node));
    (*self)->data = inData;
    (*self)->next = NULL;
    (*self)->prev = NULL;
}

void insertAtHead(struct Node **self, int inData){
    struct Node *new;
    initNode(&new, inData);
    if (*self != NULL){
        (*self)->prev = new;
        new->next = *self;
    }
    *self = new;
}

void insertAtTail(struct Node **self, int inData){
    struct Node *new, *temp;
    initNode(&new, inData);
    temp = *self;
    while(temp->next != NULL){
        temp = temp->next;
    }
    new->prev = temp;
    temp->next = new;
}

int deleteAtHead(struct Node **self){};

int deleteAtTail(struct Node **self){};

void print(struct Node **self){};

void reversePrint(struct Node **self){};

struct Node* find(int data){
    /*
    find via linear search

    if (found){
        return pointer
    }
    else{
        return NULL
    }*/
}