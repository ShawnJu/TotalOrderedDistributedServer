arrayreq = []
arrayrsp = []

with open("client_log.txt", "r") as ins:
    array = []
    for line in ins:
    	if line.split()[1] == "REQ":
        	arrayreq.append(line.split()[2])
        if line.split()[1] == "RSP":
        	arrayrsp.append(line.split()[2])

arrayreq.sort()
for index in range(len(arrayreq)):
	print(arrayreq[index])