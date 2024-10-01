#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tree.h"
#include "queue.h"

void initNode(struct Node **self, int inData){
    *self = (struct Node*)malloc(sizeof(struct Node));
    (*self)->data = inData;
    (*self)->left = NULL;
    (*self)->right = NULL;
}

void add(struct Node **self, int inData){
    struct Node *new;
    initNode(&new, inData);

    if (*self == NULL){   
        *self = new;
    }
    else{
        struct Node *queue[100];
        int front = -1, rear = -1;
        struct Node *temp;
        queue[++rear] = *self;

        while (front != rear){
            temp = queue[++front];
            
            if (temp->left == NULL){
                temp->left == new;
                return;
            }
            else{
                queue[++rear] = temp->left;
            }

            if (temp->right == NULL){
                temp->right == new;
                return;
            }
            else{
                queue[++rear] = temp->right;
            }

        }
        
    }
}

//Just returns the pointer to the node after the replacement
struct Node* remove(struct Node **self, int data){
    //if empty return null
    if (*self == NULL){
        return NULL;
    }

    //Find the node we want to delete,
    struct Node* temp = find(self, data);

    //If it doesn't exist, return null
    if(temp == NULL){
        return NULL;
    }
    //if it does replace its data value, with the deepest right most node's value
    else{
         temp->data = deleteDeepestRightMostNode(self)->data;
         return temp;
    }
}

//Should only be used in the delete function so no need for an empty tree check
struct Node* deleteDeepestRightMostNode(struct Node **self){
    struct Node *queue[100];
    int front = -1, rear = -1, tempIndex = -1, leftOrRight = 0;
    struct Node *temp;
    //Include the root from the start
    queue[++rear] = *self;

    //Using an array and front and rear pointers, put all nodes in an array
    while (front != rear){
        temp = queue[++front];
        
        if (temp->left != NULL){
            //take note of the parent node index and whether the left or right child is added
            queue[++rear] = temp->left;
            tempIndex = front;
            leftOrRight = -1;
        }

        if (temp->right != NULL){
            queue[++rear] = temp->right;
            tempIndex = front;
            leftOrRight = 1;
        }
    }
    
    //Get rid of the correct link of the parent node
    if (leftOrRight == -1){
        queue[tempIndex]->left = NULL;
    }
    else{
        queue[tempIndex]->right = NULL;
    }

    //return the last node don't forget to free the memory in the heap
    temp = queue[front];
    free(queue[front]);
    return temp;


}

//Find the correct node using a pre order traversal, return the Node pointer
struct Node* find(struct Node *self, int data){
    if (self == NULL){
         return NULL;
    }

    if (self->data == data){
        return self;
    }
    else if (self->left != NULL){
        struct Node* temp = find(self->left, data);
        if (temp != NULL){
            return temp;
        }
    }
    else if (self->right != NULL){
        struct Node* temp = find(self->right, data);
        if (temp != NULL){
            return temp;
        }
    }
    else{
        return NULL;
    }
}

void preOrderSearch(struct Node *self, char **result){
    
    if (self == NULL){
        return;
    }
    
    char buffer[12];
    sprintf(buffer, "%d", self->data, " ");
    strcat(*result, buffer);

    preOrderSearch(self->left, result);
    preOrderSearch(self->right, result);

}

void inOrderSearch(struct Node *self, char **result){
    
    if (self == NULL){
        return;
    }
    
    inOrderSearch(self->left, result);

    char buffer[12];
    sprintf(buffer, "%d", self->data, " ");
    strcat(*result, buffer);

    inOrderSearch(self->right, result);

}

void postOrderSearch(struct Node *self, char **result){
    
    if (self == NULL){
        return;
    }
    
    postOrderSearch(self->left, result);
    postOrderSearch(self->right, result);

    char buffer[12];
    sprintf(buffer, "%d", self->data, " ");
    strcat(*result, buffer);

}

