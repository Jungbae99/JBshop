package shop.jbshop.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.jbshop.domain.BaseAuditingListener;
import shop.jbshop.domain.Order;
import shop.jbshop.domain.cart.Cart;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String username;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Author author = Author.USER;

    private String note;

    @Embedded
    private Oauth oauth;

    @Builder
    public Member(String email, String password, String username, String phoneNumber, Grade grade, Address address, Author author, String note, String oauthType) {
        this.password = password;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.grade = grade;
        this.address = address;
        this.author = author;
        this.note = note;
        if (oauthType != null) {
            Oauth oauth = Oauth.createOauth(OauthType.valueOf(oauthType));
            this.oauth = oauth;
        }

    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateAddress(String city, String street, String zipcode) {
        Address address = new Address(city, street, zipcode);
        this.address = address;
    }

    public void deleteMember() {
        super.delete();
    }


}
