# DirectDebug
 DirectDebug: Automated Testing and Debugging of Feature Models

Viet-Man Le et al., 2021. DirectDebug: Automated Testing and Debugging of Feature Models. In Proceedings of the 43rd International Conference on Software Engineering: New Ideas and Emerging Results (ICSE-NIER 2021). (to appear)

## Features

### Feature model generation

Syntax:
dd -g <#feature models> <#features> <rate of CTC> <max products> <path to save>

Ex:
dd -g 2 500 40 10000 ./data/fms

### Calculates the statistics for feature models

Syntax:
dd -s <folder path> <output file path>

Ex:
dd -s ./data/fms ./data/statisticsAll.re

### Testsuite generation

Syntax:
dd -ts -g <folder path> <path to save> <#max combinations> <randomly search>

Ex:
dd -ts -g ./data/fms ./data/testsuite 1000 false

### Test cases classification

Syntax:
dd -tc -c <configuration file>

Ex:
dd -tc -c ./data/configurations.csv

### Test cases selection

Syntax:
dd -tc -s <configuration file> <#scenarios> <%violated test cases>

Ex:
dd -tc -s ./data/configuration.csv 5 0.3

### DirectDebug evaluation

Syntax:
dd -e <configuration file> <filesave>

Ex:
dd -e ./data/conf/conf.csv ./data/results.txt