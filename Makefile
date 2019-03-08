LIFE_CLI=se.juneday.lifegame.test.LifeCli
VERIFY=se.juneday.lifegame.verification.LifeVerifier
CLASSPATH=.:src:lib/org.json.jar

%.class:%.java
	javac  -cp $(CLASSPATH) ./$<

JAVA_FILES= \
  src/se/juneday/lifegame/domain/*.java \
  src/se/juneday/lifegame/json/ExpressionParser.java \
  src/se/juneday/lifegame/json/JParser.java \
  src/se/juneday/lifegame/util/Log.java \
  src/se/juneday/lifegame/engine/LifeGameEngine.java \
  src/se/juneday/lifegame/verification/LifeVerifier.java \
  src/se/juneday/lifegame/test/LifeCli.java \
  src/se/juneday/lifegame/test/TestParser.java \
  src/se/juneday/lifegame/test/TestExpressionParser.java
JAVA_CLASSES=$(JAVA_FILES:.java=.class)

all: $(BIN_DIR)  $(JAVA_CLASSES)

$(BIN_DIR):
	mkdir -p $(BIN_DIR) 

cli: $(JAVA_CLASSES)
	java -cp $(CLASSPATH) $(LIFE_CLI)

swe: $(JAVA_CLASSES)
	java -cp $(CLASSPATH) $(LIFE_CLI) data/univ-swe.json

swe-game: $(JAVA_CLASSES)
	java -cp $(CLASSPATH) $(LIFE_CLI) data/univ-game-swe.json

swe-test1: $(JAVA_CLASSES)
	printf "2\n0\n1\n3\n0\n1\n0\n1\n0\n0\n" | java -cp $(CLASSPATH) $(LIFE_CLI) data/univ-swe.json

swe-test: $(JAVA_CLASSES)
	printf "2\n0\n" | java -cp $(CLASSPATH) $(LIFE_CLI) data/univ-swe.json

verify: $(JAVA_CLASSES)
	java -cp $(CLASSPATH) $(VERIFY) data/univ-swe.json

autocli: $(JAVA_CLASSES)
	printf "0\n0\n0\n" | java -cp $(CLASSPATH) $(LIFE_CLI)

clean:
	rm -f $(JAVA_CLASSES)
	find . -name "*~" | xargs rm -f
	find . -name "*.class" | xargs rm -f
