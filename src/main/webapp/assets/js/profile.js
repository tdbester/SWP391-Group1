$(document).ready(function() {
    // Xử lý active link trong sidebar
    $('.sidebar a').on('click', function() {
        $('.sidebar a').removeClass('active');
        $(this).addClass('active');
    });

    // Kiểm tra URL hash khi trang load
    if(window.location.hash) {
        const hash = window.location.hash;
        $('.sidebar a').removeClass('active');
        $(`.sidebar a[href="${hash}"]`).addClass('active');
    }

    // Smooth scroll cho các link trong sidebar
    $('.sidebar a').on('click', function(e) {
        e.preventDefault();
        const target = $(this).attr('href');
        $('html, body').animate({
            scrollTop: $(target).offset().top - 80
        }, 800);

        // Thay đổi URL mà không reload trang
        history.pushState(null, null, target);
    });
});