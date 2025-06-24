package com.duantn.controllers.controllerHocVien;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/hoc-tap")
public class HocTapController {

    @GetMapping("/khoa-hoc/{id}")
    public String hocTapKhoaHoc(@PathVariable("id") Long khoaHocId, Model model) {
        // Dữ liệu mẫu cho khóa học
        Map<String, Object> khoaHoc = new HashMap<>();
        khoaHoc.put("id", khoaHocId);
        khoaHoc.put("tenKhoaHoc", "Lập trình Java cơ bản");
        khoaHoc.put("moTa", "Khóa học cung cấp kiến thức cơ bản về lập trình Java");
        khoaHoc.put("giangVien", "Nguyễn Văn A");
        khoaHoc.put("anhBia", "java-course.jpg");
        khoaHoc.put("soBaiGiang", 15);
        khoaHoc.put("soBaiTap", 8);
        khoaHoc.put("thoiGianHoc", "20 giờ");

        // Dữ liệu mẫu cho các chương
        List<Map<String, Object>> chuongList = Arrays.asList(
            createChuong(1L, "Chương 1: Giới thiệu Java", Arrays.asList(
                createBaiGiang(1L, "Bài 1: Cài đặt môi trường", "video1.mp4", 15, false),
                createBaiGiang(2L, "Bài 2: Hello World đầu tiên", "video2.mp4", 20, false),
                createBaiTap(1L, "Bài tập trắc nghiệm chương 1", 10, false)
            )),
            createChuong(2L, "Chương 2: Biến và kiểu dữ liệu", Arrays.asList(
                createBaiGiang(3L, "Bài 3: Khai báo biến", "video3.mp4", 18, false),
                createBaiGiang(4L, "Bài 4: Các kiểu dữ liệu cơ bản", "video4.mp4", 25, false),
                createBaiTap(2L, "Bài tập trắc nghiệm chương 2", 15, false)
            )),
            createChuong(3L, "Chương 3: Cấu trúc điều khiển", Arrays.asList(
                createBaiGiang(5L, "Bài 5: Câu lệnh if-else", "video5.mp4", 22, false),
                createBaiGiang(6L, "Bài 6: Vòng lặp for", "video6.mp4", 20, false),
                createBaiTap(3L, "Bài tập trắc nghiệm chương 3", 12, false)
            ))
        );

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongList", chuongList);
        return "views/gdienHocVien/hoc-tap";
    }

    @GetMapping("/bai-giang/{id}")
    public String xemBaiGiang(@PathVariable("id") Long baiGiangId, Model model) {
        // Dữ liệu mẫu cho bài giảng
        Map<String, Object> baiGiang = new HashMap<>();
        baiGiang.put("id", baiGiangId);
        baiGiang.put("tenBaiGiang", "Bài 1: Cài đặt môi trường");
        baiGiang.put("videoUrl", "video1.mp4");
        baiGiang.put("thoiGian", 15);
        baiGiang.put("moTa", "Trong bài học này, chúng ta sẽ học cách cài đặt môi trường lập trình Java...");
        baiGiang.put("noiDung", "Nội dung chi tiết của bài giảng...");

        model.addAttribute("baiGiang", baiGiang);
        return "views/gdienHocVien/xem-bai-giang";
    }

    @GetMapping("/bai-tap/{id}")
    public String lamBaiTap(@PathVariable("id") Long baiTapId, Model model) {
        // Dữ liệu mẫu cho bài tập trắc nghiệm
        Map<String, Object> baiTap = new HashMap<>();
        baiTap.put("id", baiTapId);
        baiTap.put("tenBaiTap", "Bài tập trắc nghiệm chương 1");
        baiTap.put("soCauHoi", 10);
        baiTap.put("thoiGian", 30);

        List<Map<String, Object>> cauHoiList = Arrays.asList(
            createCauHoi(1L, "Java là ngôn ngữ lập trình gì?", Arrays.asList(
                "Ngôn ngữ lập trình hướng đối tượng",
                "Ngôn ngữ lập trình thủ tục",
                "Ngôn ngữ lập trình hàm",
                "Ngôn ngữ lập trình logic"
            ), 0),
            createCauHoi(2L, "File có đuôi .java chứa gì?", Arrays.asList(
                "Mã máy",
                "Mã nguồn Java",
                "Tài liệu",
                "Hình ảnh"
            ), 1),
            createCauHoi(3L, "Lệnh nào để biên dịch file Java?", Arrays.asList(
                "java Hello.java",
                "javac Hello.java",
                "run Hello.java",
                "compile Hello.java"
            ), 1)
        );

        model.addAttribute("baiTap", baiTap);
        model.addAttribute("cauHoiList", cauHoiList);
        return "views/gdienHocVien/lam-bai-tap";
    }

    private Map<String, Object> createChuong(Long id, String tenChuong, List<Map<String, Object>> baiHocList) {
        Map<String, Object> chuong = new HashMap<>();
        chuong.put("id", id);
        chuong.put("tenChuong", tenChuong);
        chuong.put("baiHocList", baiHocList);
        return chuong;
    }

    private Map<String, Object> createBaiGiang(Long id, String tenBai, String videoUrl, int thoiGian, boolean daXem) {
        Map<String, Object> baiGiang = new HashMap<>();
        baiGiang.put("id", id);
        baiGiang.put("tenBai", tenBai);
        baiGiang.put("videoUrl", videoUrl);
        baiGiang.put("thoiGian", thoiGian);
        baiGiang.put("daXem", daXem);
        baiGiang.put("daLam", false);
        baiGiang.put("loai", "baiGiang");
        return baiGiang;
    }

    private Map<String, Object> createBaiTap(Long id, String tenBai, int soCau, boolean daLam) {
        Map<String, Object> baiTap = new HashMap<>();
        baiTap.put("id", id);
        baiTap.put("tenBai", tenBai);
        baiTap.put("soCau", soCau);
        baiTap.put("daLam", daLam);
        baiTap.put("daXem", false);
        baiTap.put("loai", "baiTap");
        return baiTap;
    }

    private Map<String, Object> createCauHoi(Long id, String noiDung, List<String> dapAn, int dapAnDung) {
        Map<String, Object> cauHoi = new HashMap<>();
        cauHoi.put("id", id);
        cauHoi.put("noiDung", noiDung);
        cauHoi.put("dapAn", dapAn);
        cauHoi.put("dapAnDung", dapAnDung);
        return cauHoi;
    }
} 