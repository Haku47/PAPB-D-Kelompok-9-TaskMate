package com.example.taskmate.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

interface AppStrings {
    // Bottom Nav & General Titles
    val navHome: String
    val navCourses: String
    val navTasks: String
    val navSchedule: String
    val navProfile: String

    // Home Screen
    val greeting: String
    val activeTasksNotice: (Int) -> String
    val sectionTasks: String
    val sectionSchedule: String

    // Course List
    val courseListTitle: String
    val searchCoursePlaceholder: String
    val addCourseFab: String

    // Tasks List
    val tasksListTitle: String
    val searchTaskPlaceholder: String
    val addTaskFab: String
    val taskBadge: String
    fun formatDaysLeft(days: String): String

    // Schedule
    val classScheduleTitle: String
    val dayMonday: String
    val dayTuesday: String
    val dayWednesday: String
    val dayThursday: String
    val dayFriday: String
    val daySaturday: String
    val daySunday: String
    fun translateDay(day: String): String
    fun translateStatus(status: String): String

    // Profile
    val profileTitle: String
    val tasksFinishedLabel: String
    val activeCoursesLabel: String

    // Settings
    val settingsTitle: String
    val displayModeLabel: String
    val modeSystem: String
    val modeLight: String
    val modeDark: String
    val fontSizeLabel: String
    val fontSizeSmall: String
    val fontSizeRegular: String
    val fontSizeLarge: String
    val languageLabel: String
    val langEnglish: String
    val langIndonesian: String
    val logoutButton: String

    // Detail & CRUD Screens
    val taskDetailTitle: String
    val markAsFinished: String
    val taskTitleLabel: String
    val courseLabel: String
    val dueDateLabel: String
    val taskStatusLabel: String
    val descriptionLabel: String
    val editTaskDetailTitle: String
    val saveChanges: String

    val courseDetailTitle: String
    val courseNameLabel: String
    val courseTitleLabel: String
    val lecturerNameLabel: String
    val scheduleInfoLabel: String
    val editCourseTitle: String
    val addCourseTitle: String
    val addTaskTitle: String

    val dayFieldLabel: String
    val startTimeLabel: String
    val endTimeLabel: String
    val roomLabel: String
    val locationLabel: String
    val selectTimeTitle: String
    val switchToKeyboardMode: String
    val switchToDialMode: String
    val timeFormat12Hour: String
    val timeFormat24Hour: String
    val anotherSessionButton: String
    val sessionHeader: (Int) -> String
    val removeSession: String

    // Dialogs
    val dialogChangesSaved: String
    val dialogCourseAdded: String
    val dialogTaskAdded: String
    val dialogTaskFinished: String
    val dialogTaskDeleted: String
    val dialogCourseDeleted: String
    val deleteTaskConfirmTitle: String
    val deleteTaskConfirmMessage: (String) -> String
    val deleteCourseConfirmTitle: String
    val deleteCourseConfirmMessage: (String) -> String
    val deleteButton: String
    val cancelButton: String
    val dialogOk: String
    val dialogClose: String

    // Auth Screens
    val welcomeTitle: String
    val loginTitle: String
    val usernameLabel: String
    val enterUsernamePlaceholder: String
    val emailLabel: String
    val enterEmailPlaceholder: String
    val passwordLabel: String
    val enterPasswordPlaceholder: String
    val forgotPassword: String
    val loginButton: String
    val dontHaveAccount: String
    val signUpLink: String

    val signUpTitle: String
    val confirmPasswordLabel: String
    val enterConfirmPasswordPlaceholder: String
    val signUpButton: String
    val alreadyHaveAccount: String
    val loginLink: String

    val forgotPasswordTitle: String
    val forgotPasswordDesc: String
    val sendEmailButton: String

    val emailSentTitle: String
    val emailSentDesc: String
    val backToLoginButton: String

    val changePasswordTitle: String
    val newPasswordLabel: String
    val enterNewPasswordPlaceholder: String
    val confirmNewPasswordLabel: String
    val enterConfirmNewPasswordPlaceholder: String
    val savePasswordButton: String
}

object EnglishStrings : AppStrings {
    override val navHome = "Home"
    override val navCourses = "Courses"
    override val navTasks = "Tasks"
    override val navSchedule = "Schedule"
    override val navProfile = "Profile"

    override val greeting = "Hello, Mad!"
    override val activeTasksNotice: (Int) -> String = { count -> "You've got $count active tasks!" }
    override val sectionTasks = "Tasks"
    override val sectionSchedule = "Your Schedule"

    override val courseListTitle = "Course List"
    override val searchCoursePlaceholder = "Search course..."
    override val addCourseFab = "Add Course"

    override val tasksListTitle = "Tasks List"
    override val searchTaskPlaceholder = "Search task..."
    override val addTaskFab = "Add Task"
    override val taskBadge = "TASK"
    override fun formatDaysLeft(days: String): String = days

    override val classScheduleTitle = "Class Schedule"
    override val dayMonday = "Monday"
    override val dayTuesday = "Tuesday"
    override val dayWednesday = "Wednesday"
    override val dayThursday = "Thursday"
    override val dayFriday = "Friday"
    override val daySaturday = "Saturday"
    override val daySunday = "Sunday"
    override fun translateDay(day: String): String = when (day.lowercase()) {
        "monday", "senin" -> "Monday"
        "tuesday", "selasa" -> "Tuesday"
        "wednesday", "rabu" -> "Wednesday"
        "thursday", "kamis" -> "Thursday"
        "friday", "jumat" -> "Friday"
        "saturday", "sabtu" -> "Saturday"
        "sunday", "minggu" -> "Sunday"
        else -> day
    }
    override fun translateStatus(status: String): String = status

    override val profileTitle = "Profile"
    override val tasksFinishedLabel = "Tasks Finished"
    override val activeCoursesLabel = "Active Courses"

    override val settingsTitle = "Settings"
    override val displayModeLabel = "Display Mode"
    override val modeSystem = "System"
    override val modeLight = "Light"
    override val modeDark = "Dark"
    override val fontSizeLabel = "Font Size"
    override val fontSizeSmall = "Small"
    override val fontSizeRegular = "Regular"
    override val fontSizeLarge = "Large"
    override val languageLabel = "Language"
    override val langEnglish = "English"
    override val langIndonesian = "Indonesian"
    override val logoutButton = "Log Out"

    override val taskDetailTitle = "Task Detail"
    override val markAsFinished = "Mark as Finished"
    override val taskTitleLabel = "Task Title"
    override val courseLabel = "Course"
    override val dueDateLabel = "Due Date"
    override val taskStatusLabel = "Task Status"
    override val descriptionLabel = "Description"
    override val editTaskDetailTitle = "Edit Task Detail"
    override val saveChanges = "Save Changes"

    override val courseDetailTitle = "Course Detail"
    override val courseNameLabel = "Course Name"
    override val courseTitleLabel = "Course Title"
    override val lecturerNameLabel = "Lecturer Name"
    override val scheduleInfoLabel = "Schedule Information"
    override val editCourseTitle = "Edit Course"
    override val addCourseTitle = "Add Course"
    override val addTaskTitle = "Add Task"

    override val dayFieldLabel = "Day"
    override val startTimeLabel = "Start Time"
    override val endTimeLabel = "End Time"
    override val roomLabel = "Room"
    override val locationLabel = "Location"
    override val selectTimeTitle = "Select Time"
    override val switchToKeyboardMode = "Switch to keyboard input"
    override val switchToDialMode = "Switch to clock dial"
    override val timeFormat12Hour = "12-Hour (AM/PM)"
    override val timeFormat24Hour = "24-Hour"
    override val anotherSessionButton = "+ Another Session"
    override val sessionHeader: (Int) -> String = { index -> "Session $index" }
    override val removeSession = "Remove Session"

    override val dialogChangesSaved = "Changes saved successfully!"
    override val dialogCourseAdded = "Course added successfully!"
    override val dialogTaskAdded = "Task added successfully!"
    override val dialogTaskFinished = "Task marked as finished successfully!"
    override val dialogTaskDeleted = "Task deleted successfully!"
    override val dialogCourseDeleted = "Course deleted successfully!"
    override val deleteTaskConfirmTitle = "Delete Task?"
    override val deleteTaskConfirmMessage: (String) -> String = { title -> "Are you sure you want to delete '$title'?" }
    override val deleteCourseConfirmTitle = "Delete Course?"
    override val deleteCourseConfirmMessage: (String) -> String = { title -> "Are you sure you want to delete '$title'?" }
    override val deleteButton = "Delete"
    override val cancelButton = "Cancel"
    override val dialogOk = "OK"
    override val dialogClose = "Close"

    override val welcomeTitle = "Welcome to TaskMate"
    override val loginTitle = "Login"
    override val usernameLabel = "Username"
    override val enterUsernamePlaceholder = "Enter your username"
    override val emailLabel = "Email"
    override val enterEmailPlaceholder = "Enter your email"
    override val passwordLabel = "Password"
    override val enterPasswordPlaceholder = "Enter your password"
    override val forgotPassword = "Forgot Password"
    override val loginButton = "Login"
    override val dontHaveAccount = "Don't have an account? "
    override val signUpLink = "Sign Up"

    override val signUpTitle = "Sign Up"
    override val confirmPasswordLabel = "Confirm Password"
    override val enterConfirmPasswordPlaceholder = "Enter your password again"
    override val signUpButton = "Sign Up"
    override val alreadyHaveAccount = "Already have an account? "
    override val loginLink = "Login"

    override val forgotPasswordTitle = "Forgot Password"
    override val forgotPasswordDesc = "Enter your registered email below to receive password reset instruction"
    override val sendEmailButton = "Send Email"

    override val emailSentTitle = "Email Sent"
    override val emailSentDesc = "We have sent password reset instructions to your email."
    override val backToLoginButton = "Back to Login"

    override val changePasswordTitle = "Change Password"
    override val newPasswordLabel = "New Password"
    override val enterNewPasswordPlaceholder = "Enter new password"
    override val confirmNewPasswordLabel = "Confirm New Password"
    override val enterConfirmNewPasswordPlaceholder = "Enter new password again"
    override val savePasswordButton = "Save"
}

object IndonesianStrings : AppStrings {
    override val navHome = "Beranda"
    override val navCourses = "Mata Kuliah"
    override val navTasks = "Tugas"
    override val navSchedule = "Jadwal"
    override val navProfile = "Profil"

    override val greeting = "Halo, Mad!"
    override val activeTasksNotice: (Int) -> String = { count -> "Kamu punya $count tugas aktif!" }
    override val sectionTasks = "Tugas"
    override val sectionSchedule = "Jadwal Kamu"

    override val courseListTitle = "Daftar Mata Kuliah"
    override val searchCoursePlaceholder = "Cari mata kuliah..."
    override val addCourseFab = "Tambah Mata Kuliah"

    override val tasksListTitle = "Daftar Tugas"
    override val searchTaskPlaceholder = "Cari tugas..."
    override val addTaskFab = "Tambah Tugas"
    override val taskBadge = "TUGAS"
    override fun formatDaysLeft(days: String): String = when {
        days.contains("days left", ignoreCase = true) -> days.replace("days left", "hari lagi", ignoreCase = true)
        days.contains("day left", ignoreCase = true) -> days.replace("day left", "hari lagi", ignoreCase = true)
        else -> days
    }

    override val classScheduleTitle = "Jadwal Kuliah"
    override val dayMonday = "Senin"
    override val dayTuesday = "Selasa"
    override val dayWednesday = "Rabu"
    override val dayThursday = "Kamis"
    override val dayFriday = "Jumat"
    override val daySaturday = "Sabtu"
    override val daySunday = "Minggu"
    override fun translateDay(day: String): String = when (day.lowercase()) {
        "monday", "senin" -> "Senin"
        "tuesday", "selasa" -> "Selasa"
        "wednesday", "rabu" -> "Rabu"
        "thursday", "kamis" -> "Kamis"
        "friday", "jumat" -> "Jumat"
        "saturday", "sabtu" -> "Sabtu"
        "sunday", "minggu" -> "Minggu"
        else -> day
    }
    override fun translateStatus(status: String): String = when (status.lowercase()) {
        "in progress" -> "Sedang Berjalan"
        "completed", "finished" -> "Selesai"
        "pending" -> "Menunggu"
        else -> status
    }

    override val profileTitle = "Profil"
    override val tasksFinishedLabel = "Tugas Selesai"
    override val activeCoursesLabel = "Mata Kuliah Aktif"

    override val settingsTitle = "Pengaturan"
    override val displayModeLabel = "Mode Tampilan"
    override val modeSystem = "Sistem"
    override val modeLight = "Terang"
    override val modeDark = "Gelap"
    override val fontSizeLabel = "Ukuran Font"
    override val fontSizeSmall = "Kecil"
    override val fontSizeRegular = "Reguler"
    override val fontSizeLarge = "Besar"
    override val languageLabel = "Bahasa"
    override val langEnglish = "English"
    override val langIndonesian = "Indonesia"
    override val logoutButton = "Keluar"

    override val taskDetailTitle = "Detail Tugas"
    override val markAsFinished = "Tandai Selesai"
    override val taskTitleLabel = "Judul Tugas"
    override val courseLabel = "Mata Kuliah"
    override val dueDateLabel = "Tenggat Waktu"
    override val taskStatusLabel = "Status Tugas"
    override val descriptionLabel = "Deskripsi"
    override val editTaskDetailTitle = "Edit Detail Tugas"
    override val saveChanges = "Simpan Perubahan"

    override val courseDetailTitle = "Detail Mata Kuliah"
    override val courseNameLabel = "Nama Mata Kuliah"
    override val courseTitleLabel = "Nama Mata Kuliah"
    override val lecturerNameLabel = "Nama Dosen"
    override val scheduleInfoLabel = "Informasi Jadwal"
    override val editCourseTitle = "Edit Mata Kuliah"
    override val addCourseTitle = "Tambah Mata Kuliah"
    override val addTaskTitle = "Tambah Tugas"

    override val dayFieldLabel = "Hari"
    override val startTimeLabel = "Waktu Mulai"
    override val endTimeLabel = "Waktu Selesai"
    override val roomLabel = "Ruangan"
    override val locationLabel = "Lokasi"
    override val selectTimeTitle = "Pilih Jam"
    override val switchToKeyboardMode = "Beralih ke ketik manual"
    override val switchToDialMode = "Kembali ke mode jarum"
    override val timeFormat12Hour = "12 Jam (AM/PM)"
    override val timeFormat24Hour = "24 Jam"
    override val anotherSessionButton = "+ Sesi Lainnya"
    override val sessionHeader: (Int) -> String = { index -> "Sesi $index" }
    override val removeSession = "Hapus Sesi"

    override val dialogChangesSaved = "Perubahan berhasil disimpan!"
    override val dialogCourseAdded = "Mata kuliah berhasil ditambahkan!"
    override val dialogTaskAdded = "Tugas berhasil ditambahkan!"
    override val dialogTaskFinished = "Tugas berhasil ditandai selesai!"
    override val dialogTaskDeleted = "Tugas berhasil dihapus!"
    override val dialogCourseDeleted = "Mata kuliah berhasil dihapus!"
    override val deleteTaskConfirmTitle = "Hapus Tugas?"
    override val deleteTaskConfirmMessage: (String) -> String = { title -> "Apakah Anda yakin ingin menghapus '$title'?" }
    override val deleteCourseConfirmTitle = "Hapus Mata Kuliah?"
    override val deleteCourseConfirmMessage: (String) -> String = { title -> "Apakah Anda yakin ingin menghapus '$title'?" }
    override val deleteButton = "Hapus"
    override val cancelButton = "Batal"
    override val dialogOk = "OK"
    override val dialogClose = "Tutup"

    override val welcomeTitle = "Selamat Datang di TaskMate"
    override val loginTitle = "Masuk"
    override val usernameLabel = "Nama Pengguna"
    override val enterUsernamePlaceholder = "Masukkan nama pengguna Anda"
    override val emailLabel = "Email"
    override val enterEmailPlaceholder = "Masukkan email Anda"
    override val passwordLabel = "Kata Sandi"
    override val enterPasswordPlaceholder = "Masukkan kata sandi Anda"
    override val forgotPassword = "Lupa Kata Sandi"
    override val loginButton = "Masuk"
    override val dontHaveAccount = "Belum punya akun? "
    override val signUpLink = "Daftar"

    override val signUpTitle = "Daftar"
    override val confirmPasswordLabel = "Konfirmasi Kata Sandi"
    override val enterConfirmPasswordPlaceholder = "Masukkan kata sandi lagi"
    override val signUpButton = "Daftar"
    override val alreadyHaveAccount = "Sudah punya akun? "
    override val loginLink = "Masuk"

    override val forgotPasswordTitle = "Lupa Kata Sandi"
    override val forgotPasswordDesc = "Masukkan email terdaftar Anda di bawah untuk menerima petunjuk reset kata sandi"
    override val sendEmailButton = "Kirim Email"

    override val emailSentTitle = "Email Terkirim"
    override val emailSentDesc = "Kami telah mengirimkan instruksi reset kata sandi ke email Anda."
    override val backToLoginButton = "Kembali ke Halaman Masuk"

    override val changePasswordTitle = "Ubah Kata Sandi"
    override val newPasswordLabel = "Kata Sandi Baru"
    override val enterNewPasswordPlaceholder = "Masukkan kata sandi baru"
    override val confirmNewPasswordLabel = "Konfirmasi Kata Sandi Baru"
    override val enterConfirmNewPasswordPlaceholder = "Masukkan kata sandi baru lagi"
    override val savePasswordButton = "Simpan"
}

val LocalAppStrings = staticCompositionLocalOf<AppStrings> { EnglishStrings }

val strings: AppStrings
    @Composable
    get() = LocalAppStrings.current
