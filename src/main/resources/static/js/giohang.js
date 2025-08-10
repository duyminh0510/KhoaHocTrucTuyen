document.addEventListener('DOMContentLoaded', function () {
    const cartKey = 'cart';

    // Lấy giỏ hàng từ localStorage
    function getCart() {
        return JSON.parse(localStorage.getItem(cartKey)) || [];
    }

    // Lưu giỏ hàng vào localStorage
    function saveCart(cart) {
        localStorage.setItem(cartKey, JSON.stringify(cart));
    }

    // Hiển thị modal thông báo
    function showMessageModal(title, message, type = 'success') {
        const modalTitle = document.getElementById('messageModalTitle');
        const modalBody = document.getElementById('messageModalBody');
        const modalHeader = document.getElementById('messageModalHeader');

        modalTitle.textContent = title;
        modalBody.textContent = message;

        // Đặt màu theo loại thông báo
        modalHeader.className = 'modal-header text-white';
        if (type === 'success') {
            modalHeader.classList.add('bg-success');
        } else if (type === 'error') {
            modalHeader.classList.add('bg-danger');
        } else if (type === 'warning') {
            modalHeader.classList.add('bg-warning');
        }

        const modal = new bootstrap.Modal(document.getElementById('messageModal'));
        modal.show();
    }

    // Hiển thị modal "Đã thêm vào giỏ hàng"
    function showCartModal(item) {
        document.getElementById('modalName').textContent = item.tenKhoaHoc;
        document.getElementById('modalGia').textContent = parseInt(item.gia).toLocaleString('vi-VN');
        document.getElementById('modalGiangVien').textContent = item.giangVien;
        document.getElementById('modalImage').src = item.anhBia;

        const modal = new bootstrap.Modal(document.getElementById('cartModal'));
        modal.show();
    }

    // Thêm vào giỏ hàng
    function addToCart(item) {
        const isLoggedIn = /*[[${#authorization.expression('isAuthenticated()')}]]*/ false;

        if (isLoggedIn) {
            fetch('/gio-hang/them', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(item)
            }).then(res => {
                if (res.ok) {
                    showCartModal(item);
                } else if (res.status === 409) {
                    showMessageModal('Thông báo', 'Khóa học đã có trong giỏ hàng.', 'warning');
                } else {
                    showMessageModal('Lỗi', 'Thêm giỏ hàng thất bại.', 'error');
                }
            }).catch(err => {
                showMessageModal('Lỗi', 'Đã xảy ra lỗi kết nối đến máy chủ.', 'error');
            });
        } else {
            const cart = getCart();
            const exists = cart.find(i => i.khoaHocId === item.khoaHocId);
            if (!exists) {
                cart.push(item);
                saveCart(cart);
                showCartModal(item);
            } else {
                showMessageModal('Thông báo', 'Khóa học đã có trong giỏ hàng.', 'warning');
            }
        }
    }

    // Gán sự kiện click cho các nút thêm vào giỏ
    document.querySelectorAll('.add-to-cart-btn').forEach(btn => {
        btn.addEventListener('click', (event) => {
            event.stopPropagation();
            const item = {
                khoaHocId: parseInt(btn.dataset.id),
                tenKhoaHoc: btn.dataset.name,
                gia: parseFloat(btn.dataset.gia),
                giaGoc: parseFloat(btn.dataset.giagoc),
                anhBia: btn.dataset.anh,
                giangVien: btn.dataset.giangvien
            };
            addToCart(item);
        });
    });

    // Đồng bộ giỏ khi vừa đăng nhập
    const isLoggedIn = /*[[${#authorization.expression('isAuthenticated()')}]]*/ false;
    const hasCart = localStorage.getItem('cart');
    const isSynced = localStorage.getItem('cartSynced');

    if (isLoggedIn && hasCart && !isSynced) {
        const cart = JSON.parse(hasCart);

        fetch('/gio-hang/sync', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cart)
        }).then(res => {
            if (res.ok) {
                localStorage.removeItem('cart');
                localStorage.setItem('cartSynced', 'true');
                showMessageModal('Thành công', 'Đã đồng bộ giỏ hàng của bạn.', 'success');
                setTimeout(() => location.reload(), 2000);
            } else {
                showMessageModal('Lỗi', 'Không thể đồng bộ giỏ hàng.', 'error');
            }
        }).catch(() => {
            showMessageModal('Lỗi', 'Lỗi kết nối khi đồng bộ giỏ hàng.', 'error');
        });
    }
});
