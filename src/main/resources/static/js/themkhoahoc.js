const chuongContainer = document.getElementById("chuong-container");

function themChuong() {
    const chuongTemplate = document.getElementById("chuong-template").content.cloneNode(true);
    const chuongEl = chuongTemplate.querySelector(".chuong-item");

    const chuongIndex = chuongContainer.querySelectorAll(".chuong-item").length;
    chuongEl.querySelector(".chuong-title").innerText = "Phần " + (chuongIndex + 1);

    // Thay thế tất cả __index__ trong name, id, for...
    chuongEl.querySelectorAll("*").forEach(el => {
        if (el.hasAttribute("name")) {
            el.setAttribute("name", el.getAttribute("name").replace(/__index__/g, chuongIndex));
        }
        if (el.hasAttribute("id")) {
            el.setAttribute("id", el.getAttribute("id").replace(/__index__/g, chuongIndex));
        }
        if (el.hasAttribute("for")) {
            el.setAttribute("for", el.getAttribute("for").replace(/__index__/g, chuongIndex));
        }
    });

    // Gắn lại sự kiện toggle mô tả nếu có
    chuongEl.querySelectorAll(".btn-toggle-mota").forEach(btn => {
        btn.onclick = function() {
            toggleMoTa(this);
        };
    });

    // Thêm phần tử chương vào giao diện
    chuongContainer.appendChild(chuongEl);

    // ❗ Chỉ thêm bài giảng mặc định nếu có dữ liệu
    if (coDuLieu) {
        const btnThemBai = chuongEl.querySelector("button[onclick*='themBaiGiang']");

        if (btnThemBai) {
            themBaiGiang(btnThemBai);
        }
    }

    // Kích hoạt tooltip
    const tooltipTriggerList = [].slice.call(chuongEl.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}


function themBaiGiang(btn) {
    const chuongEl = btn.closest(".chuong-item");
    const cIndex = Array.from(chuongContainer.children).indexOf(chuongEl);
    const baiGiangContainer = chuongEl.querySelector(".bai-giang-container");
    const baigiangIndex = baiGiangContainer.children.length;

    const baigiangTemplate = document.getElementById("baigiang-template").content.cloneNode(true);
    const baigiangHTML = baigiangTemplate.firstElementChild.outerHTML
        .replace(/__cIndex__/g, cIndex)
        .replace(/__bIndex__/g, baigiangIndex);

    const wrapper = document.createElement("div");
    wrapper.innerHTML = baigiangHTML;
    const baigiangEl = wrapper.firstElementChild;
    baigiangEl.querySelector(".baigiang-title").innerText = "Bài giảng " + (baigiangIndex + 1);

    // Gán mặc định "VIDEO"
    const loaiBanDau = baigiangEl.querySelector(".loai-bai-giang-input").value;

    // Hiển thị và enable đúng loại
    const videoFields = baigiangEl.querySelector(".video-fields");
    const baivietFields = baigiangEl.querySelector(".baiviet-fields");

    if (videoFields) {
        videoFields.style.display = (loaiBanDau === "VIDEO") ? "block" : "none";
        videoFields.querySelectorAll("input, textarea").forEach(el => el.disabled = loaiBanDau !== "VIDEO");
    }

    if (baivietFields) {
        baivietFields.style.display = (loaiBanDau === "TAILIEU") ? "block" : "none";
        baivietFields.querySelectorAll("textarea").forEach(el => el.disabled = loaiBanDau !== "TAILIEU");
    }

    // Đặt nút active đúng loại
    const nutLoai = baigiangEl.querySelector(`.btn-chon-loai[data-value="${loaiBanDau}"]`);
    if (nutLoai) nutLoai.classList.add("active");

    // Thêm vào container
    baiGiangContainer.appendChild(baigiangEl);

    // Cập nhật lại nhãn loại
    capNhatTenLoaiBaiGiang();

    // Kích hoạt tooltip nếu có
    const tooltipTriggerList = [].slice.call(baigiangEl.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}


function xoaChuong(btn) {
    btn.closest(".chuong-item").remove();
}

function xoaBaiGiang(btn) {
    const chuongEl = btn.closest(".chuong-item");
    const baiGiangContainer = chuongEl.querySelector(".bai-giang-container");
    const baigiangItem = btn.closest(".baigiang-item");

    // Dispose tooltip trong phần tử này
    baigiangItem.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(el => {
        const instance = bootstrap.Tooltip.getInstance(el);
        if (instance) instance.dispose();
    });

    // Xóa phần tử
    baigiangItem.remove();

    // ✅ Sau khi xóa, cập nhật lại index, tên, id, tiêu đề
    const baiGiangs = baiGiangContainer.querySelectorAll(".baigiang-item");

    baiGiangs.forEach((el, bIndex) => {
        // Cập nhật tiêu đề
        const title = el.querySelector(".baigiang-title");
        if (title) title.innerText = "Bài giảng " + (bIndex + 1);

        // Cập nhật name, id, for
        el.querySelectorAll("*").forEach(child => {
            ["name", "id", "for"].forEach(attr => {
                if (child.hasAttribute(attr)) {
                    child.setAttribute(attr, child.getAttribute(attr)
                        .replace(/baiGiangs\[\d+\]/g, `baiGiangs[${bIndex}]`)
                        .replace(/__bIndex__/g, bIndex)
                    );
                }
            });
        });
    });

    // Cập nhật tên loại
    capNhatTenLoaiBaiGiang();
}



function toggleMoTa(btn) {
    // 1. Dispose tooltip hiện tại
    const tooltipInstance = bootstrap.Tooltip.getInstance(btn);
    if (tooltipInstance) tooltipInstance.dispose();

    // 2. Toggle hiển thị mô tả
    const cardBody = btn.closest(".card").querySelector(".card-body");
    const moTaEl = cardBody.querySelector(".mo-ta-chuong");

    if (moTaEl) {
        moTaEl.style.display = (moTaEl.style.display === "none" || moTaEl.style.display === "") ? "block" : "none";
    }

    // 3. Tạo lại tooltip mới (để hover lần sau vẫn hoạt động)
    new bootstrap.Tooltip(btn, {
        trigger: "hover"
    });
}




document.addEventListener("DOMContentLoaded", function() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    if (document.querySelectorAll(".chuong-item").length === 0) {
        themChuong();
    }

    capNhatTenLoaiBaiGiang(); // gọi đúng lúc

    document.querySelectorAll(".btn-toggle-mota").forEach(btn => {
        btn.onclick = function() {
            toggleMoTa(this);
        };
    });
});

document.addEventListener("click", function(e) {
    if (e.target.matches(".btn-chon-loai")) {
        e.preventDefault();

        const button = e.target;
        const selectedValue = button.getAttribute("data-value");
        const baigiangItem = button.closest(".baigiang-item");

        const hiddenInput = baigiangItem.querySelector(".loai-bai-giang-input");
        const allButtons = baigiangItem.querySelectorAll(".btn-chon-loai");
        const videoFields = baigiangItem.querySelector(".video-fields");
        const baivietFields = baigiangItem.querySelector(".baiviet-fields");
        const tracnghiemFields = baigiangItem.querySelector(".tracnghiem-fields");

        const isAlreadyActive = button.classList.contains("active");

        // Bỏ active tất cả
        allButtons.forEach(btn => btn.classList.remove("active"));

        // Ẩn hết các loại
        if (videoFields) {
            videoFields.style.display = "none";
            videoFields.querySelectorAll("input, textarea").forEach(el => el.disabled = true);
        }
        if (baivietFields) {
            baivietFields.style.display = "none";
            baivietFields.querySelectorAll("textarea").forEach(el => el.disabled = true);
        }
        if (tracnghiemFields) {
            tracnghiemFields.style.display = "none";
            tracnghiemFields.querySelectorAll("input, textarea").forEach(el => el.disabled = true);
        }

        if (isAlreadyActive) {
            // Nếu đang được chọn → bỏ chọn (ẩn phần nội dung)
            if (hiddenInput) hiddenInput.value = "";
        } else {
            // Nếu chưa chọn → chọn mới
            if (hiddenInput) hiddenInput.value = selectedValue;
            button.classList.add("active");

            // Hiện phần tương ứng
            if (selectedValue === "VIDEO" && videoFields) {
                videoFields.style.display = "block";
                videoFields.querySelectorAll("input, textarea").forEach(el => el.disabled = false);
            } else if (selectedValue === "TAILIEU" && baivietFields) {
                baivietFields.style.display = "block";
                baivietFields.querySelectorAll("textarea").forEach(el => el.disabled = false);
            } else if (selectedValue === "TRACNGHIEM" && tracnghiemFields) {
                tracnghiemFields.style.display = "block";
                tracnghiemFields.querySelectorAll("input, textarea").forEach(el => el.disabled = false);
            }
        }
    }
});


function capNhatTenLoaiBaiGiang() {
    document.querySelectorAll(".baigiang-item").forEach(baigiangEl => {
        // Luôn ẩn cả 3 loại
        const videoFields = baigiangEl.querySelector(".video-fields");
        const baivietFields = baigiangEl.querySelector(".baiviet-fields");
        const tracnghiemFields = baigiangEl.querySelector(".tracnghiem-fields");

        if (videoFields) {
            videoFields.style.display = "none";
            videoFields.querySelectorAll("input, textarea").forEach(el => el.disabled = true);
        }

        if (baivietFields) {
            baivietFields.style.display = "none";
            baivietFields.querySelectorAll("textarea").forEach(el => el.disabled = true);
        }


        if (tracnghiemFields) {
            tracnghiemFields.style.display = "none";
            tracnghiemFields.querySelectorAll("input").forEach(el => el.disabled = true);
        }



        // Bỏ active của tất cả nút chọn loại
        baigiangEl.querySelectorAll(".btn-chon-loai").forEach(btn => {
            btn.classList.remove("active");
        });
    });
}


function moModalXoaChuong(chuongId) {
    document.getElementById("inputChuongIdXoa").value = chuongId;
    var modal = new bootstrap.Modal(document.getElementById("modalXoaChuong"));
    modal.show();
}

function moModalXoaBaiGiang(baiGiangId) {
    document.getElementById("inputBaiGiangIdXoa").value = baiGiangId;
    const modal = new bootstrap.Modal(document.getElementById("modalXoaBaiGiang"));
    modal.show();
}



// function themCauHoi(btn) {
//     const container = btn.closest(".tracnghiem-fields").querySelector(".cau-hoi-container");
//     const soCau = container.children.length;

//     const baiGiangItem = btn.closest(".baigiang-item");
//     const prefixInput = baiGiangItem.querySelector("input[name*='.tenBaiGiang']");
//     const nameAttr = prefixInput ? prefixInput.getAttribute("name") : null;
//     const prefix = nameAttr ? nameAttr.substring(0, nameAttr.indexOf(".tenBaiGiang")) : "";

//     const html = `
//     <div class="cau-hoi-item border rounded p-3 mt-3 bg-light">
//         <label class="form-label">📝 Câu hỏi ${soCau + 1}:</label>
//         <input type="text" class="form-control mb-2"
//             name="${prefix}.tracNghiem.cauHoiList[${soCau}].tenCauHoi"
//             placeholder="Nhập nội dung câu hỏi">

//         <div class="row g-3">
//             <div class="col-md-6 d-flex align-items-center">
//                 <input type="radio" class="form-check-input me-2"
//                     name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnDung"
//                     value="A" />
//                 <input type="text" class="form-control"
//                     name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnA"
//                     placeholder="Đáp án A" />
//             </div>
//             <div class="col-md-6 d-flex align-items-center">
//                 <input type="radio" class="form-check-input me-2"
//                     name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnDung"
//                     value="B" />
//                 <input type="text" class="form-control"
//                     name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnB"
//                     placeholder="Đáp án B" />
//             </div>
//             <div class="col-md-6 d-flex align-items-center">
//                 <input type="radio" class="form-check-input me-2"
//                     name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnDung"
//                     value="C" />
//                 <input type="text" class="form-control"
//                     name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnC"
//                     placeholder="Đáp án C" />
//             </div>
//             <div class="col-md-6 d-flex align-items-center">
//                 <input type="radio" class="form-check-input me-2"
//                     name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnDung"
//                     value="D" />
//                 <input type="text" class="form-control"
//                     name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnD"
//                     placeholder="Đáp án D" />
//             </div>
//         </div>

//         <label class="form-label mt-3">💡 Giải thích đáp án:</label>
//         <textarea class="form-control"
//             name="${prefix}.tracNghiem.cauHoiList[${soCau}].giaiThich"
//             placeholder="Giải thích câu trả lời (tùy chọn)"></textarea>
//     </div>
//     `;

//     container.insertAdjacentHTML("beforeend", html);
// }

function themCauHoi(btn) {
    const container = btn.closest(".tracnghiem-fields").querySelector(".cau-hoi-container");
    const soCau = container.children.length;

    const baiGiangItem = btn.closest(".baigiang-item");
    const prefixInput = baiGiangItem.querySelector("input[name*='.tenBaiGiang']");
    const nameAttr = prefixInput ? prefixInput.getAttribute("name") : null;
    const prefix = nameAttr ? nameAttr.substring(0, nameAttr.indexOf(".tenBaiGiang")) : "";

    const html = `
    <div class="cau-hoi-item border rounded p-3 mt-3 bg-light">

    <input type="hidden"
    name="${prefix}.tracNghiem.cauHoiList[${soCau}].cauHoiId"
    value="" /

            <label class="form-label">📝 Câu hỏi ${soCau + 1}:</label>
            <input type="text" class="form-control mb-2"
            name="${prefix}.tracNghiem.cauHoiList[${soCau}].tenCauHoi"
            placeholder="Nhập nội dung câu hỏi">

        <div class="row g-3 mt-2">
            ${["A", "B", "C", "D"].map((label, idx) => `
                <div class="col-md-6 d-flex align-items-center">
                    <input type="radio" class="form-check-input me-2"
                        name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnDungIndex"
                        value="${idx}" />

                    <input type="text" class="form-control"
                        name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnList[${idx}].noiDungDapAn"
                        placeholder="Đáp án ${label}" />

                    <input type="hidden"
                        name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnList[${idx}].thuTuDapAn"
                        value="${idx + 1}" />

                    <input type="hidden"
                        name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnList[${idx}].trangthai"
                        value="true" />

                         <input type="hidden"
                    name="${prefix}.tracNghiem.cauHoiList[${soCau}].dapAnList[${idx}].dapAnId"
                    value="" />
                </div>
            `).join("")}
        </div>

        <label class="form-label mt-3">💡 Giải thích đáp án đúng:</label>
        <textarea class="form-control"
            name="${prefix}.tracNghiem.cauHoiList[${soCau}].giaiThich"
            placeholder="Giải thích đáp án đúng (nếu có)"></textarea>
    </div>
    `;

    container.insertAdjacentHTML("beforeend", html);
}