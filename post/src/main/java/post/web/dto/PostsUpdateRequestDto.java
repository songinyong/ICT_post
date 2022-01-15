/*
 * 게시물 정보 수정 dto
 * 현재 블록체인 연동후 업데이트 필요
 * */

package post.web.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {

	private float price;
	
	@Builder
	public PostsUpdateRequestDto(float price) {
		this.price = price ;
	}
}
