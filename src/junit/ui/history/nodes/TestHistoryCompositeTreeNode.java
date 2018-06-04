package junit.ui.history.nodes;

import java.awt.Color;

import ui.history.nodes.HistoryClient2PbxMessageTreeNode;
import ui.history.nodes.HistoryCompositeTreeNode;
import ui.history.nodes.HistoryMessageTreeNode;
import junit.framework.TestCase;

public class TestHistoryCompositeTreeNode extends TestCase {

    private HistoryCompositeTreeNode compositeNode;
    private String title = "TEST";

    public void setUp() {
        compositeNode = new HistoryCompositeTreeNode(title);
    }

    public void testTitle() {
        compositeNode.getTitle().equals(title);
    }

    public void testSubNodes() {
        HistoryMessageTreeNode node1 = new HistoryClient2PbxMessageTreeNode(null);
        HistoryMessageTreeNode node2 = new HistoryClient2PbxMessageTreeNode(null);

        // Test Add
        assertEquals(0, compositeNode.getChildCount());
        compositeNode.addChildNode(node1);
        assertEquals(1, compositeNode.getChildCount());
        compositeNode.addChildNode(node2);
        assertEquals(2, compositeNode.getChildCount());

        // Test Child
        assertNull(compositeNode.getChild("blabla"));
        assertEquals(compositeNode.getChild(0), node1);
        assertEquals(compositeNode.getChild(1), node2);
    }

    public void testInterfaceMethods() {
        assertTrue(compositeNode.getClosedIcon() != null);
        assertTrue(compositeNode.getOpenIcon() != null);
        assertTrue(compositeNode.getIcon() == null);
        assertTrue(new Color(0, 0, 0).equals(compositeNode.getTextColor()));
        assertFalse(compositeNode.isLeaf());
    }
}
