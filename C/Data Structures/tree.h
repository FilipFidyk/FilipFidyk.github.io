#ifndef TREE_H
#define TREE_H

struct Node{
    int data;
    struct Node *right;
    struct Node *left;
};

void initNode(struct Node **self, int inData);
int add(struct Node **self, int data);
struct Node* remove(struct Node **self, int data);
struct Node* deleteDeepestRightMostNode(struct Node **self);
int find(struct Node *self, int data);
int* toArray(struct Node **self);
void preOrderSearch(struct Node **self, char **result);
void inOrderSearch(struct Node **self, char **result);
void postOrderSearch(struct Node **self, char **result);

#endif //TREE_H