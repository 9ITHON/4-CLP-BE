package com.qithon.clearplate.domain.user.controller;

import com.amazonaws.Response;
import com.qithon.clearplate.domain.user.dto.response.UserResponse;
import com.qithon.clearplate.domain.user.dto.response.UserStampResponse;
import com.qithon.clearplate.domain.user.dto.response.UserStampSummaryResponse;
import com.qithon.clearplate.domain.user.entity.User;
import com.qithon.clearplate.domain.user.service.UserService;
import com.qithon.clearplate.global.common.dto.response.ResponseDTO;
import com.qithon.clearplate.global.security.config.ServletLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "유저 관련 api 입니다.", description = "로그인된 유저 정보를 조회합니다.")
public class UserController {

  private final UserService userService;
  private final ServletLogin servletLogin;


  @ApiResponse(
      responseCode = "200",
      description = "### ✅ 로그인된 유저가 존재할 경우",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                         {
                    "timestamp": "2025-07-12 00:24:39",
                    "data": {
                      "id": 1,
                      "socialEmail": "4321933402@kakao",
                      "nickname": "홍준표",
                      "cpPoint": 0
                    }
                  }
                        """

          )
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "--- \n### ❌ 로그인된 유저가 없을경우 ",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                   {
                      "timestamp": "2025-07-12 00:21:25",
                      "status": "BAD_REQUEST",
                      "message": "로그인된 유저가 없습니다."
                    }
                  """

          )
      )
  )
  @GetMapping
  public ResponseEntity<?> getLoginUser(HttpServletRequest request) {
    try {
      UserResponse userResponse = userService.getLoginUser(request);
      return ResponseEntity.ok().body(ResponseDTO.response(userResponse));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(
          ResponseDTO.response(HttpStatus.BAD_REQUEST, "로그인된 유저가 없습니다.", null));
    }
  }

  @Operation(summary = "포인트", description = "사용자의 모든 포인트를 조회합니다.")
  @GetMapping("/point")
  public ResponseEntity<Long> getUserPoint(HttpServletRequest request) {
    Long userId = servletLogin.extractUserIdFromRefreshToken(request);
    Long point = userService.getUserPoint(userId);
    return ResponseEntity.ok(point);


  }

  @Operation(summary = "사용자의 스탬프 조회", description = "사용자가 획득한 스탬프를 조회합니다.")
  @GetMapping("/stamps")
  public ResponseEntity<UserStampSummaryResponse> getUserStamps(HttpServletRequest request) {
    Long userId = servletLogin.extractUserIdFromRefreshToken(request);
    UserStampSummaryResponse response = userService.getUserStamps(userId);
    return ResponseEntity.ok(response);
  }

}
