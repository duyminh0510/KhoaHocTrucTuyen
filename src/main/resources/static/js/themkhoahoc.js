const chuongContainer = document.getElementById("chuong-container");

function themChuong() {
    const chuongTemplate = document.getElementById("chuong-template").content.cloneNode(true);
    const chuongEl = chuongTemplate.querySelector(".chuong-item");

    const chuongIndex = chuongContainer.querySelectorAll(".chuong-item").length;
    chuongEl.querySelector(".chuong-title").innerText = "Phần " + (chuongIndex + 1);

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

    chuongEl.querySelectorAll(".btn-toggle-mota").forEach(btn => {
        btn.onclick = function() {
            toggleMoTa(this);
        };
    });

    chuongContainer.appendChild(chuongEl);

    if (coDuLieu) {
        const btnThemBai = chuongEl.querySelector("button[onclick*='themBaiGiang']");
        if (btnThemBai) themBaiGiang(btnThemBai);
    }

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

    baiGiangContainer.appendChild(baigiangEl);

    capNhatTenLoaiBaiGiang();

    baigiangEl.querySelectorAll(".btn-chon-loai").forEach(button => {
        button.addEventListener("click", function() {
            const selectedValue = this.getAttribute("data-value");
            const baigiangItem = this.closest(".baigiang-item");
            const hiddenInput = baigiangItem.querySelector(".loai-bai-giang-input");
            const allButtons = baigiangItem.querySelectorAll(".btn-chon-loai");
            const videoFields = baigiangItem.querySelector(".video-fields");
            const baivietFields = baigiangItem.querySelector(".baiviet-fields");
            const tracnghiemFields = baigiangItem.querySelector(".tracnghiem-fields");

            const isAlreadyActive = this.classList.contains("active");

            allButtons.forEach(btn => btn.classList.remove("active"));

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
                tracnghiemFields.querySelectorAll("input, textarea, button").forEach(el => el.disabled = true);
            }

            if (isAlreadyActive) {
                if (hiddenInput) hiddenInput.value = "";
            } else {
                if (hiddenInput) hiddenInput.value = selectedValue;
                this.classList.add("active");

                if (selectedValue === "VIDEO" && videoFields) {
                    videoFields.style.display = "block";
                    videoFields.querySelectorAll("input, textarea").forEach(el => el.disabled = false);
                } else if (selectedValue === "TAILIEU" && baivietFields) {
                    baivietFields.style.display = "block";
                    baivietFields.querySelectorAll("textarea").forEach(el => el.disabled = false);
                } else if (selectedValue === "TRACNGHIEM" && tracnghiemFields) {
                    tracnghiemFields.style.display = "block";
                    tracnghiemFields.querySelectorAll("input, textarea, button").forEach(el => el.disabled = false);
                }
            }
        });
    });

    const tooltipTriggerList = [].slice.call(baigiangEl.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

function capNhatTenLoaiBaiGiang() {
    document.querySelectorAll(".baigiang-item").forEach(baigiangEl => {
        const loaiInput = baigiangEl.querySelector(".loai-bai-giang-input");
        const loaiDaChon = loaiInput ? loaiInput.value : "";

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
            tracnghiemFields.querySelectorAll("input, textarea, button").forEach(el => el.disabled = true);
        }

        baigiangEl.querySelectorAll(".btn-chon-loai").forEach(btn => btn.classList.remove("active"));

        if (loaiDaChon === "VIDEO" && videoFields) {
            videoFields.style.display = "block";
            videoFields.querySelectorAll("input, textarea").forEach(el => el.disabled = false);
        } else if (loaiDaChon === "TAILIEU" && baivietFields) {
            baivietFields.style.display = "block";
            baivietFields.querySelectorAll("textarea").forEach(el => el.disabled = false);
        } else if (loaiDaChon === "TRACNGHIEM" && tracnghiemFields) {
            tracnghiemFields.style.display = "block";
            tracnghiemFields.querySelectorAll("input, textarea, button").forEach(el => el.disabled = false);
        }

        const nutLoai = baigiangEl.querySelector(`.btn-chon-loai[data-value="${loaiDaChon}"]`);
        if (nutLoai) nutLoai.classList.add("active");
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
            tracnghiemFields.querySelectorAll("input, textarea, button").forEach(el => el.disabled = true);
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
                tracnghiemFields.querySelectorAll("input, textarea, button").forEach(el => el.disabled = false);
            }
        }
    }
});


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