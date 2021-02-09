# DirectDebug: Automated Testing and Debugging of Feature Models

This repository shows the implementation and the evaluation of the DirectDebug algorithm, 
which will be presented at the ICSE-NIER 2021 in the paper titled 
*DirectDebug: Automated Testing and Debugging of Feature Models*. 
The implementation or the application in this repository can be used to fully reproduce the work described in our paper.

DirectDebug is a direct diagnosis approach to the automated testing and debugging of variability models. 
The algorithm supports software engineers in the identification of minimal sets of faulty constraints 
that are responsible for an unintended behavior of a feature model. This approach has the potential 
to significantly decrease development and maintenance efforts for feature models.

## Evaluation process

The DirectDebug evaluation process consists of five steps as follows:

### Step 1 - Feature models generation

We can synthesize randomly feature models using [Betty framework](https://www.isa.us.es/betty/welcome). 
Then the generated feature models were selected on the basis of the number of Choco constraints.
The numbers of Choco constraints of selected feature models are 10, 20, 50, 100, 500, and 1000.
The folder *./data/fms* stores 18 feature models which were selected to the evaluation.
The first number in the filename represents the number of Choco constraints. 

### Step 2 - Test suite generation

After selecting feature models, we generated a test suite for each feature model.
Each test suite consists of 5 types of test cases: dead features, false optional, full mandatory, false mandatory,
and partial configuration. The folder *./data/testsuite* stores 18 test suites for 18 selected feature models.

### Step 3 - Test cases classification

In this step, test cases of each test suite will be classified into violated test cases,
or non-violated test cases. The folder *./data/classifiedTS* stores 18 classified test suites.
   
### Step 4 - Test cases selection

For each selected feature model, we select 21 scenarios for 7 cardinalities of test cases.
The cardinalities of test cases are 5, 10, 25, 50, 100, 250, 500.
The folder *./data/testcases* stores 378 selected scenarios.
   
### Step 5 - DirectDebug evaluation

The input of the final step is 18 feature models and 378 selected scenarios of test cases. 
The output is a table in which each entry represents the average diagnosis computing time derived from 3 repetitions
(see Table III in [1]).

## How to reproduce the experiment

### Get your own copy to run offline

#### Prerequisites



### dd.jar

**dd.jar** is a standalone Java application that encapsulates all evaluation steps in one program. The latest version of Java (Java version 8 update 270 or later) is necessary to execute **dd.jar**.

**dd.jar** provides 6 functionalities as follows:

#### Feature models generation

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

#### Feature model statistics

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

#### Test suite generation

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

#### Test cases classification

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

#### Test cases selection

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

#### DirectDebug evaluation

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

## References

[1] Viet-Man Le et al., 2021. DirectDebug: Automated Testing and Debugging of Feature Models. In Proceedings of the 43rd International Conference on Software Engineering: New Ideas and Emerging Results (ICSE-NIER 2021). (to appear)
