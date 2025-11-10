const API_BASE_URL = 'http://localhost:8080/api/v1';

/**
 * Wrapper for API calls with built-in error handling and JSON processing.
 */
async function apiCall(url, options = {}) {
    try {
        const response = await fetch(url, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            },
        });

        const readResponseBody = async (res) => {
            // No Content (204) or zero Content-Length
            if (res.status === 204 || res.headers.get("content-length") === "0") {
                return {};
            }
            const contentType = res.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                return await res.json();
            } else {
                return await res.text();
            }
        };

        if (response.ok) {
            return await readResponseBody(response);
        }

        // Extract error message from server response if possible
        const errorData = await readResponseBody(response);
        let errorMessage;
        if (typeof errorData === 'string') {
            errorMessage = errorData;
        } else if (typeof errorData === 'object' && errorData !== null && errorData.message) {
            errorMessage = errorData.message;
        } else {
            // Fallback for generic HTTP errors (like 404 Not Found if backend doesn't send a body)
            errorMessage = `Request failed with status ${response.status} (${response.statusText})`;
        }

        throw new Error(errorMessage);

    } catch (error) {
        // This catches network errors (server down, no internet)
        console.error("API Error:", error.message);
        throw error;
    }
}

/**
 * Displays a toast notification.
 * @param {string} message - The text to display.
 * @param {string} type - 'info' (default), 'success', or 'error'.
 */
function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    if (!container) {
        console.error("Toast container not found!");
        alert(message); // Fallback if DOM isn't ready
        return;
    }

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;

    container.appendChild(toast);

    // Remove after 3 seconds
    setTimeout(() => {
        toast.classList.add('fade-out');
        toast.addEventListener('animationend', () => {
            toast.remove();
        });
    }, 3000);
}

// --- View Management ---

function hideAllViews() {
    document.getElementById('login-container').style.display = 'none';
    document.getElementById('register-admin-container').style.display = 'none';
    document.getElementById('register-student-container').style.display = 'none';
    document.getElementById('student-container').style.display = 'none';
    document.getElementById('admin-container').style.display = 'none';
}

function showLoginView() {
    hideAllViews();
    document.getElementById('login-container').style.display = 'block';
}

function showStudentRegistrationView() {
    hideAllViews();
    document.getElementById('register-student-container').style.display = 'block';
}

function showAdminRegistrationView() {
    hideAllViews();
    document.getElementById('register-admin-container').style.display = 'block';
}

// --- Authentication Handlers ---

async function handleLogin(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const admin = document.getElementById('isAdmin').checked;

    if (!email || !password) {
        showToast('Please enter both email and password.', 'error');
        return;
    }

    try {
        const response = await apiCall(`${API_BASE_URL}/login`, {
            method: 'POST',
            body: JSON.stringify({ email, password, admin })
        });

        if (response === "admin") {
            localStorage.setItem('loggedInEmail', email);
            localStorage.setItem('userType', 'admin');
            showToast('Admin login successful', 'success');
            document.getElementById('login-form').reset();
            displayAdminInfo(email);
        } else if (response === "student") {
            localStorage.setItem('loggedInEmail', email);
            localStorage.setItem('userType', 'student');
            showToast('Student login successful', 'success');
            document.getElementById('login-form').reset();
            displayStudentInfo(email);
        } else {
            // Fallback if the server returns 200 OK but an unexpected string
            throw new Error('Unexpected login response from server.');
        }
    } catch (error) {
        showToast(error.message || 'Login failed. Please check your credentials.', 'error');
    }
}

async function handleStudentRegistration(event) {
    event.preventDefault();
    // Simple password validation
    const password = document.getElementById('student-password').value;
    if (password.length < 6) {
        showToast('Password must be at least 6 characters long.', 'error');
        return;
    }

    try {
        await apiCall(`${API_BASE_URL}/registration/student`, {
            method: 'POST',
            body: JSON.stringify({
                firstName: document.getElementById('student-first-name').value,
                lastName: document.getElementById('student-last-name').value,
                email: document.getElementById('student-email').value,
                password: password
            })
        });
        showToast('Registration successful! Please log in.', 'success');
        document.getElementById('register-student-form').reset();
        showLoginView();
    } catch (error) {
        showToast(error.message || 'Registration failed.', 'error');
    }
}

async function handleAdminRegistration(event) {
    event.preventDefault();
    try {
        await apiCall(`${API_BASE_URL}/registration/admin`, {
            method: 'POST',
            body: JSON.stringify({
                email: document.getElementById('admin-email').value,
                password: document.getElementById('admin-password').value
            })
        });
        showToast('Admin registration successful! Please log in.', 'success');
        document.getElementById('register-admin-form').reset();
        showLoginView();
    } catch (error) {
        showToast(error.message || 'Registration failed.', 'error');
    }
}

// --- Student Dashboard ---

async function getStudent(email) {
    try {
        return await apiCall(`${API_BASE_URL}/student?email=${encodeURIComponent(email)}`, {
            method: 'GET',
        });
    } catch (error) {
        console.error('Error retrieving student:', error);
        showToast(`Failed to load student data: ${error.message}`, 'error');
        return null;
    }
}

async function displayStudentInfo(email) {
    const studentDetails = await getStudent(email);
    if (!studentDetails) {
        detailsContainer.innerHTML = '<p>Error loading data. Please try refreshing.</p>';
        return;
    }

    const { student, enrollmentDetails } = studentDetails;

    hideAllViews();
    const container = document.getElementById('student-container');
    container.style.display = 'block';

    const detailsContainer = document.getElementById('student-details-container');
    detailsContainer.innerHTML = `
        <div id="student-details-card">
        <button class="logout-btn" onclick="handleLogout()">Logout</button>
            <h2>Student Profile</h2>
            <p><strong>Name:</strong> ${student.firstName} ${student.lastName}</p>
            <p><strong>Student ID:</strong> ${student.studentId}</p>
            <p><strong>Email:</strong> ${student.email}</p>
        </div>
        <div id="student-enrolled-courses-card">
            <h2>Enrolled Courses</h2>
            <div id="student-enrolled-courses-list">
                ${generateEnrollmentHtml(enrollmentDetails)}
            </div>
        </div>
    `;

    // Auto-fill the Student ID in the enrollment form for convenience
    document.getElementById('enrollment-student-id').value = student.studentId;
}

function generateEnrollmentHtml(enrollmentDetails) {
    if (!enrollmentDetails || enrollmentDetails.length === 0) {
        return '<p>Not enrolled in any courses currently.</p>';
    }

    // Get the student ID once from the local storage
    const studentId = localStorage.getItem('userType') === 'student'
        ? document.getElementById('enrollment-student-id').value
        : null;

    if (!studentId) {
        // This case should not happen if the user is logged in as a student
        return '<p>Error: Could not retrieve student ID.</p>';
    }

    const rows = enrollmentDetails.map(course => `
        <tr>
            <td>${course.courseId}</td>
            <td>${course.courseName}</td>
            <td>${course.partner}</td>
            <td>${course.grade || 'N/A'}</td>
            <td>${course.semester}</td>
            <td>${course.year}</td>
            <td>
                <button 
                    class="unenroll-btn" 
                    onclick="handleUnenrollmentEvent('${studentId}', '${course.courseId}')"
                    title="Unenroll from ${course.courseName}"
                >
                    &times; </button>
            </td>
        </tr>
    `).join('');

    return `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Course Name</th>
                    <th>Partner</th>
                    <th>Grade</th>
                    <th>Sem</th>
                    <th>Year</th>
                    <th>Action</th> </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

async function handleEnrollmentEvent(event) {
    event.preventDefault();
    try {
        await apiCall(`${API_BASE_URL}/registration/enroll`, {
            method: 'POST',
            body: JSON.stringify({
                studentId: document.getElementById('enrollment-student-id').value,
                courseId: document.getElementById('enrollment-course-id').value,
                year: document.getElementById('enrollment-year').value,
                semester: document.getElementById('enrollment-semester').value
            })
        });

        showToast('Enrolled successfully!', 'success');
        document.getElementById('enrollment-form').reset();
        // Refresh data
        displayStudentInfo(localStorage.getItem('loggedInEmail'));
    } catch (error) {
        showToast(error.message || 'Enrollment failed.', 'error');
    }
}

async function handleUnenrollmentEvent(studentId, courseId) {
    try {
        await apiCall(`${API_BASE_URL}/registration/unenroll`, {
            method: 'POST',
            body: JSON.stringify({
                studentId: studentId,
                courseId: courseId,
            })
        });

        showToast('Unenrolled successfully!', 'success');
        displayStudentInfo(localStorage.getItem('loggedInEmail'));
    } catch (error) {
        showToast(error.message || 'Unenrollment failed.', 'error');
    }
}

// --- Admin Dashboard ---

function displayAdminInfo(email) {
    hideAllViews();
    document.getElementById('admin-container').style.display = 'block';
    document.getElementById('admin-details-container').innerHTML = `
        <button class="logout-btn" onclick="handleLogout()">Logout</button>
        <h2>Admin Profile</h2>
        <p><strong>Logged in as:</strong> ${email}</p>
    `;
}

async function handleCourseAddition(event) {
    event.preventDefault();
    try {
        await apiCall(`${API_BASE_URL}/registration/course`, {
            method: 'POST',
            body: JSON.stringify({
                courseId: document.getElementById('add-course-id').value,
                courseName: document.getElementById('add-course-name').value,
                partner: document.getElementById('add-course-partner').value,
                rating: document.getElementById('add-course-rating').value,
                certificateType: document.getElementById('add-course-certificate-type').value,
                duration: document.getElementById('add-course-duration').value
            })
        });
        showToast('Course added successfully!', 'success');
        document.getElementById('add-course-form').reset();
    } catch (error) {
        showToast(error.message || 'Failed to add course.', 'error');
    }
}

// Initialize app on load
window.onload = () => {
    // Optional: Check if already logged in (simplified version)
    const loggedInEmail = localStorage.getItem('loggedInEmail');
    const userType = localStorage.getItem('userType');

    if (loggedInEmail && userType === 'student') {
        displayStudentInfo(loggedInEmail);
    } else if (loggedInEmail && userType === 'admin') {
        displayAdminInfo(loggedInEmail);
    } else {
        showLoginView();
    }
};

/**
 * Handles the user logout process.
 * Clears session data (localStorage) and shows the login view.
 */
function handleLogout() {
    localStorage.removeItem('loggedInEmail');
    localStorage.removeItem('userType');
    showToast('Logged out successfully.', 'info');
    showLoginView();
}

