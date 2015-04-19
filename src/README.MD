#Total Ordered Distributed Server


##PURPOSE:

    Simple total ordering distributed server based on state machine model



##INSTRUCTION:

    Direct to src folder, run ./make to re-compile the source code.

    Run ./make registry to register port 8000 that we set by default

    Direct to the main folder, manually type in:

    java AccountServerImpl.class [serverID] [path_to_configure_file]

    java ClientB.class [path_to_configure_file]

    ** where path_to_configure_file is the path to the configure file incldeing server information, and the server ID is the destinated ID for each process.

##MAKE && TEST:

	Makefile contains several testing cases.

	After makefile and register the RMI server

	run ./make test_server_[one/three/five] in terminal

	run ./make test_client_[one/three/five] in terminal

	** num of server and client should be the same


##PERFORMANCE TESTING RESULT:

    the cal.py(running on modified client_log file) would calculate the average response gap of the 1, 3, 5 servers are respectively 0, 1 ,2 in millionsecond.


##KNOWN BUGS:

	The halt does not gracefully shut down the server, it throws exception to the screen.


