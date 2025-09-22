// /resource/js/fileDownload.js 같은 위치에 두고 공통으로 로드
(function (global) {
  const BASE = '/api/files'; // 컨트롤러가 @RequestMapping("/api/files") 라면 이대로
  const CD_KEYS = ['Content-Disposition', 'content-disposition'];

  function _extractFilenameFromCD(cd) {
    if (!cd) return null;

    // RFC5987 우선: filename*=UTF-8''encoded
    const star = /filename\*\s*=\s*([^']*)''([^;]+)/i.exec(cd);
    if (star && star[2]) {
      try { return decodeURIComponent(star[2]); } catch (_) {}
      return star[2];
    }

    // 일반 filename=".."
    const quoted = /filename\s*=\s*"([^"]+)"/i.exec(cd);
    if (quoted && quoted[1]) return quoted[1];

    // filename=plain
    const plain = /filename\s*=\s*([^;]+)/i.exec(cd);
    if (plain && plain[1]) return plain[1].trim();

    return null;
    }

  async function downloadById(fileId, opts = {}) {
    if (!fileId) throw new Error('fileId가 비었습니다.');
    const headers = Object.assign({}, opts.headers || {});

    const res = await fetch(`${BASE}/download/${encodeURIComponent(fileId)}`, {
      method: 'GET',
      headers,
      // 크로스도메인이거나 인증이 있으면 credentials 옵션도 고려
      // credentials: 'include'
    });

    if (!res.ok) {
      const txt = await res.text().catch(() => '');
      throw new Error(`다운로드 실패 (${res.status}) ${txt}`);
    }

    const blob = await res.blob();
    let cd = null;
    for (const k of CD_KEYS) {
      if (res.headers.has(k)) { cd = res.headers.get(k); break; }
    }
    const filename = _extractFilenameFromCD(cd) || `file_${fileId}`;

    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    setTimeout(() => {
      URL.revokeObjectURL(url);
      a.remove();
    }, 1000);
  }

  // data-file-id 버튼을 자동 연결
  function wire(container = document) {
    container.addEventListener('click', (e) => {
      const btn = e.target.closest('[data-file-id][data-action="download"]');
      if (!btn) return;
      const id = btn.getAttribute('data-file-id');
      downloadById(id).catch(err => {
        console.error(err);
        alert(err.message || '다운로드 실패');
      });
    });
  }

  // 전역 공개
  global.YoimFile = { downloadById, wire };
})(window);
