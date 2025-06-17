#ifndef CS241_ANALYSIS_H
#define CS241_ANALYSIS_H

#include <pcap.h>

void* threadAnalyse(void* args);
void* loopMonitor(void* args);
void analyse(const struct pcap_pkthdr *header, const unsigned char *packet);
void printBlacklist(const char *url, const char *sIP, const char *dIP);
unsigned int find_subsequence(const unsigned char *data, int data_len, const unsigned char *substr, int substr_len);

#endif
