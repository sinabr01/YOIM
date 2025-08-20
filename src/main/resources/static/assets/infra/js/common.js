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


    //비밀번호 보이기
    const pwInput = document.querySelector('.password input');
    const pwBtn = document.querySelector('.btn-pw-show');

    pwBtn.addEventListener('click', () => {
        const isShow = pwInput.type === 'text';
        pwInput.type = isShow ? 'password' : 'text';
        pwBtn.classList.toggle('on', !isShow);
    });

    //로그인 타입 선택 애니메이션
    const typeBox = document.querySelector('.login-type');
    const tabs = typeBox.querySelectorAll('.btn');
    const groups = document.querySelectorAll('.login-group .group');

    tabs.forEach((btn, i) => {
        btn.addEventListener('click', () => {
            // 버튼 active 토글
            tabs.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            // indicator 이동
            typeBox.classList.toggle('move-1', i === 1);

            // 그룹 전환
            groups.forEach((g, gi) => {
                g.classList.toggle('active', gi === i);
            });
        });
    });




});
