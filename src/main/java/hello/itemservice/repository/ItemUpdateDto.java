package hello.itemservice.repository;

import lombok.Data;

@Data
public class ItemUpdateDto {

    /**
     * ItemSearchCondition, ItemUpdateDto 는 Service, Repository에서 사용을 한다
     * 그러면 이 객체들은 어디에 위치하는게 좋을까?
     *
     * Service 계층에 생성할 수도 있지만 최종적으로 Repository에서 사용을 하기 때문에
     * Repository에 위치하는게 옳다
     */

    private String itemName;
    private Integer price;
    private Integer quantity;

    public ItemUpdateDto() {
    }

    public ItemUpdateDto(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
