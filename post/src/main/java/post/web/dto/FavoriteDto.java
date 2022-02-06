/*즐겨찾기 처리 dto*/

package post.web.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import post.domain.posts.Favorite;

@Getter
@NoArgsConstructor
public class FavoriteDto {
	

    private String tokenId;
    private String wallet;
	private LocalDateTime createdDate ;
  
    
    public FavoriteDto(String tokenId, String wallet, LocalDateTime createdDate ) {
    	this.tokenId = tokenId ;
    	this.wallet = wallet ;
    	this.createdDate = createdDate ;
    }
    
    public Favorite toEntity() {
    	return Favorite.builder()
    			.tokenId(tokenId)
    			.wallet(wallet)
    			.createdDate(createdDate)
    			.delItem(0)
    			.build() ;
    }
}
