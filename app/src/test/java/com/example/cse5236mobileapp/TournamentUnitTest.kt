package com.example.cse5236mobileapp

import com.example.cse5236mobileapp.model.Tournament
import org.junit.Assert.assertEquals
import org.junit.Test

class TournamentUnitTest {
    @Test
    fun testTourneyFullFour() {
        val expected = true

        val tourney = Tournament (
            players = listOf("jyablok@gmail.com", "zyablok@gmail.com", "test@gmail.com", "yum@gmail.com"),
            numberPlayers = "4"
        )

        val actual = tourney.isTournamentFull()

        assertEquals(expected, actual)
    }

    @Test
    fun testTourneyNotFullEight() {
        val expected = false

        val tourney = Tournament (
            players = listOf("jyablok@gmail.com", "zyablok@gmail.com", "test@gmail.com",
                "yum@gmail.com", "pop@gmail.com", "lol@gmail.com", "junit@gmail.com"),
            numberPlayers = "8"
        )

        val actual = tourney.isTournamentFull()

        assertEquals(expected, actual)
    }

    @Test
    fun testTourneyFullEight() {
        val expected = true

        val tourney = Tournament (
            players = listOf("jyablok@gmail.com", "zyablok@gmail.com", "test@gmail.com",
                "yum@gmail.com", "pop@gmail.com", "lol@gmail.com", "junit@gmail.com",
                "lastperson@gmail.com"),
            numberPlayers = "8"
        )

        val actual = tourney.isTournamentFull()

        assertEquals(expected, actual)
    }

    @Test
    fun userIsPlayer() {
        val expected = true

        val tourney = Tournament (
            players = listOf("jyablok@gmail.com", "zyablok@gmail.com", "test@gmail.com", "yum@gmail.com"),
            numberPlayers = "8"
        )

        val actual = tourney.isUserAPlayer("jyablok@gmail.com")

        assertEquals(expected, actual)
    }

    @Test
    fun userIsPlayerCapital() {
        val expected = false

        val tourney = Tournament (
            players = listOf("jyablok@gmail.com", "zyablok@gmail.com", "test@gmail.com", "yum@gmail.com"),
            numberPlayers = "8"
        )

        val actual = tourney.isUserAPlayer("Jyablok@gmail.com")

        assertEquals(expected, actual)
    }

    @Test
    fun userIsPlayerTypo() {
        val expected = false

        val tourney = Tournament (
            players = listOf("jyablok@gmail.com", "zyablok@gmail.com", "yum@gmail.com"),
            numberPlayers = "8"
        )

        val actual = tourney.isUserAPlayer("zyablock@gmail.com")

        assertEquals(expected, actual)
    }

    @Test
    fun userIsOnlyPlayer() {
        val expected = true

        val tourney = Tournament (
            players = listOf("yum@gmail.com"),
            numberPlayers = "4"
        )

        val actual = tourney.isUserAPlayer("yum@gmail.com")

        assertEquals(expected, actual)
    }

    @Test
    fun uniqueJoinCodes() {
        val expected = false
        val tourneyOne = Tournament()
        tourneyOne.generateJoinCode()

        val tourneyTwo = Tournament()
        tourneyTwo.generateJoinCode()

        val actual = tourneyOne.joinCode == tourneyTwo.joinCode

        assertEquals(expected, actual)
    }

    @Test
    fun correctNumRoundsFourPlayers() {
        val expected = 2

        val tourney = Tournament(
            numberPlayers = "4"
        )

        val actual = tourney.getNumberOfRounds()

        assertEquals(expected, actual)
    }

    @Test
    fun correctNumRoundsEightPlayers() {
        val expected = 3

        val tourney = Tournament(
            numberPlayers = "8"
        )

        val actual = tourney.getNumberOfRounds()

        assertEquals(expected, actual)
    }

    @Test
    fun correctNumRoundsSixteenPlayers() {
        val expected = 4

        val tourney = Tournament(
            numberPlayers = "16"
        )

        val actual = tourney.getNumberOfRounds()

        assertEquals(expected, actual)
    }
}