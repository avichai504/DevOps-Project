// Handle Dropdown Navigation
document.getElementById('jobOperationsSelect').addEventListener('change', function () {
    const section = document.querySelector(this.value);
    if (section) {
        section.scrollIntoView({ behavior: 'smooth' });
    }
    this.selectedIndex = 0;
});

// Utility function to show the response
function showResponse(responseId, data) {
    const responseElement = document.getElementById(responseId);
    responseElement.style.display = 'block';  // Show the response section
    responseElement.textContent = JSON.stringify(data, null, 2);
}

// CI/CD Job CRUD Actions

// Create Job
document.getElementById('createJobForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const jsonData = JSON.stringify(Object.fromEntries(formData.entries()));

    fetch('/api/jobs', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: jsonData
    })
        .then(response => response.json())
        .then(data => showResponse('createJobResponse', data))
        .catch(error => showResponse('createJobResponse', { error: error.message }));
});

// Get Job by ID
document.getElementById('getJobForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const id = event.target.id.value;

    fetch(`/api/jobs/${id}`)
        .then(response => response.json())
        .then(data => showResponse('getJobResponse', data))
        .catch(error => showResponse('getJobResponse', { error: error.message }));
});

// Update Job
document.getElementById('updateJobForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const id = event.target.id.value;
    const formData = new FormData(event.target);
    const jsonData = JSON.stringify(Object.fromEntries(formData.entries()));

    fetch(`/api/jobs/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: jsonData
    })
        .then(response => response.json())
        .then(data => showResponse('updateJobResponse', data))
        .catch(error => showResponse('updateJobResponse', { error: error.message }));
});

// Delete Job
document.getElementById('deleteJobForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const id = event.target.id.value;

    fetch(`/api/jobs/${id}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                showResponse('deleteJobResponse', { message: 'Job deleted successfully' });
            } else {
                throw new Error('Job deletion failed');
            }
        })
        .catch(error => showResponse('deleteJobResponse', { error: error.message }));
});

// Get Jobs by Status
document.getElementById('getJobsByStatusForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const status = event.target.status.value;

    fetch(`/api/jobs/status/${status}`)
        .then(response => response.json())
        .then(data => showResponse('getJobsByStatusResponse', data))
        .catch(error => showResponse('getJobsByStatusResponse', { error: error.message }));
});

// Get Jobs by Job Type
document.getElementById('getJobsByTypeForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const jobType = event.target.jobType.value;

    fetch(`/api/jobs/jobType/${jobType}`)
        .then(response => response.json())
        .then(data => showResponse('getJobsByTypeResponse', data))
        .catch(error => showResponse('getJobsByTypeResponse', { error: error.message }));
});

// Get Jobs by Date Range
document.getElementById('getJobsByDateRangeForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const startDate = event.target.startDate.value;
    const endDate = event.target.endDate.value;

    fetch(`/api/jobs/date-range?startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`)
        .then(response => response.json())
        .then(data => showResponse('getJobsByDateRangeResponse', data))
        .catch(error => showResponse('getJobsByDateRangeResponse', { error: error.message }));
});
