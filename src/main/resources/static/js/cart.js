function updateQuantity(button, delta) {
    const bookId = button.getAttribute('data-id');
    const input = button.parentElement.querySelector('.quantity');
    let currentValue = parseInt(input.value);
    let newValue = currentValue + delta;
    if (newValue < 1) {
        newValue = 1;
    }
    input.value = newValue;
    // Redirect to update URL
    window.location.href = `/cart/updateCart/${bookId}/${newValue}`;
}
