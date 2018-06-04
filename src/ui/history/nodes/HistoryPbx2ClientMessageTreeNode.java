package ui.history.nodes;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import proxy.UDPDatagram;

public class HistoryPbx2ClientMessageTreeNode extends HistoryMessageTreeNode {

    private final String iconPath = "img/pbx2c.png";

    public HistoryPbx2ClientMessageTreeNode(UDPDatagram packet) {
        super(packet);
    }

    public Icon getIcon() {
        return new ImageIcon(this.getClass().getResource(iconPath));
    }

    public Icon getClosedIcon() {
        // Return null because no Closed-Icon is defined for this leaf-node
        return null;
    }

    public Icon getOpenIcon() {
        // Return null because no Open-Icon is defined for this leaf-node
        return null;
    }

    public Color getTextColor() {
        return new Color(64, 0, 0);
    }

}
