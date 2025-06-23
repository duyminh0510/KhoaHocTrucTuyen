package com.duantn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.duantn.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
