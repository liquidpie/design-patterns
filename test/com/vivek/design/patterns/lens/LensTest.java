package com.vivek.design.patterns.lens;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class LensTest {

    @Test
    void testLenses() {
        // Basic Lenses
        Lens<Movie, String> movieTitleLens = Lens.of(s -> s.title, Movie::withTitle);

        Lens<Show, Movie> showMovieLens = Lens.of(s -> s.movie, Show::withMovie);
        Lens<Show, LocalDateTime> showDateTimeLens = Lens.of(s -> s.dateTime, Show::withDateTime);

        Lens<Booking, Show> bookingShowLens = Lens.of(s -> s.show, Booking::withShow);
        Lens<Booking, Integer> bookingSeatsLens = Lens.of(s -> s.numSeats, Booking::withNumSeats);

        Lens<User, String> userNameLens = Lens.of(s -> s.username, User::withUsername);
        Lens<User, String> userEmailLens = Lens.of(s -> s.emailId, User::withEmailId);
        Lens<User, Booking> userBookingLens = Lens.of(s -> s.booking, User::withBooking);

        // Lens composition
        Lens<User, String> changeMovieName = userBookingLens.andThen(bookingShowLens).andThen(showMovieLens).andThen(movieTitleLens);
        Lens<User, LocalDateTime> changeShowDateTime = userBookingLens.andThen(bookingShowLens).andThen(showDateTimeLens);
        Lens<User, Integer> changeBookingSeats = userBookingLens.andThen(bookingSeatsLens);

        // Immutable Structure
        User user = new User("johndoe", "jdoe@example.com", new Booking(new Show(new Movie("foo bar"), LocalDateTime.now()), 2));

        // Mutations through lenses
        user = changeMovieName.mod(user, s -> "street race");
        Assertions.assertEquals("street race", user.booking.show.movie.title);

        user = changeShowDateTime.mod(user, s -> LocalDateTime.of(2021, 10, 14, 5, 30));
        Assertions.assertEquals("2021-10-14T05:30", user.booking.show.dateTime.toString());

        user = changeBookingSeats.mod(user, s -> 3);
        Assertions.assertEquals(3, user.booking.numSeats);

    }

    public static class User {
        public final String username;
        public final String emailId;
        public final Booking booking;

        public User(String username, String emailId, Booking booking) {
            this.username = username;
            this.emailId = emailId;
            this.booking = booking;
        }

        public User withUsername(String u) {
            return new User(u, emailId, booking);
        }

        public User withEmailId(String e) {
            return new User(username, e, booking);
        }

        public User withBooking(Booking b) {
            return new User(username, emailId, b);
        }

        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", emailId='" + emailId + '\'' +
                    ", booking=" + booking +
                    '}';
        }
    }

    public static class Booking {

        public final Show show;
        public final Integer numSeats;

        public Booking(Show show, Integer numSeats) {
            this.show = show;
            this.numSeats = numSeats;
        }

        public Booking withShow(Show s) {
            return new Booking(s, numSeats);
        }

        public Booking withNumSeats(Integer n) {
            return new Booking(show, n);
        }

        @Override
        public String toString() {
            return "Booking{" +
                    "show=" + show +
                    ", numSeats=" + numSeats +
                    '}';
        }
    }

    public static class Show {

        public final Movie movie;
        public final LocalDateTime dateTime;

        public Show(Movie movie, LocalDateTime dateTime) {
            this.movie = movie;
            this.dateTime = dateTime;
        }

        public Show withMovie(Movie m) {
            return new Show(m, dateTime);
        }

        public Show withDateTime(LocalDateTime dt) {
            return new Show(movie, dt);
        }

        @Override
        public String toString() {
            return "Show{" +
                    "movie=" + movie +
                    ", dateTime=" + dateTime +
                    '}';
        }
    }

    public static class Movie {

        public final String title;

        public Movie(String title) {
            this.title = title;
        }

        public Movie withTitle(String t) {
            return new Movie(t);
        }

        @Override
        public String toString() {
            return "Movie{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

}
