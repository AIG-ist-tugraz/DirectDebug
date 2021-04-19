#!/bin/bash
# Run new DirectDebug evaluation
# NOTE: this script doesn't run well 
# since the feature model generation functionality of
# Betty usually raise a time out error.
# Author: Viet-Man Le, vietman.le@ist.tugraz.at
echo "DirectDebug Evaluation"
echo "This shell script will compile the program, and run the DirectDebug evaluation with the dataset used on the paper entitled DirectDebug: Automated Testing and Debugging of Feature Models."
echo ""

echo "Compiling the source files"
javac -d classes-target -cp lib/\* @classes

echo "Packaging the source files"
jar cfm d2bug_eval.jar manifest.txt -C classes-target .

# A time out error is usually come out here !!!
echo "Feature model generation"
java -jar d2bug_eval.jar -g conf1.1.txt

echo "Testsuite generation"
java -jar d2bug_eval.jar -ts conf1.2.txt

echo "Test case classification"
java -jar d2bug_eval.jar -tc conf1.2.txt

echo "Scenario selection"
java -jar d2bug_eval.jar -ss conf1.2.txt

echo "DirectDebug evaluation"
java -jar d2bug_eval.jar -e conf1.2.txt