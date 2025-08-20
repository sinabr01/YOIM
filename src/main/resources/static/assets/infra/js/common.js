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
    const allCheck = document.querySelector('.all-check input[type="checkbox"]');
    const subChecks = document.querySelectorAll('.check-wrap .check input[type="checkbox"]');
    const requiredChecks = document.querySelectorAll('.check-wrap .check:nth-child(-n+3) input[type="checkbox"]');
    const authButton = document.querySelector('.btn.line-blue');

    allCheck.addEventListener('change', (e) => {
        const isChecked = e.target.checked;
        subChecks.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
        checkAllRequired();
    });

    subChecks.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const allChecked = Array.from(subChecks).every(c => c.checked);
            allCheck.checked = allChecked;
            // 필수 항목 체크 상태를 확인
            checkAllRequired();
        });
    });

    const checkAllRequired = () => {
        const allRequiredChecked = Array.from(requiredChecks).every(c => c.checked);
        if (allRequiredChecked) {
            authButton.classList.remove('inactive');
            authButton.disabled = false;
        } else {
            authButton.classList.add('inactive');
            authButton.disabled = true;
        }
    };

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
