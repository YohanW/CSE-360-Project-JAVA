import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class textAnalyzer {

    // this function is from
    // https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
    public static <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    public static void analyzeText(String inputPath, String outputPath, String justification, int numberBlankLines, int numberOfCharactersPerLine) throws Exception {
        int numWordProcessed = 0;           // ok
        int numBlankLinesRemoved = 0;       // ok
        int out_numLines = 0;               // ok
        double out_avrgWordsPerLine = 0;    // I didn't count the blank lines
        double out_avrgLineLength = 0;
        File rfile = new File(inputPath);
        File wfile;

        // Create output file or use existing output file
        // If the second argument "outputPath" is "create", we create an output file
        if(outputPath.equals("create")) {
            System.out.println("The output is stored in file: textAnalyzer_out.txt");
            wfile = new File("textAnalyzer_out.txt");
            wfile.createNewFile();
        // If the second argument "outputPath" is not "create", we read the file
        } else {
            wfile = new File(outputPath);
        }

        // FIRST: check if input & output are valid
        
        // check if the input or output file is a text file
    	if(rfile.isFile() && rfile.getName().endsWith(".txt")) {
    		if(wfile.isFile() && wfile.getName().endsWith(".txt")) {
    			;
    		} else {
    			System.out.println("ERROR: Output file is not a text file!");
    			System.exit(0);
    		}
    	} else {
    		System.out.println("ERROR: Input file is not a text file!");
    		System.exit(0);
    	}

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rfile)));
        BufferedWriter bw = new BufferedWriter(new PrintWriter(wfile));
        String[] wordList = {};
        String line = null;

        // SECOND: we read each line of the input, and store all words into wordList
        
        while((line = br.readLine()) != null) {
            if(line.equals("")) {   // if there is no character in this line
                numBlankLinesRemoved++;
            } else {    // if there exist some characters in this line
                String[] wordsInLine = line.split("\\s+");
                if(wordsInLine.length == 0) {
                    numBlankLinesRemoved++;
                } else {
                    numWordProcessed += wordsInLine.length;
                    wordList = concatenate(wordList, wordsInLine);
                }
            }
        }

        // THIRD: we assign the words into each line in the following matrix
        // and count the number of spaces required
        
        String[][] wordMatrix = new String[wordList.length][40];
        int row = 0;
        int col = 0;
        int numberOfRow = 0;

        // we count the number of spaces we need to write into each row
        int[] spaceCount = new int[wordList.length]; 
        // we count the number of words in each row
        int[] wordsCount = new int[wordList.length];

        for(int i=0; i<wordList.length; i++) {
            if(col==0) {
                numberOfRow++;
                if(wordList[i].length() >= numberOfCharactersPerLine-1) {
                    wordMatrix[row][col] = wordList[i];
                    spaceCount[row] = Math.max(0, numberOfCharactersPerLine-wordList[i].length());
                    wordsCount[row] = 1;
                    row++;
                } else {
                    wordMatrix[row][col] = wordList[i];
                    spaceCount[row] = numberOfCharactersPerLine-wordList[i].length();
                    wordsCount[row] = 1;
                    col++;
                }
            } else {
                if(wordList[i].length() >= spaceCount[row]) {
                    row++;
                    col = 0;
                    numberOfRow++;
                    if(wordList[i].length() >= numberOfCharactersPerLine-1) {
                        wordMatrix[row][col] = wordList[i];
                        spaceCount[row] = Math.max(0, 80-wordList[i].length());
                        wordsCount[row] = 1;
                        row++;
                    } else {
                        wordMatrix[row][col] = wordList[i];
                        spaceCount[row] = numberOfCharactersPerLine-wordList[i].length();
                        wordsCount[row] = 1;
                        col++;
                    }
                } else {
                    wordMatrix[row][col] = wordList[i];
                    spaceCount[row] = spaceCount[row]-1-wordList[i].length();
                    wordsCount[row]++;
                    col++;
                }
            }
        }

        if(numberOfRow == 0) {
            System.out.println("ERROR: Input file has no words!");
            System.exit(0);
        } else {
            out_avrgWordsPerLine = (double)numWordProcessed / numberOfRow;
        }
        


        // Fourth: we write the output file given the wordMatrix and the spaceCount

        if(justification.equals("left") || justification.equals("right")) {
            for(int i=0; i<numberOfRow; i++) {
                if(i!=0) {
                    bw.append("\n");
                    for(int j=0; j<numberBlankLines; j++) {
                        bw.append("\n");
                    }
                }
                if(justification.equals("right") && spaceCount[i]>0) {
                    for(int k=0; k<spaceCount[i]; k++) {
                        bw.append(" ");
                    }
                }
                for(int j=0; j<numberOfCharactersPerLine/2; j++) {
                    if(wordMatrix[i][j] == null) {
                        break;
                    }
                    if(j==0) {
                        bw.append(wordMatrix[i][j]);
                        out_avrgLineLength += wordMatrix[i][j].length();
                    } else {
                        bw.append(" " + wordMatrix[i][j]);
                        out_avrgLineLength = out_avrgLineLength + 1 + wordMatrix[i][j].length();
                    }
                }
                if(justification.equals("right")) {
                    if(spaceCount[i]>0) {
                        out_avrgLineLength += spaceCount[i];
                    }
                }
            }
        } else if (justification.equals("full")) {
            for(int i=0; i<numberOfRow; i++) {
                // we use tmp to record how many spaces are to be added between each word
                int tmp = 0;
                int modulus = 0;

                if(i!=0) {
                    bw.append("\n");
                    for(int j=0; j<numberBlankLines; j++) {
                        bw.append("\n");
                    }
                }

                // if there is only one word in this row
                if(wordsCount[i] == 1) {
                    tmp = spaceCount[i] / 2;
                    for(int k=0; k<tmp; k++) {
                        bw.append(" ");
                    }
                    bw.append(wordMatrix[i][0]);
                    for(int k=0; k<spaceCount[i]-tmp; k++) {
                        bw.append(" ");
                    }
                    out_avrgLineLength = out_avrgLineLength + wordMatrix[i][0].length() + spaceCount[i];
                // if there are at least 2 words in this row
                } else {
                    tmp = spaceCount[i] / (wordsCount[i]-1);
                    modulus = spaceCount[i] % (wordsCount[i]-1);
                    for(int j=0; j<40; j++) {
                        if(wordMatrix[i][j] == null) {
                            break;
                        }
                        if(j==0) {
                            bw.append(wordMatrix[i][j]);
                        } else {
                            for(int k=0; k<tmp+1; k++) {
                                bw.append(" ");
                            }
                            if(modulus>0) {
                                bw.append(" ");
                                modulus--;
                            }
                            bw.append(wordMatrix[i][j]);
                        }
                    }
                    out_avrgLineLength += numberOfCharactersPerLine;
                }
            }
        } else {
            System.out.println("ERROR: No this kind of justification!");
            System.exit(0);
        }


        br.close();
        bw.close();

        out_numLines = numberOfRow + (numberOfRow-1) * numberBlankLines;
        out_avrgLineLength = (double)out_avrgLineLength / numberOfRow;


        // FIFTH: show the result
        // Please show the results of analysis by modifying the following codes
        System.out.println("numWordProcessed: " + numWordProcessed);
        System.out.println("numBlankLinesRemoved: " + numBlankLinesRemoved);
        System.out.println("out_numLines: " + out_numLines);
        System.out.printf("out_avrgWordsPerLine: %.2f \n", out_avrgWordsPerLine);
		System.out.printf("out_avrgLineLength: %.2f \n", out_avrgLineLength);
    }    


    public static void main(String[] args) throws Exception {
        int numberBlankLines = Integer.parseInt(args[3]);
        int numberOfCharactersPerLine = Integer.parseInt(args[4]);
        // analyzeText(inputPath, outputPath, justification, numberBlankLines)
        analyzeText(args[0], args[1], args[2], numberBlankLines, numberOfCharactersPerLine);
    }
}

