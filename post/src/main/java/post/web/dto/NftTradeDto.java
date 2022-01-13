package post.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NftTradeDto {

	private String token_id ;
	private String sender ;
	private String owner;
	private String to ;
	
	public NftTradeDto(String token_id, String sender, String owner, String to) {
		this.token_id = token_id;
		this.sender = sender ;
		this.owner = owner ;
		this.to = to;
	}
}
