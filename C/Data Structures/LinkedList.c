#include <stdlib.h>
#include <stdio.h>
#include "LinkedList.h"

//Initialising of nodes in the heap
void initNode(struct Node **self, int inValue, struct Node *inNext){
    *self = (struct Node*)calloc(1, sizeof(struct Node));
    (*self)->value = inValue;
    (*self)->next = inNext;
}

void addToHead(struct Node **self, int newValue){
    struct Node *newNode;
    initNode(&newNode, newValue, *self);
    *self = newNode;
}

void addToTail(struct Node **self, int newValue){
    //set up a temp node that we will use to reach the tail
    struct Node *temp = *self;
    
    //follow the pointers until the tail is in temp
    while (temp->next != NULL){
        temp = temp->next;
    }

    //set up an actual node and initialise it, then add it to the previous tail
    //added a pointer to the previous dereferenced pointer so it should work
    struct Node *new;
    initNode(&new, newValue, NULL);
    temp->next = new;
    
}

void insert(struct Node **self, int newValue, int loc){
    struct Node *temp = *self;

    //if index 0 just use addToHead
    if (loc == 0){
        addToHead(self, newValue);
    }
    //else iterate through to the index 1 behind, if such index doesn't exist, exit the function
    else{
        for (int i = 0; i < loc - 1;i++){
            if (temp->next == NULL){
                addToTail(self, newValue);
            }
            else{
                temp = temp->next;
            }
        }
        // if index is reached, we want to add the new node as the next and add the previous next to the new nodes link
        struct Node *newNode;
        initNode(&newNode, newValue, temp->next);
        temp->next = newNode;
    }
}

int size(struct Node **self){
    
    if (*self == NULL){
        return 0;
    }
    struct Node *temp = *self;
    int num = 1;

    while (temp->next !=NULL){
        temp = temp->next;
        num++;
    }
    return num;
}

int deleteHead(struct Node **self){
    if (*self == NULL){
        return -1;
    }
    else{
        struct Node *temp = (*self)->next;
        free(*self);
        *self = temp;
        return 1;
    }
}

int deleteTail(struct Node **self){
    if (*self == NULL){
        return -1;
    }
    else{
        struct Node *temp = *self;
        while((temp->next)->next != NULL){
            temp = temp->next;
        }
        free(temp->next);
        temp->next = NULL;
    }
}

int deleteLoc(struct Node **self, int loc){
    if (*self == NULL){
        return -1;
    }
    //Reach the one before the one we want to delete if possible, take the deleting one next and place it into the one behind
    //And free the heap memory
    else{
        struct Node *temp = *self;
        for (int i = 0; i < loc - 1; i++){
            if (temp->next == NULL){
                return -1;
            }
            else{
                temp = temp->next;
            }
        }
        struct Node *tempNext = (temp->next)->next;
        free(temp->next);
        temp->next = tempNext;
    }
}

int main(){
    struct Node *head;
    initNode(&head, 1, NULL);




    printf("%d", head->value);
}
