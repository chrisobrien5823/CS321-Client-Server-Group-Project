######################################
# Author: Chris O'Brien 
# Major: Computer Science
# Creation Date: March 31st, 2026
# Due Date: April 2nd, 2026
# Course: CS321
# Professor Name: Prof. Shimkanon
# Assignment: Project Phase #1
# Filename: makefile
# Purpose: These commands will
# compile files for the project
######################################

all: server client

server: Server.java
	javac Server.java

client: client.c
	gcc -o client client.c -lnsl -lm

clean:
	rm server client
