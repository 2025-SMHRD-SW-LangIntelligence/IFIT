package com.ParQ.ParQ.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotResponseDto {
    private Long id;
    private String prkCenterId;
    private String prkPlceNm;
    private String prkPlceAdres;
    private String prkPlceAdresSido;
    private String prkPlceAdresSigungu;
    private String prkCmprtCo;
    private String prkPlceEntrcLa;
    private String prkPlceEntrcLo;
    private String matchingId;
    // 운영정보
    private String parkingChrgeBsTime;
    private String parkingChrgeBsChrge;
    private String parkingChrgeAditUnitChrge;
    private String parkingChrgeMonUnitChrge;
    private String parkingChrgeOneDayChrge;
    private String mondayStart;
    private String mondayEnd;
    private String tuesdayStart;
    private String tuesdayEnd;
    private String wednesdayStart;
    private String wednesdayEnd;
    private String thursdayStart;
    private String thursdayEnd;
    private String fridayStart;
    private String fridayEnd;
    private String saturdayStart;
    private String saturdayEnd;
    private String sundayStart;
    private String sundayEnd;
    private String holidayStart;
    private String holidayEnd;
} 