//Author:  Prof. Shimkanon
//Purpose: Quote-of-the-Day client program,
//         retrieving quotes via TCP

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

int main(void)
{
    int s;
    struct sockaddr_in addr;
    char buf[1024];
    int n;

    s = socket(AF_INET, SOCK_STREAM, 0);

    addr.sin_family = AF_INET;
    addr.sin_port = htons(4008);
    inet_pton(AF_INET, "127.0.0.1", &addr.sin_addr);

    connect(s, (struct sockaddr *)&addr, sizeof(addr));

    n = recv(s, buf, sizeof(buf) - 1, 0);
    buf[n] = '\0';

    printf("%s", buf);

    close(s);
    return 0;
}

//1: Create a socket
//2: Create a structure to hold the quote of the day
//3: Configure our socket
//4: Connect our socket to the server
//5: Receive the QOTD
//6: Print out QOTD

