package at.noahb.springdonations.payload.request;

import java.time.LocalDate;

public record NewDonationRequestBody(int amount, LocalDate date) {

}
