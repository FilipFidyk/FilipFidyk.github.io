#ifndef D_LINKED_LIST_H
#define D_LINKED_LIST_H


struct Node {
    int data;
    struct Node *next;
    struct Node *prev;
};

void initNode(struct Node **self, int inData);
void insertAtHead(struct Node **self, int inData);
void insertAtTail(struct Node **self, int inData);
int deleteAtHead(struct Node **self);
int deleteAtTail(struct Node **self);
void print(struct Node **self);
void reversePrint(struct Node **self);
struct Node* find(int data);

#endif //D_LINKED_LIST_H