document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("form.ajax-form").forEach(form => {
        form.addEventListener("submit", function (e) {
            e.preventDefault(); // Ngăn submit mặc định

            const formData = new FormData(form);
            const action = form.getAttribute("action");
            const method = form.getAttribute("method") || "post";

            const submitBtn = form.querySelector("button[type='submit']");
            const originalText = submitBtn.innerHTML;

            // ✅ Đổi nút thành "Đang gửi..." + spinner
            submitBtn.innerHTML = `<span class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>Đang gửi...`;
            submitBtn.disabled = true;

            fetch(action, {
                method: method.toUpperCase(),
                body: formData
            })
                .then(async response => {
                    const text = await response.text();
                    if (!response.ok) {
                        if (response.status === 401) {
                            throw new Error("Vui lòng đăng nhập để chia sẻ.");
                        }
                        throw new Error(text || "Lỗi khi gửi yêu cầu");
                    }
                    return text;
                })
                .then(data => {
                    let messageDiv = form.querySelector(".ajax-message");
                    if (!messageDiv && form.nextElementSibling && form.nextElementSibling.classList.contains("ajax-message")) {
                        messageDiv = form.nextElementSibling;
                    }

                    if (messageDiv) {
                        messageDiv.innerHTML = `<div class="alert alert-success mt-2">${data}</div>`;
                    } else {
                        alert(data);
                    }

                    // ✅ Đổi nút thành "Đã gửi ✅"
                    submitBtn.innerHTML = `Đã gửi ✅`;

                    // ✅ Đóng modal sau 1.5 giây
                    const modalElement = form.closest(".modal");
                    if (modalElement) {
                        const modalInstance = bootstrap.Modal.getInstance(modalElement);
                        if (modalInstance) {
                            setTimeout(() => modalInstance.hide(), 2000);
                        }
                    }

                    // ✅ Reset form + đổi lại text sau 3s
                    setTimeout(() => {
                        form.reset();
                        submitBtn.innerHTML = originalText;
                        submitBtn.disabled = false;
                    }, 3000);
                })
                .catch(error => {
                    let messageDiv = form.querySelector(".ajax-message");
                    if (!messageDiv && form.nextElementSibling && form.nextElementSibling.classList.contains("ajax-message")) {
                        messageDiv = form.nextElementSibling;
                    }

                    if (messageDiv) {
                        if (error.message.includes("Vui lòng đăng nhập để chia sẻ.")) {
                            messageDiv.innerHTML = `
        <div class="alert alert-warning mt-2 d-flex align-items-center">
            <i class="fas fa-sign-in-alt me-2"></i>
            <span>${error.message} </span>
            <a href="/auth/dangnhap" class="ms-2 text-primary text-decoration-underline" style="cursor: pointer; font-weight: bold;">Đăng nhập</a>
        </div>
    `;

                            // ✅ Tự động ẩn thông báo sau 2 giây
                            setTimeout(() => {
                                messageDiv.innerHTML = "";
                                const modalElement = form.closest(".modal");
                                if (modalElement) {
                                    const modalInstance = bootstrap.Modal.getInstance(modalElement);
                                    if (modalInstance) modalInstance.hide();
                                }
                            }, 3000);
                        } else {
                            messageDiv.innerHTML = `<div class="alert alert-danger mt-2">Lỗi: ${error.message}</div>`;
                        }
                    } else {
                        alert("Lỗi: " + error.message);
                    }

                    // ✅ Khôi phục lại nút ban đầu
                    submitBtn.innerHTML = originalText;
                    submitBtn.disabled = false;
                });

        });
    });
});


document.addEventListener("DOMContentLoaded", function () {
    function likeCourse(id) {
        fetch('/khoaHoc/' + id + '/like', {
            method: 'POST'
        })
            .then(response => {
                if (response.status === 401 || response.status === 403) {
                    const loginModal = new bootstrap.Modal(document.getElementById('loginRequiredModal'));
                    loginModal.show();
                    return;
                }
                if (!response.ok) throw new Error('Lỗi khi gửi yêu cầu');
                return response.json();
            })
            .then(data => {
                if (!data) return;

                // Cập nhật tất cả các nút like có cùng data-id
                document.querySelectorAll('.like-btn[data-id="' + id + '"]').forEach(btn => {
                    const countSpan = btn.querySelector('span');
                    countSpan.innerText = data.newLikeCount;

                    if (data.isLiked) {
                        btn.classList.add('btn-danger');
                        btn.classList.remove('btn-outline-danger');
                    } else {
                        btn.classList.remove('btn-danger');
                        btn.classList.add('btn-outline-danger');

                        const isYeuThichTab = window.location.href.includes("tab=yeu-thich");
                        if (isYeuThichTab) {
                            const cardCol = btn.closest('.col-12') || btn.closest('.col-sm-6') || btn.closest('.col-lg-3');
                            if (cardCol) cardCol.remove();
                        }
                    }
                });
            })
            .catch(err => {
                console.error(err);
            });
    }

    function bindLikeButtons() {
        document.querySelectorAll(".like-btn").forEach((btn) => {
            if (!btn.dataset.bound) {
                btn.addEventListener("click", function (e) {
                    e.stopPropagation();
                    const courseId = btn.getAttribute("data-id");
                    likeCourse(courseId);
                });
                btn.dataset.bound = "true";
            }
        });
    }

    bindLikeButtons();

    document.querySelectorAll(".share-btn").forEach((btn) => {
        btn.addEventListener("click", function (e) {
            e.stopPropagation();
        });
    });

    document.querySelectorAll(".add-to-cart-form").forEach((form) => {
        form.addEventListener("click", function (e) {
            e.stopPropagation();
        });
    });
});


document.addEventListener("DOMContentLoaded", function () {
    const tabs = document.querySelectorAll('.tab-link');
    const contents = document.querySelectorAll('.tab-content');

    // Mặc định active tab đầu tiên
    if (tabs.length > 0) {
        tabs[0].classList.add('active');
        contents.forEach(content => {
            content.style.display = content.getAttribute('data-dmid') === tabs[0].getAttribute('data-dmid') ? 'block' : 'none';
        });
    }

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const id = tab.getAttribute('data-dmid');

            // Set tab màu
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            // Ẩn/hiện phần danh mục tương ứng
            contents.forEach(content => {
                content.style.display = content.getAttribute('data-dmid') === id ? 'block' : 'none';
            });
        });
    });
});

// JavaScript cho phần cuộn ngang khóa học đã mua
document.addEventListener("DOMContentLoaded", function () {
    const enrolledCoursesScroll = document.querySelector('.enrolled-courses-scroll');
    
    if (enrolledCoursesScroll) {
        // Thêm smooth scrolling
        enrolledCoursesScroll.style.scrollBehavior = 'smooth';
        
        // Thêm nút điều hướng nếu có nhiều khóa học
        const courseCards = enrolledCoursesScroll.querySelectorAll('.enrolled-course-card');
        if (courseCards.length > 3) {
            addScrollNavigation(enrolledCoursesScroll);
        }
        
        // Thêm hiệu ứng hover cho card
        courseCards.forEach(card => {
            card.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-5px) scale(1.02)';
            });
            
            card.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0) scale(1)';
            });
        });
        
        // Xử lý sự kiện click cho nút chia sẻ
        const shareButtons = enrolledCoursesScroll.querySelectorAll('.share-btn');
        shareButtons.forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.stopPropagation(); // Ngăn không cho card bị click
            });
        });
    }
});

function addScrollNavigation(container) {
    const scrollContainer = container.querySelector('.row');
    const scrollAmount = 300; // Số pixel cuộn mỗi lần
    
    // Tạo nút điều hướng
    const navContainer = document.createElement('div');
    navContainer.className = 'scroll-nav d-flex justify-content-between align-items-center mt-3';
    navContainer.innerHTML = `
        <button class="btn btn-sm btn-outline-primary scroll-left" style="display: none;">
            <i class="fas fa-chevron-left"></i>
        </button>
        <button class="btn btn-sm btn-outline-primary scroll-right">
            <i class="fas fa-chevron-right"></i>
        </button>
    `;
    
    container.parentNode.insertBefore(navContainer, container.nextSibling);
    
    const leftBtn = navContainer.querySelector('.scroll-left');
    const rightBtn = navContainer.querySelector('.scroll-right');
    
    // Xử lý nút cuộn trái
    leftBtn.addEventListener('click', () => {
        scrollContainer.scrollBy({
            left: -scrollAmount,
            behavior: 'smooth'
        });
    });
    
    // Xử lý nút cuộn phải
    rightBtn.addEventListener('click', () => {
        scrollContainer.scrollBy({
            left: scrollAmount,
            behavior: 'smooth'
        });
    });
    
    // Hiển thị/ẩn nút dựa trên vị trí cuộn
    scrollContainer.addEventListener('scroll', () => {
        const scrollLeft = scrollContainer.scrollLeft;
        const maxScroll = scrollContainer.scrollWidth - scrollContainer.clientWidth;
        
        leftBtn.style.display = scrollLeft > 0 ? 'block' : 'none';
        rightBtn.style.display = scrollLeft < maxScroll ? 'block' : 'none';
    });
}