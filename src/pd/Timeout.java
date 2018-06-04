package pd;


public class Timeout implements ITimeout {
//    private static Timer timer = new Timer();
    
	private int timeInMilliseconds = -1;
	private String message = "";
    private MyTimeout myTimeout = null;
    
    //Internal class to handle timeout 
    class MyTimeout{
        private long startTime = 0;
        
        public void start(){
            startTime = System.currentTimeMillis();
        }

        public boolean timeoutExpired(){
            return getRemainingTimeInMilliseconds() <=0;
        }
        
        public int getRemainingTimeInMilliseconds(){
            return timeInMilliseconds - (int) (System.currentTimeMillis() - startTime);
        }
    }
    

    public Timeout(int timeInMilliseconds, String message){
        if (timeInMilliseconds < 0){
            throw new IllegalArgumentException("timeInMilliseconds must be a positive integer value");
        }
        this.timeInMilliseconds = timeInMilliseconds;
        this.message = message;
    }

	public String getMessage() {
		return message;
	}

	public int getTimeInMilliseconds() {
		return timeInMilliseconds;
	}

	public boolean timeoutExpired(){
        boolean timeoutExpired = false;
        if(myTimeout != null){
            timeoutExpired = myTimeout.timeoutExpired();
        }
        return timeoutExpired;
    }
    
    public int getRemainingTimeInMilliseconds(){
        int remainingTimeInMilliseconds = 0;
        if(myTimeout != null){
            remainingTimeInMilliseconds = myTimeout.getRemainingTimeInMilliseconds();
        }
        return remainingTimeInMilliseconds;
    }

    public void startTimeout() {
        myTimeout = new MyTimeout();
        myTimeout.start();
    }
}
