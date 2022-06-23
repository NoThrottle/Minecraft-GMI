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
     public static String path;

     @Override
     public void run() {
        PathClean(path);
     }

     public boolean PathClean(String path) {
  	   try {
  		   if (isDirEmpty(Paths.get(path))) {
  			   value = true;
  		   }
  		   else {
  			     
  			   File folder = new File(path);
  			   File[] listOfFiles = folder.listFiles();

  			   for (int i = 0; i < listOfFiles.length; i++) {
  			     if (listOfFiles[i].isFile()) {
  			       System.out.println("File " + listOfFiles[i].getName());
  			     } else if (listOfFiles[i].isDirectory()) {
  			       System.out.println("Directory " + listOfFiles[i].getName());
  			     }
  			   }
  			   
  			   int s = 0;
  			   for (File file : listOfFiles) {
  				   if (file.getName().contains("mods_01_")) {
  							Runtime.getRuntime().exec("powershell rm "+ file.getPath());
  							
  							try {
  								Runtime.getRuntime().exec("rm "+ file.getPath());
  							}
  							catch(IOException e){
  								
  							}
  					   
  				   }
  				   else
  				   {	

  					   JOptionPane.showMessageDialog(null, "The mods folder contains something else (maybe an old mod set or version). Empty the folder then try again", "Error: ", JOptionPane.INFORMATION_MESSAGE);
  					   DesktopApi.browse(file.toURI());
  					   
  					   value = false;
  				   }
  				   

  				 value = true;
  				   
  			   }
  			   

  		   }
  	   }
  	   catch(IOException e) {
  		   JOptionPane.showMessageDialog(null, "Error:" + e, "Error: ", JOptionPane.INFORMATION_MESSAGE);
  		 value = false;
  		   
  	   }
  	return false;
     }
     
     private static boolean isDirEmpty(final Path directory) throws IOException {
 	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
 	    	return !dirStream.iterator().hasNext();
 	    }
 	}
     
     public boolean getValue() {
         return value;
     }
 }