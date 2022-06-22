package mainpkg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class installgmi {
	final Display display = Display.getDefault();
	Shell shlHsmcGmi = new Shell(display);
	Button btndld = new Button(shlHsmcGmi, SWT.NONE);
	private Text text;
	final ProgressBar progressBar = new ProgressBar(shlHsmcGmi, SWT.SMOOTH);

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			installgmi window = new installgmi();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		
		shlHsmcGmi.setSize(450, 245);
		shlHsmcGmi.setText("HSMC GMI");
		
		Button btnbrowse = new Button(shlHsmcGmi, SWT.NONE);
		btnbrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File(GetClientModPath(".mc").toString())); // start at application current directory
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showSaveDialog(fc);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				    text.setText(fc.getSelectedFile().getPath());
				}
			}
		});

		btnbrowse.setBounds(341, 99, 65, 25);
		btnbrowse.setText("Browse");
		
		text = new Text(shlHsmcGmi, SWT.BORDER);
		text.setText(GetClientModPath("mods").toString());
		text.setBounds(28, 99, 307, 25);
		
		btndld.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if (VerifyPath(text.getText()) && PathClean(text.getText())) {
					download();
					btndld.setEnabled(false);
				}

			}
		});
		btndld.setBounds(352, 171, 72, 25);
		btndld.setText("Download");
		
		Label lblLabel = new Label(shlHsmcGmi, SWT.NONE);
		lblLabel.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 9, SWT.NORMAL));
		lblLabel.setBounds(28, 45, 101, 15);
		lblLabel.setText("Pack Version: 1.0.0");
		
		Label lblNewLabel = new Label(shlHsmcGmi, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Metropolis Semi Bold", 16, SWT.NORMAL));
		lblNewLabel.setBounds(28, 23, 268, 25);
		lblNewLabel.setText("GMI for HighSkyMinecraft");
		
		progressBar.setBounds(10, 171, 336, 25);
		
		Label lblModsFolderLocation = new Label(shlHsmcGmi, SWT.NONE);
		lblModsFolderLocation.setBounds(28, 80, 144, 15);
		lblModsFolderLocation.setText("Mods Folder Location");
		
		Label lblProgress = new Label(shlHsmcGmi, SWT.NONE);
		lblProgress.setBounds(10, 150, 55, 15);
		lblProgress.setText("Progress:");

		shlHsmcGmi.open();
		shlHsmcGmi.layout();
		while (!shlHsmcGmi.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public static Path GetClientModPath(String what) {
		
		AppDirs appDirs = AppDirsFactory.getInstance();
		
		if (what == "mods") {
			Path path = Paths.get(appDirs.getUserDataDir(".minecraft", "mods", null, true));
			return path;
		} else if (what == ".mc") {
			Path path = Paths.get(appDirs.getUserDataDir(".minecraft", null, null, true));
			return path;
		} else {
			return null;
		}

		//send process to log
	}
	
	//////////////////////////////////////
	   
   private static String[] modlist = 
	   	  {"https://cdn.modrinth.com/data/P7dR8mSH/versions/0.56.1+1.19/fabric-api-0.56.1%2B1.19.jar", 
	   			"https://cdn.modrinth.com/data/Fb4jn8m6/versions/1.19-3.6.1/FallingTree-1.19-3.6.1.jar", 

	   			"https://cdn.modrinth.com/data/1bZhdhsH/versions/fabric-1.19-1.2.16/plasmovoice-fabric-1.19-1.2.16.jar", 
	   			"https://media.forgecdn.net/files/3831/918/better-nether-7.0.2.jar", 
	   			"https://media.forgecdn.net/files/3831/904/better-end-2.0.2.jar", 
	   			"https://cdn.modrinth.com/data/EsAfCjCV/versions/fabric-mc1.19-2.4.0/appleskin-fabric-mc1.19-2.4.0.jar", 
	   			"https://media.forgecdn.net/files/3822/878/Croptopia-1.19-FABRIC-2.0.5.jar", 
	   			"https://media.forgecdn.net/files/3831/901/bclib-2.0.5.jar", 
	   			"https://media.forgecdn.net/files/3805/637/moreberries-1.5.1.jar", 
	   			"https://cdn.modrinth.com/data/9s6osm5g/versions/7.0.72+fabric/cloth-config-7.0.72-fabric.jar", 

	   			"https://media.forgecdn.net/files/3835/518/fabrication-3.0-pre4%2B1.19.jar", 
	   			"https://media.forgecdn.net/files/3827/640/Couplings-1.9.0%2B1.19.jar", 
	   			"https://media.forgecdn.net/files/3831/60/dehydration-1.2.9.jar", 

	   			"https://media.forgecdn.net/files/3836/369/backslot-1.2.8.jar", 

	   			"https://cdn.modrinth.com/data/hvFnDODi/versions/0.1.3/lazydfu-0.1.3.jar", 
	   			"https://cdn.modrinth.com/data/Ha28R6CL/versions/1.8.0+kotlin.1.7.0/fabric-language-kotlin-1.8.0%2Bkotlin.1.7.0.jar", 
	   			"https://cdn.modrinth.com/data/E6FUtRJh/versions/3.5.4-fabric/Adorn-3.5.4%2B1.19-fabric.jar", 

	   			"https://media.forgecdn.net/files/3824/701/time-and-wind-ct-1.4.2%2B1.19.jar", 
	   			"https://cdn.modrinth.com/data/WhbRG4iK/versions/1.12.1/fallingleaves-1.12.1%2B1.19.jar", 
	   			"https://cdn.modrinth.com/data/rUgZvGzi/versions/1.8.1/eating-animation-1.8.1.jar", 
	   			"https://cdn.modrinth.com/data/aC3cM3Vq/versions/1.19-2.22-fabric/MouseTweaks-fabric-mc1.19-2.22.jar", 
	   			"https://cdn.modrinth.com/data/YL57xq9U/versions/1.19.x-v1.2.5/iris-mc1.19-1.2.5.jar", 
	   			"https://cdn.modrinth.com/data/yBW8D80W/versions/2.1.2+1.19/lambdynamiclights-2.1.2%2B1.19.jar", 
	   			"https://cdn.modrinth.com/data/AANobbMI/versions/mc1.19-0.4.2/sodium-fabric-mc1.19-0.4.2%2Bbuild.16.jar"

   			};
  
   
   public void download() {
      int i = 0;
      String path = text.getText();
      progressBar.setMaximum(modlist.length-1);
      
      new Thread(new Runnable() {			
			@Override
			public void run() {
				boolean thisa = true;
				while(thisa)
				if (modlist.length == new File(path).list().length) {
					try {
						Thread.sleep(10*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Download is complete", "Confirmation: ", JOptionPane.INFORMATION_MESSAGE);
					thisa=false;
					
					 if (display.isDisposed())
				            return;
				          display.asyncExec(new Runnable() {
				            public void run() {
				              if (btndld.isDisposed())
				                return;
				              btndld.setEnabled(true);
				            }
				          });
					
				}
			}			        
	  }).start();
      
	  while(i != modlist.length) {
		  			  
		  final int a = i;
		  final String b = path;
		  new Thread(new Runnable() {
			  
				String url = modlist[a];	
				
				@Override
				public void run() {
			        try {
			            downloadUsingNIO(url, b + "/mod_" + a + ".jar");
			        } catch (IOException e) {
			        	JOptionPane.showMessageDialog(null, "Download failed: " + e, "Error: ", JOptionPane.INFORMATION_MESSAGE);
			            e.printStackTrace();
			        }
			        
			        if (display.isDisposed())
			            return;
			          display.asyncExec(new Runnable() {
			            public void run() {
			              if (progressBar.isDisposed())
			                return;
			              progressBar.setSelection(progressBar.getSelection()+1);
			            }
			          });
				}			        
		  }).start();
			        
		i++;			
	        
	  }		  
	  
   }
   	  

   private static void downloadUsingNIO(String urlStr, String file) throws IOException {
       URL url = new URL(urlStr);
       ReadableByteChannel rbc = Channels.newChannel(url.openStream());
       FileOutputStream fos = new FileOutputStream(file);
       fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
       fos.close();
       rbc.close();
   }

   public boolean VerifyPath(String path) {   
	   if (new File(path).exists()) {
		   return true;
	   }
	   else {
		   return false;
	   }	   
   }
   
   public boolean PathClean(String path) {
	   try {
		   if (isDirEmpty(Paths.get(path))) {
			   return true;
		   }
		   else {
			   JOptionPane.showMessageDialog(null, "The mods folder contains something else (maybe an old mod set or version). Empty the folder then try again", "Error: ", JOptionPane.INFORMATION_MESSAGE);
			   return false;
		   }
	   }
	   catch(IOException e) {
		   JOptionPane.showMessageDialog(null, "Error:" + e, "Error: ", JOptionPane.INFORMATION_MESSAGE);
		   return false;
		   
	   }
   }
   
   private static boolean isDirEmpty(final Path directory) throws IOException {
	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
	        return !dirStream.iterator().hasNext();
	    }
	}
   
}
