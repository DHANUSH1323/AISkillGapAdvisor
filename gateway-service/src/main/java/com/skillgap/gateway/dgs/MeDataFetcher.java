package com.skillgap.gateway.dgs;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.context.DgsContext;
import com.netflix.graphql.dgs.internal.DgsWebMvcRequestData;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import com.skillgap.gateway.external.UserServiceCaller;

import java.util.Map;

@DgsComponent
public class MeDataFetcher {

    private final UserServiceCaller userServiceCaller;

    @Autowired
    public MeDataFetcher(UserServiceCaller userServiceCaller) {
        this.userServiceCaller = userServiceCaller;
    }

    @DgsQuery
    public Map<String, Object> me(DataFetchingEnvironment env) {

        DgsWebMvcRequestData requestData =
                (DgsWebMvcRequestData) DgsContext.getRequestData(env);

        if (requestData == null) {
            throw new RuntimeException("No HTTP request available");
        }

        String authHeader = requestData.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Authorization header");
        }

        String token = authHeader.substring("Bearer ".length());

        Map<String, Object> response = userServiceCaller.getMe(token);

        return Map.of(
                "id", response.get("sub"),
                "username", response.get("username")
        );
    }
}