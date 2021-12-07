package post.web.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import post.domain.posts.Posts;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

	private Long id ;
	private String title ;
	
	private int user_id;
	private long item_id;
	private int sell_state;
	private int price;
	private LocalDateTime createdDate ;
	private LocalDateTime ModifiedDate ;
	private String nft_description;
	private String nft_hash;
	private String token_id;
	private String creator;
	private String image_path ;
	private String owner;
	
	//후에 msa 구현후 user의 id를 받아와 저장private String user_id ; 
	//is_sell은 판매중 0 , 거래중 1, 판매완료 2 3개의 상태를 가지고 있으면 기본적으로 등록시에는 0이 디폴트이다.
	
    @Builder
    public PostsSaveRequestDto(String title, int user_id, long item_id, int sell_state, int price, String nft_description, String nft_hash, String token_id, String creator, String image_path, String owner  ) {
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
	
	public Posts toEntity() {
		return Posts.builder()
				.title(title)
				.price(price)
				.user_id(user_id)
				.item_id(item_id)
				.sell_state(sell_state)
				.nft_description(nft_description)
				.nft_hash(nft_hash)
				.token_id(token_id)
				.creator(creator)
				.image_path(image_path)
				.owner(owner)
				.build();
	}
	
}
