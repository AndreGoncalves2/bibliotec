package br.com.bibliotec.ui.componets;

import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CustomUpload extends Upload {
    
    private InputStream fileContentStream;
    private String fileName;
    
    public CustomUpload(MultiFileMemoryBuffer buffer) {
        super(buffer);
        setReceiver((fileName, mimeType) -> {
            this.fileName = fileName;
            return new ByteArrayOutputStream() {
                @Override
                public void close() {
                    fileContentStream = new ByteArrayInputStream(this.toByteArray());
                }
            };
        });
    }
    
    public void addFileRemoveListener(Runnable remove) {
        getElement().addEventListener("file-remove", event -> remove.run());
    }

    public InputStream getFileContentStream() {
        return fileContentStream;
    }

    public String getFileName() {
        return fileName;
    }
}
