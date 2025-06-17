#ifndef QUEUE_H
#define QUEUE_H

#include "packet.h"

struct node{ // data structure for each node
  struct packet *item;
  struct node *next;
};

struct queue{ // data structure for queue
  struct node *head;
  struct node *tail;
};

void create_queue(struct queue **q);

int isempty(struct queue *q);

void enqueue(struct queue **q, const struct pcap_pkthdr *header, const unsigned char *packet);

void dequeue(struct queue **q);

void destroy_queue(struct queue **q);

#endif //QUEUE_H
