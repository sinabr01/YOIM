// fileUpload.js
// 전역 네임스페이스: window.YoimFiles
// jQuery 의존 (window.jQuery 필요)

(function (global, $) {
  if (!$) throw new Error('YoimFiles: jQuery가 필요합니다.');

  async function createGroup(uploadCode, userId) {
    const params = new URLSearchParams();
    params.append('uploadCode', uploadCode);
    if (userId) params.append('userId', userId);

    const res = await fetch('/api/files/uploads', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params
    });
    if (!res.ok) throw new Error('createGroup 실패: ' + (await res.text()));

    const json = await res.json();
    if (!json || typeof json.fileUploadId === 'undefined') {
      throw new Error('fileUploadId가 응답에 없음');
    }
    return json.fileUploadId;
  }

  async function uploadFiles({ fileUploadId, files, isMain = 0, sortOrder = null }) {
    if (!fileUploadId) throw new Error('fileUploadId가 필요합니다.');
    if (!files || files.length === 0) throw new Error('업로드할 파일이 없습니다.');

    const fd = new FormData();
    for (const f of files) fd.append('files', f);
    if (isMain != null) fd.append('isMain', isMain);
    if (sortOrder != null) fd.append('sortOrder', sortOrder);

    const res = await fetch(`/api/files/uploads/${fileUploadId}/files`, {
      method: 'POST',
      body: fd
    });
    if (!res.ok) throw new Error('upload 실패: ' + (await res.text()));
    return res.json(); // [{fileId:..., ...}, ...]
  }

  async function finalize(fileUploadId) {
    if (!fileUploadId) throw new Error('fileUploadId가 필요합니다.');
    const res = await fetch(`/api/files/uploads/${fileUploadId}/finalize`, { method: 'PATCH' });
    if (!res.ok) throw new Error('finalize 실패: ' + (await res.text()));
    return res.json();
  }

  /**
   * 한 화면에 업로더 인스턴스 만들기
   * - uploadCode: 'PROFILE' | 'MEETING' | ...
   * - input: 파일 input 셀렉터
   * - button: 업로드 버튼 셀렉터
   * - userId: (선택)
   * - autoCreateOnChange: 파일 선택 시 그룹 선생성 (기본 true)
   * - isMain/sortOrder: 기본 업로드 옵션
   * - onCreated(id), onSuccess(resp), onError(err)
   */
  function createUploader({
    uploadCode,
    input,
    button,
    userId,
    autoCreateOnChange = true,
    isMain = 1,
    sortOrder = 1,
    onCreated,
    onSuccess,
    onError
  }) {
    const $input = $(input);
    const $button = $(button);
    let fileUploadId = null;

    async function ensureGroup() {
      if (!fileUploadId) {
        fileUploadId = await createGroup(uploadCode, userId);
        if (onCreated) onCreated(fileUploadId);
      }
      return fileUploadId;
    }

    if (autoCreateOnChange) {
      $input.on('change', async function () {
        if (this.files && this.files.length > 0 && !fileUploadId) {
          try {
            await ensureGroup();
          } catch (e) {
            if (onError) onError(e); else alert(e.message);
          }
        }
      });
    }

    $button.on('click', async function () {
      try {
        const files = $input[0].files;
        if (!files || files.length === 0) {
          alert('파일을 선택하세요.');
          return;
        }
        await ensureGroup();
        const resp = await uploadFiles({ fileUploadId, files, isMain, sortOrder });
        if (onSuccess) onSuccess(resp);
        else alert('업로드 완료');
      } catch (e) {
        if (onError) onError(e); else alert(e.message);
      }
    });

    return {
      // 필요하면 외부에서 직접 제어
      getId: () => fileUploadId,
      setId: (id) => { fileUploadId = id; },
      ensureGroup,
      uploadSelected: async () => {
        const files = $input[0].files;
        await ensureGroup();
        return uploadFiles({ fileUploadId, files, isMain, sortOrder });
      },
      finalize: () => finalize(fileUploadId)
    };
  }

  // 전역 공개 API
  global.YoimFiles = {
    createGroup,
    uploadFiles,
    finalize,
    createUploader
  };

})(window, window.jQuery);