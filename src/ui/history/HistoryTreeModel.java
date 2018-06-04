package ui.history;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import proxy.SIPHistory;
import ui.history.nodes.HistoryCompositeTreeNode;
import ui.history.nodes.IHistoryTreeNode;

public class HistoryTreeModel implements TreeModel, Observer {

    public static final int ORDER_BY_TIME_MODE = 0;
    public static final int GROUP_BY_CSEQ_MODE = 1;

    private int mode = ORDER_BY_TIME_MODE;
    private HistoryCompositeTreeNode rootNode;
    private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();
    private SIPHistory sipHistory;

    public HistoryTreeModel(SIPHistory sipHistory) {
        this.sipHistory = sipHistory;
        updateTree();
    }

    // Sign Off Observer from Observable and all its Observable Child-Nodes
    private void signOffObserver( HistoryCompositeTreeNode compositeNode ) {
        compositeNode.deleteObserver(this);
        for (int i = 0; i < compositeNode.getChildCount(); i++) {
            IHistoryTreeNode node = compositeNode.getChild(i);
            if (node instanceof HistoryCompositeTreeNode) {
                signOffObserver((HistoryCompositeTreeNode) node);
            }
        }
    }

    // Register Observer to Observable with and all Observable Child-Nodes
    private void registerObserver( HistoryCompositeTreeNode compositeNode ) {
        compositeNode.addObserver(this);
        for (int i = 0; i < compositeNode.getChildCount(); i++) {
            IHistoryTreeNode node = compositeNode.getChild(i);
            if (node instanceof HistoryCompositeTreeNode) {
                registerObserver((HistoryCompositeTreeNode) node);
            }
        }
    }

    private void updateTree() {
        // Sign off Observer from old Root-Node
        if (rootNode != null) {
            signOffObserver(rootNode);
        }

        switch (mode) {
            case GROUP_BY_CSEQ_MODE:
                rootNode = sipHistory.getGroupedByCSeqRoot();
                break;
            case ORDER_BY_TIME_MODE:
                rootNode = sipHistory.getOrderedByTimeRoot();
                break;
        }

        // Attach Observer to new Root-Node
        registerObserver(rootNode);

        fireTreeStructureChanged();
    }

    public void setMode( int mode ) {
        // Set the mode
        this.mode = mode;
        updateTree();
    }

    public Object getRoot() {
        return rootNode;
    }

    public Object getChild( Object parent, int index ) {
        return ((IHistoryTreeNode) parent).getChild(index);
    }

    public int getChildCount( Object parent ) {
        return ((IHistoryTreeNode) parent).getChildCount();
    }

    public boolean isLeaf( Object node ) {
        return ((IHistoryTreeNode) node).isLeaf();
    }

    public void valueForPathChanged( TreePath path, Object newValue ) {
        // do nothing

    }

    public int getIndexOfChild( Object parent, Object child ) {
        return ((IHistoryTreeNode) parent).getIndexOfChild((IHistoryTreeNode) child);
    }

    public void addTreeModelListener( TreeModelListener l ) {
        treeModelListeners.add(l);

    }

    public void removeTreeModelListener( TreeModelListener l ) {
        treeModelListeners.remove(l);

    }

    private void fireTreeStructureChanged() {
        for (TreeModelListener listener : treeModelListeners) {
            listener.treeStructureChanged(new TreeModelEvent(this,
                    new Object[] { rootNode }));
        }
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

    public void update( Observable o, Object arg ) {
        HistoryTreeChange treeChange = (HistoryTreeChange) arg;
        IHistoryTreeNode childNode = treeChange.getNode();
        IHistoryTreeNode parentNode = treeChange.getParentNode();

        // PathToParent -> Nodes to Root without the new Node
        Object[] pathToParent;
        if (parentNode == rootNode) {
            pathToParent = new Object[] { parentNode };
        }
        else {
            pathToParent = new Object[] { rootNode, parentNode };
        }
        int[] childIndices = new int[] { treeChange.getIndex() };
        Object[] child = new Object[] { childNode };

        if (treeChange.getType() == HistoryTreeChange.ADDED) {

            if (childNode instanceof HistoryCompositeTreeNode) {
                ((HistoryCompositeTreeNode) childNode).addObserver(this);
            }
            fireTreeNodesInserted(new TreeModelEvent(this, pathToParent, childIndices,
                    child));
        }
        else if (treeChange.getType() == HistoryTreeChange.REMOVED) {
            if (childNode instanceof HistoryCompositeTreeNode) {
                ((HistoryCompositeTreeNode) childNode).deleteObserver(this);
            }
            fireTreeNodesRemoved(new TreeModelEvent(this, pathToParent, childIndices,
                    child));

        }

    }

}
