package luceneproj;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class projclass {
	public static String indexPath="index";
	public static String outputfile = "output.txt";
	public static String inputfile = "input.txt";
///////// C:\Users\SARAF\Desktop\index output.txt input.txt /////////////
	public static void main(String[] args) throws IOException {
	
	indexPath = args[0];
	outputfile = args[1];
	inputfile = args[2];
	String key = null ;
		
		// Intermediate writing files for debugging
//	    File fou = new File("out.txt");
//	    FileOutputStream fo = new FileOutputStream(fou);
//	    BufferedWriter bww = new BufferedWriter(new OutputStreamWriter(fo));
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////CREATING INVERTED INDEX///////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		IndexReader reader = DirectoryReader.open(FSDirectory.open(FileSystems.getDefault().getPath(indexPath)));
		Fields fields = MultiFields.getFields (reader);
		Map<String, LinkedList<Integer>> dictionary = new HashMap<>();
		Iterator<String> iterator = fields.iterator();
		String field = iterator.next();
        while(iterator.hasNext()) {
            field = iterator.next();
				Terms terms = fields.terms (field);
				TermsEnum termsEnum = terms.iterator();
				while (termsEnum.next() != null) {
					BytesRef re = termsEnum.term();
					PostingsEnum postings = null;
					postings = termsEnum.postings(postings);
					LinkedList<Integer> postinglist = new LinkedList<Integer>();
					for (int docID = postings.nextDoc(); docID != DocIdSetIterator.NO_MORE_DOCS; docID = postings.nextDoc())
					{
						postinglist.add(Integer.valueOf(docID));
					}
					key = re.utf8ToString() ;
				    dictionary.put(key,postinglist);	
				}	
		}
		reader.close();

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////READING QUERY LINE BY LINE////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputfile), "UTF8"));
		    String line;
		    /////Defining the output buffer 
		    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfile), "UTF8"));
		    
		    while ((line = br.readLine()) != null) {
		       String[] query = line.split(" ");
		       query = removeElement(query, "") ; 
		       if(query.length == 1 && !query[0].equals("")) {
			       printpostings(query, bw, dictionary) ;   
		       }
		       if(query.length > 1) {
		    	   printpostings(query, bw, dictionary) ;
		    	   taat(query, bw, dictionary) ;
		    	   DaatAnd(query, bw, dictionary) ;
		    	   DaatOr(query, bw, dictionary) ;
		       }
		    }
		    bw.close();
		    br.close() ;
		}
	
    public static String[] removeElement(String[] A, String elem) {
        int length=A.length; 
        String[] C = new String[0] ;
        if(length==0) return C ; 
        int i=0; 
        for(int j=0; j<length; j++)
        {
          if(A[j].equals(elem) == false)
          {
            A[i]=A[j];
            i++; 
          }
        }
        String[] B = new String[i] ;
        for(int x=0 ; x < i ; x++) {
        	B[x] = A[x] ;
        }
        return B; 
      }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////PRINTING POSTINGS LIST AS IT IS////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public static void printpostings(String[] query, BufferedWriter bw, Map<String, LinkedList<Integer>> dictionary) throws IOException {
		// TAKING QUERY TERMS AND PRINTING POSTINGS LIST FOR EACH TERM
		for( int i = 0; i <= query.length - 1; i++)
		{
			bw.write("GetPostings");
			bw.newLine();
			bw.write(query[i]);
			bw.newLine();
		    LinkedList<Integer> value = dictionary.get(query[i]);
			bw.write("Postings list: ");
//			System.out.println(query[i]);
//			System.out.println(value);
			for( int j = 0; j <= value.size() - 1; j++) {	
				bw.write(value.get(j)+" ");
			}
			bw.newLine();
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////COMMON TAAT IMPLEMENTATION WHICH CALLS TAATAND AND TAATOR////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void taat(String[] query, BufferedWriter bw, Map<String, LinkedList<Integer>> dictionary) throws IOException {
		bw.write("TaatAnd");
		bw.newLine();
		// Creating an array for sorting purposes
		int[] lengthArray = new int[query.length];
		
		//Print terms and also add values to the lengthArray
		for( int i = 0; i <= query.length - 1; i++)
		{
			LinkedList<Integer> value = dictionary.get(query[i]);
			bw.write(query[i]+" ");	
			lengthArray[i] = value.size();
		}
		
		bw.newLine();
		// Sort the lengthArray and obtain the new index
		ArrayIndexComparator comparator = new ArrayIndexComparator(lengthArray);
		Integer[] indexes = comparator.createIndexArray();
		Arrays.sort(indexes, comparator);
		
		// Create a new linkedlist to store the final value
		LinkedList<Integer> listFinal = new LinkedList<Integer>() ;
		LinkedList<Integer> list1 = dictionary.get(query[indexes[0]]);
		
		// Iteratively finding the TAAT
		int numCounterAND = 0 ;
		for( int i = 1; i <= indexes.length - 1; i++)
		{
			LinkedList<Integer> list2 = dictionary.get(query[indexes[i]]);
			taatResults result = sortedIntersect(list1, list2); 
			list1 = result.resultVar ;
			numCounterAND += result.countVar ; 
		}
		// Obtained value used to display
		listFinal = list1 ;
		bw.write("Results: ");
		if(listFinal.size()==0) {
			bw.write("empty");
		}
		else {
			for( int i = 0; i <= listFinal.size() - 1; i++)
			{
				bw.write(listFinal.get(i)+" ");	
			}	
		}
		bw.newLine();
		bw.write("Number of documents in results: " + listFinal.size());
		bw.newLine();
		bw.write("Number of comparisons: " + numCounterAND);
		bw.newLine();
		
		// Setup for TAAT OR 
		bw.write("TaatOr");
		bw.newLine();
		
		// Print all the terms again for TAAT OR
		for( int i = 0; i <= query.length - 1; i++)
		{
			bw.write(query[i]+" ");	
		}
		bw.newLine();
		// 
		// Reinitializing the listFinal and list1 for iterations
		listFinal = null ;
		list1 = dictionary.get(query[indexes[0]]);
		int numCounterOR = 0;
		for( int i = 1; i <= indexes.length - 1; i++)
		{
			LinkedList<Integer> list2 = dictionary.get(query[indexes[i]]);
			taatResults result = sortedMerge(list1, list2); 
			list1 = result.resultVar ;
			numCounterOR += result.countVar ;
		}
		listFinal = list1 ;
		bw.write("Results: ");
		if(listFinal.size()==0) {
			bw.write("empty");
		}
		else {
			for( int i = 0; i <= listFinal.size() - 1; i++)
			{
				bw.write(listFinal.get(i)+" ");	
			}	
		}
		bw.newLine();
		bw.write("Number of documents in results: " + listFinal.size());
		bw.newLine();
		bw.write("Number of comparisons: " + numCounterOR);
		bw.newLine();
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////INTERSECTION FUNCTION FOR TAAT AND////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static taatResults sortedIntersect(LinkedList<Integer> list1, LinkedList<Integer> list2) {
		 LinkedList<Integer> res = new LinkedList<Integer>() ; 
		 int i = 0 ;
		 int j = 0 ;
		 int count = 0 ;
		 // HERE LIST1 AND LIST2 NEEDS TO BE MERGED
		 // FOR SKIP POINTER IMPLEMENTATION THE SQUAREROOT FOR EACH LIST LENGTH IS FIRST DETERMINED
		 int skip1 = (int) Math.sqrt(list1.size()) ;
		 int skip2 = (int) Math.sqrt(list2.size()) ;
		 
		 // IF BOTH THE LISTS ARE NULL THE BLANK VALUE IS RETURNED
	        if (list1 == null || list2 == null) {
	        	return new taatResults(res, count);
	        }
	     // ITERATING WITHIN THE LISTS 
	        while (i<list1.size() && j<list2.size()) {
	        	// WHEN LIST1 HAS SMALLER ELEMENT
	    		if(list1.get(i) < list2.get(j)) {
	    			// VERIFYING SKIP POINTER VALUE AND CHECKING IF SKIP POINTER EXISTS
	    			if(i+skip1 < list1.size() && i%skip1 ==0) {
		    			if(list1.get(i + skip1) < list2.get(j)){
		    				i = i + skip1 ;
		    			}
		    			else if(list1.get(i + skip1) == list2.get(j)) {
		    				res.add(list1.get(i+skip1));
		    				i = i + skip1 + 1 ;
		    				j++ ;
		    			}
		    			else {
		    				i++ ;
			    			count++ ;
		    			}
	    			}
	    			else {i++;
	    			}
	    		// WHEN LIST2 HAS SMALLER ELEMENT
	    		}else if (list1.get(i) > list2.get(j)){
	    			// VERIFYING FOR SKIP LIST AND CHECKING IF SKIP POINTER EXISTS
	    			if(j+skip2 < list2.size() && j%skip2==0) {
		    			if(list1.get(i) > list2.get(j+skip2)){
		    				j = j + skip2 + 1 ;
		    			}
		    			else if(list1.get(i) == list2.get(j+skip2)) {
		    				res.add(list1.get(i));
		    				j = j + skip2 + 1 ;
		    				i++ ;
		    			}
		    			else {
		    				j++ ;
			    			count++ ;
		    			}
	    			}
	    			else {j++;
	    			}
	    		// WHEN BOTH VALUES ARE EQUAL
	    		}else{
	    				res.add(list1.get(i));
	    				i++;
	    				j++;
	    			}
	    		count++ ;
	        }
	        return new taatResults(res, count);
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////MERGE FUNCTION FOR TAAT OR////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static taatResults sortedMerge(LinkedList<Integer> list1, LinkedList<Integer> list2) {
		 LinkedList<Integer> res = new LinkedList<Integer>() ;
		 int i = 0 ;
		 int j = 0 ;
		 int count = 0 ;
		 	// WHEN BOTH THE LISTS ARE EMPTY
	        if (list1 == null && list2 == null) {
	        	return new taatResults(res, count); 
	        }
	        // WHEN LIST1 IS EMPTY
	        if (list1 == null) {
	        	return new taatResults(list2, count); 
	        }
	        // WHEN LIST 2 IS EMPTY
	        if (list2 == null) {
	        	return new taatResults(list1, count); 
	        }
	        // ITERATING INSIDE BOTH THE LISTS
	        while (i<list1.size() && j<list2.size()) {
	        	// LIST1 HAS SMALLER ELEMENT
	    		if(list1.get(i) < list2.get(j))
	    		{
	    			res.add(list1.get(i));
	    			i++;
	    		}
	    		// LIST2 HAS SMALLER ELEMENT
	    		else if (list1.get(i) > list2.get(j))
	    		{
	    			res.add(list2.get(j));
	    			j++;
	    		}
	    		// BOTH THE LISTS ELEMENT ARE EQUAL
	    		else{
	    			res.add(list1.get(i));
	    			i++;
	    			j++;
	    		}
	    		count ++ ;
	        }
	        // FILLING IN REMAINING ELEMENTS
	        while (i < list1.size()) {
	        	res.add(list1.get(i)) ;
	        	i++ ;
	        }
	        
	        while (j < list2.size()) {
	        	res.add(list2.get(j)) ;
	        	j++ ;
	        }
	        return new taatResults(res, count); 
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////DAAT AND FUNCTION////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

 	public static void DaatAnd(String[] query, BufferedWriter bw, Map<String, LinkedList<Integer>> dictionary) throws IOException {
		LinkedList<Integer> res = new LinkedList<Integer>() ; 
		int minimumDocId = Integer.MAX_VALUE;  // Keeps minimum value with respect to current pointer 
		int commonDocId = 0; // The document present in all postings list 
		int comparisons = 0; // To calculate the number of comparisons required
		int skip = 0 ; // Skip pointer implementation 
		int[] pointer = new int[query.length]; // Array which stores the pointer count for all the postings list
		Boolean breakCondition=false; // Checks whether any array is finished traversing for break condition
		bw.write("DaatAnd");
		bw.newLine();
		for( int i = 0; i <= query.length - 1; i++)
		{
			bw.write(query[i]+" ");	
		}
		 while(true)
		 {
			 commonDocId = 0;
			 breakCondition = false;
			 minimumDocId=Integer.MAX_VALUE;
		 		//Find minimumDocId element among all lists
			 	for(int i = 0 ; i < query.length ; i++) 
			 	 {	
			 		 LinkedList<Integer> value = dictionary.get(query[i]);
			 		 // If the posting list is empty or the pointer has crossed 
			 		 // the postings list then there is no point in still counting
			 		 // So breaking out of the while loop
			 		 
			 		 if((value==null)||(pointer[i] >= value.size()))
			 		{
			 			breakCondition=true;
			 			break;	
			 		}

			 		if((value.get(pointer[i])) < minimumDocId)
			 		{	
			 			if(i>0) {
			 				skip = (int) Math.sqrt(value.size()) ;
			 				if(pointer[i] + skip < value.size() && value.get(pointer[i]+skip) < minimumDocId && pointer[i]%skip == 0) {
			 					minimumDocId=value.get(pointer[i]+skip);
			 					pointer[i] += skip ;
			 				}
			 				else {
					 			minimumDocId=value.get(pointer[i]);
			 				}
			 			}
				 		else {
				 			minimumDocId=value.get(pointer[i]);
				 		}
			 		}

			 	} 
			 	 
			 	 if((breakCondition==true)||(minimumDocId==Integer.MAX_VALUE))
			 	 {
			 		 break;
			 	 }
			 	 
			 	 // Summing up the comparisons for calculating minima at each step gives the total count
			 	 comparisons += (query.length-1); 
			 	 
			 	 for(int j = 0 ; j < query.length ; j++)
			 	 {	 
			 		 LinkedList<Integer> value = dictionary.get(query[j]);
			 		 //Incrementing minimum pointers and checking if all the term is there in all posting lists
					 if((value.get(pointer[j])) == minimumDocId)
					 {
						 pointer[j]++;
						 commonDocId++;
						 
					 }
			 	 }
			 	
			 	 //Checking if document is present in all the postings list
			 	 if(commonDocId==query.length)
			 	 {
					 res.add(minimumDocId);		 		 	 
			 	 }
			 	 
	}
		    bw.newLine();
			bw.write("Results: ");
			if(res.size()==0) {
				bw.write("empty");
			}
			else {
				for( int i = 0; i <= res.size() - 1; i++)
				{
					bw.write(res.get(i)+" ");	
				}	
			}
			bw.newLine();
			bw.write("Number of documents in results: " + res.size());
			bw.newLine();
			bw.write("Number of comparisons: "+ comparisons);
			bw.newLine();

	}
 
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////DAAT OR FUNCTION////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

 	
 	public static void DaatOr(String[] query, BufferedWriter bw, Map<String, LinkedList<Integer>> dictionary) throws IOException {
		LinkedList<Integer> res = new LinkedList<Integer>() ; 
		int minimumDocId = Integer.MAX_VALUE;
		int comparisons = 0;
		int count = 0;
		int[] pointer = new int[query.length]; 
		bw.write("DaatOr");
		bw.newLine();
		for( int i = 0; i <= query.length - 1; i++)
		{
			bw.write(query[i]+" ");	
		}
		 while(true)
		 {
			 minimumDocId=Integer.MAX_VALUE;
			 count=0;
		 		//Find minimumDocId element among all lists
			 	for(int i = 0 ; i < query.length ; i++) 
			 	 {	
			 		 LinkedList<Integer> value = dictionary.get(query[i]);
			 		 if((value != null) && pointer[i] < value.size()) {
			 			count++ ;
			 			if(value.get(pointer[i]) < minimumDocId) {
			 				minimumDocId=value.get(pointer[i]);
			 			}
			 		 }
			 	 }
			 	// If none of the lists have any elements then the miminumdocId will not change
			 	 if(minimumDocId==Integer.MAX_VALUE)
			 	 {
			 		 break;
			 	 }

			     comparisons+=(count-1); 
			 	 
			 	 
			 	 for(int i = 0 ; i < query.length ; i++)
			 	 {
			 		 LinkedList<Integer> value = dictionary.get(query[i]);
			 		 if(query[i]!=null && pointer[i] < value.size() && value.get(pointer[i]) == minimumDocId)
			 		 {
						pointer[i]++;
			 		 }
			 	}
			 	res.add(minimumDocId) ;
			 	 
		 }
		    bw.newLine();
			bw.write("Results: ");
			if(res.size()==0) {
				bw.write("empty");
			}
			else {
				for( int i = 0; i <= res.size() - 1; i++)
				{
					bw.write(res.get(i)+" ");	
				}	
			}
			bw.newLine();
			bw.write("Number of documents in results: " + res.size());
			bw.newLine();
			bw.write("Number of comparisons: "+ comparisons);
			bw.newLine();

	}

}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////CLASS CREATED SO THAT TWO VARIABLES CAN BE RETURNED////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class taatResults { 
	LinkedList<Integer> resultVar; 
    int countVar; 
    taatResults(LinkedList<Integer> m, int a) 
    { 
        resultVar = m; 
        countVar = a; 
    } 
} 
