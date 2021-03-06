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
Thereafter, the generated feature models were selected on the basis of the number of Choco constraints 
and the ratio of cross-tree constraints.
The numbers of Choco constraints of selected feature models are 10, 20, 50, 100, 500, and 1000.
The folder *./data/fms* stores 18 feature models which were selected for the evaluation.
The number in the middle of the filename represents the number of Choco constraints. For instance, FM_10_2 means that the number of Choco constraints is 10.

### Step 2 - Test suite generation

After selecting feature models, we generated a test suite for each feature model.
Each test suite consists of 5 types of test cases: dead features, false optional, full mandatory, false mandatory,
and partial configuration. The folder *./data/testsuite* stores 18 test suites for 18 selected feature models.

### Step 3 - Test cases classification

Test cases of each test suite will be classified into violated test cases,
or non-violated test cases. The folder *./data/classifiedTS* stores 18 classified test suites.

### Step 4 - Test cases selection

For each selected feature model, we selected 21 scenarios for 7 cardinalities of test cases.
The cardinalities of test cases are 5, 10, 25, 50, 100, 250, 500.
The folder *./data/testcases* stores 378 selected scenarios.

### Step 5 - DirectDebug evaluation

The input of this final step is 18 feature models and 378 selected scenarios of test cases. 
The output is a table in which each entry represents the average diagnosis computing time derived from 3 repetitions
(see Table III in [1]).

## Implementation

This implementation supports the evaluation process via **six** sub-programs which
can be triggered by command line arguments.

| *arguments* | *description* |
| ----------- | ----------- |
| ```-g``` | feature models generation |
| ```-s``` | feature model statistics |
| ```-ts -g``` | test suite generation |
| ```-tc -c``` | test cases classification |
| ```-tc -s``` | test cases selection |
| ```-e``` | DirectDebug evaluation |

## How to reproduce the experiment

### Use the standalone Java application

We published a standalone Java application naming **dd.jar** that encapsulates the evaluation steps in one program.

**dd.jar** is available from the [latest release](https://github.com/AIG-ist-tugraz/DirectDebug/releases/tag/v1.0).
For further details of this app, we refer to [dd.jar guideline](https://github.com/AIG-ist-tugraz/DirectDebug/blob/main/dd.jar.md).

### Get your own copy to run offline

Some part of our implementation depends on [Betty framework](https://www.isa.us.es/betty/welcome) 
and ["fmapi" library](http://gsd.uwaterloo.ca/). Thus, after cloning the source code into your system, 
you need to run 7 Maven configurations, which already included in the project. If you use the IntelliJ IDE,
then open Maven window (*View/Tool Windows/Maven*) and double-click on 7 configurations in the Run Configurations section.

### Construct Configuration files

Three sub-programs (Test cases classification, Test cases selection, and DirectDebug evaluation) use configuration files
(in a CSV format) as an input of the sub-programs.

For **Test cases classification and Test cases selection**, a configuration file is required (see *./data/configurations.csv*)
which has the following four values for each record:

- The path to a feature model file
- The path to the corresponding testsuite file of the feature model
- The path to the corresponding classified testsuite file of the feature model
- The path where selected test cases will be stored

For **DirectDebug evaluation**, some configuration files are needed (see *./data/conf*).
First, a main configuration file (see *./data/conf/conf.csv*) has 6 records corresponding to 
6 cardinalities of constraints. For each record, there are 7 values representing 7 other configuration files,
that defines scenarios of test cases.

Second, there are 7 configuration files in each sub-folder which defines scenarios of test cases.
For each record in those files, there are four values as follows:
- The first value is the path to a feature model file.
- Three remaining values represent the paths to three scenarios of test cases.

## References

[1] Viet-Man Le, Alexander Felfernig et al., 2021. DirectDebug: Automated Testing and Debugging of Feature Models. In Proceedings of the 43rd International Conference on Software Engineering: New Ideas and Emerging Results (ICSE-NIER 2021). (to appear) [[PDF] from arxiv.org](https://arxiv.org/pdf/2102.05949.pdf)
