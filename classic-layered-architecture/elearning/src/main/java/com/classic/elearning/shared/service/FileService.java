package com.classic.elearning.shared.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FileService {

    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");

    public void createDirectoryIfNotExists(@NonNull String name) {
        final var path = Path.of(WORKING_DIRECTORY, name);
        final var directory = path.toFile();

        if (directory.exists()) {
            return;
        }

        final boolean success = directory.mkdirs();
        if (!success) {
            throw new IllegalStateException("Failed to create directory");
        }
    }

    public File saveFile(@NonNull String name, @NonNull byte[] content) {
        final var path = Path.of(WORKING_DIRECTORY, name);
        final var file = path.toFile();

        try (final var fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(content);
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new IllegalStateException("Failed to save file");
        }

        return file;
    }

    public void removeFile(@NonNull File file) {
        final boolean success = file.delete();
        if (!success) {
            throw new IllegalStateException("Failed to remove file");
        }
    }

}
