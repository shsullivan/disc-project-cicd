package com.sullivan.disc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Shawn Sullivan
 * CEN 3024C-31774
 * July 8, 2025
 * This DTO class allows database login information supplied by the user to be passed from the GUI to the backend and
 * used to initialize the standalone database.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbLoginRequestDTO {

    // Attributes
    public String host;
    public String port;
    public String username;
    public String password;
}
