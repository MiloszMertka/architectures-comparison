package com.clean.elearning.course.domain;

import java.io.File;
import java.util.Objects;

public class CourseMaterial {

    private final String name;
    private final File file;

    public CourseMaterial(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof CourseMaterial that)) {
            return false;
        }
        
        return Objects.equals(name, that.name);
    }

}
