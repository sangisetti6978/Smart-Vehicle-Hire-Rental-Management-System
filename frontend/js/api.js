// API Configuration — uses current hostname so it works from any device
const API_BASE_URL = `http://${window.location.hostname}:8888`;

// Resolve vehicle image URL — handles server-relative paths, full URLs, and missing images
function resolveVehicleImg(url, fallbackName) {
    if (!url) return 'https://via.placeholder.com/400x220/e2e8f0/64748b?text=' + encodeURIComponent(fallbackName || 'Vehicle');
    if (url.startsWith('http://') || url.startsWith('https://') || url.startsWith('data:')) return url;
    return API_BASE_URL + url;
}

// API Call Function
async function apiCall(endpoint, method = 'GET', data = null, token = null) {
    const headers = {
        'Content-Type': 'application/json'
    };
    
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    
    const config = {
        method,
        headers
    };
    
    if (data && (method === 'POST' || method === 'PUT')) {
        config.body = JSON.stringify(data);
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        
        if (!response.ok) {
            // Handle authentication/authorization failures
            if (response.status === 401 || response.status === 403) {
                // Check if this is an API call that requires auth (not login/register)
                if (!endpoint.startsWith('/api/auth/')) {
                    console.warn('Auth failed (token may be expired/invalid). Redirecting to login.');
                    localStorage.removeItem('token');
                    localStorage.removeItem('user');
                    // Redirect to appropriate login page based on the endpoint
                    let loginPage = 'choose-role.html';
                    if (endpoint.includes('/api/owner/')) loginPage = 'login-owner.html';
                    else if (endpoint.includes('/api/customer/')) loginPage = 'login-customer.html';
                    else if (endpoint.includes('/api/admin/')) loginPage = 'login.html?role=admin';
                    window.location.href = loginPage;
                    throw new Error('Session expired. Please login again.');
                }
            }
            let errorMessage = `HTTP error! status: ${response.status}`;
            try {
                const error = await response.json();
                errorMessage = error.message || error.error || errorMessage;
            } catch (e) {
                // Response body is not JSON
            }
            throw new Error(errorMessage);
        }
        
        // Handle empty responses (e.g., 204 No Content or empty body)
        const text = await response.text();
        if (!text) return null;
        return JSON.parse(text);
    } catch (error) {
        console.error('API call error:', error);
        throw error;
    }
}

// Export for use in other files
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { apiCall, API_BASE_URL };
}
