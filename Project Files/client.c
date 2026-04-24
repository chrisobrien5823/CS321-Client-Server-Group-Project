/************************************************************/
/* Author: Ben Maigur                                       */
/* Major: Computer Science                                  */
/* Creation Date: 26 March, 2026                            */
/* Due Date: 2 April, 2026                                  */
/* Course: CS321-01                                         */
/* Professor Name: Prof. Shimkanon                          */
/* Assignment: Client-Server Project Phase #1               */
/* Filename: client.c                                       */
/* Purpose: A C client server file adapted from qotdClient.c*/
/*          class example. Provides arguments to specify    */
/*          port number                                     */
/************************************************************/

// Changelog:
// - Initial creation of client from class example (3/17/26)
// - Implemented user args parsing for port num; Submission of client.c with phase #1 deliverables (4/10/26)
// - Added menu for server data transport. Looking into threading to handle server responses (4/19/26)
// - Finished threading. All tests passed (4/20/26)
// - Fixing infinite-loop issues; Working on implementing user menu + bug fixes (4/23/26)
// - Added appropriate documentation. Ran into server testing quirk (4/23/26)


#include <math.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <stdbool.h>
#include <stdlib.h>

// Threading lib
// https://www.geeksforgeeks.org/c/multithreading-in-c/
#include <pthread.h>





/** NOTE: For compilation, you must include the -lm flag to include math.h binaries.
     e.g. gcc -o client client.c -lnsl -lm 
*/



// Function prototypes
uint16_t parse_port_number_from_args(char [], char *); 
void* listen_for_server_changes_and_update(void *);
void* user_menu(void *);
void display_board(char []);




/** 
https://stackoverflow.com/questions/13131982/create-thread-passing-arguments

With multithreading on c, you can only thread a function that has one arg of 
void*. Apparently, the solution is to put any arguments in a struct. Heres 
a struct for the menu and any functions that need the socket and user num.

*/
struct program_info {
    char* student_num;
    int *socket;
    char* board;

};




int main(int argc, char *argv[])
{  

    //https://www.geeksforgeeks.org/c/socket-programming-cc/ 
 
    int sock; // Socket
    struct sockaddr_in address; // Struct that holds IP address data
    char buffer[1024]; // The buffer to recieve data 
    uint16_t port; // Port value

    // If there are less than two arguments, give a usage guideline
    if(argv[2] == NULL || argv[1] == NULL) {
        printf("Usage: ./client [port] [size_of_port] \nex. ./client 4008 4 \nNOTE: Already defaults to address 127.0.0.1\n");
        return 0;
    }

    // printf("%d \n", argc);


    // for(int i = 0; i < argc; i++) {
    //     printf("%s \n", argv[i]);
    // }

    // printf("Port from arg: %s", argv[1]);
    // printf("Size of: %ld", sizeof(argv[1]));

    sock = socket(AF_INET, SOCK_STREAM, 0); // Create the socket


    port = parse_port_number_from_args(argv[1], argv[2]); // Get the user input of port and port size and translate into a unsigned 16 bit integer (uint16_t)

    //printf("Port: %d", port);


    address.sin_family = AF_INET; // Specify the AF_INET type
    address.sin_port = htons(port); // (According to Google) htons converts a 16 bit number to "network byte order". In this case the port num
    
    //printf("%d", htons(port));
    
    inet_pton(AF_INET, "127.0.0.1", &address.sin_addr); // inet_pton coverts multiple components to a valid IP address

    printf("What is your student #?");

    char student_num[25]; // Holds the user's student number

    fgets(student_num, sizeof(student_num), stdin);

    printf("\nConnecting to IP 127.0.0.1 on port %d \n", port);
    
    connect(sock, (struct sockaddr *)&address, sizeof(address)); // Connect to the server

    printf("Connected. \nWelcome %s", student_num);


    // init param struct + pass socket num to struct
    // see def of program_info struct above
    struct program_info *args = malloc(sizeof *args);
    args->socket = &sock;
    args->student_num = student_num;
    
    // Create two threads to hold menu and listener to prevent blocking between sending and recieving. 
    pthread_t thread1;
    pthread_t thread2; 

    pthread_create(&thread1, NULL, user_menu, args);
    pthread_create(&thread2, NULL, listen_for_server_changes_and_update, args);
    
    pthread_join(thread1, NULL); // pthread
    
    
    //user_menu(user_exit, &student_num, &sock);
    printf("Connection to 127.0.0.1 closed. Goodbye! \n");

    close(sock);
    


    return 0;
}





/***************************************************************************/
/* Function name: parse_port_number_from_args                              */
/* Description: Parses the port number from the  user arguments (char)     */
/*              and creates a unsigned 16-bit to be interpreted by the     */
/*              socket                                                     */
/* Parameters: arg - char *:  The port number as a char user argument      */
/*             port_size - char *: The length of the port number           */
/* Return Value: uint16_t - The port num represented as a unsigned 16 bit  */
/*                          integer                                        */
/***************************************************************************/
uint16_t parse_port_number_from_args(char *arg, char *port_size) {

    uint16_t port; 

    // printf("Getting there. Value: %s", arg);
    // printf("Sizeof: %ld \n", sizeof(arg));
    // printf("Port size val: %s ", port_size);
    // printf("sizeof port val: %ld \n", sizeof(port_size));


    int port_size_val = (int)(port_size[0]) - (int)('0');
    // printf("%d \n", port_size_val);

     // Getting the port from user input required some outside research. Here are my sources:
    // - https://www.geeksforgeeks.org/cpp/command-line-arguments-in-c-cpp/
    // - https://stackoverflow.com/questions/5029840/convert-char-to-int-in-c-and-c
    // - https://stackoverflow.com/questions/213042/how-do-you-do-exponentiation-in-c
    // - https://stackoverflow.com/questions/11336477/gcc-will-not-properly-include-math-h

    for(int i=0; i < (port_size_val); i++) {
        //printf("%ld ", (long)(((int)arg[i] - (int)'0') * pow((double)10, (double)(3 - i))));

        port += (long)(((int)arg[i] - (int)'0') * pow((double)10, (double)(3 - i))); // This separates the port values (e.g. 4008 = 4000 + 000 + 00 + 8)
        
        // (int)argv[i] - (int)'0': Since command line arguments are char[], we need to loop over each character in
        //                             argv[i], get its ASCII value, then subtract it from the ASCII value of '0'. This 
        //                             will return the integer value (e.g. '4' -> 4)

    }; 

    //printf("%d", port);
    return port;
}




/***************************************************************************/
/* Function name: user_menu                                                */
/* Description: Threaded function. Runs on first thread, accepting user    */
/*              input to send canvas commands or close the connection      */
/* Parameters: args – void *: socket parameters represented in a struct    */
/* Return Value: void                                                      */
/***************************************************************************/
void* user_menu(void *args) {

    struct program_info *info = args;


    bool exit = false;
    //rintf("LOG: Running on menu thread");

    while(exit == false) {
        char user_option;

        printf("\nMenu\n---------------\n");
        printf("1. (S)end a message to the server\n2. (E)xit the program");
        printf("\nWhat command would you like to perform #%s?\n", info->student_num);

        scanf("%c", &user_option);
        
        int* sock = info->socket;

        if(user_option == 'S') {
            char x;
            char y;
            char color;

            //printf("Hi");

            printf("What pixel would you like to change? (0 - 7) \n");

            printf("X: ");
            scanf("%s", &x);
            printf("\nY: ");
            scanf("%s", &y);
            printf("\nColor: ");
            scanf("%s", &color);

            //printf("%d %d", (int)x, (int)'9');

            //Make sure the user is not inputting anything besides 0-8
            if(((int)x >= (int)'8') || ((int)y >= (int)'8')) {
                printf("These values are above 7. Input is zero indexed. Please try again\n");
            } else {
                char command[] = {'S', 'E', 'T', ' ', x, ' ', y, ' ', color}; 

                //printf("%s", command);
                send(*sock, command, sizeof(command), 0);
            }
        }else if(user_option == 'E') {
            printf("exit\n") ;
            send(*sock, "exit", sizeof("exit"), 0);
            exit = true;
            break;
        }else {
            printf("Invalid. Try again");
        }

        if(exit == true) {
            break;
        }
        
    }
    
       
}





/***************************************************************************/
/* Function name: listen_for_server_changes_and_update                     */
/* Description: Threaded function. Runs on second thread, constantly       */
/*              looking for server responses, and updates internal board   */
/*              accordingly                                                */
/* Parameters: args – void *: socket parameters represented in a struct    */
/* Return Value: void                                                      */
/***************************************************************************/
void* listen_for_server_changes_and_update(void *args) {

    struct program_info *info = args;

    //printf("Running on listen thread\n");

    char buf[1024];

    int *sock = info->socket;

    bool break_loop = false;

    while(break_loop == false) {
        int received = recv(*sock, buf, sizeof(buf) - 1, 0 );
        if(received == -1) {
            printf("ERROR: NO DATA IN BUFFER\nThis may be an internal error, or port number specified points to a different server.\nPlease try again (Type \'E\') \n");
            break_loop = true;
        }else  {
            display_board(buf);
            buf[received] = '\0';
        }
	//args->board = &received;
	    
    }
}





/***************************************************************************/
/* Function name: display_board                                            */
/* Description: Recieves buffer data from listener method, displaying the  */
/*              board in a 8x8 configuration                               */
/* Parameters:  board - char[1024] : a array of characters containing      */
/*              buffer data                                                */
/* Return Value: void                                                      */
/***************************************************************************/
void display_board(char board[1024]) {

    printf("Board: \n");

    int pointer = 0;

	for(int i=0; i<8; i++){
		for(int j=0; j<8; j++) {
            printf("%c", (char)board[pointer + j]);

		}
        pointer += 8;
        printf("\n");
	}
}


// Other sources I used for added context. It is important to mention that in the process
// of looking up sources and documentation, I made sure to understand the concepts listed and provided
// my notes in comments in this file. Other sources I used were found within the official docs of Java
// and C, and man pages that were accessed through the terminal.


// - https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html#read--
// - https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html#isClosed--
// - https://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html
// - https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/WebSocket.html
// - https://www.geeksforgeeks.org/c/multithreading-in-c/
// - https://www.geeksforgeeks.org/c/fgets-function-in-c/
// - https://www.geeksforgeeks.org/c/thread-functions-in-c-c/
// - https://www.geeksforgeeks.org/computer-networks/tcp-connection-termination/
// - https://www.geeksforgeeks.org/system-design/event-driven-architecture-system-design/
// - https://stackoverflow.com/questions/39398616/c-socket-atomic-non-blocking-read
// - https://stackoverflow.com/questions/6567742/passing-an-array-as-an-argument-to-a-function-in-c
// - https://stackoverflow.com/questions/29251723/how-to-safely-close-a-thread-which-has-a-infinite-loop-in-it
// - https://stackoverflow.com/questions/2229498/passing-by-reference-in-c
// - https://stackoverflow.com/questions/33404478/initialization-makes-pointer-from-integer-without-a-cast-c
// - https://stackoverflow.com/questions/9781373/a-try-catch-method-in-while-loop
// - https://stackoverflow.com/questions/26883698/accessing-the-elements-of-a-cha
// - https://stackoverflow.com/questions/16117322/switch-statement-within-while-loop-in-c
// - https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#copyOfRange%28byte%5B%5D,%20int,%20int%29
// - https://stackoverflow.com/questions/18367539/slicing-byte-arrays-in-java