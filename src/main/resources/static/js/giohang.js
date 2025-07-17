            document.addEventListener('DOMContentLoaded', function() {
                const cartKey = 'cart';

                function getCart() {
                    return JSON.parse(localStorage.getItem(cartKey)) || [];
                }

                function saveCart(cart) {
                    localStorage.setItem(cartKey, JSON.stringify(cart));
                }

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
                                alert("Đã thêm vào giỏ hàng (tài khoản)!");
                            } else {
                                alert("Thêm giỏ hàng thất bại.");
                            }
                        });
                    } else {
                        const cart = getCart();
                        const exists = cart.find(i => i.khoaHocId === item.khoaHocId);
                        if (!exists) {
                            cart.push(item);
                            saveCart(cart);
                            alert("Đã thêm vào giỏ hàng!");
                        } else {
                            alert("Khóa học đã có trong giỏ hàng.");
                        }
                    }
                }


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
            });



            document.addEventListener('DOMContentLoaded', () => {
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
                            location.reload();
                        }
                    });
                }
            });