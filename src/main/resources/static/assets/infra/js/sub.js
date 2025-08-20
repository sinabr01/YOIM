document.addEventListener('DOMContentLoaded', () => {


    const birth = document.querySelector('.birth');
    if (!birth) return;

    const yearSB  = birth.querySelector('[data-type="year"]');
    const monthSB = birth.querySelector('[data-type="month"]');
    const daySB   = birth.querySelector('[data-type="day"]');

    // 동적 옵션 생성
    const yearUl  = yearSB.querySelector('.select-options');
    const monthUl = monthSB.querySelector('.select-options');
    const dayUl   = daySB.querySelector('.select-options');

    const currentYear = new Date().getFullYear();

    for (let y = currentYear; y >= 1950; y--) {
        const li = document.createElement('li');
        li.className = 'select-option';
        li.textContent = `${y}년`;
        li.dataset.value = y;
        yearUl.appendChild(li);
    }

    for (let m = 1; m <= 12; m++) {
        const li = document.createElement('li');
        li.className = 'select-option';
        li.textContent = `${m}월`;
        li.dataset.value = m;
        monthUl.appendChild(li);
    }

    function rebuildDays() {
        const y = Number(yearSB.querySelector('.select-btn').dataset.value || currentYear);
        const m = Number(monthSB.querySelector('.select-btn').dataset.value || 1);

        dayUl.innerHTML = '';
        const lastDay = new Date(y, m, 0).getDate(); // 윤년 포함 자동 계산
        for (let d = 1; d <= lastDay; d++) {
            const li = document.createElement('li');
            li.className = 'select-option';
            li.textContent = `${d}일`;
            li.dataset.value = d;
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




    //회원가입 개인정보입력 버튼 활성화
    const joinForm = document.querySelector('.join-wrap.join-info');
    if (!joinForm) return;

    const completeBtn = joinForm.querySelector('.btn.line-blue');

    function checkForm() {
        const email    = joinForm.querySelector('input[placeholder="yomi@example.com"]')?.value.trim();
        const pw1      = joinForm.querySelector('input[placeholder="비밀번호를 입력하세요."]')?.value.trim();
        const pw2      = joinForm.querySelector('input[placeholder="위 비밀번호와 동일하게 입력"]')?.value.trim();
        const year     = joinForm.querySelector('[data-type="year"] .select-btn')?.dataset.value;
        const month    = joinForm.querySelector('[data-type="month"] .select-btn')?.dataset.value;
        const day      = joinForm.querySelector('[data-type="day"] .select-btn')?.dataset.value;
        const gender   = joinForm.querySelector('input[name="agree"]:checked')?.value;
        const nickname = joinForm.querySelector('input[placeholder="닉네임을 입력하세요"]')?.value.trim();

        const allFilled = email && pw1 && pw2 && year && month && day && gender && nickname;

        if (allFilled) {
            completeBtn.classList.remove('inactive');
            completeBtn.disabled = false;
        } else {
            completeBtn.classList.add('inactive');
            completeBtn.disabled = true;
        }
    }

    // input, password, text, radio 모두 감지
    joinForm.querySelectorAll('input').forEach(input => {
        input.addEventListener('input', checkForm);
        input.addEventListener('change', checkForm);
    });

    // 커스텀 셀렉트박스도 감지
    joinForm.querySelectorAll('.custom-select-box').forEach(sb => {
        sb.addEventListener('select:change', checkForm);
    });

    // 초기 상태
    checkForm();
});
