all: out
	javac -Xlint src/*.java -d out

run: all
	java -cp out -server -XX:+UseNUMA -XX:+UseParallelGC -XX:+AggressiveOpts TodoRest

test: all
	java -cp out TestSuite

out:
	mkdir out
