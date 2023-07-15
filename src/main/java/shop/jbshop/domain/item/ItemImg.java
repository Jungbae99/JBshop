package shop.jbshop.domain.item;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.jbshop.domain.BaseAuditingListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImg extends BaseAuditingListener{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    //서버이름
    @Column(nullable = false)
    private String savedName;

    //원본이름
    private String originName;

    @Size(max = 512)
    @Column(nullable = false)
    private String url;

    @Builder
    ItemImg(String url, String savedName, String originName) {
        this.url = url;
        this.savedName = savedName;
        this.originName = originName;
    }

    public void addItemImg(Item item) {
        this.item = item;
        item.getItemImgs().add(this);
    }

    public void deleteItemImg() {
        super.delete();
    }

}
