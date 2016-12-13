all: out
	javac -Xlint -sourcepath src -d out src/TodoRest.java src/TestSuite.java

run: all
	java -cp out -server -XX:+UseNUMA -XX:+UseParallelGC -XX:+AggressiveOpts TodoRest

test: all
	java -cp out TestSuite

out:
	mkdir out
