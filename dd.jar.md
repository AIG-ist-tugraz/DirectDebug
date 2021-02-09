# dd.jar
**dd.jar** is a standalone Java application that encapsulates all evaluation steps in one program. The latest version of Java (Java version 8 update 270 or later) is necessary to execute **dd.jar**.

**dd.jar** provides 6 functionalities as follows:

## Feature models generation

Syntax:
```
java -jar dd.jar -g <#feature models> <#features> <%CTC> <#max products> <path to save>
```

where

| *parameters* | *description* |
| ----------- | ----------- |
| ```<#feature models>``` | the number of feature models to be generated |
| ```<#features>``` | the number of features in the feature model to be generated |
| ```<%CTC>``` | the percentage of cross-tree constraints |
| ```<#max products>``` | the max number of products of the feature model to be generated |
| ```<path to save>``` | the path to the folder where generated feature models will be saved |

Example:
```
java -jar dd.jar -g 2 500 40 10000 ./data/fms
``` 

## Feature model statistics

Syntax:
```
java -jar dd.jar -s <folder path> <output file path>
```

where

| *parameters* | *description* |
| ----------- | ----------- |
| ```<folder path>``` | the folder where feature models locate |
| ```<output file path>``` | the path to the save file |

Example:
```
java -jar dd.jar -s ./data/fms ./data/statisticsAll.re
```

## Test suite generation

Syntax:
```
java -jar dd.jar -ts -g <folder path> <path to save> <#max combinations> <randomly search>
```
where

| *parameters* | *description* |
| ----------- | ----------- |
| ```<folder path>``` | the folder where feature models locate |
| ```<path to save>``` | the folder where generated testsuites will be saved |
| ```<#max combinations>``` | the maximum number of combinations be selected when generating partial configurations |
|```<randomly search>```| true/false - if true, then the selection of combinations is random |

Ex:
```
java -jar dd.jar -ts -g ./data/fms ./data/testsuite 1000 false
```

## Test cases classification

Syntax:
```
java -jar dd.jar -tc -c <configuration file>
```

where

| *parameters* | *description* |
| ----------- | ----------- |
| ```<configuration file>``` | a configuration file |

Ex:
```
java -jar dd.jar -tc -c ./data/configurations.csv
```

## Test cases selection

Syntax:
```
java -jar dd.jar -tc -s <configuration file> <#scenarios> <%violated test cases>
```

where

| *parameters* | *description* |
| ----------- | ----------- |
| ```<configuration file>``` | a configuration file |
| ```<#scenarios>``` | the number of scenarios |
| ```<%violated test cases>``` | the percentage of violated test cases |

Ex:
```
java -jar dd.jar -tc -s ./data/configuration.csv 5 0.3
```

## DirectDebug evaluation

Syntax:
```
java -jar dd.jar -e <configuration file> <filesave>
```

where

| *parameters* | *description* |
| ----------- | ----------- |
| ```<configuration file>``` | a configuration file |
| ```<filesave>``` | the file to save the evaluation results |

Ex:
```
java -jar dd.jar -e ./data/conf/conf.csv ./data/results.txt
```