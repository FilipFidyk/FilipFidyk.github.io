#include <stdio.h>
#include <stdlib.h>
#include <string.h>  // For memcpy
#include <pcap.h>
#include "queue.h"
#include "packet.h"

//initiates the queue
void create_queue(struct queue **q){ 
  *q =(struct queue *)malloc(sizeof(struct queue));
  (*q)->head=NULL;
  (*q)->tail=NULL;
}

//destroys the queue and frees the memory
void destroy_queue(struct queue **q){  
  while(!isempty(*q)){
    dequeue(q);
  }
  free(*q);
}

// checks if queue is empty
int isempty(struct queue *q){ 
  return(q->head==NULL);
}

//enqueues a node with an item
void enqueue(struct queue **q, const struct pcap_pkthdr *header, const unsigned char *packet){ 
  struct node *new_node=(struct node *)malloc(sizeof(struct node));

  //dynamically allocate the packet data as we will use it after dispatch returns
  struct pcap_pkthdr *headerCpy = (struct pcap_pkthdr *)malloc(sizeof(struct pcap_pkthdr));
  memcpy(headerCpy, header, sizeof(struct pcap_pkthdr));
  unsigned char *packetCpy = (unsigned char*)malloc(header->len);
  memcpy(packetCpy, packet, header->len);

  //Create the packet struct and allocate it to the node
  struct packet *item = (struct packet*)malloc(sizeof(struct packet));
  item->header = headerCpy;
  item->packet = packetCpy;

  new_node->item=item;
  new_node->next=NULL;
  if(isempty(*q)){
    (*q)->head=new_node;
    (*q)->tail=new_node;
  }
  else{
    (*q)->tail->next=new_node;
    (*q)->tail=new_node;
  }
}

//dequeues a the head node - doesnt free the data that was memcpy'ed in enqueue
//Will be the responsility of threadAnalyse after it isn't needed anymore 
void dequeue(struct queue **q){ 
  struct node *head_node;
  if(isempty(*q)){
    printf("Error: attempt to dequeue from an empty queue");
  }
  else{
    head_node=(*q)->head;
    (*q)->head=(*q)->head->next;
    if((*q)->head==NULL)
      (*q)->tail=NULL;

    free(head_node->item);
    free(head_node);
  }
}