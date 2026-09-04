// CUSTOM POPUP FUNCTION
function showPopup(message, type) {

    const popup = document.getElementById("customPopup");

    if (!popup) {
        return;
    }

    popup.textContent = message;

    popup.className = "custom-popup";

    if (type === "success") {
        popup.classList.add("popup-success");
    } else {
        popup.classList.add("popup-error");
    }

    popup.style.display = "block";

    setTimeout(() => {
        popup.style.display = "none";
    }, 2000);
}

// REGISTER FORM
document.getElementById('registerForm')?.addEventListener('submit', function(e) {

    e.preventDefault();

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const department = document.getElementById('department').value;

    fetch("http://localhost:8080/students/register", {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            name: name,
            email: email,
            password: password,
            department: department
        })
    })

    .then(response => {

        if (!response.ok) {
            throw new Error("Registration failed");
        }

        return response.json();
    })

    .then(student => {

        const successMessage =
            document.getElementById("successMessage");

        successMessage.style.display = "block";

        setTimeout(() => {

            window.location.href = "index.html";

        }, 1500);

    })

    .catch(error => {

        console.error(error);

        const errorMessage =
            document.getElementById("errorMessage");

        if (errorMessage) {
            errorMessage.style.display = "block";
        }

    });

});

// LOGIN FORM
document.getElementById('loginForm')?.addEventListener('submit', function(e) {

    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch("http://localhost:8080/students/login", {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            email: email,
            password: password
        })
    })

    .then(async response => {

    if (!response.ok) {

        const errorData =
            await response.json();

        throw new Error(
            errorData.message ||
            "Login failed"
        );
    }

    return response.json();
})

    .then(student => {

        // Logged-in student details save pannrom
        localStorage.setItem("studentId", student.id);
        localStorage.setItem("studentName", student.name);
        localStorage.setItem("studentEmail", student.email);
        localStorage.setItem("studentDept", student.department);

        showPopup("Login successful!", "success");
        setTimeout(() => {
            window.location.href = "dashboard.html";
        }, 800);
    })

    .catch(error => {

    const errorMessage =
        error.message.toLowerCase();

    if (
        errorMessage.includes("email not found") ||
        errorMessage.includes("email not registered")) {

        showPopup(
            "New user? Please register first!",
            "error"
        );

    } else {

        showPopup(
            "Incorrect password. Please try again!",
            "error"
        );

    }

});

});

// DISPLAY STUDENT INFO
window.addEventListener('load', function() {

    const studentName = document.getElementById('studentName');
    const studentEmail = document.getElementById('studentEmail');
    const studentDept = document.getElementById('studentDept');

    if (studentName) {
        studentName.textContent =
            localStorage.getItem('studentName') || 'Guest Student';
    }

    if (studentEmail) {
        studentEmail.textContent =
            localStorage.getItem('studentEmail') || 'student@college.com';
    }

    if (studentDept) {
        studentDept.textContent =
            localStorage.getItem('studentDept') || 'Computer Science';
    }

    displayApplications();
    loadCompanies();
});


// SEARCH COMPANY
document.getElementById('searchInput')?.addEventListener('keyup', function() {

    let value = this.value.toLowerCase();

    let companies = document.querySelectorAll('.company-card');

    companies.forEach(card => {

        let companyName =
            card.querySelector('h3').textContent.toLowerCase();

        if (companyName.includes(value)) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }

    });
});

// APPLY COMPANY
function applyCompany(companyId, companyName) {

    fetch("http://localhost:8080/applications", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            studentId: Number(localStorage.getItem("studentId")),
            companyId: companyId
        })
    })
    .then(response => {

        if (!response.ok) {
            throw new Error("Already applied");
        }

        return response.json();
    })
    .then(data => {

        showPopup(
            "Applied successfully to " + companyName + "!",
            "success"
        );
        displayApplications();
        loadCompanies();

    })
    .catch(error => {

        showPopup("You have already applied to this company!", "error");

    });

}
// DISPLAY APPLICATIONS
function displayApplications() {

    let list = document.getElementById("appliedCompanies");

    if (!list) return;

    const studentId = localStorage.getItem("studentId");

    list.innerHTML = `
        <li class="empty-application">
            ⏳ Loading applications...
        </li>
    `;

    Promise.all([
        fetch(`http://localhost:8080/applications/student/${studentId}`)
            .then(response => response.json()),

        fetch("http://localhost:8080/companies")
            .then(response => response.json())
    ])
    .then(([applications, companies]) => {

        const count = document.getElementById("appliedCount");

        if (count) {
            count.textContent = applications.length;
        }

        list.innerHTML = "";

        if (applications.length === 0) {

            list.innerHTML = `
                <li class="empty-application">
                    📭 No applications yet. Start applying to companies!
                </li>
            `;

            return;
        }

        applications.forEach(application => {

            const company = companies.find(
                company => company.id === application.companyId
            );

            if (!company) return;

            let status = application.status || "Applied";

            let statusClass = status
                .toLowerCase()
                .replace(/\s+/g, "-");

            let li = document.createElement("li");

            li.className = "application-item";

            li.innerHTML = `
                <div class="application-info">

                    <span class="dot"></span>

                    <div class="company-item">
                        <strong>${company.companyName}</strong>
                        <span>${company.roleName}</span>
                    </div>

                </div>

                <div class="application-status">

                    <span class="status-badge ${statusClass}">
                        ${status}
                    </span>

                    ${
                        status === "Applied"
                        ? `
                            <button
                                class="withdraw-btn"
                                onclick="withdrawApplication(${application.id})">
                                Withdraw
                            </button>
                        `
                        : ""
                    }

                </div>
            `;

            list.appendChild(li);
        });

    })
    .catch(error => {

        console.error(
            "Error loading applications:",
            error
        );

        list.innerHTML = `
            <li class="empty-application">
                ❌ Unable to load applications.
            </li>
        `;

    });
}
// WITHDRAWN APPLICATION
let selectedApplicationId = null;


// OPEN WITHDRAW MODAL

function withdrawApplication(applicationId) {

    selectedApplicationId = applicationId;

    const modal =
        document.getElementById("withdrawModal");

    if (modal) {

        modal.style.display = "flex";

    }

}


// CLOSE WITHDRAW MODAL

function closeWithdrawModal() {

    const modal =
        document.getElementById("withdrawModal");

    if (modal) {

        modal.style.display = "none";

    }

    selectedApplicationId = null;

}


// CONFIRM WITHDRAW

function confirmWithdraw() {

    if (selectedApplicationId === null) {

        return;

    }

    fetch(
        `http://localhost:8080/applications/${selectedApplicationId}`,
        {
            method: "DELETE"
        }
    )

    .then(response => {

        if (!response.ok) {

            throw new Error(
                "Failed to withdraw application"
            );

        }

        return response.text();

    })

    .then(message => {

        closeWithdrawModal();

        showPopup(
            message,
            "success"
        );

        displayApplications();
        loadCompanies();

    })

    .catch(error => {

        console.error(error);

        closeWithdrawModal();

        showPopup(
            "Failed to withdraw application",
            "error"
        );

    });

}
// LOAD COMPANIES + MATCH PERCENTAGE
// LOAD COMPANIES + MATCH PERCENTAGE + MISSING SKILLS
function loadCompanies() {

    const studentId = localStorage.getItem("studentId");

    if (!studentId) {
        console.log("Student ID not found");
        return;
    }

    const grid = document.getElementById("companiesGrid");

    if (!grid) return;

    fetch("http://localhost:8080/companies")
        .then(response => response.json())
        .then(companies => {

            return fetch(
                `http://localhost:8080/matches/student/${studentId}`
            )
            .then(response => response.json())
            .then(matches => {

                grid.innerHTML = "";

                companies.forEach(company => {

                    const matchText = matches.find(match =>
                        match.startsWith(company.companyName + " - ")
                    );

                    let matchPercentage = 0;
                    let missingSkills = "Upload resume to see skill gap";

                    if (matchText) {

                        const percentage =
                            matchText.match(/(\d+)%/);

                        if (percentage) {
                            matchPercentage =
                                parseInt(percentage[1]);
                        }

                        const missingSkillsMatch =
                            matchText.match(/Missing Skills:\s*(.*)$/);

                        if (missingSkillsMatch) {
                            missingSkills =
                                missingSkillsMatch[1];
                        }
                    }

                    let matchClass = "match-low";

                    if (matchPercentage >= 80) {
                        matchClass = "match-high";
                    } else if (matchPercentage >= 50) {
                        matchClass = "match-medium";
                    }

                    grid.innerHTML += `
                        <div class="company-card">

                            <h3>${company.companyName}</h3>

                            <p>
                                <strong>Role:</strong>
                                ${company.roleName}
                            </p>

                            <p>
                                <strong>Package:</strong>
                                ${company.packageLpa}
                            </p>

                            <div class="match-section">

                                <span class="${matchClass}">
                                    🎯 ${matchPercentage}% Match
                                </span>

                            </div>

                            <p class="missing-skills">
                                <strong>⚠ Missing Skills:</strong>
                                ${missingSkills}
                            </p>

                            <button
                                class="apply-btn"
                                onclick="applyCompany(
                                    ${company.id},
                                    '${company.companyName}'
                                )">

                                Apply Now

                            </button>

                        </div>
                    `;
                });

                let highestMatch = 0;

                matches.forEach(match => {

                    const percentage =
                        match.match(/(\d+)%/);

                    if (percentage) {

                        const value =
                            parseInt(percentage[1]);

                        if (value > highestMatch) {
                            highestMatch = value;
                        }
                    }
                });

                const topMatch =
                    document.getElementById("topMatch");

                if (topMatch) {
                    topMatch.textContent =
                        highestMatch + "%";
                }

            });
        })
        .catch(error => {

            console.error(
                "Company / Match loading error:",
                error
            );

        });
}
// OPEN LOGOUT MODAL
function logout() {

    const modal =
        document.getElementById("logoutModal");

    if (modal) {

        modal.style.display = "flex";

    }

}


// CLOSE LOGOUT MODAL
function closeLogoutModal() {

    const modal =
        document.getElementById("logoutModal");

    if (modal) {

        modal.style.display = "none";

    }

}


// CONFIRM LOGOUT
function confirmLogout() {

    localStorage.clear();

    window.location.href = "index.html";

}
// RESUME UPLOAD

document.getElementById("resumeForm")?.addEventListener("submit", function(e) {

    e.preventDefault();

    const fileInput =
        document.getElementById("resumeFile");

    const file = fileInput.files[0];

    if (!file) {
        showPopup("Please select a resume!", "error");
        return;
    }

    const studentId =
        localStorage.getItem("studentId");

    const formData = new FormData();

    formData.append("file", file);
    formData.append("studentId", studentId);

    fetch("http://localhost:8080/resumes/upload", {
        method: "POST",
        body: formData
    })

    .then(response => {

        if (!response.ok) {
            throw new Error("Resume upload failed");
        }

        return response.json();

    })

    .then(data => {

        showPopup(
            "Resume uploaded successfully!",
            "success"
        );

        console.log("Uploaded Resume:", data);

    })

    .catch(error => {

        console.error(error);

        showPopup(
            "Resume upload failed!",
            "error"
        );

    });

});