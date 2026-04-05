package com.shop.shop.repository;

import com.shop.shop.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@DataJpaTest
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 권한과 함께 회원 조회
    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("select m from Member m where m.email = :email AND m.delFlag = false ORDER BY m.id DESC")
    Member getWithRoles(@Param("email") String email);

    // 권환과 함께 회원 조회(페이징)
    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("select m from Member m ORDER BY m.id DESC")
    Page<Member> findAllMemberPage(Pageable pageable);

    // 이메일로 조회
//    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("select m from Member m where m.email = :email AND m.delFlag = false ORDER BY m.id DESC")
    Member findByEmail(@Param("email") String email);

    // 이름으로 회원 조회
    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("select m from Member m where m.memberName = :memberName AND m.delFlag = false ORDER BY m.id DESC")
    List<Member> findAllByMemberName(@Param("memberName") String memberName);

    // 회원 여부 검사
    boolean existsByEmail(String email);

}
