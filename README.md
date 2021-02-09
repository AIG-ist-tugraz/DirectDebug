# DirectDebug: Automated Testing and Debugging of Feature Models

This repository shows the implementation and the evaluation of the DirectDebug algorithm, 
which will be presented at the ICSE-NIER 2021 in the paper titled 
*DirectDebug: Automated Testing and Debugging of Feature Models*. 
The implementation and the application in this repository can be used to fully reproduce the work described in our paper.

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

### Use the standalone Java application

We provide a standalone Java application naming **dd.jar** that encapsulates all evaluation steps in one program.

**dd.jar** is available from the [latest release](https://github.com/AIG-ist-tugraz/DirectDebug/releases/tag/v1.0).

### Get your own copy to run offline

#### Prerequisites

The program depends on [Betty framework](https://www.isa.us.es/betty/welcome), and ["fmapi" library](http://gsd.uwaterloo.ca/).

## References

[1] Viet-Man Le et al., 2021. DirectDebug: Automated Testing and Debugging of Feature Models. In Proceedings of the 43rd International Conference on Software Engineering: New Ideas and Emerging Results (ICSE-NIER 2021). (to appear)
