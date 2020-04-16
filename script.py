from glob import glob
from pprint import pprint
import os

pprint(glob('./src/main/java/com/ramcharans/chipotle/*/*'))

base_dirs = ['controller', 'dao', 'service']

dirs = ['./src/test/java/com/ramcharans/chipotle/transaction/' + bd for bd in base_dirs]

for d in dirs:
	os.mkdir(d)
