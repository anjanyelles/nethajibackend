package com.nethaji.Enums;

public enum Grade {
    O("Outstanding", 10.0),
    A_PLUS("Excellent", 9.0),
    A("Very Good", 8.0),
    B_PLUS("Good", 7.0),
    B("Above Average", 6.0),
    C("Average", 5.0),
    P("Pass", 4.0),
    F("Fail", 0.0);

    private final String description;
    private final Double gradePoint;

    Grade(String description, Double gradePoint) {
        this.description = description;
        this.gradePoint = gradePoint;
    }

    public String getDescription() {
        return description;
    }

    public Double getGradePoint() {
        return gradePoint;
    }

    public static Grade getGradeByPercentage(Double percentage) {
        if (percentage >= 90) return O;
        if (percentage >= 80) return A_PLUS;
        if (percentage >= 70) return A;
        if (percentage >= 60) return B_PLUS;
        if (percentage >= 50) return B;
        if (percentage >= 45) return C;
        if (percentage >= 40) return P;
        return F;
    }
}

