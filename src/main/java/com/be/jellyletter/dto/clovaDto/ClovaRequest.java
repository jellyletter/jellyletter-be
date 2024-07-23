package com.be.jellyletter.dto.clovaDto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class ClovaRequest {

    private ArrayList<ClovaRequestMessage> messages = new ArrayList<>();
    private double topP;
    private int topK;
    private int maxTokens;
    private double temperature;
    private double repeatPenalty;
    private ArrayList<String> stopBefore = new ArrayList<>();
    private boolean includeAiFilters;
}
