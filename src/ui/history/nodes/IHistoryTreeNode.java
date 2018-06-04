package ui.history.nodes;

import java.awt.Color;

import javax.swing.Icon;

import proxy.UDPDatagram;

public interface IHistoryTreeNode {

    String getTitle();

    Icon getIcon();

    Icon getClosedIcon();

    Icon getOpenIcon();

    Color getTextColor();

    boolean isLeaf();

    int getChildCount();

    int getIndexOfChild( IHistoryTreeNode node );

    IHistoryTreeNode getChild( int index );

    UDPDatagram getUDPDatagram();

}
