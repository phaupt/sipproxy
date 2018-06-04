package ui.testexec;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;


public class TestCaseHistoryTreeCellRenderer extends DefaultTreeCellRenderer {
    private final String warningIconPath = "img/warning.gif";
    private final String openIconPath = "img/folderOpen.gif";
    private final String closedIconPath = "img/folderClosed.gif";

    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
        // TODO Auto-generated method stub
        super
                .getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        
        if(leaf){
            setIcon(new ImageIcon(this.getClass().getResource(warningIconPath)));
        }
        else if(expanded){
            setIcon(new ImageIcon(this.getClass().getResource(openIconPath)));
        }
        else{
            setIcon(new ImageIcon(this.getClass().getResource(closedIconPath)));
        }
        return this;
        
    }
}
