package com.itheima;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class LogTest {
    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void testLog(){
        logger.info("{} : 开始计算...", LocalDateTime.now());

        int sum = IntStream.of(1, 5, 3, 2, 1, 4, 5, 4, 6, 7, 4, 34, 2, 23).sum();

        logger.debug("计算结果为: {}", sum);
        logger.info("{} : 结束计算...", LocalDateTime.now());
    }

// ... existing code ...
}
