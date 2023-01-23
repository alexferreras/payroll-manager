package com.payroll.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PayrollData {
    private String fullName;
    private String email;
    private String position;
    private double healthDiscountAmount;
    private double socialDiscountAmount;
    private double taxesDiscountAmount;
    private double otherDiscountAmount;
    private double grossSalary;
    private double grossPayment;
    private double netPayment;
    private String period;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private double totalDiscount;

    public double getTotalDiscount() {
        return healthDiscountAmount + socialDiscountAmount + taxesDiscountAmount +
            otherDiscountAmount;
    }

}
