package com.ParQ.ParQ.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "parking_oper")
@Getter
@Setter
public class ParkingOper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prk_center_id")
    private String prkCenterId;

    @Column(name = "matching_id")
    private String matchingId;

    @Column(name = "parking_chrge_bs_time")
    private String parkingChrgeBsTime;

    @Column(name = "parking_chrge_bs_chrge")
    private String parkingChrgeBsChrge;

    @Column(name = "parking_chrge_adit_unit_chrge")
    private String parkingChrgeAditUnitChrge;

    @Column(name = "parking_chrge_mon_unit_chrge")
    private String parkingChrgeMonUnitChrge;

    @Column(name = "parking_chrge_one_day_chrge")
    private String parkingChrgeOneDayChrge;

    @Column(name = "monday_start")
    private String mondayStart;
    @Column(name = "monday_end")
    private String mondayEnd;
    @Column(name = "tuesday_start")
    private String tuesdayStart;
    @Column(name = "tuesday_end")
    private String tuesdayEnd;
    @Column(name = "wednesday_start")
    private String wednesdayStart;
    @Column(name = "wednesday_end")
    private String wednesdayEnd;
    @Column(name = "thursday_start")
    private String thursdayStart;
    @Column(name = "thursday_end")
    private String thursdayEnd;
    @Column(name = "friday_start")
    private String fridayStart;
    @Column(name = "friday_end")
    private String fridayEnd;
    @Column(name = "saturday_start")
    private String saturdayStart;
    @Column(name = "saturday_end")
    private String saturdayEnd;
    @Column(name = "sunday_start")
    private String sundayStart;
    @Column(name = "sunday_end")
    private String sundayEnd;
    @Column(name = "holiday_start")
    private String holidayStart;
    @Column(name = "holiday_end")
    private String holidayEnd;
} 