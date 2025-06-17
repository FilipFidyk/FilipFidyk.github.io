#ifndef PACKET_H
#define PACKET_H

#include <pcap.h>

struct packet{
    const struct pcap_pkthdr *header;
    const unsigned char *packet;
};

#endif //PACKET_H