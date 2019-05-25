### Boolean Retrieval - Lucene - Java - Information Retrieval

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
