document.addEventListener('DOMContentLoaded', function () {
    initTopButton(); //탑버튼 기능
    scrollx(); //가로스크롤
    scrolly(); //세로스크롤


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
                detail: {value: selectedValue, text: option.textContent.trim()}
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
    if (!btn) return;

    const showPoint = window.innerHeight * 0.95;

    window.addEventListener('scroll', () => {
        btn.classList.toggle('show', window.scrollY > showPoint);
    });

    btn.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
}



// --- 테이블 가로 스크롤 ---
function scrollx() {
    $('.scroll-x').each(function () {
        $(this).mCustomScrollbar({
            axis: "x",
            scrollInertia: 700
        });
    });
}

// --- 테이블/리스트 세로 스크롤 공통 ---
function scrolly() {
    $('.scroll-y').each(function () {
        $(this).mCustomScrollbar({
            axis: "x",
            scrollInertia: 700
        });
    });
}

// --- 데이트피커 ---
$(function () {
    flatpickr(".datepicker", {
        dateFormat: "Y-m-d",
        locale: "ko"
    });


/*    $(".datepicker").datepicker({
        dateFormat: 'yy-mm-dd',   // yyyy-mm-dd 형식
        showOtherMonths: true,    // 앞/뒤 달 날짜도 표시
        showMonthAfterYear: true, // 년도 먼저
        changeYear: true,         // 년도 선택 드롭다운
        changeMonth: true,        // 월 선택 드롭다운
        yearSuffix: "년",
        monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
        dayNamesMin: ['일','월','화','수','목','금','토']
    });*/

    $(".startDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function (selectedDate) {
            $("#endDate").datepicker("option", "minDate", selectedDate);
        }
    });
    $(".endDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function (selectedDate) {
            $("#startDate").datepicker("option", "maxDate", selectedDate);
        }
    });

});
