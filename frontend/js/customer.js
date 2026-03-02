// Customer Dashboard Functions

// Check authentication
if (!requireAuth('CUSTOMER')) {
    throw new Error('Authentication required');
}

// Load user info
const user = getUser();
document.getElementById('userName').textContent = user.fullName;
document.getElementById('userEmail').textContent = user.email;

// Set avatar initial
const avatarEl = document.getElementById('userAvatar');
if (avatarEl && user.fullName) {
    avatarEl.textContent = user.fullName.charAt(0).toUpperCase();
}

// Store countdown intervals to clear them when needed
let countdownIntervals = {};

// Show section function
function showSection(sectionName) {
    document.querySelectorAll('[id$="Section"]').forEach(section => {
        section.style.display = 'none';
    });
    
    document.getElementById(sectionName + 'Section').style.display = 'block';
    
    document.querySelectorAll('.side-nav-item').forEach(link => {
        link.classList.remove('active');
    });
    event.target.closest('.side-nav-item').classList.add('active');
    
    if (sectionName === 'bookings') {
        loadMyBookings();
    }
}

// Format time remaining
function formatTimeRemaining(milliseconds) {
    if (milliseconds <= 0) {
        return { text: 'EXPIRED', isExpired: true };
    }
    
    const totalSeconds = Math.floor(milliseconds / 1000);
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = totalSeconds % 60;
    
    const text = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    
    return { 
        text, 
        isExpired: false,
        isUrgent: totalSeconds < 1800,
        isCritical: totalSeconds < 600
    };
}

// Start countdown timer for a booking
function startCountdown(bookingId, deadline) {
    if (countdownIntervals[bookingId]) {
        clearInterval(countdownIntervals[bookingId]);
    }
    
    const timerElement = document.getElementById(`timer-${bookingId}`);
    const statusElement = document.getElementById(`status-${bookingId}`);
    
    if (!timerElement) return;
    
    const updateTimer = () => {
        const now = new Date().getTime();
        const deadlineTime = new Date(deadline).getTime();
        const remaining = deadlineTime - now;
        
        const timeInfo = formatTimeRemaining(remaining);
        timerElement.textContent = timeInfo.text;
        
        timerElement.classList.remove('timer-normal', 'timer-urgent', 'timer-critical', 'timer-expired');
        
        if (timeInfo.isExpired) {
            timerElement.classList.add('timer-expired');
            if (statusElement) {
                statusElement.innerHTML = '<span class="material-symbols-rounded">cancel</span> Auto-Cancelled';
                statusElement.className = 'bk-badge b-cancelled';
            }
            clearInterval(countdownIntervals[bookingId]);
            setTimeout(() => loadMyBookings(), 2000);
        } else if (timeInfo.isCritical) {
            timerElement.classList.add('timer-critical');
        } else if (timeInfo.isUrgent) {
            timerElement.classList.add('timer-urgent');
        } else {
            timerElement.classList.add('timer-normal');
        }
    };
    
    updateTimer();
    countdownIntervals[bookingId] = setInterval(updateTimer, 1000);
}

// Badge icon map
function getStatusIcon(status) {
    const icons = {
        'PENDING': 'schedule',
        'ACCEPTED': 'check_circle',
        'COMPLETED': 'verified',
        'REJECTED': 'block',
        'CANCELLED': 'cancel'
    };
    return icons[status] || 'info';
}

// Render stats
function renderStats(bookings) {
    const statsContainer = document.getElementById('bookingStats');
    const total = bookings.length;
    const pending = bookings.filter(b => b.status === 'PENDING').length;
    const accepted = bookings.filter(b => b.status === 'ACCEPTED').length;
    const rejected = bookings.filter(b => b.status === 'REJECTED').length;

    statsContainer.innerHTML = `
        <div class="stat-chip">
            <div class="stat-icon total"><span class="material-symbols-rounded">confirmation_number</span></div>
            <div class="stat-info"><div class="stat-count">${total}</div><div class="stat-label">Total Bookings</div></div>
        </div>
        <div class="stat-chip">
            <div class="stat-icon pending"><span class="material-symbols-rounded">hourglass_top</span></div>
            <div class="stat-info"><div class="stat-count">${pending}</div><div class="stat-label">Pending</div></div>
        </div>
        <div class="stat-chip">
            <div class="stat-icon accepted"><span class="material-symbols-rounded">thumb_up</span></div>
            <div class="stat-info"><div class="stat-count">${accepted}</div><div class="stat-label">Accepted</div></div>
        </div>
        <div class="stat-chip">
            <div class="stat-icon rejected"><span class="material-symbols-rounded">thumb_down</span></div>
            <div class="stat-info"><div class="stat-count">${rejected}</div><div class="stat-label">Rejected</div></div>
        </div>
    `;
}

// Load bookings
async function loadMyBookings() {
    Object.values(countdownIntervals).forEach(interval => clearInterval(interval));
    countdownIntervals = {};
    
    try {
        const token = localStorage.getItem('token');
        const bookings = await apiCall('/api/customer/bookings', 'GET', null, token);
        
        const container = document.getElementById('bookingsList');
        
        if (bookings.length === 0) {
            document.getElementById('bookingStats').innerHTML = '';
            container.innerHTML = `
                <div class="empty-bookings">
                    <span class="material-symbols-rounded">directions_car</span>
                    <h3>No bookings yet</h3>
                    <p>Start exploring vehicles and make your first reservation!</p>
                    <a href="vehicle-search.html" class="btn-new-booking"><span class="material-symbols-rounded">add</span> Book a Vehicle</a>
                </div>`;
            return;
        }
        
        // Render stats
        renderStats(bookings);
        
        container.innerHTML = bookings.map(booking => {
            const isPending = booking.status === 'PENDING';
            const isAccepted = booking.status === 'ACCEPTED';
            const sl = booking.status.toLowerCase();
            
            return `
            <div class="bk-card s-${sl}">
                <div class="bk-accent"></div>
                <div class="bk-body">
                    <div class="bk-header">
                        <div class="bk-vehicle-info">
                            <div class="bk-vehicle-icon">
                                <span class="material-symbols-rounded">directions_car</span>
                            </div>
                            <div>
                                <div class="bk-vehicle-name">${booking.vehicleName}</div>
                                <div class="bk-shop-name">
                                    <span class="material-symbols-rounded">storefront</span>
                                    ${booking.shopName}
                                </div>
                            </div>
                        </div>
                        <span id="status-${booking.id}" class="bk-badge b-${sl}">
                            <span class="material-symbols-rounded">${getStatusIcon(booking.status)}</span>
                            ${booking.status}
                        </span>
                    </div>
                    
                    ${isPending ? `
                    <div class="bk-timer-box">
                        <div class="bk-timer-header">
                            <span class="material-symbols-rounded">timer</span>
                            <span class="bk-timer-label">Confirmation Countdown</span>
                        </div>
                        <div id="timer-${booking.id}" class="countdown-timer timer-normal">--:--:--</div>
                        <p class="bk-timer-warning">
                            <span class="material-symbols-rounded" style="font-size:1rem">warning</span>
                            Owner must accept before timer expires or booking will be auto-cancelled
                        </p>
                        <p class="bk-timer-hint">Visit the shop for faster confirmation</p>
                    </div>
                    ` : ''}
                    
                    ${isAccepted ? `
                    <div class="bk-confirmed-box">
                        <span class="material-symbols-rounded">verified</span>
                        Booking Confirmed by Owner!
                    </div>
                    ` : ''}
                    
                    <div class="bk-details">
                        <div class="bk-detail">
                            <span class="material-symbols-rounded">event</span>
                            <div>
                                <div class="bk-detail-label">Start Date</div>
                                <div class="bk-detail-value">${new Date(booking.startTime).toLocaleString()}</div>
                            </div>
                        </div>
                        <div class="bk-detail">
                            <span class="material-symbols-rounded">event_available</span>
                            <div>
                                <div class="bk-detail-label">End Date</div>
                                <div class="bk-detail-value">${new Date(booking.endTime).toLocaleString()}</div>
                            </div>
                        </div>
                        <div class="bk-detail">
                            <span class="material-symbols-rounded">schedule</span>
                            <div>
                                <div class="bk-detail-label">Duration</div>
                                <div class="bk-detail-value">${booking.totalHours} hours</div>
                            </div>
                        </div>
                        <div class="bk-detail">
                            <span class="material-symbols-rounded">payments</span>
                            <div>
                                <div class="bk-detail-label">Total Price</div>
                                <div class="bk-detail-value">$${booking.totalPrice}</div>
                            </div>
                        </div>
                        <div class="bk-detail">
                            <span class="material-symbols-rounded">edit_calendar</span>
                            <div>
                                <div class="bk-detail-label">Booked On</div>
                                <div class="bk-detail-value">${new Date(booking.bookingDate).toLocaleString()}</div>
                            </div>
                        </div>
                    </div>
                    
                    ${booking.notes ? `
                    <div class="bk-notes">
                        <span class="material-symbols-rounded">sticky_note_2</span>
                        <div>${booking.notes}</div>
                    </div>` : ''}
                    
                    ${(isPending || booking.status === 'COMPLETED') ? `
                    <div class="bk-actions">
                        ${isPending ? 
                            `<button class="bk-btn bk-btn-danger" onclick="cancelBooking(${booking.id})">
                                <span class="material-symbols-rounded">close</span> Cancel Booking
                            </button>` : ''}
                        ${booking.status === 'COMPLETED' ? 
                            `<button class="bk-btn bk-btn-primary" onclick="addReview(${booking.id}, ${booking.vehicleId})">
                                <span class="material-symbols-rounded">rate_review</span> Add Review
                            </button>` : ''}
                    </div>` : ''}
                </div>
            </div>
        `}).join('');
        
        // Start countdown timers for pending bookings
        bookings.forEach(booking => {
            if (booking.status === 'PENDING' && booking.confirmationDeadline) {
                startCountdown(booking.id, booking.confirmationDeadline);
            }
        });
        
    } catch (error) {
        document.getElementById('bookingStats').innerHTML = '';
        document.getElementById('bookingsList').innerHTML = `
            <div class="error-state">
                <span class="material-symbols-rounded">error</span>
                <p>Failed to load bookings. Please try again.</p>
            </div>`;
        console.error('Error loading bookings:', error);
    }
}

// Cancel booking
async function cancelBooking(bookingId) {
    if (!confirm('Are you sure you want to cancel this booking?')) {
        return;
    }
    
    try {
        const token = localStorage.getItem('token');
        await apiCall(`/api/customer/bookings/${bookingId}/cancel`, 'PUT', null, token);
        alert('Booking cancelled successfully');
        loadMyBookings();
    } catch (error) {
        alert('Failed to cancel booking: ' + error.message);
    }
}

// Add review
function addReview(bookingId, vehicleId) {
    const rating = prompt('Rate this vehicle (1-5 stars):');
    if (!rating || rating < 1 || rating > 5) {
        alert('Please enter a valid rating between 1 and 5');
        return;
    }
    
    const comment = prompt('Add your review comment (optional):');
    
    submitReview(bookingId, vehicleId, parseInt(rating), comment);
}

async function submitReview(bookingId, vehicleId, rating, comment) {
    try {
        const token = localStorage.getItem('token');
        await apiCall('/api/customer/reviews', 'POST', {
            bookingId,
            vehicleId,
            rating,
            comment: comment || ''
        }, token);
        alert('Review submitted successfully!');
    } catch (error) {
        alert('Failed to submit review: ' + error.message);
    }
}

// Initial load
loadMyBookings();
