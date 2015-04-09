#!/bin/sh

java main.AccountServerImpl 8000 0 1 &
java main.PeerCommunicationImpl 8000 0 1 & 
sleep 20
