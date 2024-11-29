package com.aoyouao.novel.controller.front;

//import com.aoyouao.novel.core.auth.UserHolder;
import com.aoyouao.novel.core.auth.UserHolder;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.ApiRouterConsts;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.req.UserLoginReqDto;
import com.aoyouao.novel.dto.req.UserRegisterReqDto;
import com.aoyouao.novel.dto.resp.UserLoginRespDto;
import com.aoyouao.novel.dto.resp.UserRegisterRespDto;
import com.aoyouao.novel.service.BookService;
import com.aoyouao.novel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_HOME_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final BookService bookService;

    @PostMapping("register")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto userRegisterReqDto){
        return userService.register(userRegisterReqDto);
    }

    @PostMapping("login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto userLoginReqDto){
        return userService.login(userLoginReqDto);
    }

    /**
     * 发表评论接口
     */
    @Operation(summary = "发表评论接口")
    @PostMapping("comment")
    public RestResp<Void> saveComment(@Valid @RequestBody UserCommentReqDto dto) {
        Long userId = UserHolder.getUserId();
        log.info("UserId from UserHolder: {}", userId);  // 打印 userId 值
        dto.setUserId(userId);
        return bookService.saveComment(dto);
    }

    @PutMapping("comment/{id}")
    public RestResp<Void> updateComment(@PathVariable Long id,@RequestBody String content){
        return bookService.updateComment(UserHolder.getUserId(),id,content);
    }

    @DeleteMapping("comment/{id}")
    public RestResp<Void> deleteComment(@PathVariable Long id){
        return bookService.deleteComment(UserHolder.getUserId(),id);
    }
}
