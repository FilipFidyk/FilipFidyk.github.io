#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "LinkedList.h"

//Dynamically allocate the linked list
void createLinkedList(struct LinkedList **self){
    *self = (struct LinkedList*)calloc(1,sizeof(struct LinkedList));
    (*self)->head = NULL;
    (*self)->size = 0;
}

int isEmpty(struct LinkedList *self){
    return !self->size;
}

//Adds items as nodes, only adds unique ones
//dynamically allocates the data as well with strdup() since the stack frame may disappear
void addLinkedList(struct LinkedList **self, const char *item){
    (*self)->size++;

    //if we're adding directly to head we don't have to do any checking
    //Could invert the if logic to reduce code duplication, doesn't matter
    if ((*self)->head == NULL){
        struct Node *node = (struct Node*)calloc(1, sizeof(struct Node));
        node->item = strdup(item);;
        node->next = NULL;
        (*self)->head = node;
        return;
    }

    struct Node *temp = (*self)->head;
    while (temp->next != NULL ){
        if (!strcmp(temp->item, item)){
            return;
        }
        temp = temp->next;
    }

    if (!strcmp(temp->item, item)){
        return;
    }
    else{
        struct Node *node = (struct Node*)calloc(1, sizeof(struct Node));
        node->item = strdup(item);
        node->next = NULL;
        temp->next = node;
    }
}

//Option to print the linked list, mainly for testing of uniqueness
void printLinkedList(struct LinkedList *self){
    if(self == NULL){
        printf("list doesn't exist");
    }
    
    struct Node *temp = self->head;
    while (temp != NULL){
        printf("%s\n", temp->item);
        temp = temp->next;
    }
}

//Deallocate all nodes and the linked list
void destroyLinkedList(struct LinkedList **self){
    if (self == NULL || *self == NULL) return;
    
    struct Node *current = (*self)->head;
    struct Node *temp;

    while (current != NULL){
        temp = current->next;
        free((void*)current->item);
        free(current);
        current = temp;
    }

    free(*self);
    *self = NULL;
}