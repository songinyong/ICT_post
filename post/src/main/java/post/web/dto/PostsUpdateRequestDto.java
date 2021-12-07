package post.web.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class PostsUpdateRequestDto {

	private int price;
	
	@Builder
	public PostsUpdateRequestDto(int price) {
		this.price = price ;
	}
}
