struct Node{ // data structure for each node
  const char *item;
  struct Node *next;
};

struct LinkedList{ // data structure for queue
  struct Node *head;
  int size;
};

void createLinkedList(struct LinkedList **self);

int isEmpty(struct LinkedList *self);

void addLinkedList(struct LinkedList **self, const char *item);

void printLinkedList(struct LinkedList *self);

void destroyLinkedList(struct LinkedList **self);