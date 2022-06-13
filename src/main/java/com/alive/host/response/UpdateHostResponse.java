package com.alive.host.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateHostResponse {

    private Long id;
    private String name;
    private String ip;

}
