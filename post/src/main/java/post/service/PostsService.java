package post.service;

import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import post.domain.posts.Posts;
import post.domain.posts.PostsRepository;
import post.web.dto.PagingDto;
import post.web.dto.PostsResponseDto;
import post.web.dto.PostsSaveRequestDto;
import post.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    
    
    //게시물 저장 기능
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
    	return postsRepository.save(requestDto.toEntity()).getId();
    }
    
    //게시물 가격 수정 기능
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
    	Posts posts = postsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시물 이거나 삭제된 게시물입니다."+id));
    	posts.update(requestDto.getPrice());
    	return id ;
    }
    
    //게시물 id 기준 게시물의 정보 불러오기 기능
    public PostsResponseDto findById(Long id) {
    	Posts entity = postsRepository.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 이거나 삭제된 게시물입니다." + id));
    	
    	return new PostsResponseDto(entity);
    }
    //사용자 id 기준으로 작성한 게시물들의 id 및 정보 불러오기 기능

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findUsersPosts(int user_id) {
        return postsRepository.findUsersPosts(user_id).stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());
    }
    
    //게시물 거래 완료시 게시물의 현재 상태 변경 기능
    
    //삭제기능
    @Transactional
    public Long deletePosts(Long id) {
    	Posts entity = postsRepository.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 이거나 삭제된 게시물입니다." + id));
    	postsRepository.delete(entity);
    	return id;
    }
    
    public Page<PagingDto> paging(Pageable pageRequest) {
    	
    	Page<Posts> postsList = postsRepository.findAll(pageRequest);
    	
    	Page<PagingDto> pagingList = postsList.map(PagingDto::new);
    	
    	return pagingList ;
    }
    
    //블록체인 서비스에서 정보를 받아옴
    public String nftinfoFromblock(String wallet_address) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json;charset=utf-8");
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers); 
		RestTemplate rt = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode response =rt.postForObject("http://13.125.152.144:5555/chain/findnft?address="+wallet_address, entity, JsonNode.class);
		

    	JsonNode items =  response.get("items");
    	List<PostsSaveRequestDto> accountList = mapper.convertValue(
    		    items, 
    		    new TypeReference<List<PostsSaveRequestDto>>(){}
    		);
    	
    	for(int i =0; i<accountList.size(); i++) {
    		postsRepository.save(accountList.get(i).toEntity());
    	}
    	        
    		
		
		return "hey" ;
    }
    
    //프젝 발표 이후 수정
    public Page<PagingDto> nftinfotoclient(Pageable pageRequest, String wallet_address) {
    	//nftinfoFromblock(wallet_address);
    	Page<Posts> postsList = postsRepository.findWallet(pageRequest, wallet_address);
    	
    	Page<PagingDto> pagingList = postsList.map(PagingDto::new);
    	
    	return pagingList ;
    }
    

}
