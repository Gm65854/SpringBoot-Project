package org.springframework.cloud.openfeign;

public @interface FeignClient {

    String name();

    String url();

}
