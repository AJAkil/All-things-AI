import os

files = os.listdir('.')

for f in files:
    if os.path.isfile(os.path.join('.\\',f)):
        if os.path.splitext(os.path.join('.\\',f))[1] == '.sol':
            print(f)
            os.remove(os.path.join('.\\',f))