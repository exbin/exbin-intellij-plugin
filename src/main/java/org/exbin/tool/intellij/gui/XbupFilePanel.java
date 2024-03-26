package org.exbin.tool.intellij.gui;

import org.exbin.framework.App;
import org.exbin.framework.action.api.ActionConsts;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.action.api.ComponentActivationService;
import org.exbin.framework.editor.xbup.gui.XBDocStatusPanel;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.framework.frame.api.FrameModuleApi;

import javax.annotation.ParametersAreNonnullByDefault;
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
        ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
        FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
        ComponentActivationService componentActivationService = frameModule.getFrameHandler().getComponentActivationService();
        actionModule.buildMenu(menuBar, ActionConsts.MAIN_MENU_ID, componentActivationService);
//        menuBar.add(new JMenu("Menu (Test)"));
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
