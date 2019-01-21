LIFE_CLI=se.juneday.lifegame.test.LifeCli
CLASSPATH=.:src:lib/org.json.jar

%.class:%.java
	javac  -cp $(CLASSPATH) ./$<

JAVA_FILES= \
  src/se/juneday/lifegame/domain/*.java \
  src/se/juneday/lifegame/json/ExpressionParser.java \
  src/se/juneday/lifegame/json/JParser.java \
  src/se/juneday/lifegame/util/Log.java \
  src/se/juneday/lifegame/engine/LifeGameEngine.java \
  src/se/juneday/lifegame/test/LifeCli.java \
  src/se/juneday/lifegame/test/TestParser.java \
  src/se/juneday/lifegame/test/TestExpressionParser.java
JAVA_CLASSES=$(JAVA_FILES:.java=.class)

all: $(BIN_DIR)  $(JAVA_CLASSES)

$(BIN_DIR):
	mkdir -p $(BIN_DIR) 

cli:
	java -cp $(CLASSPATH) $(LIFE_CLI)

clean:
	rm -f $(JAVA_CLASSES)
	find . -name "*~" | xargs rm -f
