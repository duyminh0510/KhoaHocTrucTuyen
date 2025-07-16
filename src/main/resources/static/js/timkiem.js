  document.addEventListener('DOMContentLoaded', function() {
      const searchForm = document.querySelector('.search-form');
      const searchInput = document.querySelector('input[name="q"]');
      const searchButton = document.querySelector('.btn-search');

      // Auto-focus search input
      if (searchInput) {
          searchInput.focus();
      }

      // Form validation
      if (searchForm) {
          searchForm.addEventListener('submit', function(e) {
              const query = searchInput.value.trim();
              if (query.length < 1) {
                  e.preventDefault();
                  alert('Vui lòng nhập dữ liệu tìm kiếm');
                  searchInput.focus();
                  return;
              }

              // Show loading
              if (searchButton) {
                  searchButton.innerHTML = '<i class="fas fa-spinner"></i> Đang tìm...';
                  searchButton.disabled = true;
              }
          });
      }

      // Enter key to submit
      if (searchInput) {
          searchInput.addEventListener('keypress', function(e) {
              if (e.key === 'Enter') {
                  e.preventDefault();
                  searchForm.submit();
              }
          });
      }

      // Highlight search terms in results
      const query = '[[${query}]]';
      if (query && query.length > 0) {
          const courseTitles = document.querySelectorAll('.course-card h5');
          courseTitles.forEach(function(title) {
              const text = title.textContent;
              const highlightedText = text.replace(new RegExp(query, 'gi'),
                  '<mark style="background-color: #fff3cd; padding: 2px 4px; border-radius: 3px;">$&</mark>');
              title.innerHTML = highlightedText;
          });
      }
  });