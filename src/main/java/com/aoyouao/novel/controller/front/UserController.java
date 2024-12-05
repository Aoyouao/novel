package com.aoyouao.novel.controller.front;

//import com.aoyouao.novel.core.auth.UserHolder;
import com.aoyouao.novel.core.auth.UserHolder;
import com.aoyouao.novel.core.common.resp.RestResp;
import com.aoyouao.novel.core.constant.ApiRouterConsts;
import com.aoyouao.novel.dto.req.UserCommentReqDto;
import com.aoyouao.novel.dto.req.UserInfoUptReqDto;
import com.aoyouao.novel.dto.req.UserLoginReqDto;
import com.aoyouao.novel.dto.req.UserRegisterReqDto;
import com.aoyouao.novel.dto.resp.UserInfoRespDto;
import com.aoyouao.novel.dto.resp.UserLoginRespDto;
import com.aoyouao.novel.dto.resp.UserRegisterRespDto;
import com.aoyouao.novel.service.BookService;
import com.aoyouao.novel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final BookService bookService;

    //用户注册模块
    @PostMapping("register")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto userRegisterReqDto){
        return userService.register(userRegisterReqDto);
    }

    //用户登录模块
    @PostMapping("login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto userLoginReqDto){
        return userService.login(userLoginReqDto);
    }

    //发表评论接口
    @Operation(summary = "发表评论接口")
    @PostMapping("comment")
    public RestResp<Void> saveComment(@Valid @RequestBody UserCommentReqDto dto) {
        Long userId = UserHolder.getUserId();
        log.info("UserId from UserHolder: {}", userId);  // 打印 userId 值
        dto.setUserId(userId);
        log.info("Saving comment for userId: {} and bookId: {}", dto.getUserId(), dto.getBookId());
        return bookService.saveComment(dto);
    }

    //修改评论接口
    @PutMapping("comment/{id}")
    public RestResp<Void> updateComment(@PathVariable Long id,@RequestBody String content){
        return bookService.updateComment(UserHolder.getUserId(),id,content);
    }

    //删除评论接口
    @DeleteMapping("comment/{id}")
    public RestResp<Void> deleteComment(@PathVariable Long id){
        return bookService.deleteComment(UserHolder.getUserId(),id);
    }

    //用户信息更改接口
    @PutMapping
    public RestResp<Void> updateUserInfo(@Valid @RequestBody UserInfoUptReqDto userInfoUptReqDto){
        userInfoUptReqDto.setUserId(UserHolder.getUserId());
        return userService.updateUserInfo(userInfoUptReqDto);
    }

    //用户信息查询接口
    @GetMapping
    public RestResp<UserInfoRespDto> getUserInfo(){
        return userService.getUserInfo(UserHolder.getUserId());
    }

    //用户反馈接口
    @PostMapping("feedback")
    public RestResp<Void> saveFeedback(@RequestBody String content){
        return userService.saveFeedback(UserHolder.getUserId(),content);
    }

    //用户反馈删除接口
    @GetMapping("feedback/{id}")
    public RestResp<Void> deleteFeedback(@PathVariable Long id){
        return userService.deleteFeedback(UserHolder.getUserId(),id);
    }

    //查询书架状态接口
    @GetMapping("bookshelf_status")
    public RestResp<Integer> getBookshelfStatus(Long id){
        return userService.getBookshelfStatus(UserHolder.getUserId(),id);
    }
}
