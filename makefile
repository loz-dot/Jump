all: compile run

compile: 
	javac -d . *.java

run: compile
	java timer.Main

clean: 
	rm timer/*.class