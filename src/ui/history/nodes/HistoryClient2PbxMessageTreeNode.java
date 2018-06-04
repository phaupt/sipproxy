package ui.history.nodes;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import proxy.UDPDatagram;

public class HistoryClient2PbxMessageTreeNode extends HistoryMessageTreeNode {

    private final String iconPath = "img/c2pbx.png";

    public HistoryClient2PbxMessageTreeNode(UDPDatagram packet) {
        super(packet);
    }

    public Icon getIcon() {
        return new ImageIcon(this.getClass().getResource(iconPath));
    }

    public Icon getClosedIcon() {
        // Return null because no Close-Icon is defined for this leaf-node
        return null;
    }

    public Icon getOpenIcon() {
        // Return null because no Open-Icon is defined for this leaf-node
        return null;
    }

    public Color getTextColor() {
        return new Color(0, 64, 0);
    }
}
