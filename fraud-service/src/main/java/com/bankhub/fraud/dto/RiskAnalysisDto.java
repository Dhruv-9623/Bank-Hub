package com.bankhub.fraud.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class RiskAnalysisDto {
    private BigDecimal riskScore;
    private String riskLevel;
    private List<String> riskFactors;
    private boolean isHighRisk;
    private boolean shouldBlock;
    private String decision;
    private String reasoning;
}