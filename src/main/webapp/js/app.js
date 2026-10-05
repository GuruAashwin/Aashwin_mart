/**
 * AASHWIN MART — Client-Side Application JavaScript
 */

document.addEventListener('DOMContentLoaded', () => {
    initAddToCartAjax();
    initQuantityControls();
    initRatingStars();
});

/**
 * AJAX Add to Cart handler for instant UX
 */
function initAddToCartAjax() {
    const ajaxCartButtons = document.querySelectorAll('.js-ajax-add-cart');
    ajaxCartButtons.forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.preventDefault();
            const productId = btn.dataset.productId;
            const qtyInput = document.getElementById('product-qty') || { value: 1 };
            const quantity = qtyInput.value || 1;

            btn.disabled = true;
            const originalText = btn.innerHTML;
            btn.innerHTML = 'Adding...';

            try {
                const csrfToken = document.querySelector('meta[name="csrf-token"]')?.content || '';
                const response = await fetch(`${getContextPath()}/api/v1/cart/add`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'X-CSRF-Token': csrfToken
                    },
                    body: `productId=${encodeURIComponent(productId)}&quantity=${encodeURIComponent(quantity)}`
                });

                const data = await response.json();
                if (data.success) {
                    updateCartBadge(data.data.cartCount);
                    showToast('Item successfully added to your Royal Cart!', 'success');
                } else {
                    if (response.status === 401) {
                        window.location.href = `${getContextPath()}/login?redirect=${encodeURIComponent(window.location.pathname + window.location.search)}`;
                        return;
                    }
                    showToast(data.error ? data.error.message : 'Could not add item to cart', 'error');
                }
            } catch (err) {
                showToast('Network error, please try again.', 'error');
            } finally {
                btn.disabled = false;
                btn.innerHTML = originalText;
            }
        });
    });
}

/**
 * Quantity increment/decrement buttons
 */
function initQuantityControls() {
    document.querySelectorAll('.js-qty-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const input = btn.parentElement.querySelector('input[type="number"]');
            if (!input) return;
            let current = parseInt(input.value) || 1;
            const step = btn.dataset.action === 'inc' ? 1 : -1;
            const min = parseInt(input.min) || 1;
            const max = parseInt(input.max) || 999;
            const next = Math.min(max, Math.max(min, current + step));
            input.value = next;

            // If inside a cart table, auto-submit or trigger change
            if (input.form && input.classList.contains('js-cart-qty-input')) {
                input.form.submit();
            }
        });
    });
}

/**
 * Interactive Star Rating Picker for Review Submission
 */
function initRatingStars() {
    const starContainers = document.querySelectorAll('.js-star-rating');
    starContainers.forEach(container => {
        const stars = container.querySelectorAll('.star-icon');
        const hiddenInput = container.querySelector('input[name="rating"]');

        stars.forEach((star, index) => {
            star.addEventListener('click', () => {
                const rating = index + 1;
                if (hiddenInput) hiddenInput.value = rating;
                highlightStars(stars, rating);
            });

            star.addEventListener('mouseenter', () => {
                highlightStars(stars, index + 1);
            });
        });

        container.addEventListener('mouseleave', () => {
            const current = hiddenInput ? parseInt(hiddenInput.value) || 5 : 5;
            highlightStars(stars, current);
        });
    });
}

function highlightStars(stars, count) {
    stars.forEach((star, idx) => {
        if (idx < count) {
            star.classList.add('active');
            star.textContent = '★';
        } else {
            star.classList.remove('active');
            star.textContent = '☆';
        }
    });
}

/**
 * Global Cart Badge update helper
 */
function updateCartBadge(count) {
    const badges = document.querySelectorAll('.js-cart-badge');
    badges.forEach(badge => {
        badge.textContent = count;
        badge.style.display = count > 0 ? 'inline-block' : 'none';
    });
}

/**
 * Toast notification notification helper
 */
function showToast(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.style.cssText = 'position:fixed;top:20px;right:20px;z-index:9999;display:flex;flex-direction:column;gap:10px;';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    const bg = type === 'success' ? '#059669' : type === 'error' ? '#DC2626' : '#D4AF37';
    toast.style.cssText = `background:${bg};color:#fff;padding:12px 20px;border-radius:6px;box-shadow:0 4px 14px rgba(0,0,0,0.15);font-size:0.9rem;font-weight:600;display:flex;align-items:center;gap:8px;animation:fadeIn 0.3s ease;`;
    toast.textContent = message;

    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transition = 'opacity 0.4s ease';
        setTimeout(() => toast.remove(), 400);
    }, 3500);
}

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf('/', 2)) || '';
}
