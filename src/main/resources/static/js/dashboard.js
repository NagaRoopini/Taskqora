document.addEventListener('DOMContentLoaded', function() {
    // Initialize charts if elements exist
    const taskChartCtx = document.getElementById('taskChart');
    if (taskChartCtx) {
        const completed = parseInt(taskChartCtx.dataset.completed || 0);
        const pending = parseInt(taskChartCtx.dataset.pending || 0);
        const inProgress = parseInt(taskChartCtx.dataset.inProgress || 0);

        new Chart(taskChartCtx, {
            type: 'doughnut',
            data: {
                labels: ['Completed', 'Pending', 'In Progress'],
                datasets: [{
                    data: [completed, pending, inProgress],
                    backgroundColor: ['#10b981', '#f59e0b', '#3b82f6'],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                    }
                },
                cutout: '70%'
            }
        });
        });
    }

    // Search Functionality
    const searchInput = document.querySelector('.search-bar input');
    if (searchInput) {
        searchInput.addEventListener('input', function(e) {
            const term = e.target.value.toLowerCase();
            const tableRows = document.querySelectorAll('tbody tr');
            
            tableRows.forEach(row => {
                const text = row.textContent.toLowerCase();
                if (text.includes(term)) {
                    row.classList.remove('search-hidden');
                } else {
                    row.classList.add('search-hidden');
                }
            });
        });
    }

    // Reports functionality
    const reportsBtn = document.querySelector('.btn-premium');
    if (reportsBtn && reportsBtn.textContent.includes('Reports')) {
        reportsBtn.addEventListener('click', function() {
            window.print();
        });
    }
});
