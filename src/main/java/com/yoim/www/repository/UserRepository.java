package com.yoim.www.repository;

import com.yoim.www.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByProviderId(String providerId);

    @Modifying
    @Query("UPDATE User u SET u.userNm = :userNm, u.nickNm = :nickNm, u.intro = :intro, u.gender = :gender, u.birthDate = :birthDate, u.updusrId = :updusrId, u.updtDt = CURRENT_TIMESTAMP WHERE u.userId = :userId")
    int updateUser(@Param("userId") Long userId, @Param("userNm") String userNm, @Param("nickNm") String nickNm, @Param("intro") String intro, @Param("gender") String gender, @Param("birthDate") String birthDate, @Param("updusrId") String updusrId);
}
