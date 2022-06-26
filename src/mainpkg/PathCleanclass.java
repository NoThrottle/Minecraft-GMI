package mainpkg;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class PathCleanclass implements Runnable {
     private volatile boolean value;
     public String path;
     private List<String> ignore = new ArrayList<String>();

     @Override
     public void run() {
    	ignore.add("ignore.txt");
    	
    	List<String> check = IgnoreListParse(path);   	
    	if(check != null) {
    		ignore.addAll(check);
    	}

        PathClean(path);
     }

     public void PathClean(String path) {
    	 
    	 System.out.println(path);
    	 
  	   try {
  		   if (isDirEmpty(Paths.get(path))) {
  			   value = true;
  			   return;
  		   }
  		   else {
  			     
  			   File folder = new File(path);
  			   File[] listOfFiles = folder.listFiles();
  			   boolean DeleteEverything = false;
  			   boolean ignoreOthers = false;
  			   
  			   for (File file : listOfFiles) {
  				   if (file.getName().contains("mods_01_")) {
					   
  					 System.out.println("delte normal::" +  file.getPath());
  					   DeleteFile(file);
  					   
  				   }
  				   else if (stringContainsItemFromList(file.getName(), ignore)) {
  					   
  				   }
  				   else
  				   {	
  					   if (DeleteEverything) {
  						 System.out.println("delte evyrtign::" + file.getPath());
  	  					   DeleteFile(file);
  					   }
  					   else if (ignoreOthers) {
  						   
  					   } 
  					   else {
  	  					 if (JOptionPane.showConfirmDialog(null, "The mods folder contains something else (maybe an old mod set or version). They may interfere with the modpack. Do you want to also delete them? (Recommended)", "WARNING",
 	  					        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
  	  					 {
  	  						 DeleteEverything = true;
	  	  					 DeleteFile(file);	   
	  	  					System.out.println("delte evyrtign iinit::" + file.getPath());
 	  					} 
  	  					else {
  	  						
  	  	  					 if (JOptionPane.showConfirmDialog(null, "Continue Installation?", "WARNING",
  	 	  					        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
  	  	  					 {
  	  	  						 ignoreOthers = true;	   
  	 	  					} 
  	  	  					else {
  	  	  						JOptionPane.showMessageDialog(null, "You can create an ignore.txt on your mods folder and write all .jar (e.g mod1.jar) files you want to be ignored per line.");
  	  	  						DesktopApi.browse(file.toURI());
  	 	  					    value = false;
  	 	  					    return;
  	 	  					}			
 	  					}
  					   }
  				   }
  				   
  								   
  			   }
  			   
  			 value = true;
  			 return;
  			   
  		   }
  	   }
  	   catch(IOException e) {
  		   JOptionPane.showMessageDialog(null, "Error:" + e, "Error: ", JOptionPane.INFORMATION_MESSAGE);
  		   value = false;
  		   return;
  		   
  	   }
     }
     
     private static boolean isDirEmpty(final Path directory) throws IOException {
 	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
 	    	return !dirStream.iterator().hasNext();
 	    }
 	}
     
     public boolean getValue() {
         return value;
     }
     
     public void DeleteFile(File file) {
    	 
			try {
				Runtime.getRuntime().exec("powershell rm "+ file.getPath());
				Thread.sleep(150);
				return;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Runtime.getRuntime().exec("rm "+ file.getPath());
				Thread.sleep(150);
				return;
			}
			catch(IOException | InterruptedException e){
				JOptionPane.showMessageDialog(null, "Error: Can't Delete File:" + file.getPath() + " " + e, "Error: ", JOptionPane.INFORMATION_MESSAGE);
			}  
     }
 
     public static boolean stringContainsItemFromList(String inputStr, List<String> items) {
    	    return items.stream().anyMatch(inputStr::equals);
    	}

     
     public List<String> IgnoreListParse(String path) {
    	 
    	 Path pathinter = Paths.get(path, "ignore.txt");
    	 path = pathinter.toString();
    	 
    	 if(!DoesIgnoreListExist(path)) {
    		 return null;
    	 }
    	 else {
    		 try  
    		 {  
	    		 
	    		 FileInputStream fis=new FileInputStream(path);       
	    		 Scanner sc=new Scanner(fis);
	    		 List<String> ignorelist = new ArrayList<String>();
	    		  
		    		 while(sc.hasNextLine())  
		    		 {   
			    		 ignorelist.add(sc.nextLine());
			    	 }  
			    	 sc.close();  
			    	 return ignorelist;
		     }  
    		 catch(IOException e)  
    		 {  
    			 e.printStackTrace();
    			 return null;
    		 }  
    	 }
    	 
    	 
     }
     
     public boolean DoesIgnoreListExist(String path) {
    	 return new File(path).isFile();
     }

}