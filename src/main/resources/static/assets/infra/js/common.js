document.addEventListener('DOMContentLoaded', function () {
    const mypageBtn = document.querySelector('#header .ico.menu');
    const menuAll = document.querySelector('.menu-all');

    if (mypageBtn && menuAll) {
        mypageBtn.addEventListener('click', function (e) {
            e.stopPropagation(); // 이벤트 버블링 방지
            menuAll.classList.toggle('active');
        });

        menuAll.addEventListener('click', function (e) {
            e.stopPropagation();
        });

        document.addEventListener('click', function () {
            menuAll.classList.remove('active');
        });
    }
});
