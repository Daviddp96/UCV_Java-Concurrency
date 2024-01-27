JAVAC = javac
JAVA = java
MAIN_CLASS = Main

.PHONY: all clean

all: $(MAIN_CLASS).class

$(MAIN_CLASS).class: $(MAIN_CLASS).java
	$(JAVAC) $<

run: all
	$(JAVA) $(MAIN_CLASS)

clean:
	rm -f $(MAIN_CLASS).class