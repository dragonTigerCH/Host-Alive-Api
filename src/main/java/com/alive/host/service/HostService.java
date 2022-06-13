package com.alive.host.service;

import com.alive.host.domain.Host;
import com.alive.host.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HostService {

    private final HostRepository hostRepository;
    @Transactional
    public Long join(Host host) {
        return hostRepository.save(host).getId();
    }

    public boolean isHostLimit() {
        List<Host> hostList = hostRepository.findAll();
        return hostList.size() < 100;
    }
    public Host findOne(Long id){
        return hostRepository.findById(id).orElse(null);
    }
    public List<Host> findHosts(){
        return hostRepository.findAll();
    }
    @Transactional
    public void update(Long id, String name, String ip) {
        Host host = findOne(id);
        host.setName(name);
        host.setIp(ip);
    }
    @Transactional
    public void update(Long id, boolean alive, LocalDateTime aliveTime) {
        Host host = findOne(id);
        host.setAlive(alive);
        host.setCreatedDateAlive(aliveTime);
    }

    public void deleteHost(Long id) {
        hostRepository.deleteById(id);
    }

    public boolean findByName(String name) {
        Host host = hostRepository.findByName(name);
        if (host == null){
            return true;
        } else {
            return false;
        }
    }

    public boolean findByIp(String ip) {
        Host host = hostRepository.findByIp(ip);
        if (host == null){
            return true;
        } else {
            return false;
        }
    }
}
