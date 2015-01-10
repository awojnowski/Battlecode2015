import os
import time

name = "team170";

timestamp = str(int(time.time()))
new_name = raw_input("Enter the new name for this saved bot: ");

os.system("cp -R " + name + " " + new_name);
os.system("find " + new_name + "/* -name \"*.java\" -print0 | xargs -0 sed -i '' -e 's/" + name + "/" + new_name + "/g'");