### Evaluation of IR models-

***Implementation Details -***

- The src folder contains the required configuration files i.e. schema.xml and solrconfig.xml for all three similarity classes. 
- Create a new core and use the above two files. 
- Query the Solr using the jtt.py presesnt in the json_to_trec folder. 
- This file uses googletrans to translate the tweets into all three languages i.e. russian, english and dutch and then query the solr by appending all of them. 
- This improves the recall as well as precision. 
- Also field boosting has been done using dismax query parser. 
- The results get stored in output.txt which can be used directly for trec evaluation. 
- Trec evaluation can be done for the 15 train_queries as the qrel.txt contains the manually recorded relevance for only those queries. 
- So check the MAP score for 15 queries and optimize the model. 

**This model gave an approximate of 0.775 average MAP score for all three similarity classes for the given queries.**

The three folders contains the results of 5 testqueries which cannot be evaluated as the qrel is not available for those 5 queries. 

Read the report for in depth explaination of the project. 
