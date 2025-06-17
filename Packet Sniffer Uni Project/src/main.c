#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <getopt.h>
#include <signal.h>
#include <unistd.h>

#include "sniff.h"
#include "LinkedList.h"

//Create global variables : loop variable and analysis trackers, declared here because of SIGINT handler
volatile int loop = 1;
int g_syn_num, g_arp_num, g_blacklist_num;
struct LinkedList *g_syn_ips;

//function prototypes since because i don't want my main function to be at the bottom 
void handler(int signal);
void itoa_safe(int num, char *str);

// Command line options
#define OPTSTRING "vi:"
static struct option long_opts[] = {
  {"interface", optional_argument, NULL, 'i'},
  {"verbose",   optional_argument, NULL, 'v'}
};

struct arguments {
  char *interface;
  int verbose;
};

void print_usage(char *progname) {
  fprintf(stderr, "A Packet Sniffer/Intrusion Detection System tutorial\n");
  fprintf(stderr, "Usage: %s [OPTIONS]...\n\n", progname);
  fprintf(stderr, "\t-i [interface]\tSpecify network interface to sniff\n");
  fprintf(stderr, "\t-v\t\tEnable verbose mode. Useful for Debugging\n");
}



int main(int argc, char *argv[]) {
  // Parse command line arguments
  struct arguments args = {"eth0", 0}; // Default values
  int optc;
  while ((optc = getopt_long(argc, argv, OPTSTRING, long_opts, NULL)) != EOF) {
    switch (optc) {
      case 'v':
        args.verbose = 1;
        break;
      case 'i':
        args.interface = (optarg);
        break;
      default:
        print_usage(argv[0]);
        exit(EXIT_FAILURE);
    }
  }
  // Print out settings
  printf("%s invoked. Settings:\n", argv[0]);
  printf("\tInterface: %s\n\tVerbose: %d\n", args.interface, args.verbose);
  
  // Invoke Intrusion Detection System
  g_syn_num = 0;
  g_arp_num = 0;
  g_blacklist_num = 0;
  createLinkedList(&g_syn_ips);
  signal(SIGINT, handler);
  sniff(args.interface, args.verbose);
  destroyLinkedList(&g_syn_ips);
  return 0;
}

/*
  Converts integers to string signal safely

  num - the number to convert
  str - The buffer to place the string representation into
*/
void numToStrSafe(int num, char *str){
    int i = 0;
    do {
        str[i++] = (num % 10) + '0';
        num /= 10;
    } while (num != 0);

    str[i] = '\0';

    // Reverse the string
    int start = 0;
    int end = i - 1;
    while (start < end) {
        char temp = str[start];
        str[start] = str[end];
        str[end] = temp;
        start++;
        end--;
    }
}

/*
  The signal handler for SIGINT, prints out the values of the analysis trackers
  Also flips the loop flag so multithreading finishes operations and then the application exits naturally
*/
void handler(int signal){
  char msg[256] = "\nIntrusion Detection Report:\n";
  write(STDOUT_FILENO, msg, strlen(msg));

  char num[32];
  numToStrSafe(g_syn_num, num);
  strcpy(msg, num);
  strcat(msg, " SYN packets detected from ");
  strcpy(num, "");
  numToStrSafe(g_syn_ips->size, num);
  strcat(msg, num);
  strcat(msg, " different IPs (syn attack)\n");
  write(STDOUT_FILENO, msg, strlen(msg));

  strcpy(num, "");
  numToStrSafe(g_arp_num, num);
  strcpy(msg, num);
  strcat(msg, " ARP responses (cache poisoning)\n");
  write(STDOUT_FILENO, msg, strlen(msg));

  strcpy(num, "");
  numToStrSafe(g_blacklist_num, num);
  strcpy(msg, num);
  strcat(msg, " URL Blacklist violations\n");
  write(STDOUT_FILENO, msg, strlen(msg));

  loop = 0;
}


