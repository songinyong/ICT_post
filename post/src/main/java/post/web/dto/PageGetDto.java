package post.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageGetDto {

	private int sell_state;
	private String wallet_address;
	
	
	public PageGetDto(int sell_state, String wallet_address) {
		this.sell_state = sell_state;
		this.wallet_address = wallet_address;
	}
	
	
}
