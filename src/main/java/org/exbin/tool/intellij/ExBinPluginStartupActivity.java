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

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.startup.StartupActivity;
import io.ktor.http.ContentType;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.exbin.framework.App;
import org.exbin.framework.Module;
import org.exbin.framework.ModuleProvider;
import org.exbin.framework.about.AboutModule;
import org.exbin.framework.about.api.AboutModuleApi;
import org.exbin.framework.action.ActionModule;
import org.exbin.framework.action.api.ActionModuleApi;
import org.exbin.framework.bined.BinedModule;
import org.exbin.framework.client.ClientModule;
import org.exbin.framework.client.api.ClientModuleApi;
import org.exbin.framework.component.ComponentModule;
import org.exbin.framework.component.api.ComponentModuleApi;
import org.exbin.framework.data.DataModule;
import org.exbin.framework.data.api.DataModuleApi;
import org.exbin.framework.editor.EditorModule;
import org.exbin.framework.editor.api.EditorModuleApi;
import org.exbin.framework.editor.xbup.EditorXbupModule;
import org.exbin.framework.file.FileModule;
import org.exbin.framework.file.api.FileModuleApi;
import org.exbin.framework.frame.FrameModule;
import org.exbin.framework.frame.api.FrameModuleApi;
import org.exbin.framework.help.HelpModule;
import org.exbin.framework.help.api.HelpModuleApi;
import org.exbin.framework.language.LanguageModule;
import org.exbin.framework.language.api.LanguageModuleApi;
import org.exbin.framework.operation.undo.OperationUndoModule;
import org.exbin.framework.operation.undo.api.OperationUndoModuleApi;
import org.exbin.framework.options.OptionsModule;
import org.exbin.framework.options.api.OptionsModuleApi;
import org.exbin.framework.preferences.PreferencesModule;
import org.exbin.framework.preferences.api.Preferences;
import org.exbin.framework.preferences.api.PreferencesModuleApi;
import org.exbin.framework.ui.UiModule;
import org.exbin.framework.ui.api.UiModuleApi;
import org.exbin.framework.window.WindowModule;
import org.exbin.framework.window.api.WindowModuleApi;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Post startup activity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public final class ExBinPluginStartupActivity implements ProjectActivity, StartupActivity, DumbAware {

    private boolean initialized = false;

    ExBinPluginStartupActivity() {
    }

    @Override
    public void runActivity(Project project) {
        projectOpened(project);
    }

    @Nullable @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        projectOpened(project);
        return null;
    }

    private void projectOpened(Project project) {
        if (!initialized) {
            initialized = true;
            AppModuleProvider appModuleProvider = new AppModuleProvider();
            appModuleProvider.createModules();
            App.setModuleProvider(appModuleProvider);
            appModuleProvider.init();
        }
/*        MessageBus messageBus = project.getMessageBus();
        MessageBusConnection connect = messageBus.connect();
        connect.subscribe(FileEditorManagerListener.Before.FILE_EDITOR_MANAGER, new FileEditorManagerListener.Before() {
            @Override
            public void beforeFileClosed(@Nonnull FileEditorManager source, @Nonnull VirtualFile file) {
                if (file instanceof XbupVirtualFile && !((XbupVirtualFile) file).isMoved()
                        && !((XbupVirtualFile) file).isClosing()) {
                    ((XbupVirtualFile) file).setClosing(true);
                    BinEdFileHandler fileHandler = ((XbupVirtualFile) file).getEditorFile();
                    BinEdManager binedManager = BinEdManager.getInstance();
                    if (!binedManager.releaseFile(fileHandler)) {
                        ((BinEdVirtualFile) file).setClosing(false);
                        throw new ProcessCanceledException();
                    }
                    ((BinEdVirtualFile) file).setClosing(false);
                }
            }
        });
        connect.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
            @Override
            public void after(@Nonnull List<? extends VFileEvent> events) {
                for (VFileEvent event : events) {
                    VirtualFile virtualFile = event.getFile();
                    if (event.isFromRefresh()) {
                        FileEditor[] allEditors = FileEditorManager.getInstance(project).getAllEditors(virtualFile);
                        for (FileEditor fileEditor : allEditors) {
                            if (fileEditor instanceof BinEdNativeFileEditor) {
                                BinEdNativeFile nativeFile = ((BinEdNativeFileEditor) fileEditor).getNativeFile();
                                nativeFile.reloadFile();
                            }
                        }
                    }
                }
            }
        }); */
    }

    @ParametersAreNonnullByDefault
    private static class AppModuleProvider implements ModuleProvider {

        private final Map<Class<?>, Module> modules = new HashMap<>();

        private void createModules() {
            modules.put(LanguageModuleApi.class, new LanguageModule());
            modules.put(ActionModuleApi.class, new ActionModule());
            modules.put(OperationUndoModuleApi.class, new OperationUndoModule());
            modules.put(OptionsModuleApi.class, new OptionsModule());
            modules.put(PreferencesModuleApi.class, new PreferencesModule());
            modules.put(UiModuleApi.class, new UiModule());
            modules.put(ComponentModuleApi.class, new ComponentModule());
            modules.put(WindowModuleApi.class, new WindowModule());
            modules.put(FrameModuleApi.class, new FrameModule());
            modules.put(FileModuleApi.class, new FileModule());
            modules.put(DataModuleApi.class, new DataModule());
            modules.put(EditorModuleApi.class, new EditorModule());
            modules.put(HelpModuleApi.class, new HelpModule());
            modules.put(BinedModule.class, new BinedModule());
            modules.put(EditorXbupModule.class, new EditorXbupModule());
            modules.put(ClientModuleApi.class, new ClientModule());
            modules.put(AboutModuleApi.class, new AboutModule());
        }

        private void init() {
            PreferencesModuleApi preferencesModule = App.getModule(PreferencesModuleApi.class);
            preferencesModule.setupAppPreferences(ExBinIntelliJPlugin.class);
            Preferences preferences = preferencesModule.getAppPreferences();

            FrameModuleApi frameModule = App.getModule(FrameModuleApi.class);
            frameModule.createMainMenu();
            ActionModuleApi actionModule = App.getModule(ActionModuleApi.class);
            actionModule.registerMenuClipboardActions();
            actionModule.registerToolBarClipboardActions();

            LanguageModuleApi languageModule = App.getModule(LanguageModuleApi.class);
            ResourceBundle bundle = languageModule.getBundle(ExBinIntelliJPlugin.class);
            languageModule.setAppBundle(bundle);

            AboutModuleApi aboutModule = App.getModule(AboutModuleApi.class);
            aboutModule.registerDefaultMenuItem();
            OptionsModuleApi optionsModule = App.getModule(OptionsModuleApi.class);
            optionsModule.registerMenuAction();

            XbupIntelliJEditorProvider editorProvider = new XbupIntelliJEditorProvider();
            final EditorXbupModule xbupEditorModule = App.getModule(EditorXbupModule.class);
            xbupEditorModule.setEditorProvider(editorProvider);
            xbupEditorModule.registerCatalogBrowserMenu();
            xbupEditorModule.registerDocEditingMenuActions();
            xbupEditorModule.registerDocEditingToolBarActions();
            BinedModule binaryModule = App.getModule(BinedModule.class);
            binaryModule.setEditorProvider(editorProvider);
            binaryModule.registerCodeAreaPopupMenu();
        }

        @Override
        public void launch(Runnable runnable) {
        }

        @Nonnull
        @Override
        public <T extends Module> T getModule(Class<T> moduleClass) {
            return (T) modules.get(moduleClass);
        }
    }
}
