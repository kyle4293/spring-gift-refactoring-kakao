package gift.wish;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByMemberId(Long memberId, Pageable pageable);

    @Query("SELECT w FROM Wish w WHERE w.memberId = :memberId AND w.product.id = :productId")
    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);

    @Modifying
    @Query("DELETE FROM Wish w WHERE w.memberId = :memberId AND w.product.id = :productId")
    void deleteByMemberIdAndProductId(Long memberId, Long productId);
}
