package com.classic.elearning.course.adapter.database;

import com.classic.elearning.course.domain.CourseMaterial;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.File;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CourseMaterialSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String filePath;

    public static CourseMaterialSchema fromCourseMaterial(@NonNull CourseMaterial courseMaterial) {
        return new CourseMaterialSchema(null, courseMaterial.getName(), courseMaterial.getFile().getPath());
    }

    public CourseMaterial toCourseMaterial() {
        final var file = new File(filePath);
        return new CourseMaterial(name, file);
    }

}
