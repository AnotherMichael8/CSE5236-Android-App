package com.example.cse5236mobileapp

import com.example.cse5236mobileapp.model.Game
import org.junit.Assert.assertEquals
import org.junit.Test


class GameUnitTest {
    @Test
    fun testGameWinnerBlowout() {
        val expected = "0"

        val game = Game (
            teamOne = "0",
            teamTwo = "1",
            teamOneScore = 26,
            teamTwoScore = 10,
            round = 1
        )

        val actual = game.getGameWinner()

        assertEquals(expected, actual)
    }

    @Test
    fun testGameWinnerClose() {
        val expected = "1"

        val game = Game (
            teamOne = "0",
            teamTwo = "1",
            teamOneScore = 53,
            teamTwoScore = 54,
            round = 1
        )

        val actual = game.getGameWinner()

        assertEquals(expected, actual)
    }

    @Test
    fun testGameWinnerHigh() {
        val expected = "0"

        val game = Game (
            teamOne = "0",
            teamTwo = "1",
            teamOneScore = 178,
            teamTwoScore = 162,
            round = 1
        )

        val actual = game.getGameWinner()

        assertEquals(expected, actual)
    }

    @Test
    fun getSemifinalNameTourneyFour() {
        val expected = "Semifinal"

        val game = Game (
            teamOne = "0",
            teamTwo = "1",
            teamOneScore = 107,
            teamTwoScore = 98,
            round = 1
        )

        val actual = game.getRoundName(2)

        assertEquals(expected, actual)
    }

    @Test
    fun getQuarterfinalTourneySixteen() {
        val expected = "Quarterfinal"

        val game = Game (
            round = 3
        )

        val actual = game.getRoundName(5)

        assertEquals(expected, actual)
    }

    @Test
    fun getRoundOneTourneyEight() {
        val expected = "Round 1"

        val game = Game (
            round = 1
        )

        val actual = game.getRoundName(4)

        assertEquals(expected, actual)
    }


    @Test
    fun getRoundTwoTourneySixteen() {
        val expected = "Round 2"

        val game = Game (
            round = 2
        )

        val actual = game.getRoundName(5)

        assertEquals(expected, actual)
    }

    @Test
    fun getFinalTourneyEight() {
        val expected = "Final"

        val game = Game (
            round = 3
        )

        val actual = game.getRoundName(3)

        assertEquals(expected, actual)
    }
}