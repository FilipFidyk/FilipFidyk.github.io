#include "dispatch.h"

#include <pcap.h>
#include <pthread.h>
#include <stdlib.h>
#include <net/ethernet.h>
#include <netinet/if_ether.h>

#include "packet.h"

//import global variables : loop flag, queue mutex lock and queue condition variable
extern pthread_mutex_t g_queue_lock;
extern pthread_cond_t g_queue_cond;
extern volatile int loop;

/*
  The function used by pcap_loop for captured packets. Its job is to add them to the packet queue

  user - unsigned byte pointer, allows for the passing of any data
  header - metadata of the captured packet
  packet the data in the packet
*/
void dispatch(unsigned char *user,
              const struct pcap_pkthdr *header,
              const unsigned char *packet){

  //the passed data will be of "struct dataToPass" shape, containing verbose flag, the pcap handler and the queue 
  struct dataToPass *structUser = (struct dataToPass*)user;

  if (packet == NULL){
    if (structUser->verbose){
      printf("No packet received. %s\n", pcap_geterr(structUser->handler));
    }  
  }
  else {
    if (structUser->verbose){
      dump(packet, header->len);
    }

    //If a packet is captured and the program hasn't been CTRL+C'ed add packet to the queue
    if (loop){
      pthread_mutex_lock(&g_queue_lock);
      enqueue(structUser->queue, header, packet);
      pthread_mutex_unlock(&g_queue_lock);
      pthread_cond_broadcast(&g_queue_cond);
    }
    //If the program has been CTRL+C'ed just wake up the threads
    else{
      pthread_cond_broadcast(&g_queue_cond);
    }
  }
}

// Utility/Debugging method for dumping raw packet data
void dump(const unsigned char *data, int length) {
  unsigned int i;
  static unsigned long pcount = 0;
  // Decode Packet Header
  struct ether_header *eth_header = (struct ether_header *) data;
  printf("\n\n === PACKET %ld HEADER ===", pcount);
  printf("\nSource MAC: ");
  for (i = 0; i < 6; ++i) {
    printf("%02x", eth_header->ether_shost[i]);
    if (i < 5) {
      printf(":");
    }
  }
  printf("\nDestination MAC: ");
  for (i = 0; i < 6; ++i) {
    printf("%02x", eth_header->ether_dhost[i]);
    if (i < 5) {
      printf(":");
    }
  }
  printf("\nType: %hu\n", eth_header->ether_type);
  printf(" === PACKET %ld DATA == \n", pcount);
  // Decode Packet Data (Skipping over the header)
  int data_bytes = length - ETH_HLEN;
  const unsigned char *payload = data + ETH_HLEN;
  const static int output_sz = 20; // Output this many bytes at a time
  while (data_bytes > 0) {
    int output_bytes = data_bytes < output_sz ? data_bytes : output_sz;
    // Print data in raw hexadecimal form
    for (i = 0; i < output_sz; ++i) {
      if (i < output_bytes) {
        printf("%02x ", payload[i]);
      } else {
        printf ("   "); // Maintain padding for partial lines
      }
    }
    printf ("| ");
    // Print data in ascii form
    for (i = 0; i < output_bytes; ++i) {
      char byte = payload[i];
      if (byte > 31 && byte < 127) {
        // Byte is in printable ascii range
        printf("%c", byte);
      } else {
        printf(".");
      }
    }
    printf("\n");
    payload += output_bytes;
    data_bytes -= output_bytes;
  }
  pcount++;
}
