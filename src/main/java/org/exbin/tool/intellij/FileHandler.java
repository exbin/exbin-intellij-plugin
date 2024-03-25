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

import org.exbin.framework.App;
import org.exbin.framework.client.api.ClientModuleApi;
import org.exbin.framework.editor.xbup.EditorXbupModule;
import org.exbin.framework.editor.xbup.viewer.XbupFileHandler;
import org.exbin.xbup.core.catalog.XBACatalog;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * File type for XBUP files.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class FileHandler extends XbupFileHandler {

    public FileHandler() {
        super();
        initCatalog();
    }

    public FileHandler(int id) {
        super(id);
        initCatalog();
    }

    private void initCatalog() {
        ClientModuleApi clientModule = App.getModule(ClientModuleApi.class);
        if (clientModule.getCatalog() == null) {
            EditorXbupModule xbupEditorModule = App.getModule(EditorXbupModule.class);
            //            clientModule.addClientConnectionListener(xbupEditorModule.getClientConnectionListener());
            clientModule.addPluginRepositoryListener((pluginRepository) -> {
                xbupEditorModule.setPluginRepository(pluginRepository);
            });

            Thread catalogThread = new Thread(() -> {
                clientModule.startLocalCatalog();
            });
            catalogThread.setContextClassLoader(clientModule.getClass().getClassLoader());
            catalogThread.start();
            try {
                catalogThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Thread catalogUpdateThread = new Thread(() -> {
                if (!clientModule.updateLocalCatalog()) {
                    clientModule.useBuildInCatalog();
                }

                XBACatalog catalog = clientModule.getCatalog();
                xbupEditorModule.setCatalog(catalog);
            });
            catalogUpdateThread.setContextClassLoader(clientModule.getClass().getClassLoader());
            catalogUpdateThread.start();
        }
    }
}
