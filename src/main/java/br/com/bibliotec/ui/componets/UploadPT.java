package br.com.bibliotec.ui.componets;

import com.vaadin.flow.component.upload.UploadI18N;

import java.util.Arrays;

public class UploadPT extends UploadI18N {
    public UploadPT() {
        setDropFiles(new DropFiles().setOne("Adicionar arquivo aqui")
                .setMany("Adicionar arquivos aqui"));
        setAddFiles(new AddFiles().setOne("Enviar Arquivo...")
                .setMany("Enviar Arquivos..."));
        setError(new Error().setTooManyFiles("Muitos arquivos.")
                .setFileIsTooBig("Arquivo muito grande.")
                .setIncorrectFileType("Tipo de arquivo incorreto."));
        setUploading(new Uploading()
                .setStatus(new Uploading.Status().setConnecting("Conectando...")
                        .setStalled("Parado")
                        .setProcessing("Processando arquivo...").setHeld("Em fila"))
                .setRemainingTime(new Uploading.RemainingTime()
                        .setPrefix("tempo restante: ")
                        .setUnknown("tempo restante desconhecido"))
                .setError(new Uploading.Error()
                        .setServerUnavailable(
                                "Falha no upload, por favor tente novamente mais tarde")
                        .setUnexpectedServerError(
                                "Falha no upload devido a um erro no servidor\"")
                        .setForbidden("Imagem muito grande")));
        setUnits(new Units().setSize(Arrays.asList("B", "kB", "MB", "GB", "TB",
                "PB", "EB", "ZB", "YB")));
    }
}