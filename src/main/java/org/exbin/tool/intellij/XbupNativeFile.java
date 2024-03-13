/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.tool.intellij;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.LocalTimeCounter;
import com.intellij.util.messages.MessageBusConnection;
import org.exbin.bined.EditMode;
import org.exbin.bined.swing.extended.ExtCodeArea;
import org.exbin.framework.App;
import org.exbin.framework.client.api.ClientModuleApi;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.tool.intellij.gui.XbupFilePanel;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

/**
 * File editor wrapper using BinEd editor component.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupNativeFile {

    public static final String ACTION_CLIPBOARD_CUT = "cut-to-clipboard";
    public static final String ACTION_CLIPBOARD_COPY = "copy-to-clipboard";
    public static final String ACTION_CLIPBOARD_PASTE = "paste-from-clipboard";

    private final XbupFilePanel filePanel = new XbupFilePanel();
    private final XbupFileHandler fileHandler;

    private boolean opened = false;
    private VirtualFile virtualFile;
    private XbupFileEditorState fileEditorState = new XbupFileEditorState();
    private MessageBusConnection updateConnection = null;

    public XbupNativeFile(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
        fileHandler = new XbupFileHandler();
        filePanel.setFileHandler(fileHandler);
        final ClientModuleApi clientModule = App.getModule(ClientModuleApi.class);
        fileHandler.setCatalog(clientModule.getCatalog());
        fileHandler.setPluginRepository(clientModule.getPluginRepository());
//        fileHandler.setFileApi(this);
//        fileHandler.setFileHandlingMode(FileHandlingMode.NATIVE);
//        fileHandler.setUndoHandler(undoHandler);
        openFile(virtualFile);

        // TODO undoHandler = new BinaryUndoIntelliJHandler(codeArea, project, this);

//        fileHandler.setModifiedChangeListener(() -> {
//            updateModified();
//        });
//
//        ActionMap actionMap = fileHandler.getActionMap();
//        actionMap.put(ACTION_CLIPBOARD_COPY, new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                fileHandler.copy();
//            }
//        });
//        actionMap.put(ACTION_CLIPBOARD_CUT, new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                codeArea.cut();
//            }
//        });
//        actionMap.put(ACTION_CLIPBOARD_PASTE, new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                codeArea.paste();
//            }
//        });
    }

    public boolean isModified() {
        return false; // TODO fileHandler.isModified();
    }

    public boolean releaseFile() {
//        return fileHandler.releaseFile();
        return true;
    }

    @Nonnull
    public JComponent getPanel() {
        return filePanel;
    }

    public void openFile(VirtualFile virtualFile) {
        boolean editable = virtualFile.isWritable();

//        ExtCodeArea codeArea = fileHandler.getCodeArea();
//        codeArea.setContentData(new XbupFileDataWrapper(virtualFile));
//        codeArea.setEditMode(editable ? EditMode.EXPANDING : EditMode.READ_ONLY);
        fileHandler.loadFromFile(new File(virtualFile.getPath()).toURI(), null);

        opened = true;
//        documentOriginalSize = codeArea.getDataSize();
        updateModified();
//        updateCurrentMemoryMode();
//        fileHandler.getUndoHandler().clear();

        Project project = ProjectManager.getInstance().getDefaultProject();
        updateConnection = project.getMessageBus().connect();
//        updateConnection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
//            @Override
//            public void after(@NotNull List<? extends VFileEvent> events) {
//                for (VFileEvent event : events) {
//                    if (virtualFile.equals(event.getFile())) {
//                        XbupFileDataWrapper contentData = (XbupFileDataWrapper) codeArea.getContentData();
//                        if (!contentData.isWriteInProgress()) {
//                            contentData.resetCache();
//                            SwingUtilities.invokeLater(codeArea::notifyDataChanged);
//                            fileHandler.getUndoHandler().clear();
//                            break;
//                        }
//                    }
//                }
//            }
//        });
    }

    private void updateModified() {
//        boolean modified = fileHandler.isModified();
//        // TODO: Trying to force "modified behavior"
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        if (document instanceof DocumentEx) {
            ((DocumentEx) document).setModificationStamp(LocalTimeCounter.currentTime());
        }
//        propertyChangeSupport.firePropertyChange(FileEditor.PROP_MODIFIED, !modified, modified);
    }

    @Nullable
    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public JComponent getPreferredFocusedComponent() {
        return null;
    }
}
