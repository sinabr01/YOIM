package com.yoim.www.repository;

import com.yoim.www.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query(value = "SELECT * FROM tb_notice " +
           "WHERE (:keyword IS NULL OR :keyword = '' OR title LIKE CONCAT('%', :keyword, '%')) " +
           "ORDER BY CASE WHEN pinned_yn = 'Y' THEN 0 ELSE 1 END, regist_dt DESC " +
           "LIMIT :pageStart, :pageSize", nativeQuery = true)
    List<Notice> findNoticesWithPagination(@Param("keyword") String keyword,
                                           @Param("pageStart") int pageStart,
                                           @Param("pageSize") int pageSize);

    @Query(value = "SELECT COUNT(*) FROM tb_notice " +
           "WHERE (:keyword IS NULL OR :keyword = '' OR title LIKE CONCAT('%', :keyword, '%'))", nativeQuery = true)
    int countNotices(@Param("keyword") String keyword);

    @Modifying
    @Query(value = "INSERT INTO tb_notice (title, content, pinned_yn, use_yn, regist_id, regist_dt) " +
           "VALUES (:title, :content, :pinnedYn, :useYn, :registId, NOW()) " +
           "ON DUPLICATE KEY UPDATE " +
           "title = VALUES(title), content = VALUES(content), pinned_yn = VALUES(pinned_yn), use_yn = VALUES(use_yn), " +
           "updusr_id = VALUES(regist_id), updt_dt = NOW()", nativeQuery = true)
    void upsertNotice(@Param("title") String title, @Param("content") String content,
                      @Param("pinnedYn") String pinnedYn, @Param("useYn") String useYn,
                      @Param("registId") String registId);
}
