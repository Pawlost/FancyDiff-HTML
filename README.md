#Dokumentace
#FancyDiff-HTML
================

#Obsah
* Desription
* Basic usage
* Start 
* Neccesary arguments and program start
* List of arguments
* Optional arguments
---------------------------------
#Warning 
Current version doesnt work
---------------------------------

#Desription
Main purpose of FancyDiff-HTML is to take saved HTML document from PC, download HTML document from internet, compare both documents and make a difference.

#Basic usage
After we cloned program from github we build it. 
Build is made by command mvn clean install.

After build we can start program from target file.
Help is shown by command. (Help shows all neccesary arguments)
java -jar target/program_name_and_version.jar --help

#Neccesary arguments and program start
Program start is made by command
These arguments are neccesary.
java -jar target/program_name_and_version.jar --file name_of_old_html_file.txt --revision_id id --bulk name --path /path/to/old/file --previous_bulk name

#List of arguments
* --file name.txt = set the name of document where are stored all files
* --revision_id id = made for future usage
* --bulk name = the name of new HTML file
* --path /path/ = set path to old files
* --previous_bulk name = the name of old HTML file

#Optional arguments
Tyto argumenty nejsou potřebné a slouží spíše k testování.

* --help = shows all neccesary arguments

