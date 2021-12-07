package post.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
