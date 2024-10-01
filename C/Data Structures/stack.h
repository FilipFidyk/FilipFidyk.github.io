#ifndef STACK_H
#define STACK_H

struct Stack {
    unsigned capacity;
    int top;
    int *array;
};

void createStack(struct Stack **self, unsigned inCapacity);
void push(struct Stack **self, int item);
int pop(struct Stack **self);
int top(struct Stack **self);
void toString(struct Stack **self);
int isEmpty(struct Stack **self);

#endif //STACK_H