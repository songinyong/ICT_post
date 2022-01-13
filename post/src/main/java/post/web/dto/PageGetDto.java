package post.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageGetDto {

	private int sell_state;
	private String owner;
	
	
	public PageGetDto(int sell_state, String owner) {
		this.sell_state = sell_state;
		this.owner = owner;
	}
	
	
}
