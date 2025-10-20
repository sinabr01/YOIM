document.addEventListener('DOMContentLoaded', function () {
    initTopButton(); //탑버튼 기능
    scrollX(); //가로스크롤
    scrollY(); //세로스크롤
    
    
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





// --- 커스텀 셀렉트 박스 ---
    document.querySelectorAll('.custom-select-box').forEach(selectBox => {
        const selectBtn = selectBox.querySelector('.select-btn');
        const selectOptions = selectBox.querySelector('.select-options');
        const selectedSpan = selectBtn.querySelector('span');

        const initOption =
            selectOptions.querySelector('.select-option.is-selected') ||
            selectOptions.querySelector('.select-option'); // 없으면 첫 항목
        if (initOption) {
            const initValue = initOption.dataset.value ?? initOption.textContent.trim();
            selectedSpan.textContent = initOption.textContent.trim();
            selectedSpan.classList.add('selected');     // placeholder와 구분 용도
            selectBtn.dataset.value = initValue;
        }

        // 2) (선택) placeholder 처리 유지시
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

            const selectedValue = option.dataset.value ?? option.textContent.trim();

            // 기존 선택 해제/현재 선택 추가
            selectOptions.querySelectorAll('.select-option.is-selected')
                .forEach(opt => opt.classList.remove('is-selected'));
            option.classList.add('is-selected');

            // 버튼 UI 반영
            selectedSpan.textContent = option.textContent.trim();
            selectedSpan.classList.add('selected');
            selectBox.classList.remove('active');

            // 값 저장
            selectBtn.dataset.value = selectedValue;

            // 커스텀 이벤트
            selectBox.dispatchEvent(new CustomEvent('select:change', {
                detail: { value: selectedValue, text: option.textContent.trim() }
            }));
        });
    });


// 바깥 클릭 시 닫기
    document.addEventListener('click', (e) => {
        e.stopPropagation();
        document.querySelectorAll('.custom-select-box.active')
            .forEach(b => b.classList.remove('active'));
    });


});


function initTopButton() {
    const btn = document.querySelector('.top-btn');
    const showPoint = window.innerHeight * 0.7;

    window.addEventListener('scroll', () => {
        btn.classList.toggle('show', window.scrollY > showPoint);
    });

    btn.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
}


// --- 테이블 가로 스크롤 ---
function scrollX() {
    $('.scroll-x').each(function () {
        $(this).mCustomScrollbar({
            axis: "x",
            scrollInertia: 700
        });
    });
}

// --- 테이블/리스트 세로 스크롤 공통 ---
function scrollY() {
    $('.scroll-y').each(function () {
        $(this).mCustomScrollbar({
            axis: "x",
            scrollInertia: 700
        });
    });
}
