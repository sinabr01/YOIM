document.addEventListener('DOMContentLoaded', () => {
    likeBtn(); //즐겨찾기 함수

    // --- 비밀번호 보이기 ---
    const pwWraps = document.querySelectorAll('.password');

    pwWraps.forEach(pwWrap => {
        const pwInput = pwWrap.querySelector('input');
        const pwBtn = pwWrap.querySelector('.btn-pw-show');

        if (pwInput && pwBtn) {
            pwBtn.addEventListener('click', () => {
                const isShow = pwInput.type === 'text';
                pwInput.type = isShow ? 'password' : 'text';
                pwBtn.classList.toggle('on', !isShow);
            });
        }
    });

    // --- 로그인 타입 탭 ---
    const typeBox = document.querySelector('.login-type');
    if (typeBox) {
        const tabs = typeBox.querySelectorAll('.btn');
        const groups = document.querySelectorAll('.login-group .group');
        tabs.forEach((btn, i) => {
            btn.addEventListener('click', () => {
                tabs.forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                typeBox.classList.toggle('move-1', i === 1);
                groups.forEach((g, gi) => g.classList.toggle('active', gi === i));
            });
        });
    }


    // --- 약관 전체동의 ---
    const $ = (sel, root = document) => root.querySelector(sel);
    const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));

    const allCheck       = $('.all-check input[type="checkbox"]');                 // 없을 수 있음
    const subChecks      = $$('.check-wrap .check input[type="checkbox"]');
    const requiredChecks = $$('.check-wrap .check:nth-child(-n+3) input[type="checkbox"]');
    const authButton     = $('.btn.line-blue');

    // 필수 항목 모두 체크됐는지 확인 → 버튼 활성/비활성
    function checkAllRequired() {
        if (!authButton || requiredChecks.length === 0) return;
        const ok = requiredChecks.every(c => c.checked);
        authButton.classList.toggle('inactive', !ok);
        authButton.disabled = !ok;
    }

    // 전체동의가 있을 때만 바인딩
    if (allCheck) {
        allCheck.addEventListener('change', (e) => {
            const isChecked = e.target.checked;
            subChecks.forEach(cb => { cb.checked = isChecked; });
            checkAllRequired();
        });
    }

    // 개별 체크 변경 시 전체동의 상태 동기화 + 버튼 상태 갱신
    subChecks.forEach(cb => {
        cb.addEventListener('change', () => {
            if (allCheck) {
                allCheck.checked = subChecks.length > 0 && subChecks.every(c => c.checked);
            }
            checkAllRequired();
        });
    });

    // 초기 상태 반영
    checkAllRequired();

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
    $('.more-btn').each(function(){
        var $btn = $(this);
        var $wrap = $btn.closest('.more-btn-wrap');
        var $target = $wrap.siblings('.more-wrap');
        var isMore = $target.length > 0;
        if(!isMore) $target = $wrap.closest('.editor-wrap');

        var collapsed = $btn.data('height');
        if(!collapsed){
            collapsed = parseInt($target.attr('data-collapsed'),10) || parseInt($target.css('height'),10) || 0;
        }
        $target.attr('data-collapsed', collapsed);

        $btn.off('click').on('click', function(){
            var expanded = $btn.data('expanded') === true;

            if(!expanded){
                if(isMore){
                    var full = $target.get(0).scrollHeight;
                    $target.stop().animate({height: full}, 250, function(){ $target.css('height','auto'); });
                    $btn.text('접어두기 -').data('expanded', true);
                    $wrap.closest('.more-area').addClass('open');
                }else{
                    $target.css('height','auto');
                    $wrap.fadeOut(200);
                }
            }else{
                if(isMore){
                    if($target.css('height') === 'auto'){
                        $target.height($target.get(0).scrollHeight);
                    }
                    $target.stop().animate({height: collapsed}, 250);
                    $btn.text('더보기 +').data('expanded', false);
                    $wrap.closest('.more-area').removeClass('open');
                }
            }

            if(typeof isClickTriggered !== 'undefined'){
                isClickTriggered = true;
                setTimeout(function(){
                    isClickTriggered = false;
                    if(typeof currentIndexByScroll === 'function' && typeof setActive === 'function'){
                        setActive(currentIndexByScroll());
                    }
                },300);
            }
        });
    });
})

//탭(스크롤버전)
$(function () {
    const $btnAreas = $('.tab-area.v1 .tab-btn-area');
    const $allBtns = $btnAreas.find('.tab-btn');
    const $tabs = $('.tab-area.v1 .tab');

    const ACTIVATION_THRESHOLD = 0.5;
    let isScrolling = false;
    let isClickTriggered = false;

    if ($tabs.length === 0 || $allBtns.length === 0) return;

    function headerHeights() {
        const h1 = $('#header').length ? $('#header').outerHeight() : 0;
        const h2 = $('.fixed-tit-area:visible').length ? $('.fixed-tit-area:visible').outerHeight() : 0;
        return h1 + h2;
    }

    function setActive(idx) {
        $allBtns.removeClass('active');
        $btnAreas.each(function () {
            $(this).find('.tab-btn').eq(idx).addClass('active');
        });
    }

    function currentIndexByScroll() {
        const scrollTop = $(window).scrollTop();
        const windowHeight = $(window).height();

        const activationPoint = scrollTop + (windowHeight * ACTIVATION_THRESHOLD);

        let current = 0;

        $tabs.each(function (i) {
            const tabTop = $(this).offset().top;

            if (activationPoint >= tabTop) {
                current = i;
            }
        });

        return current;
    }

    $btnAreas.on('click', '.tab-btn', function (e) {
        e.preventDefault();
        if (isScrolling) return;
        isScrolling = true;
        isClickTriggered = true;

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
            setActive(idx);
        });
    });

    let ticking = false;
    function onScrollOrResize() {
        if (!ticking && !isClickTriggered) {
            ticking = true;
            requestAnimationFrame(() => {
                setActive(currentIndexByScroll());
                ticking = false;
            });
        }
    }

    const $window = $(window);
    $window.on('scroll resize', onScrollOrResize);
    setActive(currentIndexByScroll());
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