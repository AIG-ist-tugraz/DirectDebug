#!/bin/bash
# Rerun the DirectDebug evaluation with the dataset used in the paper
# Author: Viet-Man Le, vietman.le@ist.tugraz.at
echo "DirectDebug Evaluation"
echo "This shell script will compile the program, and run the DirectDebug evaluation with the dataset used on the paper entitled DirectDebug: Automated Testing and Debugging of Feature Models."
echo ""

echo "Compiling the source files"
javac -d classes-target -cp lib/\* @classes

echo "Packaging the source files"
jar cfm d2bug_eval.jar manifest.txt -C classes-target .

echo "Run the DirectDebug evalution"
java -jar d2bug_eval.jar -e confForPaper.txt