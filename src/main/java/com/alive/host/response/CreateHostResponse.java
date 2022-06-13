package com.alive.host.response;

import com.alive.host.domain.Host;
import com.alive.host.response.api.ApiResponse;
import lombok.Data;

@Data
public class CreateHostResponse {

    private Long id;

    public CreateHostResponse(Long id) {
        this.id = id;
    }
}
