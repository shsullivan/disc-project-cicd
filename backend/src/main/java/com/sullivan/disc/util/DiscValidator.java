package com.sullivan.disc.util;

/**
 * <h1>Class: DiscValidator</h1>
 * @author Shawn Sullivan
 * CEN 3024C-31774
 * July 18, 2025
 * The DiscValidator class contains custom written validation methods used to ensure that discs uploaded to the
 * system via a text file meet all validation requirements of the DB system before being passed to the repository
 * for record creation. Utilized in the DiscService class specifically by the importFromTextFile() method.
 */

import java.math.BigDecimal;

public final class DiscValidator {

    /**
     * This class is strictly for utility. To protect against instantiation, the constructor must be made private
     */
    private DiscValidator() {

    }

    /**
     * This helper method is used to validate all String type entity attributes that have an associated maximum length
     * @param input input String generated from the provided text field
     * @param fieldName name of entity attribute being evaluated. Used to generate custom error messages that provide
     * validation failure traceability
     * @param maxLength maximum allowable length for the String attribute being validated
     * @return String to be stored in a variable that will later be passed to the disc constructor
     * @throws IllegalArgumentException with custom message describing validation failure
     */
    private static String validateTextField(String input, String fieldName, int maxLength) {
        if (input == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        } else if (input.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        } else if (input.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " cannot be longer than " + maxLength + " characters");
        }
        return input;
    }
    // Helper method to validate text fields that do not have a max length restriction
    // currently only used once, but created for scalability
    private static String validateNotEmpty(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return input;
    }

    /**
     * Condition attribute must be a positive number. This method is used to ensure that Condition attributes
     * generated from text file upload are positive integers.
     * @param input input String generated from the provided text file
     * @param fieldName name of entity attribute being evaluated. Used to generate error messages
     * @return int to be stored in a variable that will late be passed to the disc constructor
     * @throws IllegalArgumentException with custom message if validation fails
     */
    public static int validatePositiveInt(String input, String fieldName) {
        try {
            int value = Integer.parseInt(input.trim());
            if (value <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0");
            }
            return value;
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    /**
     * msrp attribute must be a positive BigDecimal to avoid DB errors. This method is used to ensure that
     * msrp attributes generated from text file upload are positive BigDecimals
     * @param input input String generated from the uploaded text file
     * @param fieldName name of entity attribute being evaluated. Used to generate error messages for easier
     * failure traceability
     * @return BigDecimal to be stored in a variable that will later be passed to the Disc constructor
     * @throws IllegalArgumentException with custom message if validation fails
     */
    public static BigDecimal validatePositiveBigDecimal(String input, String fieldName) {
        try {
            BigDecimal value = BigDecimal.valueOf(Double.parseDouble(input.trim()));
            if (value.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0");
            }
            return value;
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid value");
        }
    }

    /**
     * The sold and returned entity attributes must be a boolean value to avoid DB errors. This method is used
     * to ensure that sold and returned attributes generated from text file upload are "true" or "false"
     * @param input input String generated from the uploaded text file
     * @param fieldName name of entity attribute being evaluated. Used to generate error messages for easier
     * upload failure traceability
     * @return boolean to be stored in a variable that will later be passed to the Disc constructor
     * @throws IllegalArgumentException with custom message if validation fails
     */
    public static boolean validateBooleanInput(String input, String fieldName) {
        input = input.trim().toLowerCase();
        if (input.equals("true") || input.equals("false")) {
            return Boolean.parseBoolean(input);
        } else {
            throw new IllegalArgumentException(fieldName + " must be true or false");
        }
    }

    /**
     * Uses the validateTextField method with a max length of 20 characters to validate the manufacturer
     * entity attribute
     * @param input input String generated from the uploaded text file
     * @return String to be stored in a variable that will later be passed to the Manufacturer constructor
     */
    public static String validateManufacturer(String input) {
        return validateTextField(input, "Manufacturer", 20);
    }

    /**
     * Uses the validateTextField method with a max length of 20 characters to validate the mold
     * entity attribute
     * @param input input String generated from the uploaded text file
     * @return String to be stored in a variable that will later be passed to the Mold constructor
     */
    public static String validateMold(String input) {
        return validateTextField(input, "Mold", 20);
    }

    /**
     * Uses the validateTextField method with a max length of 20 characters to validate the plastic
     * entity attribute
     * @param input input String generated from the uploaded text file
     * @return String to be stored in a variable that will later be passed to the Disc constructor
     */
    public static String validatePlastic(String input) {
        return validateTextField(input, "Plastic", 20);
    }

    /**
     * Uses the validateTextField method with a max length of 20 characters to validate the color
     * entity attribute
     * @param input input String generated from the uploaded text file
     * @return String to be stored in a variable that will later be passed to the Disc constructor
     */
    public static String validateColor(String input) {
        return validateTextField(input, "Color", 20);
    }

    /**
     * The condition entity attribute must be a positive number but also must be between 1 and 10. This method
     * ensures that the condition attribute uploaded from a text file meets those criteria
     * @param input input String generated from the uploaded text file
     * @return int to be stored in a variable that will later be passed to the Disc constructor
     * @throws IllegalArgumentException with custom message if validation fails
     */
    public static int validateCondition(String input) {
        int condition  = validatePositiveInt(input, "Condition");

        if (condition < 1 || condition > 10) {
            throw new IllegalArgumentException("Condition must be between 1 and 10");
        }
        return condition;
    }

    /**
     * Uses the validateNotEmpty method to ensure that the description entity attribute provided from an
     * uploaded text file is not empty (no maximum length for description)
     * @param input input String generated from the uploaded text file
     * @return String to be stored in a variable that will later be passed to the Disc constructor
     */
    public static String validateDescription(String input) {
        return validateNotEmpty(input, "Description");
    }

    /**
     * Uses the validateTextField method with a max length of 40 characters to validate all attributes of
     * the contact object entity attribute
     * @param input input String generated from the uploaded text file
     * @return String to be stored in a variable that will later be passed to the Contact constructor
     */
    public static String validateContactName(String input) {
        return validateTextField(input, "Contact Name", 40);
    }

    /**
     * The contactPhone must be provided with a length of 10 characters that must all be digits. This method
     * ensures that the contactPhone provided by the uploaded text file meets those constraints
     * @param input input String generated from the uploaded text file
     * @return String to be stored in a variable that will later be passed to the Contact constructor
     * @throws IllegalArgumentException with custom message if validation fails
     */
    public static String validateContactPhone(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Contact Phone cannot be empty");
        } else if (input.length() != 10) {
            throw new IllegalArgumentException("Contact Phone must be 10 digits");
        } else if (!input.matches("\\d{10}")) {
            throw new IllegalArgumentException("Contact Phone must contain only digits");
        }
        return input;
    }

    /**
     * Uses the validateTextField method with a max length of 50 characters to validate the foundAt
     * entity attribute
     * @param input input String generated from the uploaded text file
     * @return String to be stored in a variable that will later be passed to the Disc constructor
     */
    public static String validateFoundAt(String input) {
        return validateTextField(input, "Found at location", 50);
    }
}

