// PROJECTS SCROLLSPY
const scrollSpy = new bootstrap.ScrollSpy(document.body, {
    target: '#list-example'
});

// BACK TO TOP BTN
let backToTopButton = $("#back-to-top");
let pixelsScrolled = 200

window.onscroll = function () {
    if (
        document.body.scrollTop > pixelsScrolled ||
        document.documentElement.scrollTop > pixelsScrolled
    ) {
        backToTopButton.fadeIn("slow")
    } else {
        backToTopButton.fadeOut("slow")
    }
};

backToTopButton.click(backToTop);

function backToTop() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
}