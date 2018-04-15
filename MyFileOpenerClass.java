
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JFileChooser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sakshamsaraswat
 */
public class MyFileOpenerClass {
    
    JFileChooser file_chooser = new JFileChooser();
    //StringBuilder sb = new StringBuilder();
    
    int characters = 0, words = 0, lines = 0;
    int cnt = 0;
    
    public void pick_me() throws FileNotFoundException, IOException
    {
      if(file_chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)  
      {
        File file = file_chooser.getSelectedFile();
        Scanner input = new Scanner(file);
        //BufferedReader in = new BufferedReader(new FileReader(file));
        
        //PrintWriter writer = new PrintWriter(file);
        
        
        while(input.hasNext())
        {
            //sb.append(input.nextLine());
            //sb.append("\n");
            
            String p = input.nextLine();
            lines++;
            
            String[] split = p.split(" ");
            
            for(int i = 0; i < split.length; i++)
                if(split[i].length() == 1)
                    characters++;
                else
                    words++;
            
            
            
                if(p.trim().isEmpty()){
                cnt++;
                                      }
                
            
            
           
        }
        
        
        input.close(); 
        //writer.close();
          
      } 
      else
      {
          
         //sb.append("No file was selected");
          
      } 
    }
    public int getLines()
        {
            return lines;
        }
        public int getWords()
        {
            return characters + words;
        }
        
    public int getBlankLines()
    {
         return cnt;
    }
    
}
