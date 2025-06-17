#ifndef CS241_DISPATCH_H
#define CS241_DISPATCH_H

#include <pcap.h>
#include "queue.h"

struct dataToPass{
    int verbose;
    pcap_t *handler;
    struct queue **queue;
};

void dispatch(unsigned char *user, const struct pcap_pkthdr *header, const unsigned char *packet);
void dump(const unsigned char *data, int length);

#endif
