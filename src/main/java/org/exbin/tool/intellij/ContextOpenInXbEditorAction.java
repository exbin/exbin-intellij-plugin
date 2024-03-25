/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.tool.intellij;

import com.intellij.ide.highlighter.ArchiveFileType;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.DirectoryFileType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.ArchiveFileSystem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JOptionPane;
import java.util.List;

/**
 * Open file in XB editor for Open In submenu action.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ContextOpenInXbEditorAction extends AnAction implements DumbAware {

    private boolean actionVisible = true;

    public ContextOpenInXbEditorAction() {
        super("XB Editor (ExBin Plugin)");
    }

    @Nonnull
    @Override
    public ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            project = ProjectManager.getInstance().getDefaultProject();
        }

        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        openVirtualFileAsXb(project, virtualFile);
    }

    @Override
    public void update(AnActionEvent event) {
        VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        Presentation presentation = event.getPresentation();
        if (actionVisible) {
//            presentation.setEnabled(virtualFile != null && virtualFile.isValid() && !(virtualFile.isDirectory()
//                    || virtualFile.getFileType() instanceof DirectoryFileType));
        }

        presentation.setVisible(actionVisible);
    }

    public static void openVirtualFileAsXb(Project project, @Nullable VirtualFile virtualFile) {
        boolean isValid = virtualFile != null && virtualFile.isValid();
        if (isValid && virtualFile.isDirectory()) {
            isValid = false;
            if (virtualFile.getFileType() instanceof ArchiveFileType) {
                if (virtualFile.getFileSystem() instanceof JarFileSystem) {
                    virtualFile = ((JarFileSystem) virtualFile.getFileSystem()).getVirtualFileForJar(virtualFile);
                } else {
                    virtualFile = ((ArchiveFileSystem) virtualFile.getFileSystem()).getLocalByEntry(virtualFile);
                }
                isValid = virtualFile != null && virtualFile.isValid();
            }
        }

        if (isValid) {
            openValidVirtualFile(project, virtualFile);
        } else {
            JOptionPane.showMessageDialog(null,
                    "File reported as invalid",
                    "Unable to open file",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Nonnull
    public static XbupVirtualFile openValidVirtualFile(Project project, VirtualFile virtualFile) {
        XbupVirtualFile xbupVirtualFile = new XbupVirtualFile();
        xbupVirtualFile.openVirtualFile(virtualFile);
        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, xbupVirtualFile, 0);
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        List<FileEditor> editors = fileEditorManager.openEditor(descriptor, true);
        fileEditorManager.setSelectedEditor(xbupVirtualFile, XbupFileEditorProvider.XBUP_EDITOR_TYPE_ID);
        for (FileEditor fileEditor : editors) {
            if (fileEditor instanceof XbupFileEditor) {
                // TODO xbupVirtualFile.openFile(xbupVirtualFile.getEditorFile());
                break;
            } else {
                // TODO: Drop other editors
                fileEditor.dispose();
            }
        }
        return xbupVirtualFile;
    }
}
