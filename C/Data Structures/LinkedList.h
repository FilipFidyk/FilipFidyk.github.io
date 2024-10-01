#ifndef LINKED_LIST_H
#define LINKED_LIST_H

struct Node{
    int value;
    struct Node *next;
};

void initNode(struct Node **self, int inValue, struct Node *inNext);
void addToTail(struct Node **self, int inValue);
void addToHead(struct Node **self, int inValue);
void insert(struct Node **self, int inValue, int loc);
int size(struct Node **self);
int deleteHead(struct Node **self);
int deleteTail(struct Node **self);
int deletLoc(struct Node **self, int loc);

#endif // LINKED_LIST_H