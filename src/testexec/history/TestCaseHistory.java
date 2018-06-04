package testexec.history;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

public class TestCaseHistory implements ITestCaseHistory {
    private Vector<ITestCaseRun> testCaseRuns = new Vector<ITestCaseRun>();
    private Vector<ITestCaseHistoryObserver> observers = new Vector<ITestCaseHistoryObserver>();
    
    public TestCaseHistory(File directory){
        //Create file filter --> only accept statistic xml files
        FilenameFilter filter = new FilenameFilter(){
            public boolean accept( File dir, String name ) {
                return name.endsWith(ITestCaseRun.DEFAULT_STATISTICS_SUFFIX);
            }
        };        
        //Add all TestRuns in the directory to the vector
        File[] statisticsXMLFiles = directory.listFiles(filter);
        for(File xmlStatistic : statisticsXMLFiles){
            ITestCaseRun testCaseRun = TestCaseRun.load( xmlStatistic, new HistoryLogsFactory());
            if(testCaseRun != null){
                addTestCaseRun(testCaseRun);
            }
        }
    }
    public void addTestCaseRun( ITestCaseRun testCaseRun ) {
        testCaseRuns.add(testCaseRun);
        notifyObservers_Add(testCaseRun);
    }
    
    public void deleteTestCaseRun( ITestCaseRun testCaseRun ) {
        int index = testCaseRuns.indexOf(testCaseRun);
        testCaseRuns.remove(testCaseRun);
        testCaseRun.delete();
        notifyObservers_Remove(testCaseRun, index);
        
        
    }
    
    public Vector<ITestCaseRun> getTestCaseRuns() {
        return testCaseRuns;
    }
    public void addTestHistoryObserver( ITestCaseHistoryObserver observer ) {
        synchronized(observers){          
            observers.add(observer);
        }
        
    }
    public void removeTestHistoryRunObserver( ITestCaseHistoryObserver observer ) {
        synchronized(observers){          
            observers.remove(observer);
        }
    }  
    
    private void notifyObservers_Add(ITestCaseRun testCaseRun){
        synchronized(observers){            
            for(ITestCaseHistoryObserver observer : observers){
                observer.testCaseRunAdded(testCaseRun);
            }
        }
    }
    
    private void notifyObservers_Remove(ITestCaseRun testCaseRun, int index){
        synchronized(observers){
            for(ITestCaseHistoryObserver observer : observers){
                observer.testCaseRunRemoved(testCaseRun, index);
            }
        }
    }
}
