#include <stdlib.h>
#include <stdio.h>
#include "LinkedList.h"
#include "hashset.h"

//This can easily be adjusted to a hashmap, by letting multiple of an element be included
//This can be done simply in the add function

void initTable(struct HashTable **self){
    (*self)->table = (struct Node**)calloc(50, sizeof(struct Node*));
    (*self)->linkedListNum = 50;
    (*self)->size = 0;
    for (int i = 0; i < 50; i++){
        initNode((*self)->table[i], INT_MIN, NULL);
    }
}

int hashCode(struct HashTable *self, int data){
    return data % self->linkedListNum;
}

void add(struct HashTable **self, int data){
    int index = hashCode(*self, data);

    struct Node *temp = (*self)->table[index], *new;

    if (temp->value == INT_MIN){
        temp->value == data;
        (*self)->size++;
    }
    else if (temp->value == data){
        return;
    }
    
    while (temp->next != NULL){
        temp = temp->next;
        if (temp->value == data){
            return;
        }
    }
        initNode(&new, data, NULL);
        temp->next = new;
        (*self)->size++;
}

void remove(struct HashTable **self, int data){
    
    //Get the right hashed index and store that linked list
    int index = hashCode(*self, data);
    struct Node *temp = (*self)->table[index];

    //If our value is the head, delete the head
    if (temp->value == data){
        free(*self);
        *self = temp->next;
        (*self)->size--;
        return;
    }

    //If not find the node before the one we want to delete
    int found = 0;
    while (temp->next != NULL){
        if (temp->next->value == data){
            found = 1;
            break;
        }
    }

    //If it is a tail, do a tail delete, if it isn't a tail adjust the next link accordingly
    if (found){
        if (temp->next->next == NULL){
            free(temp->next);
            temp->next == NULL;
        }
        else {
            struct Node *tempNext = temp->next->next;
            free(temp->next);
            temp->next = tempNext;
        }

        (*self)->size--;
        return 1;
    }
    else{
        return -1;
    }
}

int contains(struct HashTable **self, int data){
    //Similar to remove but without the free of data and adjusting links :D
}