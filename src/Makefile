all: AccountServer clientB

clientB: 
	javac main/ClientB.java

registry:
	rmiregistry 8000&

AccountServer: 
	javac -g main/AccountServerImpl.java 

test_client_one:
	java main.ClientB configuration1

test_client_three:
	java main.ClientB configuration2

test_client_five:
	java main.ClientB configuration3

test_server_one:
	java main.AccountServerImpl 0 configuration1 &

test_server_three:	
	java main.AccountServerImpl 0 configuration2 &
	java main.AccountServerImpl 1 configuration2 &
	java main.AccountServerImpl 2 configuration2 &

test_server_five:
	java main.AccountServerImpl 0 configuration3 &
	java main.AccountServerImpl 1 configuration3 &
	java main.AccountServerImpl 2 configuration3 &
	java main.AccountServerImpl 3 configuration3 &
	java main.AccountServerImpl 4 configuration3 &


clean:
	rm -rf main/*.class *.txt 
