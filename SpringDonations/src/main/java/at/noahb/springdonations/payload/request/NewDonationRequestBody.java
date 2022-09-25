package at.noahb.springdonations.payload.request;

import at.noahb.springdonations.domain.Person;

import java.time.LocalDate;

public record NewDonationRequestBody(int amount, LocalDate date) {

}
