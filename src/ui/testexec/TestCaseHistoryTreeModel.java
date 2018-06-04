package ui.testexec;

import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import testexec.history.ITestCaseHistory;
import testexec.history.ITestCaseHistoryObserver;
import testexec.history.ITestCaseRun;
import testexec.history.ITestCaseRunObserver;


public class TestCaseHistoryTreeModel implements TreeModel, ITestCaseHistoryObserver, ITestCaseRunObserver {

    private ITestCaseHistory testCaseHistory = null;
    private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();

    
    public TestCaseHistoryTreeModel(ITestCaseHistory testCaseHistory){
        this.testCaseHistory = testCaseHistory;

        //Add to observer list
        testCaseHistory.addTestHistoryObserver(this);
    }
    
    public Object getChild( Object parent, int index ) {
        Object child = null;
        if(parent == testCaseHistory){
            child = testCaseHistory.getTestCaseRuns().get(index);
        }
        else{
            //Parent = TestCaseRun
            ITestCaseRun testCaseRun = ((ITestCaseRun) parent);
            child = testCaseRun.getWarningRequestMsgIDs().get(index);
        }
        return child;
    }

    public int getChildCount( Object parent ) {
        int childCount = -1;
        if(parent == testCaseHistory){
            childCount = testCaseHistory.getTestCaseRuns().size();
        }
        else{
            //Parent = TestCaseRun
            ITestCaseRun testCaseRun = ((ITestCaseRun) parent);
            childCount = testCaseRun.getWarningRequestMsgIDs().size();
        }
        return childCount;
    }

    public int getIndexOfChild( Object parent, Object child ) {
        int indexOfChild = -1;
        if(parent == testCaseHistory){
            indexOfChild = testCaseHistory.getTestCaseRuns().indexOf(child);
        }
        else{
            //Parent = TestCaseRun
            ITestCaseRun testCaseRun = ((ITestCaseRun) parent);
            indexOfChild = testCaseRun.getWarningRequestMsgIDs().indexOf(child);
        }

        return indexOfChild;
    }

    public Object getRoot() {
        return testCaseHistory;
    }

    public boolean isLeaf( Object node ) {
        if (node instanceof ITestCaseHistory || node instanceof ITestCaseRun) {
            return false;
            
        }
        else{
            return true;
        }
    }

    public void addTreeModelListener( TreeModelListener l ) {
        treeModelListeners.add(l);

    }

    public void removeTreeModelListener( TreeModelListener l ) {
        treeModelListeners.remove(l);

    }
    public void valueForPathChanged( TreePath path, Object newValue ) {
        // do nothing

    }

    public void testCaseRunAdded( ITestCaseRun testCaseRun ) {
        testCaseRun.addTestCaseRunObserver(this);

        // PathToParent -> Nodes to Root without the new Node
        Object[] pathToParent;
        pathToParent = new Object[] { getRoot() };
        int index = testCaseHistory.getTestCaseRuns().indexOf(testCaseRun);
        int[] childIndices = new int[] {index};
        Object[] child = new Object[] { testCaseRun};
        
        fireTreeNodesInserted(new TreeModelEvent(this,pathToParent,childIndices, child));                    
    }
    

    public void testCaseRunRemoved( ITestCaseRun testCaseRun, int index ) {
        testCaseRun.removeTestCaseRunObserver(this);

        // PathToParent -> Nodes to Root without the new Node
        Object[] pathToParent;
        pathToParent = new Object[] { getRoot() };
        int[] childIndices = new int[] {index};
        Object[] child = new Object[] { testCaseRun};
        
        fireTreeNodesRemoved(new TreeModelEvent(this,pathToParent,childIndices, child));        
        
    }
    
    private void fireTreeNodesInserted( TreeModelEvent e ) {
        for (TreeModelListener listener : treeModelListeners) {
            listener.treeNodesInserted(e);
        }
    }
    private void fireTreeNodesRemoved( TreeModelEvent e ) {
        for (TreeModelListener listener : treeModelListeners) {
            listener.treeNodesRemoved(e);
        }
    }
    
    public void warningAdded(ITestCaseRun testCaseRun, String requestMsgID ) {
        // PathToParent -> Nodes to Root without the new Node
        Object[] pathToParent;
        pathToParent = new Object[] { getRoot(), testCaseRun };
        int index = testCaseRun.getWarningRequestMsgIDs().indexOf(requestMsgID);
        int[] childIndices = new int[] {index};
        Object[] child = new Object[] { requestMsgID};
        
        fireTreeNodesInserted(new TreeModelEvent(this,pathToParent,childIndices, child));                    
        
        
    }

    public void cyclesChanged() {
        // do nothing
    }

    public void endTimestampChanged() {
        // do nothing
    }

    public void okayAdded() {
        // do nothing
    }

    public void statusChanged() {
        // do nothing
    }

    public void unknownAdded() {
        // do nothing
    }

    public void targetUAChanged() {
        // TODO Auto-generated method stub
        
    }

}
