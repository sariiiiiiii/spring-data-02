package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCondition;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class JpaItemRepository implements ItemRepository {

    private final EntityManager em;

    public JpaItemRepository(EntityManager em) {
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
         * JPA 는 조회시에 미리 스냅샷을 떠놓고 트랜잭션이 끝나는 시점에
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
        return null;
    }

}
