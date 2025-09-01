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




	// 회원가입 개인정보입력 버튼 활성화
	const joinForm = document.querySelector('.join-wrap.join-info');
	if (joinForm) {
	  const completeBtn = joinForm.querySelector('.btn.line-blue');

	  // 전역에서도 호출 가능하게
	  window.checkJoinForm = function checkForm() {
	    const isSso = (joinForm.closest('form')?.dataset.sso === '1');

	    const email    = joinForm.querySelector('#email')?.value.trim();
	    const pw1      = joinForm.querySelector('#loginPw')?.value.trim();
	    const pw2      = joinForm.querySelector('#loginPwChk')?.value.trim();
	    const year     = joinForm.querySelector('[data-type="year"] .select-btn')?.dataset.value;
	    const month    = joinForm.querySelector('[data-type="month"] .select-btn')?.dataset.value;
	    const day      = joinForm.querySelector('[data-type="day"] .select-btn')?.dataset.value;
	    const gender   = joinForm.querySelector('input[name="gender"]:checked')?.value;
	    const nickname = joinForm.querySelector('#nickNm')?.value.trim();

	    const baseOk  = !!(year && month && day && gender && nickname);
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
