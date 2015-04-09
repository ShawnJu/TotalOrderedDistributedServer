#!/bin/sh

java main.AccountServerImpl 8000 0 2 &
java main.AccountServerImpl 8001 1 2 &
sleep 20
pkill java
