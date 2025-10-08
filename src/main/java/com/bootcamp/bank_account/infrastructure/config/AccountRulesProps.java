package com.bootcamp.bank_account.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AccountRulesProps.class)
@ConfigurationProperties(prefix = "account.rules")
@Data
public class AccountRulesProps {
}
