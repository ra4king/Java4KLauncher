INPUT = src/com/java4k/core/Game.java src/com/java4k/launcher/Java4KLauncher.java
MANIFEST = manifest.mf
DIR = bin/
OUTPUT = Java4KLauncher.jar

all:
	rm -rf $(DIR)
	mkdir $(DIR)
	javac -d $(DIR) $(INPUT)
	jar cvfm $(OUTPUT) $(MANIFEST) -C $(DIR) .
	rm -rf $(DIR)

run:
	java -jar $(OUTPUT) $(gamefile)

clean:
	rm -rf $(DIR) $(OUTPUT)
