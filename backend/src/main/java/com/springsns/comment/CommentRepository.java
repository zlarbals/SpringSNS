package com.springsns.comment;

import com.springsns.domain.Comment;
import com.springsns.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentByPost(Post post);

}
