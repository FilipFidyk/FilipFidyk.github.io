#include "analysis.h"

#include <pcap.h>
#include <net/ethernet.h>
#include <netinet/if_ether.h>
#include <netinet/ip.h>
#include <netinet/tcp.h>
#include <string.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#include "LinkedList.h"
#include "queue.h"
#include "packet.h"

#define ETHER_IP  0x0800
#define ETHER_ARP 0x0806
#define IP_TCP 0x06
#define HTTP_PORT 80
#define GOOGLE_URL "www.google.co.uk"
#define BBC_URL "www.bbc.co.uk"

//import global variables : loop flag, analysis trackers, all mutex locks and queue condition variable
extern volatile int loop;
extern int g_syn_num, g_arp_num, g_blacklist_num;
extern struct LinkedList *g_syn_ips;
extern pthread_mutex_t g_queue_lock, g_syn_lock, g_arp_lock, g_blacklist_lock;
extern pthread_cond_t g_queue_cond;

/*
  Thread function for the extra thread to periodically monitor the loop flag
  Breaks main thread out of pcap_loop and wakes up all threads. Necessary if no packets
  arrive after clicking CTRL+C

  args - provides the packet capturing handler as a void pointer
*/
void* loopMonitor(void* args){

    while (loop) {
      sleep(1); // Periodically check the loop flag
    }
    pcap_breakloop((pcap_t*)args);
    pthread_cond_broadcast(&g_queue_cond);
    return NULL;
}

/*
  Thread function for processing and analysing packets, dequeue them from the queue 
  and put them through analyse()

  args - provides the address of the packet queue 
*/
void* threadAnalyse(void* args){
  struct queue **queue = (struct queue**)args;

  while(loop||!isempty(*queue)){
    
    pthread_mutex_lock(&g_queue_lock);
		while(isempty(*queue)&&loop==1){
			pthread_cond_wait(&g_queue_cond,&g_queue_lock);
		}
    if(!isempty(*queue)){
      const struct pcap_pkthdr *header = (*queue)->head->item->header;
      const unsigned char *packet = (*queue)->head->item->packet;
      dequeue(queue);
      pthread_mutex_unlock(&g_queue_lock);
      analyse(header, packet);

      free((void*)header);
      free((void*)packet);
    }
    else{
      pthread_mutex_unlock(&g_queue_lock);
    }
  }
  return NULL;
}

/*
  Handles all packet inspection for SYN Flooding, ARP Poisoning, and Blacklist URLs

  header - supplies all packet metadata
  packet - supplies all packet data
*/
void analyse(const struct pcap_pkthdr *header,
             const unsigned char *packet) {
  
  //Ethernet layer
  struct ether_header *eth_header = (struct ether_header*) packet;

  if (ntohs(eth_header->ether_type) == ETHER_IP){   
    //IP layer
    struct iphdr *ip_header = (struct iphdr*)(packet+sizeof(struct ether_header));
    
    if (ip_header->protocol == IP_TCP){
      //TCP layer
      struct tcphdr *tcp_header = (struct tcphdr*)(packet+sizeof(struct ether_header)+(ip_header->ihl*4));
      
      //Take out the ips, manipulate to "struct in_addr" and put through inet_ntoa
      //inet_ntoa has an internal buffer that can be overwritten and we lose the previous execution
      pthread_mutex_lock(&g_syn_lock);
      const char* sAddress = strdup(inet_ntoa(*(struct in_addr *)&ip_header->saddr));
      const char* dAddress = strdup(inet_ntoa(*(struct in_addr *)&ip_header->daddr));

      //SYN flag check, increment and add unique IP (built-in into Linked List)
      //addLinkedList makes a copy in the heap of sAddress, necessary as sAddress is released at the end of this funcion
      if (tcp_header->syn==1){
        g_syn_num++;
        addLinkedList(&g_syn_ips, sAddress);          
      }
      pthread_mutex_unlock(&g_syn_lock);

      if (ntohs(tcp_header->th_dport) == HTTP_PORT){
        //HTTP layer
        //Need the length of all headers until HTTP 
        int preHTTPlen = sizeof(struct ether_header)+(ip_header->ihl*4)+(tcp_header->th_off*4);
        const unsigned char *http_data = packet+preHTTPlen; 
        
        //Check for google and bbc
        if(find_subsequence(http_data, header->caplen-preHTTPlen, (unsigned char*)GOOGLE_URL, strlen(GOOGLE_URL))){
          pthread_mutex_lock(&g_blacklist_lock);
          g_blacklist_num++;
          printBlacklist("google", sAddress, dAddress);
          pthread_mutex_unlock(&g_blacklist_lock);
        }
        else if(find_subsequence(http_data, header->caplen-preHTTPlen, (unsigned char*)BBC_URL, strlen(BBC_URL))){
          pthread_mutex_lock(&g_blacklist_lock);
          g_blacklist_num++;
          printBlacklist("bbc", sAddress, dAddress);
          pthread_mutex_unlock(&g_blacklist_lock);
        }
      } 
      //strdup() dynamically allocates memory so have to free the memory
      free((void*)sAddress);
      free((void*)dAddress);
    }
  }
  else if (ntohs(eth_header->ether_type) == ETHER_ARP){
    //ARP packet
    pthread_mutex_lock(&g_arp_lock);
    g_arp_num++;
    pthread_mutex_unlock(&g_arp_lock);
  }
}

/*
  Prints the the ip addresses and url of a blacklist violation

  url - Self-explanatory
  sIP - The source IP address of the data packet
  dIP - THe destination IP address of the data packet
*/
void printBlacklist(const char *url, const char *sIP, const char *dIP){
  printf("==============================\n");
  printf("Blacklisted URL Violation detected\n");
  printf("Source IP address: %s\n", sIP);
  printf("Destination IP addres: %s (%s)\n", dIP, url);
  printf("==============================\n");
}

/*
  Returns 0 or 1 if a certain subsequence of unsigned chars exists in a sequence

  data - The array where we are looking for the subsequence, aka haystack
  data_len - Length of data (Don't calculate with sizeof() it isn't statically defined)
  substr - the subsequence we are looking for
  substr_len - length of the subsequence
*/
unsigned int find_subsequence(const unsigned char *data, int data_len, const unsigned char *substr, int substr_len) {
  //Nothing to look at
  if (data_len == 0){
    return 0;
  }

  // Iterate through the data, stopping early enough to prevent overflow
  for (int i = 0; i <= data_len - substr_len; i++) {
      if (memcmp(data + i, substr, substr_len) == 0) {
          return 1; //Found
      }
  }
  return 0;  //Not found
}
