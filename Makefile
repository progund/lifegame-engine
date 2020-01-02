LIFE_CLI=se.juneday.lifegame.test.LifeCli
VERIFY=se.juneday.lifegame.verification.LifeVerifier
ORG_JSON=lib/org.json.jar
CLASSPATH=.:src:$(ORG_JSON)


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
  src/se/juneday/lifegame/test/TestExpression.java \
  src/se/juneday/lifegame/test/TestExpressionParser.java
JAVA_CLASSES=$(JAVA_FILES:.java=.class)

all: deps $(BIN_DIR)  $(JAVA_CLASSES)

$(BIN_DIR):
	mkdir -p $(BIN_DIR) 

cli: $(JAVA_CLASSES) deps
	java -cp $(CLASSPATH) $(LIFE_CLI)

swe: $(JAVA_CLASSES)
	java -cp $(CLASSPATH) $(LIFE_CLI) data/univ-game-swe.json

swe-game: $(JAVA_CLASSES)
	java -cp $(CLASSPATH) $(LIFE_CLI) data/univ-game-swe.json

sw-game: $(JAVA_CLASSES)
	java -cp $(CLASSPATH) $(LIFE_CLI) data/sw-development.json

swe-test-ok: $(JAVA_CLASSES)
	for i in 2 0 9 0 2 2 2 2 2 2 2 2 2 2 2 2 0 5 1 5 0 0 0 6 1 5 0 0 2 2 2 2 2 2 2 0 7 0 0 0 1 7 8 0 0 0; do printf "%s\n" $$i ; sleep 0.1 ; done | java -cp $(CLASSPATH) $(LIFE_CLI) data/univ-game-swe.json
 
swe-test-stuck: $(JAVA_CLASSES)
	for i in 2 0 9 0 2 2 2 2 2 2 2 2 2 2 2 2 0 5 1 5 0 0 0 6 1 5 0 0 2 2 2 2 2 2 2 0 7 0 0 1 2 0 0 0 0 1 1 1 1 1 0 0  ; do printf "%s\n" $$i ; sleep 0.1 ; done | java -cp $(CLASSPATH) $(LIFE_CLI) data/univ-game-swe.json
# 

swe-test: $(JAVA_CLASSES)
	printf "2\n0\n" | java -cp $(CLASSPATH) $(LIFE_CLI) data/univ-game-swe.json

verify: $(JAVA_CLASSES)
	java -cp $(CLASSPATH) $(VERIFY) data/univ-game-swe.json
	java -cp $(CLASSPATH) $(VERIFY) data/sw-development.json
	java -cp $(CLASSPATH) $(VERIFY) data/univ-game-en.json

situations: 
	@cat data/univ-game-swe.json | jq '.situations[].title'
	@echo -n "Situtions: " && cat data/univ-game-swe.json | jq '.situations[].title' | wc -l

autocli: $(JAVA_CLASSES)
	printf "0\n0\n0\n" | java -cp $(CLASSPATH) $(LIFE_CLI)

unit-test: $(JAVA_CLASSES)
	java -cp $(CLASSPATH) se.juneday.lifegame.test.TestParser
#	java -cp $(CLASSPATH) se.juneday.lifegame.test.TestExpressionParser
	@echo "---===[ Testing expressions ]===-----"
	java -cp $(CLASSPATH) se.juneday.lifegame.test.TestExpression

test: unit-test #swe-test1

.PHONY: doc

doc: doc/game-syntax.md 
	cd doc && pandoc game-syntax.md  -o game-syntax.pdf  

stat:
	ohcount -s

source-dist: $(JAVA_FILES)
	tar cvfz lifegame-engine.zip $(JAVA_FILES) doc

bin-dist: $(JAVA_CLASSES)
	cd src && jar cf ../lifegame-engine.jar .

dist: source-dist bin-dist

$(ORG_JSON):
	mkdir -p lib
	wget --no-check-certificate 'https://search.maven.org/remotecontent?filepath=org/json/json/20171018/json-20171018.jar' -O $@

deps: $(ORG_JSON)


clean:
	rm -f $(JAVA_CLASSES)
	find . -name "*~" | xargs rm -f
	find . -name "*.class" | xargs rm -f
