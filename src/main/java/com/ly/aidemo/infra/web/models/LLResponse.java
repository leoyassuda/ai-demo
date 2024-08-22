package com.ly.aidemo.infra.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LLResponse {
    private String status;
    private String response;
}
