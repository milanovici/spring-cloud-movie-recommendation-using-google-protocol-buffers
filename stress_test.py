import requests
import time

def proto():
	for i in range(1, 50):
		requests.get("http://localhost:9000/api/proto/recommendation/user/" + str(i))
		#time.sleep(1)

def json():
	for i in range(1, 50):
		requests.get("http://localhost:9000/api/recommendation/user/" + str(i))
		#time.sleep(1)

starttime = time.time()
proto()
timetaken = time.time() - starttime
print('Proto : ' + str(timetaken))

starttime = time.time()
json()
timetaken = time.time() - starttime
print('Json : ' + str(timetaken))
