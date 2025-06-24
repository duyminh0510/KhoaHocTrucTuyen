package com.duantn.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.duantn.entities.Chuong;

@Repository
public interface ChuongRepository extends JpaRepository<Chuong, Integer> {

    // Tìm tất cả chương theo ID khóa học
    List<Chuong> findByCourses_CourseId(Integer courseId);

    // Tìm tất cả chương theo trạng thái
    List<Chuong> findByTrangthai(Boolean trangthai);

    // Tìm chương theo ID khóa học và trạng thái
    List<Chuong> findByCourses_CourseIdAndTrangthai(Integer courseId, Boolean trangthai);

    // Tìm chương kèm bài giảng theo ID khóa học (giữ nguyên "courses")

}
