package dobin.webproject.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value = {AuditingEntityListener.class}) // 1-1. Auditing을 적용하기 위해서 @EntityListeners 어노테이션 추가.
@MappedSuperclass // 1-2. 공통 매핑 정보가 필요할 때 사용하는 어노테이션으로 부모 클래스를 상속받는 자식 클래스에게 매핑정보만 제공.
@Getter
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;
}
/*
 * - BaseEntity랑 BaseTimeEntity랑 분리시키는 이유
 *   -> 보통 테이블에는 등록일, 수정일, 등록자, 수정자를 모두 다 넣어주지만
 *      어떤 테이블은 등록자, 수정자를 넣지 않는 테이블도 있다.
 *      그럴 경우를 대비하여 그런 엔티티는 앞에서 만든 BaseTimeEntity만 상속 받게 하기 위해
 *      BaseTimeEntity, BaseEntity 이렇게 분리 시킨다.
 */