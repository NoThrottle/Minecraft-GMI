package mainpkg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.wb.swt.SWTResourceManager;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class installgmi {
	
	Display display = Display.getDefault();
	Shell shlHsmcGmi = new Shell(display);
	GridLayout gridLayout = new GridLayout();
	
	Label lblNewLabel;
	Label lblLabel;
	
	Label lblModsFolderLocation;
	Text text;
	
	Button btnbrowse;
	Label lblProgress;
	ProgressBar progressBar;
	Button btndld;

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
		

	    gridLayout.numColumns = 4;
	    shlHsmcGmi.setLayout(gridLayout);
		
		shlHsmcGmi.setText("HSMC GMI");
		new Label(shlHsmcGmi, SWT.NONE);
		
		lblNewLabel = new Label(shlHsmcGmi, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Metropolis Semi Bold", 16, SWT.NORMAL));
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,true,false, 4, 1));
		lblNewLabel.setText("GMI for HighSkyMinecraft");
		
		lblLabel = new Label(shlHsmcGmi, SWT.NONE);
		lblLabel.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 9, SWT.NORMAL));
		lblLabel.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,true,false, 4, 1));
		lblLabel.setText("Pack Version: 1.1.2");
		
		lblModsFolderLocation = new Label(shlHsmcGmi, SWT.NONE);
		lblModsFolderLocation.setLayoutData(new GridData(SWT.BEGINNING,SWT.CENTER,true,false, 4, 1));
		lblModsFolderLocation.setText("Mods Folder Location:");
		
		text = new Text(shlHsmcGmi, SWT.BORDER);
		text.setText(GetClientModPath("mods").toString());		
		text.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false, 3, 1));	
		
		btnbrowse = new Button(shlHsmcGmi, SWT.NONE);
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
		btnbrowse.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false, 1, 1));
		btnbrowse.setText("Browse");
				
		lblProgress = new Label(shlHsmcGmi, SWT.NONE);
		lblProgress.setLayoutData(new GridData(SWT.BEGINNING,SWT.CENTER,true,false, 4, 1));
		lblProgress.setText("Progress:");
		
		progressBar = new ProgressBar(shlHsmcGmi, SWT.SMOOTH);
		GridData pbgd = new GridData(SWT.FILL,SWT.CENTER,true,false, 3, 1);
		pbgd.heightHint = 20;
		progressBar.setLayoutData(pbgd);
		
		btndld = new Button(shlHsmcGmi, SWT.NONE);
		btndld.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDown(MouseEvent e) {
				
				progressBar.setSelection(0);
				
				final String path = text.getText();
				
				if (VerifyPath(text.getText())) {
					btndld.setEnabled(false);
	  				new Thread(new Runnable() {
	  					
	  					@Override
	  					public void run() {
	  						
		  					PathCleanclass pcc = new PathCleanclass();
		  					pcc.path = path;
		  					Thread threads = new Thread(pcc);
		  					
		  					threads.start();
		  					try {
								threads.join();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	
	  						
	  						if(pcc.getValue()) {
	  							download();

	  						}else {
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
				}

			}
		});
		btndld.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,false,false, 1, 1));
		btndld.setText("Download");

		shlHsmcGmi.setMinimumSize(100, 39);
		shlHsmcGmi.pack();
		shlHsmcGmi.layout();
		
		
		Monitor primary = display.getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shlHsmcGmi.getBounds();
		    
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
		
		shlHsmcGmi.setLocation(x, y);
		shlHsmcGmi.open();
		
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

	}
	
	//////////////////////////////////////
	   
   private String[] modlist = 
	   	  {
	   			"https://mediafiles.forgecdn.net/files/3773/641/badpackets-fabric-0.1.2.jar",
	   			"https://cdn.modrinth.com/data/P7dR8mSH/versions/0.56.3+1.19/fabric-api-0.56.3%2B1.19.jar",
	   			"https://cdn.modrinth.com/data/Fb4jn8m6/versions/1.19-3.6.1/FallingTree-1.19-3.6.1.jar",
	   			"https://cdn.modrinth.com/data/1bZhdhsH/versions/fabric-1.19-1.2.16/plasmovoice-fabric-1.19-1.2.16.jar",
	   			"https://media.forgecdn.net/files/3831/918/better-nether-7.0.2.jar",
	   			"https://media.forgecdn.net/files/3831/904/better-end-2.0.2.jar",
	   			"https://cdn.modrinth.com/data/EsAfCjCV/versions/fabric-mc1.19-2.4.0/appleskin-fabric-mc1.19-2.4.0.jar",
	   			"https://media.forgecdn.net/files/3822/878/Croptopia-1.19-FABRIC-2.0.5.jar",
	   			"https://media.forgecdn.net/files/3831/901/bclib-2.0.5.jar",
	   			"https://media.forgecdn.net/files/3805/637/moreberries-1.5.1.jar",
	   			"https://cdn.modrinth.com/data/9s6osm5g/versions/7.0.72+fabric/cloth-config-7.0.72-fabric.jar",
	   			"https://media.forgecdn.net/files/3827/640/Couplings-1.9.0%2B1.19.jar",
	   			"https://media.forgecdn.net/files/3831/60/dehydration-1.2.9.jar",
	   			"https://media.forgecdn.net/files/3836/369/backslot-1.2.8.jar",
	   			"https://cdn.modrinth.com/data/hvFnDODi/versions/0.1.3/lazydfu-0.1.3.jar",
	   			"https://cdn.modrinth.com/data/Ha28R6CL/versions/1.8.0+kotlin.1.7.0/fabric-language-kotlin-1.8.0%2Bkotlin.1.7.0.jar",
	   			"https://cdn.modrinth.com/data/E6FUtRJh/versions/3.5.4-fabric/Adorn-3.5.4%2B1.19-fabric.jar",
	   			"https://media.forgecdn.net/files/3824/701/time-and-wind-ct-1.4.2%2B1.19.jar",
	   			"https://mediafiles.forgecdn.net/files/3840/877/immersive-portals-2.0.2-mc1.19-fabric.jar",
	   			"https://cdn.modrinth.com/data/nU0bVIaL/versions/1.19-72-fabric/Patchouli-1.19-72-FABRIC.jar",
	   			"https://mediafiles.forgecdn.net/files/3830/133/Oh_The_Biomes_You%27ll_Go-fabric-1.19-2.0.0.4.jar",
	   			"https://mediafiles.forgecdn.net/files/3838/384/TerraBlender-fabric-1.19-2.0.0.113.jar",
	   			"https://mediafiles.forgecdn.net/files/3841/599/bitsandchisels-2.7.0.jar",
	   			"https://mediafiles.forgecdn.net/files/3834/909/levelz-1.3.6.jar",
	   			"https://mediafiles.forgecdn.net/files/3831/66/rpgz-0.5.1.jar",
	   			"https://mediafiles.forgecdn.net/files/3841/562/wraith-waystones-3.0.1%2Bmc1.19.jar",
	   			"https://mediafiles.forgecdn.net/files/3844/189/SimpleBackpack_Fabric-1.19.0-1.3.16.jar",
	   			"https://mediafiles.forgecdn.net/files/3821/57/xp_obelisk-0.4.0.r_for_1.19.x_Fabric.jar",
	   			"https://mediafiles.forgecdn.net/files/3834/982/earthtojavamobs-fabric-mc1.19-1.9.0%2B1.19.jar",
	   			"https://mediafiles.forgecdn.net/files/3843/623/architectury-5.7.28-fabric.jar",
	   			"https://mediafiles.forgecdn.net/files/3822/773/soundphysics-fabric-1.19-1.0.6.jar",
	   			"https://mediafiles.forgecdn.net/files/3823/521/notenoughanimations-fabric-1.6.0-mc1.19.jar",
	   			"https://cdn.modrinth.com/data/6AQIaxuO/versions/fabric-5.4.3/wthit-fabric-5.4.3.jar",
	   			"https://mediafiles.forgecdn.net/files/3838/331/vmp-fabric-mc1.19-0.2.0%2Bbeta.5.0-all.jar",
	   			"https://mediafiles.forgecdn.net/files/3843/368/fabrication-3.0-pre5%2B1.19.jar",
	   			"https://mediafiles.forgecdn.net/files/3819/689/transportables-1.2.1.jar",
	   			"https://mediafiles.forgecdn.net/files/3828/191/emotecraft-for-MC1.19-2.1.3-SNAPSHOT-build.30-fabric.jar",
	   			"https://mediafiles.forgecdn.net/files/3822/828/mcdar-1.5.0.jar",
	   			"https://mediafiles.forgecdn.net/files/3821/450/mcda-2.1.0.jar",
	   			"https://mediafiles.forgecdn.net/files/3822/127/weaponmaster-multi-fabric-1-19.jar",
	   			"https://cdn.modrinth.com/data/WhbRG4iK/versions/1.12.1/fallingleaves-1.12.1%2B1.19.jar",
	   			"https://cdn.modrinth.com/data/rUgZvGzi/versions/1.8.1/eating-animation-1.8.1.jar",
	   			"https://cdn.modrinth.com/data/aC3cM3Vq/versions/1.19-2.22-fabric/MouseTweaks-fabric-mc1.19-2.22.jar",
	   			"https://cdn.modrinth.com/data/YL57xq9U/versions/1.19.x-v1.2.5/iris-mc1.19-1.2.5.jar",
	   			"https://cdn.modrinth.com/data/yBW8D80W/versions/2.1.2+1.19/lambdynamiclights-2.1.2%2B1.19.jar",
	   			"https://cdn.modrinth.com/data/AANobbMI/versions/mc1.19-0.4.2/sodium-fabric-mc1.19-0.4.2%2Bbuild.16.jar",
	   			"https://cdn.modrinth.com/data/Orvt0mRa/versions/1.0.6+mc1.19/indium-1.0.6%2Bmc1.19.jar",
	   			"https://cdn.modrinth.com/data/rI0hvYcd/versions/0.5.0/visuality-0.5.0.jar",
	   			"https://mediafiles.forgecdn.net/files/3829/237/extrasounds-2.3.1%2B1.19.jar",
	   			"https://mediafiles.forgecdn.net/files/3839/977/betterfpsdist-fabric-1.19-1.9.jar",
	   			"https://mediafiles.forgecdn.net/files/3834/55/lambdabettergrass-1.3.0%2B1.19.jar",
	   			"https://mediafiles.forgecdn.net/files/3823/508/entityculling-fabric-1.5.2-mc1.19.jar",
	   			"https://mediafiles.forgecdn.net/files/3841/824/enhancedblockentities-0.7.1%2B1.19.jar",
	   			"https://mediafiles.forgecdn.net/files/3823/897/CustomSkinLoader_Fabric-14.14-SNAPSHOT-325.jar",
	   			"https://mediafiles.forgecdn.net/files/3834/421/BetterThirdPerson-Fabric-1.19-1.8.1.jar",
   			};
  
   private String[] d_modlist = {
		   "https://cdn.modrinth.com/data/uXXizFIs/versions/5.0.0-fabric/ferritecore-5.0.0-fabric.jar",
		   "https://cdn.modrinth.com/data/fQEb0iXm/versions/0.2.0/krypton-0.2.0.jar",
		   "https://cdn.modrinth.com/data/gvQqBUqZ/versions/mc1.19-0.8.0/lithium-fabric-mc1.19-0.8.0.jar",
		   "https://cdn.modrinth.com/data/H8CaAYZC/versions/1.1.1+1.19/starlight-1.1.1%2Bfabric.ae22326.jar",
		   "https://cdn.modrinth.com/data/TYaqEeAP/versions/1.6.0/SimplerAuth-1.6.0.jar",
		   "https://cdn.modrinth.com/data/muf0XoRe/versions/6.0.4+1.19/repurposed_structures_fabric-6.0.4%2B1.19.jar",
		   "https://cdn.modrinth.com/data/sml2FMaA/versions/mc1.19-1.2.2-fabric/anti-xray-1.2.2-Fabric-1.19.jar",
		   "https://cdn.modrinth.com/data/cUhi3iB2/versions/1.3.14/tabtps-fabric-mc1.19-1.3.14.jar",
		   "https://mediafiles.forgecdn.net/files/3819/692/stoneholm-1.4.4.jar",
		   "https://cdn.modrinth.com/data/P7dR8mSH/versions/0.56.3+1.19/fabric-api-0.56.3%2B1.19.jar",
		   "https://cdn.modrinth.com/data/Fb4jn8m6/versions/1.19-3.6.1/FallingTree-1.19-3.6.1.jar",
		   "https://cdn.modrinth.com/data/1bZhdhsH/versions/fabric-1.19-1.2.16/plasmovoice-fabric-1.19-1.2.16.jar",
		   "https://media.forgecdn.net/files/3831/918/better-nether-7.0.2.jar",
		   "https://media.forgecdn.net/files/3831/904/better-end-2.0.2.jar",
		   "https://cdn.modrinth.com/data/EsAfCjCV/versions/fabric-mc1.19-2.4.0/appleskin-fabric-mc1.19-2.4.0.jar",
		   "https://media.forgecdn.net/files/3822/878/Croptopia-1.19-FABRIC-2.0.5.jar",
		   "https://media.forgecdn.net/files/3831/901/bclib-2.0.5.jar",
		   "https://media.forgecdn.net/files/3805/637/moreberries-1.5.1.jar",
		   "https://cdn.modrinth.com/data/9s6osm5g/versions/7.0.72+fabric/cloth-config-7.0.72-fabric.jar",
		   "https://media.forgecdn.net/files/3827/640/Couplings-1.9.0%2B1.19.jar",
		   "https://media.forgecdn.net/files/3831/60/dehydration-1.2.9.jar",
		   "https://media.forgecdn.net/files/3836/369/backslot-1.2.8.jar",
		   "https://cdn.modrinth.com/data/hvFnDODi/versions/0.1.3/lazydfu-0.1.3.jar",
		   "https://cdn.modrinth.com/data/Ha28R6CL/versions/1.8.0+kotlin.1.7.0/fabric-language-kotlin-1.8.0%2Bkotlin.1.7.0.jar",
		   "https://cdn.modrinth.com/data/E6FUtRJh/versions/3.5.4-fabric/Adorn-3.5.4%2B1.19-fabric.jar",
		   "https://media.forgecdn.net/files/3824/701/time-and-wind-ct-1.4.2%2B1.19.jar",
		   "https://mediafiles.forgecdn.net/files/3840/877/immersive-portals-2.0.2-mc1.19-fabric.jar",
		   "https://cdn.modrinth.com/data/nU0bVIaL/versions/1.19-72-fabric/Patchouli-1.19-72-FABRIC.jar",
		   "https://mediafiles.forgecdn.net/files/3830/133/Oh_The_Biomes_You%27ll_Go-fabric-1.19-2.0.0.4.jar",
		   "https://mediafiles.forgecdn.net/files/3838/384/TerraBlender-fabric-1.19-2.0.0.113.jar",
		   "https://mediafiles.forgecdn.net/files/3841/599/bitsandchisels-2.7.0.jar",
		   "https://mediafiles.forgecdn.net/files/3834/909/levelz-1.3.6.jar",
		   "https://mediafiles.forgecdn.net/files/3831/66/rpgz-0.5.1.jar",
		   "https://mediafiles.forgecdn.net/files/3841/562/wraith-waystones-3.0.1%2Bmc1.19.jar",
		   "https://mediafiles.forgecdn.net/files/3844/189/SimpleBackpack_Fabric-1.19.0-1.3.16.jar",
		   "https://mediafiles.forgecdn.net/files/3821/57/xp_obelisk-0.4.0.r_for_1.19.x_Fabric.jar",
		   "https://mediafiles.forgecdn.net/files/3834/982/earthtojavamobs-fabric-mc1.19-1.9.0%2B1.19.jar",
		   "https://mediafiles.forgecdn.net/files/3843/623/architectury-5.7.28-fabric.jar",
		   "https://mediafiles.forgecdn.net/files/3822/773/soundphysics-fabric-1.19-1.0.6.jar",
		   "https://mediafiles.forgecdn.net/files/3823/521/notenoughanimations-fabric-1.6.0-mc1.19.jar",
		   "https://cdn.modrinth.com/data/6AQIaxuO/versions/fabric-5.4.3/wthit-fabric-5.4.3.jar",
		   "https://mediafiles.forgecdn.net/files/3838/331/vmp-fabric-mc1.19-0.2.0%2Bbeta.5.0-all.jar",
		   "https://mediafiles.forgecdn.net/files/3843/368/fabrication-3.0-pre5%2B1.19.jar",
		   "https://mediafiles.forgecdn.net/files/3819/689/transportables-1.2.1.jar",
		   "https://mediafiles.forgecdn.net/files/3828/191/emotecraft-for-MC1.19-2.1.3-SNAPSHOT-build.30-fabric.jar",
		   "https://mediafiles.forgecdn.net/files/3822/828/mcdar-1.5.0.jar",
		   "https://mediafiles.forgecdn.net/files/3821/450/mcda-2.1.0.jar",
		   "https://mediafiles.forgecdn.net/files/3822/127/weaponmaster-multi-fabric-1-19.jar",
		   "https://mediafiles.forgecdn.net/files/3773/641/badpackets-fabric-0.1.2.jar",
   };
   
   public void download() {
      int i = 0;
      
      final String[] path = new String[1];

      Display.getDefault().syncExec(
    		  new Runnable() {
			          public void run(){
			
			               path[0] = text.getText();
			          }
    		  	}
    		  );
      
      Display.getDefault().asyncExec(() -> progressBar.setMaximum(modlist.length-1));
      
      
      new Thread(new Runnable() {			
			@Override
			public void run() {				
				
				
				boolean miam = true;
	              
	              while(miam) {
	            	  
				      final int[] selection = new int[1];
				      Display.getDefault().syncExec(
				    		  new Runnable() {
							          public void run(){
							
							              selection[0] = progressBar.getSelection();
							          }
				    		  	}
				    		  );
	            	  
		              if (selection[0] == modlist.length-1) {
		            	  
//		            	  System.out.println(modlist.length);
//		            	  System.out.println(selection[0]);
		            	  
							JOptionPane.showMessageDialog(null, "Download is complete", "Confirmation: ", JOptionPane.INFORMATION_MESSAGE);
							
							 if (display.isDisposed())
						            return;
						          display.asyncExec(new Runnable() {
						            public void run() {
						              if (btndld.isDisposed())
						                return;
						              btndld.setEnabled(true);
						            }
						          });
						     miam = false;
							
						}
		              else {
		            	  
		            	  System.out.println(modlist.length);
		            	  System.out.println(selection[0]);
		            	  
		            	  try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		              }
	              }
				
				
			}			        
	  }).start();
      
	  while(i != modlist.length) {
		  			  
		  final int a = i;
		  final String b = path[0];
		  new Thread(new Runnable() {
			  
				String url = modlist[a];	
				
				@Override
				public void run() {
			        try {
			            downloadUsingNIO(url, b + "/mods_01_" + a + "-" + url.split("/")[url.split("/").length-1] + ".jar");
			            
				        if (display.isDisposed())
				            return;
				          display.asyncExec(new Runnable() {
				            public void run() {
				              if (progressBar.isDisposed())
				                return;
				              progressBar.setSelection(progressBar.getSelection()+1);
				            }
				          });
			        } catch (IOException e) {
			        	JOptionPane.showMessageDialog(null, "Download failed: " + e, "Error: ", JOptionPane.INFORMATION_MESSAGE);
			            e.printStackTrace();
			        }
			        

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
		   JOptionPane.showMessageDialog(null, "Specified path/folder does not exist. Try launching Minecraft with the mod loader first.");
		   return false;
	   }	   
   }
   
}


