import os
import random

JAVA_RESOURCES_PATH = "src/main/resources"

if not os.path.exists(JAVA_RESOURCES_PATH):
	os.makedirs(JAVA_RESOURCES_PATH)


for i in range(10000):
	with open(JAVA_RESOURCES_PATH + "/data.csv", "a") as f:
		f.write(str(random.randint(-1000, 1000)) + "\n")
