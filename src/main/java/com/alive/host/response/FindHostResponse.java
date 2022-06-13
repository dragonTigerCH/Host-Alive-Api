package com.alive.host.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FindHostResponse {

    private Long id;
    private String name;
    private String ip;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private boolean isAlive;
    private LocalDateTime createdDateAlive;

}
