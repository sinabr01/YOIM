document.addEventListener('DOMContentLoaded', function () {
    // --- 메뉴 열기/닫기 ---
    const mypageBtn = document.querySelector('#header .ico.menu');
    const menuAll = document.querySelector('.menu-all');
    if (mypageBtn && menuAll) {
        mypageBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            menuAll.classList.toggle('active');
        });
        menuAll.addEventListener('click', (e) => e.stopPropagation());
        document.addEventListener('click', () => menuAll.classList.remove('active'));
    }


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


    // --- 커스텀 셀렉트 박스 ---
    // --- 커스텀 셀렉트 박스 ---
    document.querySelectorAll('.custom-select-box').forEach(selectBox => {
        const selectBtn = selectBox.querySelector('.select-btn');
        const selectOptions = selectBox.querySelector('.select-options');
        const selectedSpan = selectBtn.querySelector('span');

        // 초기 placeholder 색
        if (!selectedSpan.textContent.trim() || /^(년도|월|일)$/.test(selectedSpan.textContent.trim())) {
            selectedSpan.classList.remove('selected');
        }

        // 열고/닫기
        selectBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            selectBox.classList.toggle('active');
        });

        // 옵션 클릭 - 이벤트 위임
        selectOptions.addEventListener('click', (e) => {
            const option = e.target.closest('.select-option');
            if (!option) return;

            const selectedValue = option.dataset.value ?? option.textContent; // ✅ 값 확정

            // UI 반영
            selectedSpan.textContent = option.textContent;
            selectedSpan.classList.add('selected');
            selectBox.classList.remove('active');

            // 🌟 선택값 저장 (sub.js에서 읽음)
            selectBtn.dataset.value = selectedValue;

            // 🔧 이벤트 detail 변수명 수정
            selectBox.dispatchEvent(new CustomEvent('select:change', {
                detail: { value: selectedValue, text: option.textContent }
            }));
        });
    });

// 바깥 클릭 시 닫기
    document.addEventListener('click', () => {
        document.querySelectorAll('.custom-select-box.active')
            .forEach(b => b.classList.remove('active'));
    });



});
