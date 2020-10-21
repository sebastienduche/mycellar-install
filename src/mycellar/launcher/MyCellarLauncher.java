package mycellar.launcher;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * <p>Titre : Cave à vin</p>
 * <p>Description : Votre description</p>
 * <p>Copyright : Copyright (c) 2011</p>
 * <p>Société : Seb Informatique</p>
 * @author Sébastien Duché
 * @version 1.3
 * @since 08/03/19
 */
class MyCellarLauncher {

	private MyCellarLauncher() {
		
		File myCellarFile = new File("MyCellar.jar");
		File directory = null;
		boolean install = true;
		if (myCellarFile.exists()) {
			install = JOptionPane.YES_OPTION == showConfirmDialog(null, "A version of MyCellar already exist in this directory.\nDo you want to reinstall it",
					"Question", JOptionPane.YES_NO_OPTION);
		}
		if (getVersion() < 11) {
			showMessageDialog(null, "Java version 11 minimum is required!\n Please install it via www.java.com", "Error", ERROR_MESSAGE);
			return;
		}
    	if (install) {
				Server.Debug("New Installation...");
    		directory = install();
    		if (directory == null) {
        		System.exit(1);
    		} else {
    			Server.Debug("Installation Done");
    		}
    	}
    		
        try {
					ProcessBuilder pb = new ProcessBuilder("java","-Dfile.encoding=UTF8","-jar","MyCellar.jar");
					pb.directory(directory);
					pb.redirectErrorStream(true);
					Process p = pb.start();
					p.waitFor(10, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException ex) {
            showException(ex);
        }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MyCellarLauncher();

	}

	double getVersion() {
		String version = System.getProperty("java.version");
		int pos = version.indexOf('.');
		pos = version.indexOf('.', pos+1);
		return Double.parseDouble (version.substring (0, pos));
	}


	private static void showException(Exception e) {
		StackTraceElement[] st = e.getStackTrace();
		String error = "";
		for (StackTraceElement elem : st) {
			error = error.concat("\n" + elem);
		}
		showMessageDialog(null, e.toString(), "Error", ERROR_MESSAGE);
		System.exit(999);
	}
	
	private File install() {
		return Server.getInstance().install();
	}

}
