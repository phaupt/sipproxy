package testexec.history;

import java.io.File;

import org.jfree.chart.JFreeChart;


public interface ITestCaseRunReport {
    public abstract void createReport(ITestCaseRun testCaseRun, File reportFile, JFreeChart chart);
}
