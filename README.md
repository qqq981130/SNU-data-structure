# SNU Data Structure 2022 Spring
This is the submitted assignments for Seoul National University - data structure course, 2022 Spring
## 1-Big Integer
### Data structure used
* Array
### Description
* The objective of this program is to do binary operations (+, -, *) between integers which exceed the limit of primitive integer type size(32bits), by using byte or char arrays.    
* It gets String in the form of "A + B" or "A - B" or "A * B" as an input, and prints out the outcome of operation until the input is "quit".   
### Assignment instructions
* The length of each input number is 100 at its largest.
* Each input number's sign is either + or -. (if there is no sign, assume as +). 
* &#45; sign is prior to + operator. For example, 1+-2 is interpreted as 1+(-2).   
* There can be one or more blank between input numbers and operators.   
### example
> $ java BigInteger   
>10000000000000000+200000000000000000   
>210000000000000000   
>   
>20000000000000000-100000000000000000   
>-80000000000000000   
>   
>30000000000000000 - 200000000000000000   
>-170000000000000000   
>   
>50000000 *+1000   
>50000000000   
>   
>-1000000 + 0   
>-1000000   	 
>   
>quit   
>$   


## 2-Movie Database
### Data structure used
* Linked List   
### Description
* The objective of this program is to build a database which stores genres and titles of movies in alphabetical order.   
* insert, delete, search, print operation provided.
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
	- end the program.
* there can be one or more blank between command and arguments.
* there is no '%' or ',' in movie genre and title.
### example
>$ java MovieDatabaseConsole   
>INSERT %ACTION% %BATMAN BEGINS%   
>INSERT %ACTION% %THE MATRIX%    
>INSERT %DRAMA% %MILLION DOLLAR BABY%    
>SEARCH %BA%   
>(ACTION, BATMAN BEGINS)   
>(DRAMA, MILLION DOLLAR BABY)   
>DELETE %DRAMA% %MILLION DOLLAR BABY%   
>PRINT   
>(ACTION, BATMAN BEGINS)   
>(ACTION, THE MATRIX)   	   
>QUIT   	   
>$   	    


## 3-Stack Calculator
### Data structure used
### Description
### Assignment instructions

## 4-Sorting
### Data structure used
### Description
### Assignment instructions

## 5-Matching
### Data structure used
### Description
### Assignment instructions

## 6-Subway
### Data structure used
### Description
### Assignment instructions