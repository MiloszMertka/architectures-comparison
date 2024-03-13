package com.clean.elearning.course.adapter.service;

import com.clean.elearning.course.usecase.port.FileRepository;
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
class LocalDiskFileGateway implements FileRepository {

    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");

    @Override
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

    @Override
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

}
