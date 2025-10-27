package com.yoim.www.util;

public class PagingAction {
	private int currentPage; // 현재페이지
	private int totalCount;	 // 전체 게시물 수
	private int totalPage;	 // 전체 페이지 수
	private int blockCount;	 // 한 페이지의  게시물의 수
	private int blockPage;	 // 한 화면에 보여줄 페이지 수
	private int startCount;	 // 한 페이지에서 보여줄 게시글의 시작 번호
	private int endCount;	 // 한 페이지에서 보여줄 게시글의 끝 번호
	private int startPage;	 // 시작 페이지
	private int endPage;	 // 마지막 페이지
	private StringBuffer pagingHtml;
	// 페이징 생성자
	public PagingAction(int currentPage, int totalCount, int blockCount, int blockPage, String formName, String callback) {
		this.blockCount = blockCount;
		this.blockPage = blockPage;
		this.currentPage = currentPage;
		this.totalCount = totalCount;

		// 전체 페이지 수
		totalPage = (int) Math.ceil((double) totalCount / blockCount);
		if (totalPage == 0) {
			totalPage = 1;
		}

		// 현재 페이지가 전체 페이지 수보다 크면 전체 페이지 수로 설정
		if (currentPage > totalPage) {
			currentPage = totalPage;
		}

		// 현재 페이지의 처음과 마지막 글의 번호 가져오기.
		startCount = (currentPage - 1) * blockCount + 1;
		endCount = (startCount - 1) + blockCount - 1;

		// 시작 페이지와 마지막 페이지 값 구하기.
		startPage = (int) ((currentPage - 1) / blockPage) * blockPage + 1;
		endPage = startPage + blockPage - 1;

		// 마지막 페이지가 전체 페이지 수보다 크면 전체 페이지 수로 설정
		if (endPage > totalPage) {
			endPage = totalPage;
		}

		pagingHtml = new StringBuffer();

		// wrapper 시작
		pagingHtml.append("<div class=\"pagination\">");

		// === 왼쪽 화살표 영역 (prev-all, prev) ===
		pagingHtml.append("<div class=\"arrow-area\">");

		// prev-all : 첫 페이지(1)로
		if (currentPage > 1) {
			if (callback.equals("")) {
				pagingHtml.append("<button type=\"button\" class=\"prev-all\" onclick=\"document.")
						.append(formName)
						.append(".currentPage.value=1;document.")
						.append(formName)
						.append(".submit();return false;\"><i class=\"ico\"></i></button>");
			} else {
				pagingHtml.append("<button type=\"button\" class=\"prev-all\" onclick=\"document.")
						.append(formName)
						.append(".currentPage.value=1;")
						.append(callback)
						.append(";return false;\"><i class=\"ico\"></i></button>");
			}
		} else {
			// 비활성 상태
			pagingHtml.append("<button type=\"button\" class=\"prev-all\" disabled><i class=\"ico\"></i></button>");
		}

		// prev : 이전 블록 (startPage - 1)
		if (currentPage > blockPage) {
			int prevPage = startPage - 1;
			if (prevPage < 1) prevPage = 1;

			if (callback.equals("")) {
				pagingHtml.append("<button type=\"button\" class=\"prev\" onclick=\"document.")
						.append(formName)
						.append(".currentPage.value=")
						.append(prevPage)
						.append(";document.")
						.append(formName)
						.append(".submit();return false;\"><i class=\"ico\"></i></button>");
			} else {
				pagingHtml.append("<button type=\"button\" class=\"prev\" onclick=\"document.")
						.append(formName)
						.append(".currentPage.value=")
						.append(prevPage)
						.append(";")
						.append(callback)
						.append(";return false;\"><i class=\"ico\"></i></button>");
			}
		} else {
			pagingHtml.append("<button type=\"button\" class=\"prev\" disabled><i class=\"ico\"></i></button>");
		}

		pagingHtml.append("</div>"); // .arrow-area 끝


		// === 가운데 페이지 번호 영역 ===
		pagingHtml.append("<ul class=\"pagination-ul\">");

		for (int i = startPage; i <= endPage; i++) {
			if (i > totalPage) {
				break;
			}

			if (i == currentPage) {
				// 현재 페이지: active + disabled
				pagingHtml.append("<li class=\"active\"><button type=\"button\" class=\"pages\" disabled>")
						.append(i)
						.append("</button></li>");
			} else {
				pagingHtml.append("<li><button type=\"button\" class=\"pages\" onclick=\"document.")
						.append(formName)
						.append(".currentPage.value=")
						.append(i)
						.append(";");
				if (callback.equals("")) {
					pagingHtml.append("document.")
							.append(formName)
							.append(".submit();return false;\">");
				} else {
					pagingHtml.append(callback)
							.append(";return false;\">");
				}
				pagingHtml.append(i)
						.append("</button></li>");
			}
		}

		pagingHtml.append("</ul>"); // .pagination-ul 끝


		// === 오른쪽 화살표 영역 (next, next-all) ===
		pagingHtml.append("<div class=\"arrow-area\">");

		// next : 다음 블록 (endPage + 1)
		if (totalPage - startPage >= blockPage) {
			int nextPage = endPage + 1;
			if (nextPage > totalPage) nextPage = totalPage;

			if (callback.equals("")) {
				pagingHtml.append("<button type=\"button\" class=\"next\" onclick=\"document.")
						.append(formName)
						.append(".currentPage.value=")
						.append(nextPage)
						.append(";document.")
						.append(formName)
						.append(".submit();return false;\"><i class=\"ico\"></i></button>");
			} else {
				pagingHtml.append("<button type=\"button\" class=\"next\" onclick=\"document.")
						.append(formName)
						.append(".currentPage.value=")
						.append(nextPage)
						.append(";")
						.append(callback)
						.append(";return false;\"><i class=\"ico\"></i></button>");
			}
		} else {
			pagingHtml.append("<button type=\"button\" class=\"next\" disabled><i class=\"ico\"></i></button>");
		}

		// next-all : 마지막 페이지(totalPage)
		if (currentPage < totalPage) {
			if (callback.equals("")) {
				pagingHtml.append("<button type=\"button\" class=\"next-all\" onclick=\"document.")
						.append(formName)
						.append(".currentPage.value=")
						.append(totalPage)
						.append(";document.")
						.append(formName)
						.append(".submit();return false;\"><i class=\"ico\"></i></button>");
			} else {
				pagingHtml.append("<button type=\"button\" class=\"next-all\" onclick=\"document.")
						.append(formName)
						.append(".currentPage.value=")
						.append(totalPage)
						.append(";")
						.append(callback)
						.append(";return false;\"><i class=\"ico\"></i></button>");
			}
		} else {
			pagingHtml.append("<button type=\"button\" class=\"next-all\" disabled><i class=\"ico\"></i></button>");
		}

		pagingHtml.append("</div>"); // .arrow-area 끝

		// wrapper 끝
		pagingHtml.append("</div>");
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getBlockCount() {
		return blockCount;
	}
	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}
	public int getBlockPage() {
		return blockPage;
	}
	public void setBlockPage(int blockPage) {
		this.blockPage = blockPage;
	}
	public int getStartCount() {
		return startCount;
	}
	public void setStartCount(int startCount) {
		this.startCount = startCount;
	}
	public int getEndCount() {
		return endCount;
	}
	public void setEndCount(int endCount) {
		this.endCount = endCount;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public StringBuffer getPagingHtml() {
		return pagingHtml;
	}
	public void setPagingHtml(StringBuffer pagingHtml) {
		this.pagingHtml = pagingHtml;
	}
}
