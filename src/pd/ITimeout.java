package pd;


public interface ITimeout {

    public String getMessage();

    public int getTimeInMilliseconds();
    public int getRemainingTimeInMilliseconds();
    public boolean timeoutExpired();
    
    public void startTimeout();

}