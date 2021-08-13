/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.demo.annotation.aop.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Eric Zhao
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    @SentinelResource(value = "test", blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
    public void test() {
        System.out.println("Test");
    }

    @Override
    @SentinelResource(value = "hello", fallback = "helloFallback")
    public String hello(long s) {
        if (s < 0) {
            throw new IllegalArgumentException("invalid arg");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("Hello at %s", simpleDateFormat.format(new Date()));
    }

    @Override
    @SentinelResource(value = "helloAnother", defaultFallback = "defaultFallback",
            exceptionsToIgnore = {IllegalStateException.class})
    public String helloAnother(String name) {
        if (name == null || "bad".equals(name)) {
            throw new IllegalArgumentException("oops");
        }
        if ("foo".equals(name)) {
            throw new IllegalStateException("oops");
        }
        return "Hello, " + name;
    }

    public String helloFallback(long s, Throwable ex) {
        // Do some log here.
        ex.printStackTrace();
        return "hello-被限流了 ";
    }

    public String defaultFallback() {
        System.out.println("Go to default fallback");
        return "default_fallback";
    }
}
