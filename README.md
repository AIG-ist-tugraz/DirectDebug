# DirectDebug: Automated Testing and Debugging of Feature Models

This repository shows the implementation and the evaluation of the DirectDebug algorithm, 
which will be presented at the ICSE-NIER 2021 in the paper entitled 
*DirectDebug: Automated Testing and Debugging of Feature Models*. 
This repository can be exploited to fully reproduce the work described in our paper.

DirectDebug is a direct diagnosis approach to the automated testing and debugging of variability models. 
The algorithm supports software engineers in the identification of minimal sets of faulty constraints 
that are responsible for an unintended behavior of a feature model. This approach has the potential 
to significantly decrease development and maintenance efforts for feature models.

## Evaluation process

The DirectDebug evaluation process consists of the following five steps:

### Step 1 - Feature models generation

We first synthesized randomly feature models using [Betty framework](https://www.isa.us.es/betty/welcome). 
Thereafter, the generated feature models were automatically selected on the basis of the number of Choco constraints |CF|
and the ratio of cross-tree constraints.

In our paper, the numbers of Choco constraints of selected feature models are 10, 20, 50, 100, 500, and 1000.
The folder *./data/paper/fms* stores 18 feature models which were selected for the evaluation.
The number in the middle of the filename represents the number of Choco constraints. For instance, FM_10_2 means that the number of Choco constraints is 10.

### Step 2 - Test suite generation

After selecting feature models, we generated a test suite for each feature model.
Each test suite consists of 5 types of test cases: dead features, false optional, full mandatory, false mandatory,
and partial configuration. The folder *./data/paper/testsuite* stores 18 test suites for 18 selected feature models.

### Step 3 - Test cases classification

Test cases of each test suite will be classified into violated test cases,
or non-violated test cases. The folder *./data/paper/classifiedTS* stores 18 classified test suites.

### Step 4 - Test cases selection / Scenario selection

We selects test-case scenarios where the ratio of violated test cases to non-violated test cases is a specific number predetermined by the user. The number of scenarios is selected depending on the combination of the number of constraints |CF| and the number of test cases |Tπ|. For each combination, the average run-time will be calculated (in Step 5) when a specific number of iterations |iter| is reached.

In our paper, for each selected feature model, we selected 21 scenarios for 7 numbers of test cases. In total, there were 378 (3 feature models x 6 |CF| x 7 |Tπ| x 3 |iter|) selected test-case scenarios. The folder *./data/paper/testcases* stores 378 selected scenarios.

### Step 5 - DirectDebug evaluation

The program calculates the average run-time of DirectDebug.

In our paper, the input of this final step is 18 feature models and 378 selected scenarios of test cases. 
The output is a table in which each entry represents the average diagnosis computing time derived from 3 repetitions
(see Table III in [1]).

## Implementation

This software package supports the evaluation process via **six** sub-programs which
can be triggered by command line arguments.

| *arguments* | *description* |
| ----------- | ----------- |
| ```-g``` | feature models generation |
| ```-st``` | feature model statistics |
| ```-ts``` | test suite generation |
| ```-tc``` | test cases classification |
| ```-ss``` | scenarios selection |
| ```-e``` | DirectDebug evaluation |
| ```-h``` | help |

## How to reproduce the experiment

### Use the standalone Java application

We published a standalone Java application naming **d2bug_eval.jar** that encapsulates the evaluation steps in one program.

**d2bug_eval.jar** is available from the [latest release](https://github.com/AIG-ist-tugraz/DirectDebug/releases/tag/v1.1).
For further details of this app, we refer to [d2bug_eval.jar guideline](https://github.com/AIG-ist-tugraz/DirectDebug/blob/main/dd.jar.md).

### Get your own copy to run offline

#### Use a bash script

We provide two bash scripts that perform all necessary steps from *compiling the source code* to *running the DirectDebug evaluation process*. In particular, **run.sh** will compile the source files, package them in one *jar* file, and run the DirectDebug evaluation with the dataset used for the paper. Besides compiling and packaging the source files, **run_all.sh** will carry out all five steps of the DirectDebug evaluation process, and you will get the new dataset, new results.

To run these bash scripts on your system after cloning the source code:

1. First, you need to make the script executable with **chmod**:

```
$ chmod u+x run.sh
```

2. Run the script by prefixing it with ```./```:

```
$ ./run.sh
```

#### Build your own software

Some part of our implementation depends on [Betty framework](https://www.isa.us.es/betty/welcome) 
and ["fmapi" library](http://gsd.uwaterloo.ca/). Thus, after cloning the source code into your system, 
you need to run 7 Maven configurations, which already included in the project. If you use the IntelliJ IDE,
then open Maven window (*View/Tool Windows/Maven*) and double-click on 7 configurations in the Run Configurations section.

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

## Use the code source for your project

[An example of using DirectDebug algorithm for testing and debugging in feature models](https://github.com/AIG-ist-tugraz/DirectDebug/blob/main/src/test/java/at/tugraz/ist/ase/debugging/DirectDebugV1Test.java)

**d2bug_eval** consists of three sub-packages: **Feature Model**, **MBDiagLib**, and **Debugging**.  **Feature Model** reads feature model files and supports *feature model generation* and *feature model statistics*. **MBDiagLib** provides (1) an abstract model to hold variables and constraints, (2) an abstract consistency checker for underlying solvers, (3) a *Choco* consistency checker using [Choco Solver](https://choco-solver.org), and (4) functions to measure the performance of algorithms in terms of run-time or the number of solver calls. **Debugging** provides components w.r.t. test-cases management, the DirectDebug implementation, a debugging model with test-cases integration, and debugging-related applications (e.g. *test suite generation*, *test cases classification*, and *test case selection*).

Besides feature models encoded in the *SXFM* format and consistency checks conducted by [Choco Solver](https://choco-solver.org), **d2bug_eval** can be extended to support further formats (e.g., *FeatureIDE* format) and other off-the-shelf solvers. Furthermore, the program can be extended to evaluate other constraint-based algorithms, such as conflict detection algorithms and diagnosis identification algorithms.

## References

[1] Viet-Man Le, Alexander Felfernig et al., 2021. DirectDebug: Automated Testing and Debugging of Feature Models. In Proceedings of the 43rd International Conference on Software Engineering: New Ideas and Emerging Results (ICSE-NIER 2021). (to appear) [[PDF] from arxiv.org](https://arxiv.org/pdf/2102.05949.pdf)
