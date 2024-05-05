package br.com.bibliotec.ui.componets;

import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

public class CustomUpload extends Upload {
    
    public CustomUpload(MultiFileMemoryBuffer buffer) {
        super(buffer);
    }
    
    public void addFileRemoveListener(Runnable remove) {
        getElement().addEventListener("file-remove", event -> remove.run());
    }
}
