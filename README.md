# CSE-360-Project-JAVA

In this release, the text analyzer is able to analyze on an input .txt file and turn it into another .txt file with a new format, where

  1. each line contains at most 80 characters unless there is a word longer than 80 characters,
  2. words in one line are separated by spaces, 
  3. the format of each line is determined by the justification type, including left, right, and full
  4. every two lines are separated by N blank lines, where N is given by the user.

Besides, the text analyzer will analyze the statistics of both input and output files, including 

  1. the number of words processed, 
  2. the number of blank lines removed, 
  3. the number of lines in the output file,
  4. the average number of words per line in the output file,
  5. the average number of characters per line in the output file (not including the blank lines).

A user is required to select a .txt file as an input file. If the user does not select a .txt file as the output file, the text analyzer will store the reformed text in file "textAnalyzer_out.txt". A user is also required to choose the justification type and the number of blank lines to be added between every two lines.
