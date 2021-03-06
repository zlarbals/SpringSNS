package com.springsns.Post;

import com.springsns.Util.MD5Generator;
import com.springsns.account.AccountRepository;
import com.springsns.domain.Account;
import com.springsns.domain.Post;
import com.springsns.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final LikeRepository likeRepository;
    private final Path fileStorageLocation = Paths.get("/Users/KYUMIN/uploads").toAbsolutePath().normalize();

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts(String email){

        List<PostResponseDto> result = new ArrayList<>();
        //List<Post> postList = postRepository.findAllPosts();
        List<Post> postList = postRepository.findAll();

        Account account = null;
        if (email != null)
            account = accountRepository.findByEmail(email);


        if (account == null) {
            // 그냥 PostResponseDto에 담아서 넘겨줌
            for (Post post : postList) {
                result.add(new PostResponseDto(post, false));
            }
        } else {
            // PostResponseDto에 isLike 부분 true로 처리
            //이부분 db 쿼리로 수정할 것.

            for (Post post : postList) {

                if (likeRepository.existsByAccountAndPost(account, post)) {
                    result.add(new PostResponseDto(post, true));
                } else {
                    result.add(new PostResponseDto(post, false));
                }
            }
        }

        return result;
    }

//    public List<PostResponseDto> getAllPosts_(String email){
//        //강의 한번 더 보고 다시 해보자.
//    }

    public PostFile processPostFile(MultipartFile file) throws IOException {

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());


        Path targetLocation = this.fileStorageLocation.resolve(originalFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        //PostFile 생성자 수정할 것.
        PostFile postFile = new PostFile(originalFileName,originalFileName,"targetLocation");

        return postFile;

    }

    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        return resource;
    }

}
