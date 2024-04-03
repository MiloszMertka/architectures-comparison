package com.classic.elearning.course.usecase.port;

import java.io.File;

public interface FileRepository {

    void createDirectoryIfNotExists(String name);

    File saveFile(String name, byte[] content);

    void removeFile(File file);

}
