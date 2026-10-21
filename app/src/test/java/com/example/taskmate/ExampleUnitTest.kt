package com.example.taskmate

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
import com.example.taskmate.ui.theme.FontSizeMode
import com.example.taskmate.ui.theme.ThemeState
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun fontSizeModes_haveCorrectScales() {
        assertEquals(0.85f, FontSizeMode.SMALL.scale, 0.001f)
        assertEquals(1.0f, FontSizeMode.REGULAR.scale, 0.001f)
        assertEquals(1.15f, FontSizeMode.LARGE.scale, 0.001f)
    }

    @Test
    fun themeState_fontSizeUpdate() {
        ThemeState.setFontSize(FontSizeMode.SMALL)
        assertEquals(FontSizeMode.SMALL, ThemeState.fontSizeMode)

        ThemeState.setFontSize(FontSizeMode.LARGE)
        assertEquals(FontSizeMode.LARGE, ThemeState.fontSizeMode)

        ThemeState.setFontSize(FontSizeMode.REGULAR)
        assertEquals(FontSizeMode.REGULAR, ThemeState.fontSizeMode)
    }

    @Test
    fun densityScaling_modifiesPixelSizes() {
        val baseDensity = Density(density = 2.0f, fontScale = 1.0f)
        val largeDensity = Density(density = 2.0f, fontScale = 1.15f)
        val smallDensity = Density(density = 2.0f, fontScale = 0.85f)

        val basePx = with(baseDensity) { 20.sp.toPx() }
        val largePx = with(largeDensity) { 20.sp.toPx() }
        val smallPx = with(smallDensity) { 20.sp.toPx() }

        assertTrue(largePx > basePx)
        assertTrue(smallPx < basePx)
        assertTrue(largePx in (basePx * 1.05f)..(basePx * 1.20f))
        assertTrue(smallPx in (basePx * 0.80f)..(basePx * 0.95f))
    }

    @Test
    fun courseTimeHelper_detectsAndTransformsTimeModes() {
        assertEquals(com.example.taskmate.ui.components.TimeFormatMode.AM, com.example.taskmate.ui.components.CourseTimeHelper.detectMode("09:35 AM"))
        assertEquals(com.example.taskmate.ui.components.TimeFormatMode.PM, com.example.taskmate.ui.components.CourseTimeHelper.detectMode("03:15 pm"))
        assertEquals(com.example.taskmate.ui.components.TimeFormatMode.TWENTY_FOUR_HOUR, com.example.taskmate.ui.components.CourseTimeHelper.detectMode("14:30"))
        assertEquals(com.example.taskmate.ui.components.TimeFormatMode.TWENTY_FOUR_HOUR, com.example.taskmate.ui.components.CourseTimeHelper.detectMode("09.35"))

        // Mode Switching
        assertEquals("09:35 AM", com.example.taskmate.ui.components.CourseTimeHelper.setTimeMode("09.35", com.example.taskmate.ui.components.TimeFormatMode.AM))
        assertEquals("09:35 PM", com.example.taskmate.ui.components.CourseTimeHelper.setTimeMode("09:35 AM", com.example.taskmate.ui.components.TimeFormatMode.PM))
        assertEquals("09:35", com.example.taskmate.ui.components.CourseTimeHelper.setTimeMode("09:35 PM", com.example.taskmate.ui.components.TimeFormatMode.TWENTY_FOUR_HOUR))

        // Range Parsing and Building
        val parsed = com.example.taskmate.ui.components.CourseTimeHelper.parseTimeRange("Monday, 09:35 AM - 11:15 AM")
        assertEquals("09:35 AM", parsed.first)
        assertEquals("11:15 AM", parsed.second)

        val built = com.example.taskmate.ui.components.CourseTimeHelper.buildTimeRange("09:35 AM", "11:15 AM")
        assertEquals("09:35 AM - 11:15 AM", built)
    }

    @Test
    fun dummyData_matchesFigmaDesign() {
        // Course List matches Figma (6 items of PAPB D)
        assertEquals(6, com.example.taskmate.data.DummyData.courses.size)
        assertTrue(com.example.taskmate.data.DummyData.courses.all { it.title == "PAPB D" })
        assertTrue(com.example.taskmate.data.DummyData.courses.all { it.room == "Room RV1.3" })
        assertTrue(com.example.taskmate.data.DummyData.courses.all { it.time == "09:35 AM - 11:15 AM" })

        // Tasks matches Figma (5 active tasks of Assignment 1: abcd)
        assertEquals(5, com.example.taskmate.data.DummyData.tasks.size)
        assertTrue(com.example.taskmate.data.DummyData.tasks.all { it.title == "Assignment 1: abcd" })
        assertTrue(com.example.taskmate.data.DummyData.tasks.all { it.course == "Course A" })
        assertTrue(com.example.taskmate.data.DummyData.tasks.all { it.daysLeft == "7 Days left" })
        assertTrue(com.example.taskmate.data.DummyData.tasks.all { it.status == "In Progress" })

        // Tuesday schedules matches Figma Home Screen
        val tuesdaySchedules = com.example.taskmate.data.DummyData.schedules.filter { it.day == "Tuesday" }
        assertEquals(3, tuesdaySchedules.size)
        assertEquals("JST D", tuesdaySchedules[0].title)
        assertEquals("Room F4.2", tuesdaySchedules[0].room)
        assertEquals("PAPB D", tuesdaySchedules[1].title)
        assertEquals("Room RV1.3", tuesdaySchedules[1].room)
        assertEquals("Komputer Grafis D", tuesdaySchedules[2].title)
        assertEquals("Room F3.1", tuesdaySchedules[2].room)
    }

    @Test
    fun dummyData_deleteOperations() {
        val testCourse = com.example.taskmate.data.CourseItem(
            id = 9999,
            title = "Temporary Test Course",
            day = "Friday",
            time = "08:00 AM - 10:00 AM",
            room = "Room Test"
        )
        com.example.taskmate.data.DummyData.addCourse(testCourse)
        assertNotNull(com.example.taskmate.data.DummyData.getCourseById(9999))

        val courseDeleted = com.example.taskmate.data.DummyData.deleteCourse(9999)
        assertTrue(courseDeleted)
        assertNull(com.example.taskmate.data.DummyData.getCourseById(9999))

        val testTask = com.example.taskmate.data.TaskItem(
            id = 8888,
            title = "Temporary Test Task",
            course = "Course Test",
            daysLeft = "1 Day left",
            dueDate = "Due Tomorrow"
        )
        com.example.taskmate.data.DummyData.addTask(testTask)
        assertNotNull(com.example.taskmate.data.DummyData.getTaskById(8888))

        val taskDeleted = com.example.taskmate.data.DummyData.deleteTask(8888)
        assertTrue(taskDeleted)
        assertNull(com.example.taskmate.data.DummyData.getTaskById(8888))
    }

    @Test
    fun courseSessions_multiSessionAddUpdateDelete() {
        val kiCourse = com.example.taskmate.data.CourseItem(
            id = 7777,
            title = "KI",
            day = "Monday",
            time = "08:45 AM - 10:25 AM",
            room = "Room F4.2",
            lecturer = "Dosen KI",
            sessions = listOf(
                com.example.taskmate.data.CourseSession(day = "Senin", time = "08:45 AM - 10:25 AM", room = "Room F4.2"),
                com.example.taskmate.data.CourseSession(day = "Selasa", time = "08:45 AM - 10:25 AM", room = "Room F4.2"),
                com.example.taskmate.data.CourseSession(day = "Rabu", time = "10:30 AM - 12:10 PM", room = "Room F4.2")
            )
        )
        com.example.taskmate.data.DummyData.addCourse(kiCourse)

        val retrieved = com.example.taskmate.data.DummyData.getCourseById(7777)
        assertNotNull(retrieved)
        assertEquals(3, retrieved!!.allSessions.size)

        // Verify schedules entries are properly normalized to Monday, Tuesday, Wednesday
        val kiSchedules = com.example.taskmate.data.DummyData.schedules.filter { it.id == 7777 }
        assertEquals(3, kiSchedules.size)
        assertTrue(kiSchedules.any { it.day == "Monday" })
        assertTrue(kiSchedules.any { it.day == "Tuesday" })
        assertTrue(kiSchedules.any { it.day == "Wednesday" })

        // Update to 2 sessions
        val updatedKiCourse = kiCourse.copy(
            sessions = listOf(
                com.example.taskmate.data.CourseSession(day = "Monday", time = "08:45 AM - 10:25 AM", room = "Room F4.2"),
                com.example.taskmate.data.CourseSession(day = "Tuesday", time = "08:45 AM - 10:25 AM", room = "Room F4.2")
            )
        )
        com.example.taskmate.data.DummyData.updateCourse(updatedKiCourse)
        val updatedKiSchedules = com.example.taskmate.data.DummyData.schedules.filter { it.id == 7777 }
        assertEquals(2, updatedKiSchedules.size)
        assertFalse(updatedKiSchedules.any { it.day == "Wednesday" })

        // Delete course
        com.example.taskmate.data.DummyData.deleteCourse(7777)
        assertEquals(0, com.example.taskmate.data.DummyData.schedules.count { it.id == 7777 })
        assertNull(com.example.taskmate.data.DummyData.getCourseById(7777))
    }

    @Test
    fun courseTimeHelper_normalizesDays() {
        assertEquals("Monday", com.example.taskmate.ui.components.CourseTimeHelper.normalizeDay("senin"))
        assertEquals("Tuesday", com.example.taskmate.ui.components.CourseTimeHelper.normalizeDay("Selasa"))
        assertEquals("Wednesday", com.example.taskmate.ui.components.CourseTimeHelper.normalizeDay("rabu"))
        assertEquals("Thursday", com.example.taskmate.ui.components.CourseTimeHelper.normalizeDay("kamis"))
        assertEquals("Friday", com.example.taskmate.ui.components.CourseTimeHelper.normalizeDay("jumat"))
        assertEquals("Saturday", com.example.taskmate.ui.components.CourseTimeHelper.normalizeDay("sabtu"))
        assertEquals("Sunday", com.example.taskmate.ui.components.CourseTimeHelper.normalizeDay("minggu"))
        assertEquals("Friday", com.example.taskmate.ui.components.CourseTimeHelper.normalizeDay("Friday"))
    }

    @Test
    fun courseTimeHelper_parseHourMinuteAndFormatTime() {
        // Parse tests
        assertEquals(Pair(9, 35), com.example.taskmate.ui.components.CourseTimeHelper.parseHourMinute("09:35 AM"))
        assertEquals(Pair(21, 35), com.example.taskmate.ui.components.CourseTimeHelper.parseHourMinute("09:35 PM"))
        assertEquals(Pair(0, 0), com.example.taskmate.ui.components.CourseTimeHelper.parseHourMinute("12:00 AM"))
        assertEquals(Pair(12, 10), com.example.taskmate.ui.components.CourseTimeHelper.parseHourMinute("12:10 PM"))
        assertEquals(Pair(14, 10), com.example.taskmate.ui.components.CourseTimeHelper.parseHourMinute("14:10"))
        assertEquals(Pair(8, 45), com.example.taskmate.ui.components.CourseTimeHelper.parseHourMinute("08.45"))
        assertEquals(Pair(9, 0), com.example.taskmate.ui.components.CourseTimeHelper.parseHourMinute(""))

        // Format tests (12h AM/PM)
        assertEquals("09:35 AM", com.example.taskmate.ui.components.CourseTimeHelper.formatTime(9, 35, false))
        assertEquals("09:35 PM", com.example.taskmate.ui.components.CourseTimeHelper.formatTime(21, 35, false))
        assertEquals("12:00 AM", com.example.taskmate.ui.components.CourseTimeHelper.formatTime(0, 0, false))
        assertEquals("12:10 PM", com.example.taskmate.ui.components.CourseTimeHelper.formatTime(12, 10, false))
        assertEquals("02:10 PM", com.example.taskmate.ui.components.CourseTimeHelper.formatTime(14, 10, false))

        // Format tests (24h)
        assertEquals("09:35", com.example.taskmate.ui.components.CourseTimeHelper.formatTime(9, 35, true))
        assertEquals("14:10", com.example.taskmate.ui.components.CourseTimeHelper.formatTime(14, 10, true))
        assertEquals("08:45", com.example.taskmate.ui.components.CourseTimeHelper.formatTime(8, 45, true))
    }
}