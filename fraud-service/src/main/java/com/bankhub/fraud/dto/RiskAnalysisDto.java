package com.bankhub.fraud.dto;

import java.math.BigDecimal;
import java.util.List;

public class RiskAnalysisDto {
    private BigDecimal riskScore;
    private String riskLevel;
    private List<String> riskFactors;
    private boolean isHighRisk;
    private boolean shouldBlock;
    private String decision;
    private String reasoning;

    public RiskAnalysisDto() {}

    public RiskAnalysisDto(BigDecimal riskScore, String riskLevel, List<String> riskFactors, boolean isHighRisk, boolean shouldBlock, String decision, String reasoning) {
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.riskFactors = riskFactors;
        this.isHighRisk = isHighRisk;
        this.shouldBlock = shouldBlock;
        this.decision = decision;
        this.reasoning = reasoning;
    }

    public static RiskAnalysisBuilder builder() { return new RiskAnalysisBuilder(); }

    public BigDecimal getRiskScore() { return riskScore; }
    public void setRiskScore(BigDecimal riskScore) { this.riskScore = riskScore; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public List<String> getRiskFactors() { return riskFactors; }
    public void setRiskFactors(List<String> riskFactors) { this.riskFactors = riskFactors; }
    public boolean isHighRisk() { return isHighRisk; }
    public void setHighRisk(boolean highRisk) { isHighRisk = highRisk; }
    public boolean isShouldBlock() { return shouldBlock; }
    public void setShouldBlock(boolean shouldBlock) { this.shouldBlock = shouldBlock; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }

    public static class RiskAnalysisBuilder {
        private BigDecimal riskScore;
        private String riskLevel;
        private List<String> riskFactors;
        private boolean isHighRisk;
        private boolean shouldBlock;
        private String decision;
        private String reasoning;

        RiskAnalysisBuilder() {}

        public RiskAnalysisBuilder riskScore(BigDecimal riskScore) { this.riskScore = riskScore; return this; }
        public RiskAnalysisBuilder riskLevel(String riskLevel) { this.riskLevel = riskLevel; return this; }
        public RiskAnalysisBuilder riskFactors(List<String> riskFactors) { this.riskFactors = riskFactors; return this; }
        public RiskAnalysisBuilder isHighRisk(boolean isHighRisk) { this.isHighRisk = isHighRisk; return this; }
        public RiskAnalysisBuilder shouldBlock(boolean shouldBlock) { this.shouldBlock = shouldBlock; return this; }
        public RiskAnalysisBuilder decision(String decision) { this.decision = decision; return this; }
        public RiskAnalysisBuilder reasoning(String reasoning) { this.reasoning = reasoning; return this; }

        public RiskAnalysisDto build() {
            return new RiskAnalysisDto(riskScore, riskLevel, riskFactors, isHighRisk, shouldBlock, decision, reasoning);
        }
    }
}