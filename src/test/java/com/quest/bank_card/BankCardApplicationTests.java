package com.quest.bank_card;

import com.quest.bank_card.containers.PostgreSQLTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.liquibase.enabled=false")
public abstract class BankCardApplicationTests extends PostgreSQLTestContainer {}
