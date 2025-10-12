document.addEventListener('DOMContentLoaded', () => {
    likeBtn(); //즐겨찾기 함수

    const birth = document.querySelector('.birth');
    if (!birth) return;

    const yearSB = birth.querySelector('[data-type="year"]');
    const monthSB = birth.querySelector('[data-type="month"]');
    const daySB = birth.querySelector('[data-type="day"]');

    // 동적 옵션 생성
    const yearUl = yearSB.querySelector('.select-options');
    const monthUl = monthSB.querySelector('.select-options');
    const dayUl = daySB.querySelector('.select-options');

    const currentYear = new Date().getFullYear();

    for (let y = currentYear; y >= 1950; y--) {
        const li = document.createElement('li');
        li.className = 'select-option';
        li.textContent = `${y}년`;
        li.dataset.value = y;
        yearUl.appendChild(li);
    }

    // 2자리 zero-padding
    const pad2 = n => String(n).padStart(2, '0');

    for (let m = 1; m <= 12; m++) {
        const li = document.createElement('li');
        li.className = 'select-option';
        li.textContent = `${m}월`;
        li.dataset.value = pad2(m);
        monthUl.appendChild(li);
    }

    function rebuildDays() {
        const y = Number(yearSB.querySelector('.select-btn').dataset.value || currentYear);
        const m = Number(monthSB.querySelector('.select-btn').dataset.value || 1);

        dayUl.innerHTML = '';
        const lastDay = new Date(y, m, 0).getDate(); // 윤년 포함 자동계산
        for (let d = 1; d <= lastDay; d++) {
            const li = document.createElement('li');
            li.className = 'select-option';
            li.textContent = `${d}일`;
            li.dataset.value = pad2(d);        // ← 여기 (01~31)
            dayUl.appendChild(li);
        }

        // 현재 선택된 '일'이 범위를 벗어나면 placeholder로 리셋
        const dayBtn = daySB.querySelector('.select-btn');
        const curDay = Number(dayBtn.dataset.value || 0);
        const span = dayBtn.querySelector('span');
        if (!curDay || curDay > lastDay) {
            dayBtn.dataset.value = '';
            span.textContent = '일';
            span.classList.remove('selected');
        }
    }

    // 초기 1월 기준 생성
    rebuildDays();

    // 연/월이 바뀌면 일 재계산
    yearSB.addEventListener('select:change', rebuildDays);
    monthSB.addEventListener('select:change', rebuildDays);


    // 회원가입 개인정보입력 버튼 활성화
    const joinForm = document.querySelector('.join-wrap.join-info');
    if (joinForm) {
        const completeBtn = joinForm.querySelector('.btn.line-blue');

        // 전역에서도 호출 가능하게
        window.checkJoinForm = function checkForm() {
            const isSso = (joinForm.closest('form')?.dataset.sso === '1');

            const email = joinForm.querySelector('#email')?.value.trim();
            const pw1 = joinForm.querySelector('#loginPw')?.value.trim();
            const pw2 = joinForm.querySelector('#loginPwChk')?.value.trim();
            const year = joinForm.querySelector('[data-type="year"] .select-btn')?.dataset.value;
            const month = joinForm.querySelector('[data-type="month"] .select-btn')?.dataset.value;
            const day = joinForm.querySelector('[data-type="day"] .select-btn')?.dataset.value;
            const gender = joinForm.querySelector('input[name="gender"]:checked')?.value;
            const nickname = joinForm.querySelector('#nickNm')?.value.trim();

            const baseOk = !!(year && month && day && gender && nickname);
            const localOk = !!(email && pw1 && pw2 && pw1.length >= 8 && pw1 === pw2);

            const allFilled = isSso ? baseOk : (baseOk && localOk);

            completeBtn.classList.toggle('inactive', !allFilled);
            completeBtn.disabled = !allFilled;
        };

        // input, radio
        joinForm.querySelectorAll('input').forEach(input => {
            input.addEventListener('input', window.checkJoinForm);
            input.addEventListener('change', window.checkJoinForm);
        });

        // 커스텀 셀렉트 변경 감지 (li[data-value] 클릭 시 select-btn.dataset.value 세팅하는 코드가 따로 있어야 함)
        joinForm.querySelectorAll('.custom-select-box').forEach(sb => {
            sb.addEventListener('select:change', window.checkJoinForm);
        });

        // 초기 상태
        window.checkJoinForm();
    }
});


// 기본 JavaScript
function likeBtn() {
    const buttons = document.querySelectorAll('.like-btn');
    if (buttons.length) {
        buttons.forEach(button => {
            button.addEventListener('click', () => {
                button.classList.toggle('active');
            });
        });
    }
}

//더보기
$(function () {
    $('.more-btn').each(function () {
        const $btn = $(this);
        const $wrap = $btn.closest('.more-btn-wrap');
        const $target = $wrap.closest('.editor-wrap, .more-wrap');
        const isMoreWrap = $target.hasClass('more-wrap');
        const originalHeight = isMoreWrap ? '270px' : '500px'; // 초기 높이

        if ($target.length) {
            $btn.off('click').on('click', function () {
                const isExpanded = $btn.data('expanded') || false;

                if (isMoreWrap) {
                    // .more-wrap의 경우: 토글 동작
                    if (!isExpanded) {
                        $target.css('height', 'auto');
                        $btn.text('접어두기 -').data('expanded', true);
                    } else {
                        $target.css('height', originalHeight);
                        $btn.text('더보기 +').data('expanded', false);
                    }
                    $wrap.show(); // .more-btn-wrap 유지
                } else {
                    // .editor-wrap의 경우: 기존 동작
                    $target.css('height', 'auto');
                    $wrap.fadeOut(200);
                }

                // 스크롤 이벤트 방지
                isClickTriggered = true;
                setTimeout(() => {
                    isClickTriggered = false;
                    setActive(currentIndexByScroll()); // 탭 상태 갱신
                }, 300); // 애니메이션 시간과 동기화
            });
        }
    });
})

//탭(스크롤버전)
$(function () {
    const $btnAreas = $('.tab-area.v1 .tab-btn-area');
    const $allBtns = $btnAreas.find('.tab-btn');
    const $tabs = $('.tab-area.v1 .tab');
    const OFFSET_MARGIN = 120; // 조정 가능한 여백
    let isScrolling = false; // 스크롤 애니메이션 플래그
    let isClickTriggered = false; // 클릭 이벤트 플래그

    if ($tabs.length === 0 || $allBtns.length === 0) return;

    // 헤더 + 고정영역 높이
    function headerHeights() {
        const h1 = $('#header').length ? $('#header').outerHeight() : 0;
        const h2 = $('.fixed-tit-area:visible').length ? $('.fixed-tit-area:visible').outerHeight() : 0;
        return h1 + h2;
    }

    // 버튼 active 동기화
    function setActive(idx) {
        $allBtns.removeClass('active');
        $btnAreas.each(function () {
            $(this).find('.tab-btn').eq(idx).addClass('active');
        });
    }

    // 스크롤 위치에 따라 현재 섹션 계산
    function currentIndexByScroll() {
        const scrollTop = $(window).scrollTop();
        const offsetTop = headerHeights() + OFFSET_MARGIN;
        let current = 0;

        $tabs.each(function (i) {
            const top = $(this).offset().top - offsetTop;
            if (scrollTop >= top) current = i;
        });
        return current;
    }

    // 클릭 → 즉시 active 동기화 + 해당 섹션으로 스크롤
    $btnAreas.on('click', '.tab-btn', function (e) {
        e.preventDefault();
        if (isScrolling) return;
        isScrolling = true;
        isClickTriggered = true; // 클릭 이벤트로 스크롤 중임을 표시

        const idx = $(this).closest('li').index();
        if (idx < 0 || idx >= $tabs.length) {
            isScrolling = false;
            isClickTriggered = false;
            return;
        }

        setActive(idx);
        const top = $tabs.eq(idx).offset().top - headerHeights() - 8;
        $('html, body').stop(true).animate({scrollTop: top}, 300, () => {
            isScrolling = false;
            isClickTriggered = false;
            setActive(idx); // 애니메이션 완료 후 상태 고정
        });
    });

    // rAF로 스크롤/리사이즈 최적화
    let ticking = false;

    function onScrollOrResize() {
        if (!ticking && !isClickTriggered) { // 클릭으로 인한 스크롤은 무시
            ticking = true;
            requestAnimationFrame(() => {
                setActive(currentIndexByScroll());
                ticking = false;
            });
        }
    }

    // 이벤트 바인딩
    const $window = $(window);
    $window.on('scroll resize', onScrollOrResize);

    // 초기 상태
    setActive(currentIndexByScroll());

    // --- like-vector 버튼 토글 ---
    function toggleActiveClass() {
        const buttons = document.querySelectorAll('.like-vector');
        if (buttons.length) {
            buttons.forEach(button => {
                button.addEventListener('click', () => {
                    button.classList.toggle('active');
                });
            });
        }
    }

    toggleActiveClass();
});