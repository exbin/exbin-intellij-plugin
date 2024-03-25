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

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * File editor provider for XBUP files.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XbupFileEditorProvider implements FileEditorProvider, DumbAware {

    public static final String XBUP_EDITOR_TYPE_ID = "org.exbin.xbup";

    @Override
    public void disposeEditor(@NotNull FileEditor editor) {
        if (editor instanceof XbupFileEditor) {
            editor.dispose();
        }
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return file instanceof XbupVirtualFile || file.getPath().startsWith(XbupVirtualFile.PATH_PREFIX);
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        XbupVirtualFile xbupVirtualFile;
        if (virtualFile instanceof XbupVirtualFile) {
            xbupVirtualFile = (XbupVirtualFile) virtualFile;
        } else {
            xbupVirtualFile = new XbupVirtualFile();
            xbupVirtualFile.openVirtualFile(virtualFile);
        }

        XbupFileEditor fileEditor = new XbupFileEditor(project, xbupVirtualFile);
        fileEditor.setDisplayName(xbupVirtualFile.getDisplayName());

        return fileEditor;
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return XBUP_EDITOR_TYPE_ID;
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element sourceElement, @NotNull Project project, @NotNull VirtualFile file) {
        return FileEditorState.INSTANCE;
    }

    @Override
    public void writeState(@NotNull FileEditorState state, @NotNull Project project, @NotNull Element targetElement) {
    }
}
