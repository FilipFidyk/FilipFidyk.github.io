#ifndef LIST_H
#define LIST_H

struct List{
    int size;
    int capacity;
    int *array;
};

void createList(struct List **self);
void changeList(struct List **self, int inCapacity);
void add(struct List **self, int data);
void insert(struct List **self, int data, int loc);
int remove(struct List **self, int data);
int size(struct List *self);
int isEmpty(struct List *self);
void toString(struct List *self, char **result);

#endif //LIST_H