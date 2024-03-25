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

import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.exbin.framework.App;
import org.exbin.framework.client.api.ClientModuleApi;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.framework.xbup.catalog.XBFileType;
import org.exbin.tool.intellij.gui.XbupFilePanel;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Virtual file for binary editor.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XbupVirtualFile extends VirtualFile implements DumbAware {

    public static final String PATH_PREFIX = "xbup://";

    private VirtualFile parentFile;
    private String displayName;
    private final XbupFilePanel filePanel = new XbupFilePanel();
    private FileHandler editorFile = new FileHandler();
    private boolean closed = false;

    public XbupVirtualFile() {
    }

    public void openVirtualFile(VirtualFile parentFile) {
        if (parentFile.getPath().startsWith(PATH_PREFIX)) {
            this.parentFile = LocalFileSystem.getInstance().findFileByPath(parentFile.getPath().substring(PATH_PREFIX.length()));
        } else {
            this.parentFile = parentFile;
        }
        String path = parentFile.getPath();
        int lastIndexOf = path.lastIndexOf('/');
        if (lastIndexOf >= 0) {
            this.displayName = path.substring(lastIndexOf + 1);
        } else {
            this.displayName = "";
        }
        final ClientModuleApi clientModule = App.getModule(ClientModuleApi.class);
        editorFile.setCatalog(clientModule.getCatalog());
        editorFile.setPluginRepository(clientModule.getPluginRepository());
        editorFile.loadFromFile(new File(path).toURI(), null);
        filePanel.setFileHandler(editorFile);
    }

    @Nonnull
    public JComponent getComponent() {
        return filePanel;
    }

    @NotNull
    @Override
    public String getName() {
        return parentFile.getName();
    }

    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    @Override
    public VirtualFileSystem getFileSystem() {
        return XbupFileSystem.getInstance();
    }

    @NotNull
    @Override
    public String getPath() {
        return PATH_PREFIX + parentFile.getPath();
    }

    @Override
    public boolean isWritable() {
        return parentFile.isWritable();
    }

    @Override
    public boolean isDirectory() {
        return parentFile.isDirectory();
    }

    @Override
    public boolean isValid() {
        return parentFile.isValid();
    }

    @Override
    public VirtualFile getParent() {
        return parentFile.getParent();
    }

    @Override
    public VirtualFile[] getChildren() {
        return parentFile.getChildren();
    }

    @NotNull
    @Override
    public OutputStream getOutputStream(Object requester, long newModificationStamp, long newTimeStamp) throws IOException {
        return parentFile.getOutputStream(requester, newModificationStamp, newTimeStamp);
    }

    @NotNull
    @Override
    public byte[] contentsToByteArray() throws IOException {
        return parentFile.contentsToByteArray();
    }

    @Override
    public long getTimeStamp() {
        return parentFile.getTimeStamp();
    }

    @Override
    public long getLength() {
        return parentFile.getLength();
    }

    @Override
    public void refresh(boolean asynchronous, boolean recursive, @Nullable Runnable postRunnable) {
        parentFile.refresh(asynchronous, recursive, postRunnable);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return parentFile.getInputStream();
    }

    public long getModificationStamp() {
        return parentFile.getModificationStamp();
    }

    @Override
    public long getModificationCount() {
        return parentFile.getModificationCount();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XbupVirtualFile that = (XbupVirtualFile) o;
        String path = getPath();
        return path.equals(that.getPath());
    }

    @Override
    public int hashCode() {
        return getPath().hashCode();
    }

    public boolean isMoved() {
        Boolean closingToReopen = getUserData(FileEditorManagerImpl.CLOSING_TO_REOPEN);
        return closingToReopen != null && closingToReopen;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

//    public JComponent getPreferredFocusedComponent() {
//        return getEditorFile().getPreferredFocusedComponent();
//    }
}
