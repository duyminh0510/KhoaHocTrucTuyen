let player;
let daCapNhatTienDo = false;

// Lấy videoId từ URL
function getVideoIdFromUrl(url) {
    const match = url.match(/embed\/([\w-]+)/);
    return match ? match[1] : '';
}

// Hàm gọi API cập nhật tiến độ và cập nhật giao diện
function capNhatTienDo(baiGiangId, khoaHocId) {
    fetch("/khoa-hoc/api/tien-do-hoc/cap-nhat", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ baiGiangId, khoaHocId })
        })
        .then(res => res.json())
        .then(data => {
            console.log("✅ Đã cập nhật tiến độ:", data);

            // Gọi API lấy phần trăm tiến độ mới
            return fetch(`/khoa-hoc/api/tien-do-hoc/phan-tram?khoaHocId=${khoaHocId}`);
        })
        .then(res => res.json())
        .then(data => {
            const phanTram = data.phanTram;
            console.log("📊 Phần trăm tiến độ mới:", phanTram + "%");

            // Cập nhật thanh tiến độ
            const progressBar = document.querySelector(".progress-bar");
            if (progressBar) {
                progressBar.style.width = phanTram + "%";
                progressBar.innerText = phanTram + "%";
            }

            const percentText = document.querySelector(".fw-bold.text-success");
            if (percentText) {
                percentText.innerText = phanTram + "%";
            }

            const iconEl = document.getElementById(`check-baiGiang-${baiGiangId}`);
            if (iconEl) {
                iconEl.classList.remove("text-secondary");
                iconEl.classList.add("text-success");
                iconEl.innerHTML = '<i class="fas fa-check-circle"></i>';
            }
        })
        .catch(err => {
            console.error("❌ Lỗi cập nhật tiến độ:", err);
        });
}

// Hàm khởi tạo YouTube Player
function onYouTubeIframeAPIReady() {
    const container = document.getElementById("videoBaiGiang");
    if (!container) return;

    const videoUrl = container.dataset.videoUrl;
    const baiGiangId = container.dataset.baiGiangId;
    const khoaHocId = container.dataset.khoaHocId;
    const videoId = getVideoIdFromUrl(videoUrl);

    if (!videoId) {
        console.error("❌ Không tìm thấy videoId.");
        return;
    }

    player = new YT.Player("videoBaiGiang", {
        height: "348",
        width: "100%",
        videoId: videoId,
        events: {
            onStateChange: (event) => {
                if (event.data === YT.PlayerState.PLAYING && !daCapNhatTienDo) {
                    const checkProgress = setInterval(() => {
                        const duration = player.getDuration();
                        const current = player.getCurrentTime();
                        const percent = (current / duration) * 100;

                        if (percent >= 85 && !daCapNhatTienDo) {
                            daCapNhatTienDo = true;
                            clearInterval(checkProgress);
                            capNhatTienDo(baiGiangId, khoaHocId); // ← Gọi cập nhật tiến độ
                        }

                        if (player.getPlayerState() !== YT.PlayerState.PLAYING) {
                            clearInterval(checkProgress);
                        }
                    }, 1000);
                }
            }
        }
    });
}

// Gọi khi API YouTube sẵn sàng
if (window.YT && window.YT.Player) {
    onYouTubeIframeAPIReady();
}
window.onYouTubeIframeAPIReady = onYouTubeIframeAPIReady;