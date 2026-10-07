package com.example.scoreapi.model;

import java.math.BigDecimal;

public record StudentScore(
        String sbd,
        String hoTen,
        BigDecimal diemToan,
        BigDecimal diemVan,
        BigDecimal diemAnhVan) {
}
