#include "sniff.h"

#include <stdio.h>
#include <stdlib.h>
#include <pcap.h>
#include <net/ethernet.h>
#include <netinet/if_ether.h>
#include <pthread.h>
#include <string.h>

#include "dispatch.h"
#include "queue.h"
#include "analysis.h"

#define NUMTHREAD 10

//Create the mutex locks and condition variable as a shared resources
pthread_mutex_t g_queue_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t g_syn_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t g_blacklist_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t g_arp_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t g_queue_cond = PTHREAD_COND_INITIALIZER;

/*
  Application main sniffing loop. Its purpose is to prepare and start the packet capturing and 
  multithreading

  interface - Passes the interface input by the user to open the connection for packet capture
  verbose - Passes the verbose option to print raw packet data
*/
void sniff(char *interface, int verbose) {
  
  char errbuf[PCAP_ERRBUF_SIZE];

  // Open the specified network interface for packet capture. pcap_open_live() returns the handle to be used for the packet
  // capturing session. check the man page of pcap_open_live()
  pcap_t *pcap_handle = pcap_open_live(interface, 4096, 1, 1000, errbuf);
  if (pcap_handle == NULL) {
    fprintf(stderr, "Unable to open interface %s\n", errbuf);
    exit(EXIT_FAILURE);
  } else {
    printf("SUCCESS! Opened %s for capture\n", interface);
  }

  //Create the packet queue and threads
  struct queue *packetQueue;
  pthread_t monitorThread;
  pthread_t tid[NUMTHREAD];
  create_queue(&packetQueue);

  //Set up the monitor thread and packet threads 
  pthread_create(&monitorThread, NULL, loopMonitor, (void*)pcap_handle);
  for (int i = 0; i < NUMTHREAD; i++){
    pthread_create(&tid[i], NULL, threadAnalyse, (void*)&packetQueue);
  }

  //Start capturing packets with dispatch and pass the following data to it
  struct dataToPass user = {verbose, pcap_handle, &packetQueue};
  pcap_loop(pcap_handle, 0, dispatch, (unsigned char*)&user);

  //Catch all the created threads and close them
  pthread_join(monitorThread, NULL);
  for (int i = 0; i < NUMTHREAD; i++){
    pthread_join(tid[i], NULL);
  }
  destroy_queue(&packetQueue);
  pcap_close(pcap_handle);

} 
