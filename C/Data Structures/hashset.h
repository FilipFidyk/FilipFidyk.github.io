#ifndef HASHSET_H
#define HASHSET_H
#include "LinkedList.h"

struct HashTable{
    struct Node **table;
    int linkedListNum;
};

void initTable(struct HashTable **self);
void add(struct HashTable **self, int data);
void remove(struct HashTable **self, int data);
int hashCode(struct HashTable *self, int data);
int contains(struct HashTable *self, int data);

#endif //HASHSET_H