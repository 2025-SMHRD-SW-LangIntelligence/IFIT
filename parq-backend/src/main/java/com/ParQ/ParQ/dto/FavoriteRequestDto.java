package com.ParQ.ParQ.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteRequestDto {
	
	@NotNull(message = "아이디는 필수입니다.")
    //@JsonProperty("userId")
	private Long userId;
	
    //@JsonProperty("parkingLotId")
	@NotNull(message = "주차장 이름은 필수입니다.")
	private Long parkingLotId;
	
}
