package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCondition;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class JpaItemRepositoryV1 implements ItemRepository {

    /**
     * @Repository 어노테이션이 붙은 클래스는 컴포넌트 대상이 되는것과 동시에 하나의 기능이 더 붙는다
     * 1. 컴포넌트 스캔의 대상이 된다
     * 2. 예외 변환 AOP의 적용 대상이 된다
     *    - 스프링과 JPA를 함께 사용하는 경우 스프링은 JPA 예외 변환기 (PersistenceExceptionTranslator)를 등록한다
     *    - 예외 변환 AOP 프록시는 JPA 관련 예외가 발생하면 JPA 예외 변환기를 통해 발한 예외를 스프링 데이터 접근 예외로 변환한다
     */

    private final EntityManager em;

    public JpaItemRepositoryV1(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    @Transactional
    public void update(Long itemId, ItemUpdateDto updateParam) {

        /**
         * 변경감지 (dirty checking)
         * JPA 는 조회시에 미리 스냅샷을 떠놓고 트랜잭션이 커밋이 되는 시점에
         * 같은 식별자로 된 데이터에 변경된 점이 있으면 update query 가 나감.
         */

        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll(ItemSearchCondition cond) {
        String jpql = "select i from Item i";
        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where";
        }
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            jpql += " i.itemName like concat('%',:itemName,'%')";
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
        }
        log.info("jpql={}", jpql);
        TypedQuery<Item> query = em.createQuery(jpql, Item.class);
        if (StringUtils.hasText(itemName)) {
            query.setParameter("itemName", itemName);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        return query.getResultList();
    }

}
