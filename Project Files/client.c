// Author: Ben Maigur
//
// A C network client adapted from the qotdClient.c class example 
//
// 




#include <math.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>




int main(int argc, char *argv[])
{
    int sock; // Socket
    struct sockaddr_in address; // Struct that holds IP address data
    char buffer[1024]; // The buffer to recieve data 
    uint16_t port; // Port value
    int reciever; // Variable that holds the server response data

    sock = socket(AF_INET, SOCK_STREAM, 0); 


    // Getting the port from user input required some outside research. Here are my sources:
    // - https://www.geeksforgeeks.org/cpp/command-line-arguments-in-c-cpp/
    // - https://stackoverflow.com/questions/5029840/convert-char-to-int-in-c-and-c
    // - https://stackoverflow.com/questions/213042/how-do-you-do-exponentiation-in-c
    // - https://stackoverflow.com/questions/11336477/gcc-will-not-properly-include-math-h

    for(int i=0; i < sizeof(argv[1]) - 4; i++) {

        //printf("%f \n", (((int)argv[1][i] - (int)'0') * pow((double)10, (double)(3 - i))));

        port += (long)(((int)argv[1][i] - (int)'0') * pow((double)10, (double)(3 - i))); // This separates the port values (e.g. 4008 = 4000 + 000 + 00 + 8)
        
        // (int)argv[1][1] - (int)'0': Since command line arguments are char[], we need to loop over each character in
        //                             argv[i], get its ASCII value, then subtract it from the ASCII value of '0'. This 
        //                             will return the integer value (e.g. '4' -> 4)

    };

    //printf("%d", port);


    address.sin_family = AF_INET; // Specify the AF_INET type
    address.sin_port = htons(port); // (According to Google) htons converts a 16 bit number to "network byte order". In this case the port num
   
    //printf("%d", htons(port));
   
    inet_pton(AF_INET, "127.0.0.1", &address.sin_addr); // inet_pton coverts multiple components to a valid IP address

    printf("\nConnecting to IP 127.0.0.1 on port %d \n", port);

    connect(sock, (struct sockaddr *)&address, sizeof(address)); // Connect to the server

    
    reciever = recv(sock, buffer, sizeof(buffer) - 1, 0); // Get the recieved data and store it
    buffer[reciever] = '\0';
    
    printf("\nConnected to IP 127.0.0.1 on port %d \n", port);
    
    printf("%s", buffer); //print out the buffer data

    close(sock); // close the socket

    printf("Connection to 127.0.0.1 on %d closed. Goodbye! \n", port);

    return 0;
}
