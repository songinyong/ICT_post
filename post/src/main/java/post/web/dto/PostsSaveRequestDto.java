package post.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import post.domain.posts.Posts;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

	private String title;
	private String item_content;
	private int price ;
	private int user_id ;
	private int item_id ;
	private int is_sell ;
	
	//후에 msa 구현후 user의 id를 받아와 저장private String user_id ; 
	//is_sell은 판매중 0 , 거래중 1, 판매완료 2 3개의 상태를 가지고 있으면 기본적으로 등록시에는 0이 디폴트이다.
	
	@Builder
	public PostsSaveRequestDto(String title, String item_content, int price, int user_id, int item_id, int is_sell) {
		this.title = title ;
		this.item_content = item_content ;
		this.price = price ;
		this.user_id = user_id ;
		this.item_id = item_id;
		this.is_sell = is_sell ;
	}
	
	public Posts toEntity() {
		return Posts.builder()
				.title(title)
				.item_content(item_content)
				.price(price)
				.user_id(user_id)
				.item_id(item_id)
				.is_sell(0)
				.build();
	}
	
}
