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
// - Added menu for server data transport. Looking into threading to handle server responses


#include <math.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <stdbool.h>

// Threading lib
// https://www.geeksforgeeks.org/c/multithreading-in-c/
#include <pthread.h>





/** NOTE: For compilation, you must include the -lm flag to include math.h binaries.
     e.g. gcc -o client client.c -lnsl -lm 
*/



// Function prototypes
uint16_t parse_port_number_from_args(char *);
void* listen_for_server_changes(int *);
void send_msg(int *);
void* user_menu(bool*, char [25], int *);






/**
    Struct for user information
*/
struct UserInfo {
    char name[25];  

};






int main(int argc, char *argv[])
{
    int sock; // Socket
    struct sockaddr_in address; // Struct that holds IP address data
    char buffer[1024]; // The buffer to recieve data 
    uint16_t port; // Port value
    int reciever; // Variable that holds the server response data



    sock = socket(AF_INET, SOCK_STREAM, 0); 


    //port = parse_port_number_from_args(argv[1]);
    port = 4008;

    printf("%d", port);


    address.sin_family = AF_INET; // Specify the AF_INET type
    address.sin_port = htons(port); // (According to Google) htons converts a 16 bit number to "network byte order". In this case the port num
    
    //printf("%d", htons(port));
    
    inet_pton(AF_INET, "127.0.0.1", &address.sin_addr); // inet_pton coverts multiple components to a valid IP address

    printf("What is your student #?");

    char student_num[25];

    fgets(student_num, sizeof(student_num), stdin);
    
    

    printf("\nConnecting to IP 127.0.0.1 on port %d \n", port);

    
    
    
    connect(sock, (struct sockaddr *)&address, sizeof(address)); // Connect to the server

    printf("Connected. \nWelcome %s", student_num);
    
    bool* user_exit = false;
    
    pthread_t thread1;
    pthread_t thread2; 


    pthread_create(&thread1, NULL, user_menu(user_exit, student_num, &sock), NULL);
    pthread_create(&thread2, NULL, listen_for_server_changes(&sock), NULL);

    pthread_join(thread1, NULL);



    user_menu(user_exit, student_num, &sock);
    
    close(sock);

    printf("Connection to 127.0.0.1 on %d closed. Goodbye! \n", port);

    return 0;
}


uint16_t parse_port_number_from_args(char *arg) {

    uint16_t port; 

    //printf("Getting there. Value: %p", arg[1]);

     // Getting the port from user input required some outside research. Here are my sources:
    // - https://www.geeksforgeeks.org/cpp/command-line-arguments-in-c-cpp/
    // - https://stackoverflow.com/questions/5029840/convert-char-to-int-in-c-and-c
    // - https://stackoverflow.com/questions/213042/how-do-you-do-exponentiation-in-c
    // - https://stackoverflow.com/questions/11336477/gcc-will-not-properly-include-math-h

    for(int i=0; i < sizeof(arg) - 4; i++) {

        //printf("%f \n", (((int)argv[1][i] - (int)'0') * pow((double)10, (double)(3 - i))));

        port += (long)(((int)arg[i] - (int)'0') * pow((double)10, (double)(3 - i))); // This separates the port values (e.g. 4008 = 4000 + 000 + 00 + 8)
        
        // (int)argv[1][1] - (int)'0': Since command line arguments are char[], we need to loop over each character in
        //                             argv[i], get its ASCII value, then subtract it from the ASCII value of '0'. This 
        //                             will return the integer value (e.g. '4' -> 4)

    };

    port += 1; // I have no idea why this works but it does. Probably some de-referencing quirk. 


    printf("%d", port);
    return port;
}


void send_msg(int* sock) {
    send(*sock, "Hi there", sizeof("Hi there"), 0);
}


void user_menu(bool* user_exit, char student_num[25], int* socket) {

    while(user_exit == false) {
        char user_option;

        printf("\nMenu\n---------------\n");
        printf("1. (S)end a message to the server\n2. (E)xit the program");
        printf("\nWhat command would you like to perform #%s?\n", student_num);

        scanf("%c", &user_option);

        switch(user_option) {
            case 'S':
                printf("Hi");
                send_msg(socket);
            case 'E':
                printf("exit");
                *user_exit = true;
            default: 
                printf("Invalid. Try again");
            }
        }
    
    user_menu(user_exit, *student_num, socket);      
}



void* listen_for_server_changes(*int socket) {
    char buf[1024];

    while(true) {
        recv(socket, buf, sizeof(buf) - 1, 0 ); 
    }
}





// // printf("You said: %s \n. It is size of: %ld", uinput, sizeof(uinput));
    
    
//     // send(sock, uinput, sizeof(uinput), 0);
    
//     // printf("\nSent");
    
//     // reciever = recv(sock, buffer, sizeof(buffer) - 1, 0); // Get the recieved data and store it
//     // buffer[reciever] = '\0';
    
    
//     // printf("%s", buffer); //print out the buffer data
    
    
    
//     /**
//     TODO: Finish constant listener for c client; communciate about constant listener architecture:
//     - Client will stay alive via while loop and listen for changes communicated by the server
//     - Server will handle multiple connections and "broadcast" canvas changes to other clients
//     - Threading for server handling multiple clients? Will discuss 
//     */
    
//     // Constant server listener; As long as there is a valid connection between the client and server, the
//     // client will listen for canvas changes broadcasted by the server.
//     //char uinput[1024];
//     char uinput[1024];
    
    
//     //https://www.geeksforgeeks.org/c/socket-programming-cc/
    
//     fgets(uinput, sizeof(uinput), stdin);
//     printf("%s", uinput);

//     send(sock, uinput, sizeof(uinput), 0);

//      // close the socket