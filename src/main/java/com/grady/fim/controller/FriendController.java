package com.grady.fim.controller;

import com.grady.fim.common.constants.ErrorCodes;
import com.grady.fim.common.exception.BusinessException;
import com.grady.fim.common.pojo.bo.JsonResult;
import com.grady.fim.common.pojo.req.AddFriendReqVo;
import com.grady.fim.common.pojo.req.AgreeRequestVo;
import com.grady.fim.common.pojo.req.RejectRequestVo;
import com.grady.fim.common.pojo.rsp.FriendListRspVo;
import com.grady.fim.common.pojo.rsp.FriendRequestRspVo;
import com.grady.fim.common.pojo.rsp.NullBody;
import com.grady.fim.common.utils.JwtTokenUtils;
import com.grady.fim.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.grady.fim.common.constants.Constants.HEADER_AUTHORIZATION;

@Api(value = "/friend")
@Log4j2
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    FriendService friendService;

    @ApiOperation(value = "获取好友列表")
    @PostMapping("/getFriends")
    public JsonResult<FriendListRspVo> getFriendList(HttpServletRequest request) throws BusinessException {
        String username = JwtTokenUtils.getUsernameFromToken(request.getHeader(HEADER_AUTHORIZATION));
        if (username == null) {
            throw new BusinessException(ErrorCodes.ILLEGAL_ARGUMENT_CODE, "Authorization 非法");
        }
        return friendService.getFriendList(username);
    }

    @ApiOperation(value = "添加好友请求")
    @PostMapping("/addRequest")
    public JsonResult<NullBody> addFriendRequest(@RequestBody AddFriendReqVo vo, HttpServletRequest request)
            throws BusinessException {
        String username = JwtTokenUtils.getUsernameFromToken(request.getHeader(HEADER_AUTHORIZATION));
        if (username == null) {
            throw new BusinessException(ErrorCodes.ILLEGAL_ARGUMENT_CODE, "Authorization 非法");
        }
        if (StringUtils.isEmpty(vo.getFriendAccount()) || StringUtils.isEmpty(username)) {
            throw new BusinessException(ErrorCodes.ILLEGAL_ARGUMENT_CODE, "Authorization 非法");
        }

        return friendService.addFriendRequest(username, vo.getFriendAccount());
    }

    @ApiOperation(value = "获取加好友请求列表")
    @PostMapping("/getFriendsRequest")
    public JsonResult<FriendRequestRspVo> getFriendsRequest(HttpServletRequest request) throws BusinessException {
        String username = JwtTokenUtils.getUsernameFromToken(request.getHeader(HEADER_AUTHORIZATION));
        if (username == null) {
            throw new BusinessException(ErrorCodes.ILLEGAL_ARGUMENT_CODE, "Authorization 非法");
        }
        return friendService.getFriendsRequest(username);
    }

    @ApiOperation(value = "同意好友请求")
    @PostMapping("/agreeRequest")
    public JsonResult<NullBody> agreeFriendRequest(@RequestBody AgreeRequestVo vo , HttpServletRequest request) throws BusinessException {
        String username = JwtTokenUtils.getUsernameFromToken(request.getHeader(HEADER_AUTHORIZATION));
        if (username == null) {
            throw new BusinessException(ErrorCodes.ILLEGAL_ARGUMENT_CODE, "Authorization 非法");
        }
        if (StringUtils.isEmpty(vo.getAgreeUserAccount())) {
            throw new BusinessException(ErrorCodes.ILLEGAL_ARGUMENT_CODE, "参数非法");
        }
        return friendService.agreeFriendRequest(username, vo.getAgreeUserAccount());
    }

    @ApiOperation(value = "拒绝好友请求")
    @PostMapping("/rejectRequest")
    public JsonResult<NullBody> rejectFriendRequest(@RequestBody RejectRequestVo vo , HttpServletRequest request) throws BusinessException {
        String username = JwtTokenUtils.getUsernameFromToken(request.getHeader(HEADER_AUTHORIZATION));
        if (username == null) {
            throw new BusinessException(ErrorCodes.ILLEGAL_ARGUMENT_CODE, "Authorization 非法");
        }
        if (StringUtils.isEmpty(vo.getRejectUserAccount())) {
            throw new BusinessException(ErrorCodes.ILLEGAL_ARGUMENT_CODE, "参数非法");
        }
        return friendService.rejectFriendRequest(username, vo.getRejectUserAccount());
    }
}
