package com.example.taskmate.data

data class CourseItem(
    val id: Int,
    val title: String,
    val day: String,
    val time: String,
    val room: String,
    val lecturer: String = "Dr. John Doe, M.Cs."
)

data class TaskItem(
    val id: Int,
    val title: String,
    val course: String,
    val daysLeft: String,
    val dueDate: String,
    val status: String = "In Progress",
    val description: String = "Complete the exercise assigned in class and upload the report before the deadline."
)

object DummyData {
    // -------------------------------------------------------------------------
    // 1. DATA KHUSUS TAB "COURSE LIST" (6 item PAPB D sesuai Figma)
    // -------------------------------------------------------------------------
    val courses = listOf(
        CourseItem(
            id = 1,
            title = "PAPB D",
            day = "Tuesday",
            time = "Tuesday, 09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 2,
            title = "PAPB D",
            day = "Tuesday",
            time = "Tuesday, 09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 3,
            title = "PAPB D",
            day = "Tuesday",
            time = "Tuesday, 09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 4,
            title = "PAPB D",
            day = "Tuesday",
            time = "Tuesday, 09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 5,
            title = "PAPB D",
            day = "Tuesday",
            time = "Tuesday, 09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 6,
            title = "PAPB D",
            day = "Tuesday",
            time = "Tuesday, 09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        )
    )

    // -------------------------------------------------------------------------
    // 2. DATA KHUSUS "CLASS SCHEDULE" & "YOUR SCHEDULE" (Monday & Tuesday)
    // -------------------------------------------------------------------------
    val schedules = listOf(
        // Monday (2 Kelas)
        CourseItem(
            id = 101,
            title = "JST D",
            day = "Monday",
            time = "Monday, 10:30 AM - 12:10 PM",
            room = "Room G1.2",
            lecturer = "Prof. Agus Widodo"
        ),
        CourseItem(
            id = 102,
            title = "Keamanan Jaringan A",
            day = "Monday",
            time = "Monday, 12:30 PM - 03:15 PM",
            room = "Room F4.6",
            lecturer = "Ir. Tri Astoto, M.Kom."
        ),

        // Tuesday (3 Kelas)
        CourseItem(
            id = 103,
            title = "JST D",
            day = "Tuesday",
            time = "Tuesday, 07:00 AM - 08:40 AM",
            room = "Room F4.2",
            lecturer = "Prof. Agus Widodo"
        ),
        CourseItem(
            id = 104,
            title = "PAPB D",
            day = "Tuesday",
            time = "Tuesday, 09:35 AM - 11:15 AM",
            room = "Room RV1.3",
            lecturer = "Dr. Eng. Herman Tolle, S.T., M.T."
        ),
        CourseItem(
            id = 105,
            title = "Komputer Grafis D",
            day = "Tuesday",
            time = "Tuesday, 03:20 PM - 05:00 PM",
            room = "Room F3.11",
            lecturer = "Sabriansyah Rizqika, S.T., M.Eng."
        )
    )

    // -------------------------------------------------------------------------
    // 3. DATA KHUSUS TAB "TASKS LIST" & "HOME SCREEN TASKS" (6 item Tugas)
    // -------------------------------------------------------------------------
    val tasks = listOf(
        TaskItem(
            id = 1,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Kerjakan latihan soal bab 1-3 pada modul praktikum."
        ),
        TaskItem(
            id = 2,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Implementasikan algoritma klasifikasi menggunakan dataset A."
        ),
        TaskItem(
            id = 3,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Buat rangkuman paper jurnal terkait arsitektur jaringan."
        ),
        TaskItem(
            id = 4,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Selesaikan laporan bab perancangan antarmuka pengguna."
        ),
        TaskItem(
            id = 5,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Studi kasus analisis performa server lokal."
        ),
        TaskItem(
            id = 6,
            title = "Assignment 1: abcd",
            course = "Course A",
            daysLeft = "7 Days left",
            dueDate = "Due Friday, Oct 16\n11:59 PM",
            status = "In Progress",
            description = "Presentasi progres proyek akhir aplikasi mobile."
        )
    )
}