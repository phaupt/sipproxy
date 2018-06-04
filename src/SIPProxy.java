import ui.SIPProxyWindow;
import ui.SplashScreen;

public class SIPProxy {

    public static void main( String[] args ) {
        Thread splashThread = new Thread(new SplashScreen());
        splashThread.start();
        Thread.yield();

        SIPProxyWindow application = new SIPProxyWindow();
       
        try {
        	// Wait till SplashThread dies
			splashThread.join();
		} catch (InterruptedException e) {
		}
            
		application.setVisible(true);
        application.toFront();
		
		// Check Error
        application.checkErrorStatus();    
    }
    
}
