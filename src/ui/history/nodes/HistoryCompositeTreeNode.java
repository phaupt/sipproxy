package ui.history.nodes;

import java.awt.Color;
import java.util.Observable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import proxy.UDPDatagram;

import ui.history.HistoryTreeChange;

public class HistoryCompositeTreeNode extends Observable implements IHistoryTreeNode {

    private String title;
    private final String closedIconPath = "img/folderClosed.gif";
    private final String openIconPath = "img/folderOpen.gif";

    Vector<IHistoryTreeNode> childNodes = new Vector<IHistoryTreeNode>();

    public HistoryCompositeTreeNode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addChildNode( IHistoryTreeNode childNode ) {
        childNodes.add(childNode);
        setChanged();
        notifyObservers(new HistoryTreeChange(HistoryTreeChange.ADDED, childNode,
                childNodes.indexOf(childNode), this));
    }

    public boolean isLeaf() {
        return false;
    }

    public int getChildCount() {
        return childNodes.size();
    }

    public int getIndexOfChild( IHistoryTreeNode node ) {
        return childNodes.indexOf(node);
    }

    public IHistoryTreeNode getChild( int index ) {
        return childNodes.elementAt(index);
    }

    public IHistoryTreeNode getChild( String title ) {
        IHistoryTreeNode child = null;
        for (IHistoryTreeNode childNode : childNodes) {
            if (childNode.getTitle() != null ? childNode.getTitle().equals(title) : false) {
                child = childNode;
                break;
            }
        }
        return child;
    }

    public IHistoryTreeNode removeChild( int index ) {
        IHistoryTreeNode removedNode = childNodes.remove(index);
        setChanged();
        notifyObservers(new HistoryTreeChange(HistoryTreeChange.REMOVED, removedNode,
                index, this));

        return removedNode;
    }

    public Icon getIcon() {
        // Return null because no Default is defined for this non-leaf-node
        return null;
    }

    public Icon getClosedIcon() {
        return new ImageIcon(this.getClass().getResource(closedIconPath));
    }

    public Icon getOpenIcon() {
        return new ImageIcon(this.getClass().getResource(openIconPath));
    }

    public Color getTextColor() {
        return new Color(0, 0, 0);
    }

    public UDPDatagram getUDPDatagram() {
        return null;
    }

}
