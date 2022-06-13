package com.alive.host.repository;

import com.alive.host.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host,Long> {

    Host findByName(String name);

    Host findByIp(String ip);
}
