package com.qithon.clearplate.domain.CLPrestaurant.controller;

import com.qithon.clearplate.domain.CLPrestaurant.dto.request.CLPLocationVerifyRequest;
import com.qithon.clearplate.domain.CLPrestaurant.dto.request.CLPRestaurantRegisterRequest;
import com.qithon.clearplate.domain.CLPrestaurant.dto.response.CLPLocationVerifyResponse;
import com.qithon.clearplate.domain.CLPrestaurant.dto.response.CLPRestaurantRegisterResponse;
import com.qithon.clearplate.domain.CLPrestaurant.entity.CLPRestaurant;
import com.qithon.clearplate.domain.CLPrestaurant.service.CLPService;
import com.qithon.clearplate.domain.CLPrestaurant.vo.Location;
import com.qithon.clearplate.global.common.dto.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clp")
@Tag(name = "clp 레스토랑 관련 api 입니다.", description = "등록된 clp 레스토랑을 조회하거나 등록하고, 인증합니다.")
public class CLPController {

  private final CLPService clpService;

  @ApiResponse(
      responseCode = "200",
      description = "### ✅ 레스토랑 등록에 성공한 경우",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                {
                    "timestamp": "2025-07-11T17:02:04.596431",
                    "data": [
                        {
                            "address_name": "경기 성남시 분당구 백현동 537",
                            "category_group_code": "FD6",
                            "category_group_name": "음식점",
                            "category_name": "음식점 > 샤브샤브",
                            "distance": "",
                            "id": "1876658997",
                            "phone": "031-622-7179",
                            "place_name": "제이스팟 알파돔타워",
                            "place_url": "http://place.map.kakao.com/1876658997",
                            "road_address_name": "경기 성남시 분당구 판교역로 152",
                            "x": "127.11054127228883",
                            "y": "37.394487112883404",
                            "image_url": null,
                            "subtitle": "따뜻한 국물이 일품인 샤브샤브 전문점"
                        }
                    ]
                }
                """
          )
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "--- \n### ❌ 레스토랑 등록에 실패한 경우",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                  {
                    "timestamp": "2025-07-11T00:30:38.405707",
                    "status": "BAD_REQUEST",
                    "message": "레스토랑 등록에 실패하였습니다."
                  }  
                 """

          )
      )
  )
  @PostMapping
  public ResponseEntity<?> registerRestaurant(@RequestBody List<CLPRestaurantRegisterRequest> requestDTOs) {
    try {
      List<CLPRestaurantRegisterResponse> response = clpService.registerRestaurantList(requestDTOs);
      return ResponseEntity.ok(ResponseDTO.response(response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body("레스토랑 등록에 실패하였습니다.: " + e.getMessage());
    }
  }



  @ApiResponse(
      responseCode = "200",
      description = "### ✅ 현재 등록된 CLP 레스토랑을 전부 조회합니다.",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                {
                    "timestamp": "2025-07-11T17:33:39.402055",
                    "data": [
                        {
                            "id": 1,
                            "addressName": "경기 성남시 분당구 백현동 537",
                            "distance": "",
                            "restaurantId": "1876658997",
                            "phone": "031-622-7179",
                            "placeName": "제이스팟 알파돔타워",
                            "placeUrl": "http://place.map.kakao.com/1876658997",
                            "roadAddressName": "경기 성남시 분당구 판교역로 152",
                            "x": "127.11054127228883",
                            "y": "37.394487112883404",
                            "imageUrl": null,
                            "subtitle": "따뜻한 국물이 일품인 샤브샤브 전문점"
                        }
                    ]
                }
                """
          )
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "--- \n### ❌ 레스토랑 조회에 실패한 경우",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                  {
                    "timestamp": "2025-07-11T00:30:38.405707",
                    "status": "BAD_REQUEST",
                    "message": "레스토랑 조회에 실패하였습니다."
                  }  
                 """

          )
      )
  )
  @Operation(summary = "CLP 레스토랑 모두 조회 api", description = "현재 등록된 CLP 레스토랑을 모두 조회합니다.")
  @GetMapping("/all")
  public ResponseEntity<?> getAllRestaurants() {
    try {
      System.out.println("CLPController.getAllRestaurants");
      List<CLPRestaurant> response = clpService.getAllRestaurants();
      return ResponseEntity.ok(ResponseDTO.response(response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ResponseDTO
              .response(HttpStatus.BAD_REQUEST, "레스토랑 조회에 실패하였습니다.", null));
    }
  }




  @ApiResponse(
      responseCode = "200",
      description = "### ✅ 조회에 성공했을 경우",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                  {
                    "timestamp": "2025-07-12 15:16:54",
                    "data": {
                      "id": 93,
                      "addressName": "경기 성남시 분당구 삼평동 660",
                      "clpCategory": {
                        "category_group_code": "FD6",
                        "category_group_name": "음식점",
                        "category_name": "음식점 > 일식 > 돈까스,우동"
                      },
                      "distance": "",
                      "restaurantId": "26797370",
                      "phone": "",
                      "placeName": "판교매일식당 판교본점",
                      "placeUrl": "http://place.map.kakao.com/26797370",
                      "roadAddressName": "경기 성남시 분당구 판교역로192번길 14-2",
                      "x": "127.11210628616844",
                      "y": "37.39738062184579",
                      "imageUrl": null,
                      "subtitle": "매일 먹어도 질리지 않는 든든한 한 끼"
                    }
                  }
                """
          )
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "--- \n### ❌ 레스토랑 조회에 실패한 경우",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                  {
                      "timestamp": "2025-07-12 15:17:27",
                      "status": "BAD_REQUEST",
                      "message": "레스토랑을 찾을 수 없습니다."
                    }  
                 """

          )
      )
  )
  @Operation(summary = "id 로 CLP 레스토랑을 조회합니다. ", description = "현재 등록된 CLP 레스토랑을 id 로 조회합니다")
  @GetMapping("/{restaurantId}")
  public ResponseEntity<?> getRestaurantById(@PathVariable String restaurantId) {
    try {
      CLPRestaurant response = clpService.getRestaurantById(restaurantId);
      return ResponseEntity.ok(ResponseDTO.response(response));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(ResponseDTO.response(HttpStatus.BAD_REQUEST, e.getMessage(), null));
    }
  }



  @ApiResponse(
      responseCode = "200",
      description = "### ✅ 레스토랑 조회에 성공한 경우",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                      {
                          "timestamp": "2025-07-12 14:21:55",
                          "data": [
                              {
                                  "id": 91,
                                  "addressName": "경기 성남시 분당구 백현동 537",
                                  "clpCategory": {
                                      "category_group_code": "FD6",
                                      "category_group_name": "음식점",
                                      "category_name": "음식점 > 샤브샤브"
                                  }
                              }
                          ]
                      }
                      """
          )
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "--- \n### ❌ 레스토랑 조회에 실패한 경우",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(
              example = """
                  {
                      "timestamp": "2025-07-12 14:21:18",
                      "status": "BAD_REQUEST",
                      "message": "레스토랑 조회에 실패하였습니다."
                    }  
                 """

          )
      )
  )
  @Operation(summary = "유저 근처 CLP 레스토랑 조회 api", description = "{distance} (미터기준) 이내의 CLP 레스토랑을 조회합니다. / body 에는 유저의 위치 정보를 기입해주세요.")
  @PostMapping("/all/{distance}")
  public ResponseEntity<?> getAllRestaurantsByLocation(@RequestBody Location userlocation, @PathVariable double distance) {
    System.out.println("/all/{distance} 호출");
    try {
        List<CLPRestaurant> response = clpService.getAllRestaurantsByLocation(userlocation, distance);
        return ResponseEntity.ok(ResponseDTO.response(response));

      } catch (RuntimeException e) {
        return ResponseEntity.badRequest()
            .body(ResponseDTO.response(HttpStatus.BAD_REQUEST, "레스토랑 조회에 실패하였습니다.", null));
      }
    }

    @ApiResponse(
        responseCode = "200",
        description = "### ✅ 사용자와 레스토랑의 위치가 50m 이내일 때 성공",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example = """
                      {
                         "timestamp": "2025-07-11T23:36:54.211888",
                         "data": {
                           "address_name": "경기 성남시 분당구 판교동 498",
                           "category_group_code": "FD6",
                           "category_group_name": "음식점",
                           "category_name": "음식점 > 일식 > 일식집",
                           "distance": "0.0",
                           "id": "26815946",
                           "phone": "031-702-9317",
                           "place_name": "긴자 판교점",
                           "place_url": "http://place.map.kakao.com/26815946",
                           "road_address_name": "경기 성남시 분당구 판교로 185",
                           "x": "127.095072999216",
                           "y": "37.4006401214003",
                           "image_url": null,
                           "subtitle": "고급스러운 분위기의 정통 일식"
                         }
                       }
                    """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "--- \n### ❌ 50m를 초과하는 경우",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                example = """
                     {
                        "timestamp": "2025-07-11T23:38:53.0533",
                        "status": "BAD_REQUEST",
                        "message": "레스토랑과 유저 사이의 거리가 50m를 초과합니다. : 88333.7m"
                      }  
                    """

            )
        )
    )
    @Operation(summary = "CLP 레스토랑 qr 인증 api", description = "유저의 위경도값과 레스토랑의 id 값을 넣어주세요. / 레스토랑과 유저 사이의 거리가 50m 이내일 때 성공합니다.")
    @PostMapping("/verify-location")
    public ResponseEntity<?> verifyLocation (@RequestBody CLPLocationVerifyRequest request){
      try {
        CLPLocationVerifyResponse response = clpService.verifyLocation(request);
        return ResponseEntity.ok(ResponseDTO.response(response));
      } catch (RuntimeException e) {
        return ResponseEntity.badRequest()
            .body(ResponseDTO.response(HttpStatus.BAD_REQUEST, e.getMessage(), null));
      }
    }

}
