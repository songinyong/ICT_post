/*
 * 주기적으로 작동해야하는 동기화 관련 메서드들을 스케줄러를 통해 실행하는 클래스
 * 
 * */

package post.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import post.service.PostsService;



@Component
public class Scheduler { 
	
	@Autowired
	private PostsService postsService;
	
	//3분에 한번 동기화를 시행한다.
    @Scheduled(cron = "0 0/2 * * * ?") 
    public void runSync() { 
    	postsService.recvNftInfofromBlckdb();
    } 
}
