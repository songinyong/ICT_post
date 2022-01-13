/*
 * 
 * 게시물 서비스 기능을 담당하는 서비스 계층
 * 
 * 21.12.08: ICT 발표전 최종 수정일 
 * 21.01.11: 기능이 많아짐에 따라 postService의 기능을 여러개의 클래스에 나눌 예정 
 * */

package post.service;

import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import post.web.dto.NftTradeDto;
import post.web.dto.PageGetDto;
import post.web.dto.PagingDto;
import post.web.dto.PostsResponseDto;
import post.web.dto.PostsSaveRequestDto;
import post.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    
    
    /*게시물 저장 기능
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
    	return postsRepository.save(requestDto.toEntity()).getId();
    }
    */
    
    /*게시물 가격 수정 기능
     * 21.01.03 수정사항: sell_state가 0일때만 가격을 수정할 수 있도록 해야함
     * 21.01.11 결과값 json으로 수정 필요
     * */
    @Transactional
    public boolean updatePrice(String token_id, PostsUpdateRequestDto rdto) {
    	if(!postsRepository.findBytokenID(token_id).isEmpty()) {
    		Optional<Posts> posts = postsRepository.findBytokenID(token_id);
    		
        	if(posts.get().getSell_state() == 0 && rdto.getPrice() >= 0 ) {
            	posts.get().update(rdto.getPrice());
            	return true ; }
        		
        	// 왜 실패 했는지 이유를 명확히 적어야함
            	else return false ;
    	}
    	
    	else return false ;
    }
    

    //게시물 id 기준 게시물의 정보 불러오기 기능
    public PostsResponseDto findById(Long id) {
    	Posts entity = postsRepository.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 이거나 삭제된 게시물입니다." + id));
    	
    	return new PostsResponseDto(entity);
    }
   
    
    /*사용자 id 기준으로 작성한 게시물들의 id 및 정보 불러오기 기능
     * 22.01.04 사용자 ID를 직접 저장하기 않기로 설계가 수정되서 
     * 사용하지 않기로함
    @Transactional(readOnly = true)
    public List<PostsResponseDto> findUsersPosts(int user_id) {
        return postsRepository.findUsersPosts(user_id).stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());
    }
    */
    
    /*sell_state 변경하기
    0일때 보관중, 1일때 장터에 올림, 2 거래 진행중
    사용자가 변경 할 수 있는 범위는 0~1
    
     최초작성일: 21.01.03
    * 21.01.11 결과값 json으로 수정 필요
    */
    @Transactional
    public boolean updateSell_state(String token_id) {
    	if(!postsRepository.findBytokenID(token_id).isEmpty()) {
    		Optional<Posts> posts = postsRepository.findBytokenID(token_id);
    		
        	if(posts.get().getSell_state() == 0 ) {
            	posts.get().stateUpdate(1);
            	return true ; }
        	else if (posts.get().getSell_state() == 1) {
            	posts.get().stateUpdate(0);
            	return true ; }
        		
            	else return false ;
    	}
    	
    	else return false ;
    	
    }
    
    /*삭제기능
     * 테스트 해보기용 Delete는 사용하면 안됨
    @Transactional
    public Long deletePosts(Long id) {
    	Posts entity = postsRepository.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 이거나 삭제된 게시물입니다." + id));
    	postsRepository.delete(entity);
    	return id;
    }*/
    
    //게시물 전체를 페이지 형태로 보여줌
    public Page<PagingDto> paging(Pageable pageRequest) {
    	
    	//결과 값이 null 이면 Page가 빈 객체로 return해줌 
    	Page<Posts> postsList = postsRepository.findAll(pageRequest);
    	Page<PagingDto> pagingList = postsList.map(PagingDto::new);
    	
    	return pagingList ;
    }
    
    //블록체인 서비스로부터 사용자 지갑별로 가지고 있는 nft 아이템 정보를 받아옴
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
    
    /*
     * 블록체인 아이템 DB로부터 정보를 받아온뒤 nft token_id를 기준으로 DB에 없는 아이템들을 추가한다.
     * crontab으로 동기화 시행
     * */
    public ResponseEntity<JSONObject> recvNftInfofromBlckdb() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json;charset=utf-8");
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers); 
		RestTemplate rt = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		
		try {
		JsonNode response =rt.getForObject("http://13.125.152.144:5555/chain/findAllnfts", JsonNode.class,entity );
    	
		
    	JsonNode items =  response.get("items");
    	System.out.println(items);
    	List<PostsSaveRequestDto> accountList = mapper.convertValue(
    		    items,
    		    new TypeReference<List<PostsSaveRequestDto>>(){}
    		);
    	//동기화하여 추가한 아이템 수 체크
    	int count = 0;
    	
    	for(int i =0; i<accountList.size(); i++) {
    		if(postsRepository.checkID(accountList.get(i).getToken_id()).isEmpty()) {
    		postsRepository.save(accountList.get(i).toEntity());
    		count++;
    		
    	    } 
    	}
		
		JSONObject resultObj = new JSONObject();  
		resultObj.put("result","true");
		resultObj.put("count",count);
    	
    	return new ResponseEntity<JSONObject>(resultObj, HttpStatus.ACCEPTED);
    	
		} catch (Exception e) {
			JSONObject resultObj = new JSONObject();  
			resultObj.put("result","false");
	    	return new ResponseEntity<JSONObject>(resultObj, HttpStatus.ACCEPTED);
		}
    }
    
    //DB에 저장된 지갑 주소 기반으로 게시물들의 정보를 페이지 형태로 불러옴
    //프젝 발표 이후 수정
    public Page<PagingDto> nftinfotoclient(Pageable pageRequest, String wallet_address) {
    	//nftinfoFromblock(wallet_address);
    	
    	//결과 값이 null 이여도 Page가 빈 객체로 return해줌 
    	Page<Posts> postsList = postsRepository.findWallet(pageRequest, wallet_address);
    	Page<PagingDto> pagingList = postsList.map(PagingDto::new);
    	
    	return pagingList ;
    }
    
    //필터 조회- 특정 지갑 아이디 기준 지정한 sell_state 게시물만 조회
    public Page<PagingDto> findByWalletAndSellState(Pageable pageRequest, PageGetDto pgdto) {
    	
    	//결과 값이 null 이여도 Page가 빈 객체로 return해줌 
    	Page<Posts> postsList = postsRepository.findWalletAndSellState(pageRequest, pgdto.getOwner(), pgdto.getSell_state());		
    	Page<PagingDto> pagingList = postsList.map(PagingDto::new);
    	
    	return pagingList ;
    }
    
    //필터 조회 - 장터 판매중인 아이템 전체 조회, 지갑 기준 자신의 아이템은 표시 되지 않음
    public Page<PagingDto> findByNotWalletAndSellState(String owner, Pageable pageRequest) {
    	
    	//결과 값이 null 이여도 Page가 빈 객체로 return해줌 
    	Page<Posts> postsList = postsRepository.findNotWalletAndSellState(pageRequest, owner, 1);		
    	Page<PagingDto> pagingList = postsList.map(PagingDto::new);
    	
    	return pagingList ;
    }
    
    //nft 아이템 trade
    public ResponseEntity<JSONObject> nfttrade(NftTradeDto tradeDto) {
    	
		RestTemplate rt = new RestTemplate();
				
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json");

	
		JSONObject createData = new JSONObject();
		JSONObject resultObj = new JSONObject();  
		

		createData.put("to", tradeDto.getTo());
		createData.put("sender",tradeDto.getSender());
		createData.put("owner" ,tradeDto.getOwner());
		createData.put("token_id", tradeDto.getToken_id());


		 HttpEntity<String> entity = 
			      new HttpEntity<String>(createData.toString(), headers);
		System.out.println(entity);
		String uri = "http://13.125.152.144:5555/chain/trade";
		
		try {
		ResponseEntity<JSONObject> result =rt.exchange(uri, HttpMethod.PUT, entity, JSONObject.class);
		resultObj.put("result","true");
		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.ACCEPTED);
		
		}
		catch (Exception e) {
			resultObj.put("result","false");
			return new ResponseEntity<JSONObject>(resultObj, HttpStatus.FORBIDDEN);
			
		}
    }
    

}
