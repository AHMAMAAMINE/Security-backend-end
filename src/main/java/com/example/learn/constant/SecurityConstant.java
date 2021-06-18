package com.example.learn.constant;

public class SecurityConstant{
    public static final long EXPIRATION_TIME=432_000_000;//expressed by miliseconds
    public static final String TOKEN_PREFIX= "Bearer ";
    public static final String JWT_TOKEN_HEADER= "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED= "Token cannot be verified";
    public static final String GET_ARRAYS_LLC= "Get Arrays,LLC";
    public static final String GET_ARRAYS_ADMINISTRATION= "User Management Portal";
    public static final String AUTHORITIES= "authorities";
    public static final String FORBIDDEN_MESSAGE= "You need to connect to get to this page";
    public static final String ACCESS_DENIED_MESSAGE="you don't have the access for this command";
    public static final String OPTIONS_HTTP_MEHOD="OPTIONS";
    public static final String[] PUBLIC_URL={"/user/login","/user/register","/user/resetpassword/**","/user/image/**"};
}
