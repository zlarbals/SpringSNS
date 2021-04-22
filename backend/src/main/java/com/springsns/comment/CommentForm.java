package com.springsns.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentForm {

    String authorNickname;

    @NotBlank
    String content;

}
