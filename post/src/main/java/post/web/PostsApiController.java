package post.web;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import post.service.PostsService;
import post.web.dto.PagingDto;
import post.web.dto.PostsResponseDto;
import post.web.dto.PostsSaveRequestDto;
import post.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins="*")
public class PostsApiController {

	private final PostsService postsService;
	
	@PostMapping("/posts")
	public Long save(@RequestBody PostsSaveRequestDto requestDto) {
		return postsService.save(requestDto);
	}
	
	@GetMapping("/posts/{id}")
	public PostsResponseDto findById (@PathVariable Long id) {
		return postsService.findById(id);
	}
	
	@GetMapping("/posts/user/{user_id}")
	public List<PostsResponseDto> findByUserId (@PathVariable int user_id) {
		return postsService.findUsersPosts(user_id);
	}
	
    @PutMapping(path="/posts/{id}", consumes="application/json")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }
    
    @DeleteMapping(path="/posts/delete/{id}")
    public Long delete(@PathVariable Long id) {
        return postsService.deletePosts(id);
    }
    
    @GetMapping("/posts/page")
    public Page<PagingDto> pageId(@PageableDefault(size=10, sort="createdDate") Pageable pageRequest) {
    	return postsService.paging(pageRequest);
    }
	
    @GetMapping("/posts/page/{wallet_address}")
    public Page<PagingDto> pagebywallet(@PageableDefault(size=10, sort="createdDate") Pageable pageRequest, @PathVariable String wallet_address) {
    	System.out.println(wallet_address);
    	return postsService.nftinfotoclient(pageRequest, wallet_address);
    }
    
	@PostMapping("/posts/wallet")
	public String test() {
		return postsService.nftinfoFromblock("0xbc7cc9517400cff0ec953efb585e424301a395b0");
	}
}
