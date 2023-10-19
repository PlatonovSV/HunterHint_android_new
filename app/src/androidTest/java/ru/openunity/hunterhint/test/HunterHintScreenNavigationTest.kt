package ru.openunity.hunterhint.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.openunity.hunterhint.ui.HunterHintApp
import ru.openunity.hunterhint.ui.HunterHintScreen
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.TestTag

class HunterHintScreenNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            HunterHintApp(navController = navController)
        }
    }

    @Test
    fun hunterHintNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(HunterHintScreen.Search.name)
    }

    @Test
    fun hunterHintNavHost_verifyBackNavigationNotShownOnSearchScreen() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun hunterHintNavHost_clickFirstGround_navigateToDetailedPage() {
        clickFirstGroundOnSearchScreen()
        navController.assertCurrentRouteName(HunterHintScreen.Detailed.name)
    }

    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }

    private fun clickFirstGroundOnSearchScreen() {
        composeTestRule.onAllNodesWithTag(TestTag.GroundInfo.name)[0].performClick()
    }

    @Test
    fun hunterHintNavHost_clickBackOnDetailedScreen_navigatesToSearchScreen() {
        clickFirstGroundOnSearchScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(HunterHintScreen.Search.name)
    }
}

