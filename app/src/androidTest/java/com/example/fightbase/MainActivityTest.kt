package com.example.fightbase

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.hasText

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fighterListAppears() {
        // Wait until fighter list is loaded
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Daniel Cormier").fetchSemanticsNodes().isNotEmpty()
        }

        // Assert one of the fighters is shown
        composeTestRule.onNodeWithText("Jon Jones").assertIsDisplayed()
    }

    @Test
    fun navigateToFighterDetailScreen() {
        // Wait for fighter list
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Jon Jones").fetchSemanticsNodes().isNotEmpty()
        }

        // Tap the fighter item
        composeTestRule.onNodeWithText("Jon Jones").performClick()

        // Wait until the nationality text is loaded
        composeTestRule.waitUntil(timeoutMillis = 8000) {
            val expectedNationalityLabel = composeTestRule.activity.getString(
                R.string.nationality_format,
                "USA"
            )
            composeTestRule.onAllNodes(hasText(expectedNationalityLabel, substring = true)
            )
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Assert it is visible
        val expectedNationalityLabel = composeTestRule.activity.getString(
            R.string.nationality_format,
            "USA"
        )
        composeTestRule.onNode(hasText(expectedNationalityLabel, substring = true)
        )
            .assertIsDisplayed()
    }
}
