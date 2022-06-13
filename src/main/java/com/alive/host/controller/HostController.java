package com.alive.host.controller;

import com.alive.host.domain.Host;
import com.alive.host.dto.HostSaveDto;
import com.alive.host.dto.HostUpdateDto;
import com.alive.host.response.*;
import com.alive.host.service.HostService;
import com.alive.host.status.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HostController {

    private final HostService hostService;

    @GetMapping("/hosts/{id}")
    public ResponseEntity<DefaultResponse<FindHostResponse>> findHost(@PathVariable("id") Long id) {

        Host findHost = hostService.findOne(id);
        //조회 id 없는경우
        if (findHost == null) {
            DefaultResponse<FindHostResponse> response = new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_HOST);
            return new ResponseEntity<DefaultResponse<FindHostResponse>>(response, HttpStatus.BAD_REQUEST);
        }

        InetAddress inetAddress = null;
        boolean reachable = false;
        LocalDateTime reachableTime = null;
        try {
            reachable = InetAddress.getByName(findHost.getIp()).isReachable(1000);
            if (reachable) {
                reachableTime = LocalDateTime.now();
                hostService.update(id,reachable,reachableTime);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FindHostResponse findHostResponse = new FindHostResponse(
                findHost.getId(), findHost.getName(), findHost.getIp(),
                findHost.getCreatedDate(), findHost.getLastModifiedDate(), reachable, reachableTime);

        DefaultResponse<FindHostResponse> response = new DefaultResponse<>(StatusCode.OK, ResponseMessage.READ_HOST, findHostResponse);

        return new ResponseEntity<DefaultResponse<FindHostResponse>>(response, HttpStatus.OK);
    }

    @GetMapping("/hosts")
    public ResponseEntity<DefaultResponse<List<FindHostResponse>>> findHost() {

        List<Host> hostList = hostService.findHosts();
        List<FindHostResponse> findHostResponses = hostList.stream().map(host -> new FindHostResponse(host.getId(), host.getName()
                , host.getIp(),host.getCreatedDate(),host.getLastModifiedDate(),host.isAlive(),host.getCreatedDateAlive()))
                .collect(Collectors.toList());

        DefaultResponse<List<FindHostResponse>> response = new DefaultResponse<>(StatusCode.OK, ResponseMessage.READ_HOST, findHostResponses);
        return new ResponseEntity<DefaultResponse<List<FindHostResponse>>>(response, HttpStatus.OK);
    }
    @GetMapping("/hosts/{name}/name-exists")
    public ResponseEntity nameCheck(@PathVariable("name")String name){
        boolean result = hostService.findByName(name);
        if (result){
            DefaultResponse<Void> response = new DefaultResponse<>(StatusCode.OK, ResponseMessage.POSSIBLE_NAME);
            return new ResponseEntity<DefaultResponse<Void>>(response,HttpStatus.OK);
        } else {
            DefaultResponse<Void> response = new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.DUPLICATE_NAME);
            return new ResponseEntity<DefaultResponse<Void>>(response,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/hosts/{ip}/ip-exists")
    public ResponseEntity ipCheck(@PathVariable("ip")String ip){
        boolean result = hostService.findByIp(ip);
        if (result){
            DefaultResponse<Void> response = new DefaultResponse<>(StatusCode.OK, ResponseMessage.POSSIBLE_IP);
            return new ResponseEntity<DefaultResponse<Void>>(response,HttpStatus.OK);
        } else {
            DefaultResponse<Void> response = new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.DUPLICATE_IP);
            return new ResponseEntity<DefaultResponse<Void>>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/hosts")
    public ResponseEntity<DefaultResponse<CreateHostResponse>> saveHost(@RequestBody HostSaveDto hostSaveDto) {

        boolean isHostLimit = hostService.isHostLimit();
        CreateHostResponse createHostResponse = null;
        Long id = 0l;
        //true 호스트의 수가 100개 이하 등록가능
        if (isHostLimit) {
            Host host = new Host(hostSaveDto.getName(), hostSaveDto.getIp());
            id = hostService.join(host);
            createHostResponse = new CreateHostResponse(id);
            DefaultResponse<CreateHostResponse> response = new DefaultResponse<>(StatusCode.CREATED, ResponseMessage.CREATED_HOST, createHostResponse);
            return new ResponseEntity<DefaultResponse<CreateHostResponse>>(response, HttpStatus.CREATED);
        } else {
            //false 호스트의 수가 100개 초과 등록불가
            createHostResponse = new CreateHostResponse(id);
            DefaultResponse<CreateHostResponse> response = new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.CANT_REGISTER, createHostResponse);
            return new ResponseEntity<DefaultResponse<CreateHostResponse>>(response, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/hosts/{id}")
    public ResponseEntity<DefaultResponse<UpdateHostResponse>> updateHost(@PathVariable("id") Long id, @RequestBody HostUpdateDto hostUpdateDto) {
        Host updateHost = hostService.findOne(id);
        if (updateHost == null) {
            DefaultResponse<UpdateHostResponse> response = new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_HOST);
            return new ResponseEntity<DefaultResponse<UpdateHostResponse>>(response, HttpStatus.BAD_REQUEST);
        }
        hostService.update(id, hostUpdateDto.getName(), hostUpdateDto.getIp());
        Host findHost = hostService.findOne(id);
        UpdateHostResponse updateHostResponse = new UpdateHostResponse(findHost.getId(), findHost.getName(), findHost.getIp());
        DefaultResponse<UpdateHostResponse> response = new DefaultResponse<>(StatusCode.CREATED, ResponseMessage.UPDATE_HOST, updateHostResponse);
        return new ResponseEntity<DefaultResponse<UpdateHostResponse>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/hosts/{id}")
    public ResponseEntity<DefaultResponse<Void>> deleteHost(@PathVariable("id") Long id) {
        Host deleteHost = hostService.findOne(id);
        if (deleteHost == null) {
            DefaultResponse<Void> response = new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_HOST);
            return new ResponseEntity<DefaultResponse<Void>>(response, HttpStatus.BAD_REQUEST);
        }
        hostService.deleteHost(id);
        DefaultResponse<Void> response = new DefaultResponse<>(StatusCode.NO_CONTENT, ResponseMessage.DELETE_HOST);
        return new ResponseEntity<DefaultResponse<Void>>(response, HttpStatus.NO_CONTENT);
    }

}
