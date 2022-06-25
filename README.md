# SNU Data Structure 2022 Spring
This is the submitted assignments for Seoul National University - data structure course, 2022 Spring
## 1-Big Integer
### Data structure used
* Array
### Description
* The objective of this assignment:
	- Binary operations (+, -, *) between integers which exceed the limit of primitive integer type size(32bits), by using byte or char arrays.    
* Input:   
	- String in the form of "A + B" or "A - B" or "A * B"   
* Output:   
	- outcome of operation.   
* End with input "quit".  
### Assignment instructions
* The length of each input number is 100 at its largest.   
* Each input number's sign is either + or -. (if there is no sign, assume as +).   
* &#45; sign is prior to + operator. For example, 1+-2 is interpreted as 1+(-2).   
* There can be one or more blank between input numbers and operators.   

## 2-Movie Database
### Data structure used
* Linked List   
### Description
* The objective of this assignment:
	- Build a database which stores genres and titles of movies in alphabetical order with insert, delete, search, print operations.
* MovieDB is a linked list. Node of this linked list has MovieList as its item, and keeps genre name as an instance variable called 'genreName'. MovieList is also a linked list. We keep the titles of the movies belonging to a certain genre, sorted in alphabetical order.
* Therefore, basically it is a linked list(of individual movies) inside a linked list(of genres).  
### Assignment instructions
* INSERT %GENRE% %TITLE%
	- Insert new movie with given genre and title. If such movie already exists in the database, do nothing.
* DELETE %GENRE% %TITLE%
	- Delete such movie with given genre and title. If such movie do not exist in the database, do nothing.  
	- If every movie belonging to a certain genre is deleted, that genre should also be deleted from the database.
* SEARCH %SEARCH WORD%
	- Search every movie which contains the 'search word' in its title. Print out their genre and title.
	- If no such movie is found, print "EMPTY".
* PRINT
	- Print out everything from the database in a sorted order.
	- If the database is empty, print "EMPTY".
* QUIT
	- End the program.
* There can be one or more blank between command and arguments.  
* There is no '%' or ',' in movie genre and title.  

## 3-Stack Calculator
### Data structure used
* List
* Stack
### Description
* The objective of this assignment:
	- Build a simple calculator.
* Input:
	- Infix expression.
* Output:
	- Postfix expression.
	- Result of calculation.
* End with input "q".
* Stacks are used to change infix expression to postfix expression.
* Stacks are used to calculate the postfix expression.
### Assignment instructions
* +, -(binary), -(unary), *, /, %, ^, (, ) can be used.
* ^ and -(unary) are right-assoicative, and the rest are left-associative.
* -(unary) should be expressed as '~' when converting infix to postfix expression.
* Overflow can be ignored.
* Print "ERROR" in case of: x/0, x%0, 0^y when y<0

## 4-Sorting
### Description
* The objective of this assignment
	- Sort numbers using six different algorithms: Bubble, Insertion, Heap, Merge, Quick, Radix.
	- Understand the importance of algorithm.
* Input can be either array of numbers or random numbers

## 5-Matching
### Data structure used
* ArrayList
* AVL Tree
* Hash-table
### Description
* The objective of this assignment:
	- Build a string index finder of a given text file.
	- Understand hash-table and AVL Tree, and how to use them in mixture.
* Input:
	- Command. (<, @, ?)
* Output:
	- Result.
* .txt form data input is sliced into substrings with length 6.   Every substring and its page(line and index) is stored into the hash-table.   Collision resolution by AVL Tree.   If same substring exist in different pages, we keep the pages by an ArrayList inside AVLNode.
### Assignment instructions
* In this assignment, for simplification, every data input and pattern string is longer than 6.
* < (FILENAME)
	- Input data. (both absolute and relative path)
* @ (INDEX NUMBER)
	- Print every substring saved in the given hash-table slot.
* ? (PATTERN)
	- Print every page(line and index) of the input data where the PATTERN is found.

## 6-Subway
### Data structure used
* Graph (Dijkstra's algorithm)
### Description
* Using Dijkstra's algorithm, this program finds out the shortest path from starting station to the destination.
* Input: 
	- .txt file which includes: 
		- Informations about the stations. 
		- Time between stations.
	- Starting station and the destination.
* Output:
	- Route.
	- Time taken.
### Assignment instructions
* in the .txt file:
	* (serial number of the station) (name of the station) (line of the station) : Informations about the stations.
	* (blank line)
	* (serial number of the station1) (serial number of the station2) (time) : Time between stations.
* Every serial number is a unique string corresponding to a single station. (Even though the station name is same, if the line is different, the serial number is also different)
* Transfer requires 5 minutes.