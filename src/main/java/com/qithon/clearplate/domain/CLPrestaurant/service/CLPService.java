package com.qithon.clearplate.domain.CLPrestaurant.service;

import com.qithon.clearplate.domain.CLPrestaurant.dto.request.CLPLocationVerifyRequest;
import com.qithon.clearplate.domain.CLPrestaurant.dto.request.CLPRestaurantRegisterRequest;
import com.qithon.clearplate.domain.CLPrestaurant.dto.response.CLPLocationVerifyResponse;
import com.qithon.clearplate.domain.CLPrestaurant.dto.response.CLPRestaurantRegisterResponse;
import com.qithon.clearplate.domain.CLPrestaurant.entity.CLPRestaurant;
import com.qithon.clearplate.domain.CLPrestaurant.repository.CLPRepository;
import com.qithon.clearplate.domain.CLPrestaurant.vo.Location;
import java.util.stream.Collectors;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Service
@ToString
@RequiredArgsConstructor
public class CLPService {

  private final CLPRepository clpRepository;

  public List<CLPRestaurantRegisterResponse> registerRestaurantList(
      List<CLPRestaurantRegisterRequest> requestDTOs) {

    clpRepository.saveAll(requestDTOs.stream()
        .map(requestDTO -> CLPRestaurant.from(requestDTO))
        .toList());
    return requestDTOs.stream()
        .map(requestDTO -> CLPRestaurantRegisterResponse.from(requestDTO))
        .toList();
  }

  public CLPRestaurant getRestaurantById(String restauranId) {
    return clpRepository.findByRestaurantId(restauranId)
        .orElseThrow(() -> new RuntimeException("레스토랑을 찾을 수 없습니다."));
  }

  public List<CLPRestaurant> getAllRestaurants() throws RuntimeException {
    List<CLPRestaurant> clpRestaurants = clpRepository.findAll();

    return clpRestaurants;
  }

  public List<CLPRestaurant> getAllRestaurantsByLocation(Location userlocation, Double distance) throws RuntimeException {
      List<CLPRestaurant> clpRestaurants = clpRepository.findAll();

      List<CLPRestaurant> filteredRestaurants = clpRestaurants.stream()
          .filter(restaurant -> Location
              .calculateDistanceInMeters(userlocation, Location.of(restaurant.getY(), restaurant.getX())) <= distance)
          .collect(Collectors.toList());

      return filteredRestaurants;
  }


  public CLPLocationVerifyResponse verifyLocation(CLPLocationVerifyRequest request) {
    if(clpRepository.findByRestaurantId(request.getRestaurantId()).isPresent()) {
      CLPRestaurant clpRestaurant = clpRepository.findByRestaurantId(request.getRestaurantId())
          .orElseThrow( () -> new RuntimeException("레스토랑을 찾을 수 없습니다."));

      //레스토랑 위치 객체
      Location restaurantLocation = Location.of(Double.parseDouble(clpRestaurant.getY()), Double.parseDouble(clpRestaurant.getX()));
      System.out.println("레스토랑 위치(기준) restaurantLocation = " + restaurantLocation.getLatitude() + ", " + restaurantLocation.getLongitude());

      //유저 위치 객체
      Location userLocation = Location.of(request.getUserLatitude(), request.getUserLongitude());
      System.out.println("유저 위치 userLocation = " + userLocation.getLatitude() + ", " + userLocation.getLongitude());


      //유저와 레스토랑 사이의 거리 계산
      Double distance =  Location.calculateDistanceInMeters(restaurantLocation, userLocation);
      System.out.println("(유저와 레스토랑의 거리) distance = " + distance + "m");
      if(distance <= 50) {
        CLPLocationVerifyResponse response = CLPLocationVerifyResponse.from(clpRestaurant);
        response.setDistance(String.valueOf(distance));
        return response;
      } else {
        throw new RuntimeException("레스토랑과 유저 사이의 거리가 50m를 초과합니다. : " + distance + "m");
      }

      //레스토랑과 유저 사이의 거리가 50m 이내인지 확인
    } else {
      throw new RuntimeException("레스토랑을 찾을 수 없습니다.");
    }
  }
}
