# d2bug_eval.jar guideline

**d2bug_eval.jar** is a standalone Java application that encapsulates all evaluation steps in one program. 
The latest version of Java (Java version 8 update 270 or later) is necessary to execute **d2bug_eval.jar**.

**d2bug_eval.jar** provides 6 functionalities as follows:

### Feature models generation

**Syntax**:
```
java -jar d2bug_eval.jar -g <configuration file>
```

*The configuration file needs the following parameters*

| *parameters* | *description* |
| ----------- | ----------- |
| ```numGenFM``` | the number of feature models to be generated |
| ```CF``` | a list of the numbers of constraints |CF| |
| ```CTC``` | the ratio of cross-tree constraints which generated feature models has to be attained |
| ```resultsPath``` | the path to the folder where generated feature models will be saved |

**Configuration file example**:
```
CF: 10,20,50,100,500,1000
numGenFM: 3
CTC: 0.4
resultsPath: ./results/
```

### Feature model statistics

**Syntax**:
```
java -jar d2bug_eval.jar -st <configuration file>
```

*The configuration file needs the following parameters*

| *parameters* | *description* |
| ----------- | ----------- |
| ```dataPath``` | the folder where feature models locate |
| ```resultsPath``` | the path to the save file |

### Test suite generation

**Syntax**:
```
java -jar d2bug_eval.jar -ts <configuration file>
```
*The configuration file needs the following parameters*

| *parameters* | *description* |
| ----------- | ----------- |
| ```dataPath``` | the folder where feature models locate |
| ```resultsPath``` | the folder where generated testsuites will be saved |

### Test cases classification

**Syntax**:
```
java -jar d2bug_eval.jar -tc <configuration file>
```

*The configuration file needs the following parameters*

| *parameters* | *description* |
| ----------- | ----------- |
| ```numGenFM``` | the number of feature models to be generated |
| ```CF``` | a list of the numbers of constraints |CF| |
| ```dataPath``` | the folder where feature models and testsuites locate |
| ```resultsPath``` | the folder where classified testsuites will be saved |

### Test cases selection / scenario selection

**Syntax**:
```
java -jar d2bug_eval.jar -ss <configuration file>
```

*The configuration file needs the following parameters*

| *parameters* | *description* |
| ----------- | ----------- |
| ```numGenFM``` | the number of feature models to be generated |
| ```CF``` | a list of the numbers of constraints |CF| |
| ```TS``` | a list of the numbers of test cases |Tπ| |
| ```numIter``` | the number of iterations |iter| |
| ```perViolated_nonViolated``` | the percentage of violated test cases to non-violated test cases |
| ```dataPath``` | the folder where classified testsuites locate |
| ```resultsPath``` | the folder where scenarios will be saved |

### DirectDebug evaluation

**Syntax**:
```
java -jar d2bug_eval.jar -e <configuration file>
```

*The configuration file needs the following parameters*

| *parameters* | *description* |
| ----------- | ----------- |
| ```showEvaluation``` | **true** |
| ```showDebug``` | **true** |
| ```CF``` | a list of the numbers of constraints |CF| |
| ```TS``` | a list of the numbers of test cases |Tπ| |
| ```numGenFM``` | the number of feature models to be generated |
| ```numIter``` | the number of iterations |iter| |
| ```dataPath``` | the folder where feature models and scenarios locate |
| ```resultsPath``` | the folder where the *results.txt* file will be saved |

### Construct Configuration files

The software package supports a wide range of parameters as follows:

- ```showEvaluation```: (**false** by default) determines whether the program prints out the results to the console and the *results.txt* file.
- ```showDebug```: (**false** by default) determines whether the program prints out the information messages to the console and the *results.txt* file.
- ```CF```: (**10,20,50,100,500,1000** by default) a list of the numbers of constraints |CF|.
- ```TC```: (**5,10,25,50,100,250,500** by default) a list of the numbers of test cases |Tπ|.
- ```numGenFM```: (**3** by default) the number of feature model generated for each number of constraints |CF|.
- ```CTC```: (**0.4** by default) the ratio of cross-tree constraints which generated feature models has to be attained.
- ```numIter```: (**3** by default) the number of iterations |iter|.
- ```perViolated_nonViolated```: (**0.3** by default) the percentage of violated test cases to non-violated test cases.
- ```dataPath```: (**./data/** by default) the folder where the dataset is stored.
- ```resultsPath```: (**./results/** by default) the folder where the results will be saved.

For further details on configuring these parameters, we refer to three example configuration files, **confForPaper.txt**, **conf1.1.txt**, **conf1.2.txt**. **confForPaper.txt** is used by **run.sh**, and two remaining files are used by **run_all.sh**.
