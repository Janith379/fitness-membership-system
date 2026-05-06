/**
 * FitZone Gym - Client-Side JavaScript
 * Provides form validation and delete confirmation dialogs.
 */

document.addEventListener('DOMContentLoaded', function () {

    // --- Registration Form Client-Side Validation ---
    const regForm = document.getElementById('registrationForm');
    if (regForm) {
        regForm.addEventListener('submit', function (e) {
            let valid = true;

            // Validate full name
            const fullName = document.getElementById('fullName');
            if (fullName && fullName.value.trim().length < 2) {
                setInvalid(fullName, 'Name must be at least 2 characters');
                valid = false;
            } else if (fullName) {
                clearInvalid(fullName);
            }

            // Validate email
            const email = document.getElementById('email');
            if (email) {
                const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailPattern.test(email.value.trim())) {
                    setInvalid(email, 'Please enter a valid email address');
                    valid = false;
                } else {
                    clearInvalid(email);
                }
            }

            // Validate phone
            const phone = document.getElementById('phone');
            if (phone) {
                const phonePattern = /^[0-9]{10,15}$/;
                if (!phonePattern.test(phone.value.trim())) {
                    setInvalid(phone, 'Phone must be 10-15 digits');
                    valid = false;
                } else {
                    clearInvalid(phone);
                }
            }

            // Validate age
            const age = document.getElementById('age');
            if (age) {
                const ageVal = parseInt(age.value);
                if (isNaN(ageVal) || ageVal < 16 || ageVal > 100) {
                    setInvalid(age, 'Age must be between 16 and 100');
                    valid = false;
                } else {
                    clearInvalid(age);
                }
            }

            if (!valid) {
                e.preventDefault();
            }
        });
    }

    // --- Payment Form Client-Side Validation ---
    const payForm = document.getElementById('paymentForm');
    if (payForm) {
        payForm.addEventListener('submit', function (e) {
            let valid = true;

            // Validate cardholder name
            const cardName = document.getElementById('cardholderName');
            if (cardName && cardName.value.trim().length < 2) {
                setInvalid(cardName, 'Cardholder name is required');
                valid = false;
            } else if (cardName) {
                clearInvalid(cardName);
            }

            // Validate card number
            const cardNumber = document.getElementById('cardNumber');
            if (cardNumber) {
                const cardPattern = /^[0-9]{13,19}$/;
                if (!cardPattern.test(cardNumber.value.replace(/\s/g, ''))) {
                    setInvalid(cardNumber, 'Card number must be 13-19 digits');
                    valid = false;
                } else {
                    clearInvalid(cardNumber);
                }
            }

            // Validate expiry
            const expiry = document.getElementById('expiryDate');
            if (expiry) {
                const expiryPattern = /^(0[1-9]|1[0-2])\/[0-9]{2}$/;
                if (!expiryPattern.test(expiry.value.trim())) {
                    setInvalid(expiry, 'Expiry must be MM/YY format');
                    valid = false;
                } else {
                    clearInvalid(expiry);
                }
            }

            // Validate CVV
            const cvv = document.getElementById('cvv');
            if (cvv) {
                const cvvPattern = /^[0-9]{3,4}$/;
                if (!cvvPattern.test(cvv.value.trim())) {
                    setInvalid(cvv, 'CVV must be 3 or 4 digits');
                    valid = false;
                } else {
                    clearInvalid(cvv);
                }
            }

            if (!valid) {
                e.preventDefault();
            }
        });

        // Format card number input with spaces
        const cardNumberInput = document.getElementById('cardNumber');
        if (cardNumberInput) {
            cardNumberInput.addEventListener('input', function () {
                // Remove non-digit characters for validation
                this.value = this.value.replace(/[^0-9]/g, '');
            });
        }
    }

    // --- Auto-dismiss alerts after 5 seconds ---
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () {
                alert.remove();
            }, 500);
        }, 5000);
    });

    // --- Utility Functions ---
    function setInvalid(element, message) {
        element.classList.add('is-invalid');
        // Check if feedback element exists
        let feedback = element.parentElement.querySelector('.invalid-feedback');
        if (!feedback) {
            feedback = document.createElement('div');
            feedback.className = 'invalid-feedback';
            element.parentElement.appendChild(feedback);
        }
        feedback.textContent = message;
        feedback.style.display = 'block';
    }

    function clearInvalid(element) {
        element.classList.remove('is-invalid');
        const feedback = element.parentElement.querySelector('.invalid-feedback');
        if (feedback) {
            feedback.style.display = 'none';
        }
    }

    // --- Scroll Reveal Animation ---
    const revealElements = document.querySelectorAll('.reveal, .reveal-left, .reveal-right, .reveal-scale');
    
    if ('IntersectionObserver' in window) {
        const revealObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('active');
                    observer.unobserve(entry.target); // Stop observing once revealed
                }
            });
        }, {
            root: null,
            threshold: 0.15, // Trigger when 15% of the element is visible
            rootMargin: '0px 0px -50px 0px'
        });

        revealElements.forEach(el => revealObserver.observe(el));
    } else {
        // Fallback for older browsers
        revealElements.forEach(el => el.classList.add('active'));
    }
});
