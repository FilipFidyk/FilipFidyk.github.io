#ifndef QUEUE_H
#define QUEUE_H

struct Queue{
    int capacity;
    unsigned front;
    unsigned back;
    int *array;
};

void createQueue(struct Queue **self, unsigned inCapacity);
int enqueue(struct Queue **self, int item);
int dequeue(struct Queue **self);
int front (struct Queue **self);
int isEmpty(struct Queue **self);



#endif //QUEUE_H