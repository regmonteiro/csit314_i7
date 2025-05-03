document.addEventListener("DOMContentLoaded", function () {
    const forms = document.querySelectorAll("form");

    forms.forEach(form => {
        const loader = form.querySelector("#loader");
        if (loader) {
            form.addEventListener("submit", () => {
                loader.style.display = "flex";
            });
        }
    });
});
