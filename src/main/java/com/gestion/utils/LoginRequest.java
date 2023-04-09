package com.gestion.utils;


import jakarta.validation.constraints.NotBlank;

/** Class that represent a login request
 *
 */
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    /** Get the username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /** Set the username
     *
     * @param username another username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Get the password
     *
     * @return the username
     */
    public String getPassword() {
        return password;
    }

    /** Set the password
     *
     * @param password another password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
