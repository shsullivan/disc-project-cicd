package com.sullivan.disc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <h1>ImportResultDTO</h1>
 * @author Shawn Sullivan
 * CEN 3024C - 31774
 * July 18, 2025
 * This class is a specialty DTO utilized by the ImportFromTextFile() method in the DiscService class to provide
 * information to the front end to be displayed to the user whether lines of a text file were uploaded to the
 * DB
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportResultDTO {

    public List<String> successes;
    public List<String> failures;

}
