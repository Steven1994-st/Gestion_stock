package com.gestion.utils;


import java.util.List;

/** Class that represent the Json Web Token that a client will receive
 *
 */
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String login;
    private List<String> roles;
    private Object user;

    /** Default constructor
     */
    public JwtResponse() {
    }

    /** Class constructor
     *
     * @param accessToken a token
     * @param login a login
     * @param roles a list of roles
     */
    public JwtResponse(String accessToken, String login, List<String> roles) {
        this.token = accessToken;
        this.login = login;
        this.roles = roles;
    }

    /** Constructor with all argument
     *
     * @param accessToken a token
     * @param login a login
     * @param roles a list of roles
     * @param user an user
     */
    public JwtResponse(String accessToken, String login, List<String> roles, Object user) {
        this.token = accessToken;
        this.login = login;
        this.roles = roles;
        this.user = user;
    }

    /** Get the accessToken
     *
     * @return the token
     */
    public String getAccessToken() {
        return token;
    }

    /** Get the login
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /** Set the login
     *
     * @param login another login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /** Get the user
     *
     * @return the user
     */
    public Object getUser() {
        return user;
    }

    /** Set the user
     *
     * @param user a user
     */
    public void setUser(Object user) {
        this.user = user;
    }

    /** Get the roles
     *
     * @return the roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /** Set the access token
     *
     * @param accessToken access token
     */
    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    /** Get the token type
     *
     * @return the type
     */
    public String getTokenType() {
        return type;
    }

    /** Set the token type
     *
     * @param tokenType a type of token
     */
    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }
}

