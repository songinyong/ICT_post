package post.web.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import post.domain.posts.Posts;

@Getter
@NoArgsConstructor
public class PostsResponseDto {

	private Long id ;
	private String title ;
	private String item_content ;
	private int user_id;
	private long item_id;
	private int is_sell;
	private int price;
	private LocalDateTime createdDate ;
	private LocalDateTime ModifiedDate ;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.item_content = entity.getItem_content();
        this.user_id = entity.getUser_id();
        this.item_id = entity.getItem_id();
        this.is_sell = entity.getIs_sell();
        this.price = entity.getPrice();
        this.createdDate = entity.getCreatedDate();
        this.ModifiedDate = entity.getModifiedDate();
    }
	
}
