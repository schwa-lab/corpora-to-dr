.PHONY: all clean compile run-to-docrep run-to-uima


all: compile

clean:
	mvn clean

compile:
	mvn compile

run-to-docrep: compile
	env MAVEN_OPTS="-ea" mvn exec:java -Dexec.mainClass=ontonotes5.main.RunCPE -Dexec.args="desc/ToDocrepCPE.xml"

run-to-uima: compile
	env MAVEN_OPTS="-ea" mvn exec:java -Dexec.mainClass=ontonotes5.main.RunCPE -Dexec.args="desc/ToUimaCPE.xml"
