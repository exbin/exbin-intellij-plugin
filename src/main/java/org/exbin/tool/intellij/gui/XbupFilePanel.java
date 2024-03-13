package org.exbin.tool.intellij.gui;

import org.exbin.framework.editor.xbup.gui.XBDocStatusPanel;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import java.awt.BorderLayout;

@ParametersAreNonnullByDefault
public class XbupFilePanel extends JPanel {

    private XbupFileHandler fileHandler;
    private JMenuBar menuBar = new JMenuBar();
    private XBDocStatusPanel statusPanel = new XBDocStatusPanel();

    public XbupFilePanel() {
        super(new BorderLayout());
        menuBar.add(new JMenu("Menu (Test)"));
        add(menuBar, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);
    }

    public void setFileHandler(XbupFileHandler fileHandler) {
        this.fileHandler = fileHandler;
        add(fileHandler.getComponent(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
