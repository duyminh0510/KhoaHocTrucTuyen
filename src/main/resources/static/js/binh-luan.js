  const baiGiangId = document.querySelector('main').getAttribute('data-bai-giang-id');

  function showReplyForm(button) {
      const commentId = button.getAttribute('data-comment-id');
      const commenterName = button.getAttribute('data-commenter-name');
      const container = document.getElementById('reply-container-' + commentId);

      if (container.innerHTML.trim() !== "") {
          container.innerHTML = "";
          return;
      }

      container.innerHTML = `
            <form onsubmit="submitReply(event, '${commentId}')" class="reply-form mt-2">
                <div class="mb-2">
                    <label class="form-label">Trả lời ${commenterName}:</label>
                    <textarea name="noiDung" class="form-control" rows="2" required placeholder="Nhập nội dung..."></textarea>
                </div>
                <button type="submit" class="btn btn-sm btn-success">Gửi trả lời</button>
            </form>
        `;
  }

  function submitReply(event, commentId) {
      event.preventDefault();
      const form = event.target;
      const formData = new FormData(form);

      fetch(`/bai-giang/${baiGiangId}/binh-luan/reply/${commentId}`, {
              method: 'POST',
              body: formData
          })
          .then(res => res.json())
          .then(data => {
              document.getElementById('reply-container-' + commentId).innerHTML = "";
              location.reload();
          })
          .catch(err => {
              console.error("Lỗi gửi trả lời:", err);
              alert("Lỗi khi gửi trả lời. Vui lòng thử lại.");
          });
  }

  function deleteComment(commentId) {
      if (!confirm("Bạn có chắc chắn muốn xóa bình luận này?")) return;

      fetch(`/bai-giang/${baiGiangId}/binh-luan/delete/${commentId}`, {
              method: 'DELETE'
          })
          .then(res => {
              if (res.ok) location.reload();
              else alert("Không thể xóa bình luận.");
          })
          .catch(err => {
              console.error("Lỗi khi xóa bình luận:", err);
              alert("Lỗi xảy ra. Vui lòng thử lại.");
          });
  }