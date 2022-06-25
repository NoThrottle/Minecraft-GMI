package mainpkg;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

public class PathCleanclass implements Runnable {
     private volatile boolean value;
     public String path;

     @Override
     public void run() {
        PathClean(path);
     }

     public void PathClean(String path) {
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
					   
  					   DeleteFile(file);
  					   
  				   }
  				   else
  				   {	
  					   if (DeleteEverything) {
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
 	  					} 
  	  					else {
  	  						
  	  	  					 if (JOptionPane.showConfirmDialog(null, "Continue Installation?", "WARNING",
  	 	  					        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
  	  	  					 {
  	  	  						 ignoreOthers = true;	   
  	 	  					} 
  	  	  					else {
  	  	  						DesktopApi.browse(file.toURI());
  	 	  					    value = false;
  	 	  					    return;
  	 	  					}			
 	  					}
  					   }
  				   }
  				   
  				 value = true;
  				 return; 				   
  			   }
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
				return;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();				
			}
			
			try {
				Runtime.getRuntime().exec("rm "+ file.getPath());
				return;
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(null, "Error: Can't Delete File:" + file.getPath() + " " + e, "Error: ", JOptionPane.INFORMATION_MESSAGE);
			}  
     }
 }