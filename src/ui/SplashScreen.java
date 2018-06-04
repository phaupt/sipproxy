package ui;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JWindow;

import util.SimpleLogger;

public class SplashScreen extends JWindow implements Runnable {

    public void run() {
        this.setSize(425, 95);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        
        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
		}
 
        try {
            // Show Splash Screen
        	this.setVisible(true);  
			Thread.sleep(2500);
		} catch (InterruptedException e) {
            SimpleLogger.log(e.getMessage(), SimpleLogger.ERROR_LOG);
		}
		
        this.dispose();    
    }

    public void paint( Graphics g ) {
        Image splashImage = getToolkit().getImage(
                this.getClass().getResource("img/SplashLogo.gif"));
        g.drawImage(splashImage, 0, 0, this);
    }
}