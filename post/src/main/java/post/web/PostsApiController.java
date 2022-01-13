package post.web;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import post.service.PostsService;
import post.web.dto.NftTradeDto;
import post.web.dto.PageGetDto;
import post.web.dto.PagingDto;
import post.web.dto.PostsResponseDto;
import post.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins="*")
public class PostsApiController {

	private final PostsService postsService;
	
	//전체 게시물 정보 불러옴 - 초기 버전 현재 제거
	/*
	@PostMapping("/posts")
	public Long save(@RequestBody PostsSaveRequestDto requestDto) {
		return postsService.save(requestDto);
	}*/
	
	//게시물 아이디에 해당하는 정보 불러옴
	@GetMapping("/posts/{id}")
	public PostsResponseDto findById (@PathVariable Long id) {
		return postsService.findById(id);
	}
	
	/*유저 id 기준으로 게시물들의 정보를 불러옴
	 * 설계 변경으로 현재 사용 중단됨
	@GetMapping("/posts/user/{user_id}")
	public List<PostsResponseDto> findByUserId (@PathVariable int user_id) {
		return postsService.findUsersPosts(user_id);
	}
	*/
	
	
	//게시물 price 변경
    @PutMapping(path="/posts/price/{token_id}", consumes="application/json")
    public boolean update(@PathVariable String token_id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.updatePrice(token_id, requestDto);
    }
    
    //게시물 sell_state 변경
    @PutMapping(path="/posts/sell_state/{token_id}", consumes="application/json")
    public boolean sell_Update(@PathVariable String token_id) {
        return postsService.updateSell_state(token_id);
    }
    
    /*게시물 삭제
    @DeleteMapping(path="/posts/delete/{id}")
    public Long delete(@PathVariable Long id) {
        return postsService.deletePosts(id);
    }*/
    
    //특정 지갑 주소 and sell_state 기준 페이지로 출력
    @PostMapping("/posts/MyPage")
    public Page<PagingDto> pagewalletandsellState(@PageableDefault(size=10, sort="createdDate") Pageable pageRequest, @RequestBody PageGetDto pgdto) {
    	return postsService.findByWalletAndSellState(pageRequest, pgdto);
    }
    
    //특정 지갑 주소를 제외한 전체 AND sell_state 기준 페이지로 출력
    @PostMapping("/posts/Market/{owner}")
    public Page<PagingDto> pageNotwalletandsellState(@PathVariable String owner, @PageableDefault(size=10, sort="createdDate") Pageable pageRequest) {
    	return postsService.findByNotWalletAndSellState(owner, pageRequest);
    }
    
    //전체 게시물들의 정보를 페이지 형태로 불러옴
    @GetMapping("/posts/page")
    public Page<PagingDto> pageId(@PageableDefault(size=10, sort="createdDate") Pageable pageRequest) {
    	return postsService.paging(pageRequest);
    }
	
    //지갑 주소 기반으로 게시물들의 정보를 페이지 형태로 불러옴
    @GetMapping("/posts/page/{wallet_address}")
    public Page<PagingDto> pagebywallet(@PageableDefault(size=10, sort="createdDate") Pageable pageRequest, @PathVariable String wallet_address) {
    	System.out.println(wallet_address);
    	return postsService.nftinfotoclient(pageRequest, wallet_address);
    }
    
    //컨트랙트에서 지갑 주소 기준으로 nft 아이템 정보들을 불러옴
	@PostMapping("/posts/wallet")
	public String test() {
		return postsService.nftinfoFromblock("0xbc7cc9517400cff0ec953efb585e424301a395b0");
	}
	
	@GetMapping("/posts/blck/item")
	public ResponseEntity<JSONObject> testPost() {
		return postsService.recvNftInfofromBlckdb();
	}
	//nft 거래 api
	@PostMapping("/posts/trade")
	public ResponseEntity<JSONObject> nftTrade(@RequestBody NftTradeDto nfttradedto) {
		return postsService.nfttrade(nfttradedto);
	}
}
