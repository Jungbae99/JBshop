package shop.jbshop.domain.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.jbshop.domain.BaseAuditingListener;
import shop.jbshop.domain.OrderItem;
import shop.jbshop.domain.cart.Cart;
import shop.jbshop.domain.cart.CartItem;
import shop.jbshop.exception.NotEnoughStockException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseAuditingListener{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private String itemText;

    private int itemPrice;

    private int itemCount;

    private int itemStock;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category itemCategory;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImg> itemImgs = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<CartItem> cartItemList = new ArrayList<>();


    // :TODO test
    public String getImageAsBase64() {
        if (itemImgs.isEmpty()) {
            return null;
        }
        ItemImg itemImg = itemImgs.get(0);
        String imagePath = itemImg.getUrl();

        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return base64Image;
        } catch (IOException e) {
            throw new RuntimeException("이미지를 불러오는 동안 오류가 발생했습니다.", e);
        }
    }

    @Builder
    public Item(String name, String text, int price, int stock, Category category) {
        this.itemName = name;
        this.itemText = text;
        this.itemPrice = price;
        this.itemStock = stock;
        this.itemCategory = category;
    }

    public void updateName(String itemName) {
        this.itemName = itemName;
    }

    public void updateText(String itemText) {
        this.itemText = itemText;
    }

    public void updatePrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void updateStock(int itemStock) {
        this.itemStock = itemStock;
    }

    // 재고 감소
    public void removeStock(int count) {
        int restStock = this.itemStock - count;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.itemStock = restStock;
    }

    // 판매수량 증가
    public void updateCount(int count) {
        this.itemCount += count;
    }

    public void deleteItem() {
        for (ItemImg itemImg : itemImgs) {
            itemImg.deleteItemImg();
        }
        super.delete();
    }


}
