package com.unimelbCoder.melbcode.models.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitmqPropertiesDTO {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String virtualhost;

    private Integer poolSize;

    private Boolean switchFlag;

}
