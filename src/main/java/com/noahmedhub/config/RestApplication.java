package com.noahmedhub.config;

import com.noahmedhub.api.CorsFilter; // 👈 导入 CorsFilter 所在的包
import com.noahmedhub.resources.FaqResource;
import com.noahmedhub.resources.UserResource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(UserResource.class);
        resources.add(FaqResource.class);
        resources.add(CorsFilter.class); // 👈 注册全局跨域过滤器
        return resources;
    }
}