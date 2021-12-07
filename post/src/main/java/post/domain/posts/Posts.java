package post.domain.posts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import post.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor
@Entity
@Table(name="posts")
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable =false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String item_content;

    @Column(nullable = false)
    private int user_id;
    
    @Column(nullable = false)
    private long item_id;
    
    @Column(nullable = false)
    private int is_sell;
    
    @Column(nullable = false)
    private int price;
    

    @Builder
    public Posts(String title, String item_content, int user_id, long item_id, int price, int is_sell) {
        this.title = title;
        this.item_content = item_content;
        this.user_id = user_id ;
        this.item_id = item_id;
        this.is_sell = is_sell ;
        this.price = price ;
    }

    // price 업데이트
    public void update(int price) {
        this.price = price;
    }
}