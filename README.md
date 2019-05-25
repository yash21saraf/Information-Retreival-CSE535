## Information Retrieval Projects -

All the projects have been performed as the part of course work for CSE535 Information Retreival. 

-----
### PART -1 - Setting up and data cleaning using SOLR for twitter data analysis

-----

#### Implementation Details - 


- Create a twitter developer account and obtain the API keys.
- Insert the API keys in the twitter_download.py and download tweets by changing the query terms 


- After downloading the query index the tweets in solr 
- Use the above provided conf folder in the schema mode. 
- Also download the jython jar and paste in the solr folder. 

- In the solrconfig required updateprocessor has been defined to make the required changes to the data. 

- And the fields are defined properly in schema.xml. 
- After indexing the solr can be queried for different terms. 

-----
### PART -2 - Boolean Retrieval - Lucene - Java - Information Retrieval

-----

- Using lucene to read index and create my own boolean query retrieval system. 

***There are two files in the project -*** 

- The main file is projclass which defines all the methods for obtaining the Inverted Index, TAAT and DAAT stratergies for AND and OR boolean operations. 
- The second file is just a class which implements the sorting of the postings lists. 

#### Implementation Details -

- The skip pointers has also been implemented for the AND boolean operations for both the stratergies. 

***Instructions to run the code:***

- Download core lucene library from the following link and import the library into the project before running the project. ([Index of /dist/lucene/java](http://archive.apache.org/dist/lucene/java/))

- For this project the 7.4.0 has been used. Make sure you use the same one as the lucene has issues with backward compatiblity. 

- Have the solr created index in a folder and provide the path for the index.
- Provide the output.txt and input.txt as arguments. 
- The input text file should contain all the terms white space seperated. 
- And queries seperated by a new line character. 

-----
### PART -3 - Evaluation of IR models

-----

For the task of Information retrieval many retrieval models are used, for example 

- Vector spce model 
- BM25 model
- Divergence from randomness model 

All models have very different approach to things. So the objective of the project is to analyze the performance of each model on solr and optimize the performance by using several techniques. 

The techniques used are query boosting, query translation, synonym classes, parameter adjustment for different models, etc

All the results have been tabulated and described in the report.# Information-Retreival-CSE535
