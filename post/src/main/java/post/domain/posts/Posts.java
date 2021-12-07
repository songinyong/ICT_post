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

    @Column(nullable = false)
    private int user_id;
    
    @Column(nullable = false)
    private long item_id;
    
    @Column(nullable = false)
    private int sell_state;
    
    @Column(nullable = false)
    private int price;
    
	@Column()
	private String nft_description;
	@Column()
	private String nft_hash;
	
	//아이템 아이디를 16진수롤 변환해서 저장
	@Column()
	private String token_id ;

	@Column()
	private String creator;
	@Column()
	private String image_path;
	@Column()
	private String owner;
    

    @Builder
    public Posts(String title, int user_id, long item_id, int sell_state, int price, String nft_description, String nft_hash, String token_id, String creator, String image_path, String owner  ) {
        this.title = title;    
        this.user_id = user_id ;
        this.item_id = item_id;
        this.sell_state = sell_state ;
        this.price = price ;
        this.nft_description = nft_description;
        this.nft_hash = nft_hash;
        this.token_id = token_id;
        this.creator = creator;
        this.image_path = image_path;
        this.owner = owner;
        
    }

    // price 업데이트
    public void update(int price) {
        this.price = price;
    }
}