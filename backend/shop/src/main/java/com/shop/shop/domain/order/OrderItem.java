package com.shop.shop.domain.order;

import com.shop.shop.domain.cart.Cart;
import com.shop.shop.domain.item.Item;
import com.shop.shop.domain.item.ItemImage;
import com.shop.shop.domain.item.ItemOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Order_Item")
@Builder
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    private int orderPrice;
    private int discountRate;
    private int discountPrice;
    private int qty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    private ItemOption itemOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_image_id")
    private ItemImage itemImage;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // 주문상품 생성
    public static OrderItem createdOrderItem(int qty, Item item, Order order, ItemOption itemOption, ItemImage itemImage) {
        OrderItem orderItem = new OrderItem();
        int orderPrice = item.getPrice() * qty;

        orderItem.changeOrderPrice(orderPrice);
        orderItem.changeDiscountRate(item.getDiscountRate());
        orderItem.changeDiscountPrice(orderPrice * (1 - (item.getDiscountRate() / 100)));
        orderItem.changeQty(qty);
        orderItem.changeItem(item);
        orderItem.changeItemOption(itemOption);
        if (itemImage != null) {
            orderItem.changeItemImage(itemImage);
        }else {
            orderItem.changeItemImage(null);
        }
        orderItem.changeOrder(order);

        itemOption.removeStock(qty);
        return orderItem;
    }

    // 카트에서 상품을 가져오기
    public OrderItem getItemFromCart(Cart cart, Order order) {
        OrderItem orderItem = new OrderItem();
        int orderPrice = cart.getItem().getPrice() * cart.getQty();

        orderItem.changeOrderPrice(orderPrice);
        orderItem.changeDiscountRate(cart.getItem().getDiscountRate());
        orderItem.changeDiscountPrice(orderPrice * (1 - (cart.getItem().getDiscountRate() / 100)));
        orderItem.changeQty(cart.getQty());
        orderItem.changeItem(cart.getItem());
        orderItem.changeItemOption(cart.getItemOption());
        if (cart.getItemImage() != null) {
            orderItem.changeItemImage(cart.getItemImage());
        } else {
            orderItem.changeItemImage(null);
        }

        orderItem.changeOrder(order);

        cart.getItemOption().removeStock(cart.getQty());
        return orderItem;
    }

//    public OrderItem(Cart cart, Order order) {
//        this.orderPrice = cart.getItem().getPrice() * cart.getQty();
//        this.qty = cart.getQty();
//        this.item = cart.getItem();
//        this.itemOption = cart.getItemOption();
//        this.order = order;
//    }

//    // 카트에서 상품 정보를 가져와 생성
//    public static OrderItem cartToOrderItem(int orderPrice, int qty, Item item, Order order, ItemOption itemOption) {
//        OrderItem orderItem = new OrderItem();
//
//        orderItem.changeOrderPrice(orderPrice);
//        orderItem.changeQty(qty);
//        orderItem.changeItem(item);
//        orderItem.changeOrder(order);
//
//        itemOption.removeStock(qty);
//        return orderItem;
//    }

    // 주문 취소 - 상품에 주문 취소된 수량만큼 재고 추가
    public void cancel() {
        getItemOption().addStock(qty);
    }

    // 해당 상품의 총 결제 금액 - 상품 가격 * 주문 수량
    public int getTotalPrice() {
        return getOrderPrice() * getQty();
    }

    // orderPrice 값 수정
    public void changeOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    // qty 값 수정
    public void changeQty(int qty) {
        this.qty = qty;
    }

    // item 값 수정
    public void changeItem(Item item) {
        this.item = item;
    }

    // order 값 수정
    public void changeOrder(Order order) {
        this.order = order;
    }

    // itemOption 수정
    public void changeItemOption(ItemOption itemOption) {
        this.itemOption = itemOption;
    }

    // itemImage 수정
    public void changeItemImage(ItemImage itemImage) {
        this.itemImage = itemImage;
    }

    // discountRate 수정
    public void changeDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    // discountPrice 수정
    public void changeDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

}
