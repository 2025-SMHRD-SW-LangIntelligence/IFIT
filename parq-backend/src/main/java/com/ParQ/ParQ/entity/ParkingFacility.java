package com.ParQ.ParQ.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "parking_facility")
@Getter
@Setter
public class ParkingFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prk_center_id")
    private String prkCenterId;

    @Column(name = "prk_plce_nm")
    private String prkPlceNm;

    @Column(name = "prk_plce_adres")
    private String prkPlceAdres;

    @Column(name = "prk_plce_adres_sido")
    private String prkPlceAdresSido;

    @Column(name = "prk_plce_adres_sigungu")
    private String prkPlceAdresSigungu;

    @Column(name = "prk_cmprt_co")
    private String prkCmprtCo;

    @Column(name = "prk_plce_entrc_la")
    private String prkPlceEntrcLa;

    @Column(name = "prk_plce_entrc_lo")
    private String prkPlceEntrcLo;

    @Column(name = "matching_id")
    private String matchingId;
} 