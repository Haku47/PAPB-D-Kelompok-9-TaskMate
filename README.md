<p align="center">
  <img src="app/src/main/res/drawable/ic_taskmate_logo.png" alt="TaskMate Logo" width="115" />
</p>

<h1 align="center">TaskMate</h1>

<p align="center">
  <b>A Modern, Cloud-Synchronized Student Task & Course Management Android Application</b><br>
  <i>Dirancang presisi berbasis Jetpack Compose, Material 3, Hybrid Storage Architecture, Room DB, WorkManager, dan Retrofit API</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026--37)-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android Platform" />
  <img src="https://img.shields.io/badge/Language-Kotlin%202.2-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/Backend-Firebase%20Auth%20%2B%20Firestore-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase" />
  <img src="https://img.shields.io/badge/Local%20DB-Room%20SQLite%20%2B%20DataStore-F4511E?style=for-the-badge&logo=sqlite&logoColor=white" alt="Room Database" />
  <img src="https://img.shields.io/badge/Unit%20Tests-23%2F23%20Passed-brightgreen?style=for-the-badge&logo=checkmarx&logoColor=white" alt="Tests" />
</p>

---

## 📌 Ringkasan Akademik

| Informasi Akademik | Detail Keterangan |
| :--- | :--- |
| **Mata Kuliah** | Pemrograman Aplikasi Perangkat Bergerak (PAPB) — Kelas D |
| **Kelompok** | Kelompok 9 |
| **Target Evaluasi** | Proyek Tugas Kelompok & Ujian Akhir Praktikum (UAP) |
| **Pola Arsitektur** | MVVM (Model-View-ViewModel) + Repository Pattern + Clean Architecture |
| **Penyimpanan Data** | **Hybrid Architecture**: Cloud Single Source of Truth + Room SQLite Offline-First Cache |
| **Cakupan Teori PAPB** | **Sangat Memuaskan (6 Teori Penuh)**: Activity, Navigation, Coroutines, ViewModel, Room DB, Retrofit API |

---

## 📋 Daftar Isi
1. [Tentang TaskMate](#-tentang-taskmate)
2. [Arsitektur Sistem (Hybrid Local + Cloud)](#-arsitektur-sistem-hybrid-local--cloud)
3. [Fitur Utama & Modul Aplikasi](#-fitur-utama--modul-aplikasi)
4. [Struktur Database & Keamanan Data](#-struktur-database--keamanan-data)
5. [Pohon Direktori Proyek](#-pohon-direktori-proyek)
6. [Teknologi & Dependensi](#-teknologi--dependensi)
7. [Panduan Instalasi & Menjalankan Proyek](#-panduan-instalasi--menjalankan-proyek)
8. [Hasil Pengujian Unit (Unit Tests)](#-hasil-pengujian-unit-unit-tests)
9. [Tim Pengembang (Kelompok 9)](#-tim-pengembang-kelompok-9)

---

## 💡 Tentang TaskMate

**TaskMate** adalah solusi manajemen jadwal perkuliahan dan tugas akademik mahasiswa yang dibangun secara modern dengan **Jetpack Compose (Material 3)**. Aplikasi ini dirancang untuk menjawab tantangan pengelolaan waktu mahasiswa melalui perpaduan sinkronisasi awan terpusat dan keandalan akses offline berbasis **Room Database**, **WorkManager**, dan **Retrofit REST API**.

### 🌟 Nilai Unggulan Aplikasi:
- 🌐 **Sinkronisasi Multi-Perangkat (Cloud)**: Akun pengguna terhubung langsung ke **Firebase Authentication** dan **Cloud Firestore**. Mahasiswa dapat login di perangkat smartphone mana pun dengan data profil yang selalu tersinkronisasi.
- ⚡ **Offline-First & Room SQLite DB**: Sesi aktif, data mata kuliah, dan daftar tugas tersimpan secara terpusat di **Room Database** (3 tabel: `users`, `courses`, `tasks`) dan **DataStore Preferences**.
- 🔔 **Pengingat Otomatis (WorkManager)**: Latar belakang aplikasi diperkuat dengan **WorkManager** yang secara otomatis mengecek deadline tugas dan mengirimkan notifikasi pengingat.
- 📰 **Motivasi Harian (Retrofit REST API)**: Terintegrasi dengan **Retrofit 2 HTTP Client** untuk mengambil data motivasi harian.
- 🎨 **Presisi Desain Figma**: Komponen antarmuka (Clock dial time picker, keyboard entry mode, date picker, kartu mata kuliah, dan dashboard statistik) dirancang presisi sesuai mockup UI/UX.
- 🔒 **Standar Keamanan Tinggi**: Tidak menyimpan password pengguna dalam bentuk plaintext di database lokal maupun cloud.

---

## 🏛️ Arsitektur Sistem (Hybrid Local + Cloud)

TaskMate memisahkan tanggung jawab kode secara terstruktur menggunakan prinsip **Clean Architecture** dan **MVVM Pattern**:

```
+-----------------------------------------------------------------------------------+
|                        Presentation Layer (Jetpack Compose)                       |
|   Screens (Login, SignUp, Home, Courses, Tasks, Schedule, Profile, Settings)      |
|   ViewModels (Auth, Course, Task, EditCourse, EditTask, ThemeState)               |
+-----------------------------------------------------------------------------------+
                                          |
                                          v
+-----------------------------------------------------------------------------------+
|                         Domain & Repository Layer (MVVM)                          |
|   AuthRepository       : Single Source of Truth Coordinator                       |
|   QuoteRepository      : Retrofit REST API Daily Motivation                       |
|   DummyData & Manager  : Room DB & Memory State Coordinator                       |
+-----------------------------------------------------------------------------------+
                                          |
    +-------------------------------------+-----------------------------------+
    |                                     |                                   |
    v                                     v                                   v
+-----------------------+     +-----------------------+     +-------------------+
|  Local Storage Layer  |     |   Background Layer    |     |    Cloud Layer    |
| - Room DB (SQLite)    |     | - WorkManager Worker  |     | - Firebase Auth   |
|   (User,Course,Task)  |     |   (Periodic Reminder) |     | - Cloud Firestore |
| - DataStore Prefs     |     +-----------------------+     | - Retrofit REST   |
+-----------------------+                                   +-------------------+
```

### Prinsip Keamanan & Privasi Data:
1. **Zero Plaintext Password**: Password hanya digunakan saat proses autentikasi ke Firebase Auth dan tidak pernah disimpan di storage lokal maupun dokumen Firestore.
2. **Firebase UID sebagai Kunci Utama**: Identitas unik pengguna menggunakan UID resmi dari Firebase Authentication.
3. **Reservasi Atomik Username**: Menjamin keunikan global nama pengguna melalui koleksi `usernames/{usernameLower}` sehingga tidak ada pengguna yang dapat mengambil username akun lain.

---

## 🚀 Fitur Utama & Modul Aplikasi

### 1. 🔐 Autentikasi & Akun Pengguna
- **Pendaftaran Akun (Sign Up)**: Input *Front Name*, *Back Name* (otomatis digabung menjadi *Full Name*), *Username*, *Email*, *Password*, dan *Confirm Password*. Dilengkapi validasi regex, deteksi duplikasi username/email, dan mekanisme rollback otomatis jika proses cloud terganggu.
- **Login Fleksibel**: Mahasiswa dapat masuk menggunakan **Email** maupun **Username**. Sistem secara otomatis meresolusi username ke email akun melalui cloud.
- **Lupa Password (Forgot Password)**: Pengiriman tautan reset password resmi ke email pengguna melalui Firebase Authentication.
- **Persistensi Sesi & Indikator Loading**: Sesi akun tersimpan di DataStore Preferences untuk auto-login saat aplikasi dibuka kembali.

### 2. 📚 Manajemen Mata Kuliah (Courses & Room DB)
- **Katalog Kuliah Sesuai Figma**: Menampilkan mata kuliah default (*PAPB D, Ruang RV1.3, Dosen Dr. Eng. Herman Tolle, S.T., M.T.*) yang tersimpan di Room SQLite DB (`CourseEntity`).
- **Multi-Session Scheduling**: Mendukung mata kuliah yang memiliki lebih dari satu sesi pertemuan dalam seminggu.
- **Interactive Time Picker**: Pemilihan jam mulai dan selesai kuliah menggunakan visual Jam Dial (Clock Face) maupun Input Papan Ketik (Keyboard), mendukung format 12-Jam (AM/PM) dan 24-Jam.
- **Operasi CRUD**: Tambah mata kuliah baru (*Add Course*), ubah data (*Edit Course*), dan hapus (*Delete Course*).

### 3. ✅ Manajemen Tugas (Tasks, WorkManager & Room DB)
- **Room SQLite Persistence**: Penyimpanan data tugas terikat pada `TaskEntity` dan `TaskDao`.
- **Background Task Worker (WorkManager)**: `TaskReminderWorker` mengecek deadline tugas perkuliahan secara teratur dan mengirim notifikasi lokal (*System Notification*).
- **Monitoring Tenggat Waktu**: Menampilkan sisa hari pengerjaan (*Days Left*), tanggal jatuh tempo (*Due Date*), status tugas (*In Progress* / *Completed*), dan deskripsi lengkap tugas.
- **Operasi CRUD**: Tambah tugas baru (*Add Task*), ubah rincian (*Edit Task*), dan hapus tugas (*Delete Task*).

### 4. 📅 Jadwal Mingguan & Kalender (Schedules)
- **Today Schedule**: Menampilkan jadwal kuliah hari ini secara cerdas pada dashboard Home Screen sesuai hari aktif.
- **Weekly Schedule**: Tampilan filter harian (Senin s/d Minggu) untuk perencanaan studi mahasiswa.

### 5. 🎨 Personalisasi & Retrofit API
- **Tema Terang & Gelap (Light / Dark Mode)**: Pengguna bebas memilih mode tampilan *System Default*, *Light Mode*, atau *Dark Mode* di SettingsScreen.
- **Daily Motivation (Retrofit REST API)**: Mengambil data motivasi harian yang diisi melalui Retrofit 2 HTTP Client.
- **Skala Ukuran Teks (Font Size Scaling)**: Opsi ukuran teks *Small (0.85x)*, *Regular (1.0x)*, atau *Large (1.15x)*.
- **Dwibahasa (Localization)**: Dukungan penuh dwibahasa (**Bahasa Indonesia** dan **English**) pada seluruh antarmuka.

---

## 🗄️ Struktur Database & Keamanan Data

### 1. Skema Database Lokal Room (`AppDatabase` Version 2)
- **Tabel `users` (`UserEntity`)**: Cache profil pengguna aman.
- **Tabel `courses` (`CourseEntity`)**: Menyimpan data mata kuliah dan sesi perkuliahan.
- **Tabel `tasks` (`TaskEntity`)**: Menyimpan data tugas, status, dan tanggal jatuh tempo.

### 2. Skema Cloud Firestore
```
firestore/
├── users/
│   └── {uid}/
│       ├── uid: string
│       ├── username: string
│       ├── email: string
│       ├── fullName: string
│       ├── createdAt: number (ms)
│       └── updatedAt: number (ms)
│
└── usernames/
    └── {usernameLower}/
        ├── uid: string
        └── email: string
```

---

## 📂 Pohon Direktori Proyek

```
TaskMate/
├── app/
│   ├── google-services.json              # Konfigurasi koneksi Firebase
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml       # Deklarasi izin INTERNET, NETWORK_STATE, & POST_NOTIFICATIONS
│       │   └── java/com/example/taskmate/
│       │       ├── MainActivity.kt       # Activity utama & inisialisasi tema
│       │       │
│       │       ├── data/
│       │       │   ├── DummyData.kt      # Mock data & Room DB Data Sync Coordinator
│       │       │   ├── UserManager.kt    # Adapter facade backward-compatible
│       │       │   ├── model/
│       │       │   │   └── UserAccount.kt# Domain model akun
│       │       │   ├── local/
│       │       │   │   ├── AppDatabase.kt# Room Database singleton (v2)
│       │       │   │   ├── dao/          # UserDao, CourseDao, TaskDao
│       │       │   │   └── entity/       # UserEntity, CourseEntity, TaskEntity
│       │       │   ├── remote/
│       │       │   │   ├── UserRemoteDataSource.kt # Firebase Auth & Firestore
│       │       │   │   └── api/          # QuoteApiService & QuoteRepository (Retrofit API)
│       │       │   └── repository/
│       │       │       └── AuthRepository.kt # Koordinator Hybrid SSOT
│       │       │
│       │       ├── worker/
│       │       │   └── TaskReminderWorker.kt # Background Worker pengingat tugas (WorkManager)
│       │       │
│       │       ├── navigation/
│       │       │   └── TaskMateNavGraph.kt # Grafik Navigasi Compose
│       │       │
│       │       └── ui/
│       │           ├── components/       # Komponen UI modular
│       │           ├── screens/          # Auth, Main, & Sub Screens
│       │           ├── theme/            # ThemeState, Colors, Localization
│       │           └── viewmodel/        # ViewModels untuk seluruh layar
│       │
│       └── test/java/com/example/taskmate/
│           └── ExampleUnitTest.kt        # 23 skenario uji unit otomatis (100% Pass)
│
├── build.gradle.kts                      # Konfigurasi Gradle root & dependencies
└── README.md                             # Dokumentasi resmi proyek
```

---

## 🛠️ Teknologi & Dependensi

| Kategori | Library / Framework | Versi | Peran Utama |
| :--- | :--- | :--- | :--- |
| **Language** | Kotlin | `2.2.10` | Bahasa pemrograman utama |
| **Build Tool** | Android Gradle Plugin (AGP) | `9.4.1` | Sistem kompilasi Android modern |
| **UI Kit** | Jetpack Compose (Material 3) | BoM `2026.02.01` | Deklaratif UI antarmuka responsif |
| **Navigation** | Navigation Compose | `2.8.5` | Manajemen alur navigasi antar-layar |
| **Cloud Auth & DB**| Firebase Auth & Firestore | BoM `33.7.0` | Autentikasi dan database terpusat cloud |
| **Local DB** | AndroidX Room Database | `2.7.0-alpha13` | Persistence SQLite 3 tabel (`users`, `courses`, `tasks`) |
| **Background Work**| WorkManager KTX | `2.9.1` | Latar belakang asynchronous task & notification worker |
| **HTTP REST API** | Retrofit 2 & Gson | `2.11.0` | Konsumsi Web API quote motivasi harian |
| **Session** | Jetpack DataStore Preferences | `1.1.1` | Penyimpanan token sesi pengguna |
| **Testing** | JUnit 4 + AndroidX Test | `4.13.2` | Pengujian logika dan validasi unit (23/23 Pass) |

---

## 💻 Panduan Instalasi & Menjalankan Proyek

### 1. Prasyarat Sistem
- **Android Studio**: Android Studio Koala / Ladybug / Meerkat ke atas.
- **JDK**: Eclipse Adoptium JDK 21 (`jdk-21.0.12.101-hotspot`).
- **Android OS**: Perangkat fisik atau emulator dengan Android 8.0 (API 26) ke atas.

### 2. Kloning & Kompilasi Proyek
```bash
git clone https://github.com/HakuCik/Taskmate.git
cd Taskmate
```

- **Menjalankan Rangkaian Unit Test**:
  ```bash
  ./gradlew test
  ```
- **Membuat File APK Debug Siap Pasang**:
  ```bash
  ./gradlew assembleDebug
  ```

---

## 🧪 Hasil Pengujian Unit (Unit Tests)

Pengujian otomatis dijalankan menggunakan JUnit 4 pada [`ExampleUnitTest.kt`](file:///D:/Project/Mobile/PAPB-D-Kelompok-9-TaskMate/app/src/test/java/com/example/taskmate/ExampleUnitTest.kt) dengan hasil **23/23 Lulus (100% Pass)**:

| No | Nama Skenario Uji | Domain Pengujian | Status |
| :---: | :--- | :--- | :---: |
| 1 | `addition_isCorrect` | Sanity Check JUnit Environment | ✅ PASS |
| 2 | `fontSizeModes_haveCorrectScales` | Verifikasi Skala Font (Small/Regular/Large) | ✅ PASS |
| 3 | `themeState_fontSizeUpdate` | Pembaruan Reaktif State Tipografi | ✅ PASS |
| 4 | `densityScaling_modifiesPixelSizes` | Konversi SP ke PX Berdasarkan Font Scale | ✅ PASS |
| 5 | `courseTimeHelper_detectsAndTransformsTimeModes` | Deteksi Format 12-Jam & 24-Jam Kuliah | ✅ PASS |
| 6 | `dummyData_matchesFigmaDesign` | Integritas 6 Kuliah & 5 Tugas Sesuai Figma | ✅ PASS |
| 7 | `dummyData_deleteOperations` | Validasi Penghapusan Item Kuliah & Tugas | ✅ PASS |
| 8 | `courseSessions_multiSessionAddUpdateDelete` | Penanganan Jadwal Kuliah Multi-Sesi | ✅ PASS |
| 9 | `courseTimeHelper_normalizesDays` | Normalisasi Penamaan Hari Dwibahasa | ✅ PASS |
| 10 | `courseTimeHelper_parseHourMinuteAndFormatTime` | Parsing Jam & Menit serta Rekonstruksi Waktu | ✅ PASS |
| 11 | `addCourseViewModel_validationAndCombinedInputs` | Validasi Formulir & Penggabungan Nama Dosen | ✅ PASS |
| 12 | `addTaskViewModel_validationAndCombinedDueDate` | Validasi Tugas & Penggabungan Date/Time | ✅ PASS |
| 13 | `dummyData_jsonSerializationLossless` | Serialisasi Lossless JSON Storage | ✅ PASS |
| 14 | `taskDateHelper_formattingAndParsing` | Parsing String Tanggal Jatuh Tempo Tugas | ✅ PASS |
| 15 | `editTaskViewModel_loadAndValidation` | Pengisian Otomatis & Validasi Ubah Tugas | ✅ PASS |
| 16 | `userManager_defaultAccountsExistAndLoginWorks` | Autentikasi Fallback & Sesi Mahasiswa | ✅ PASS |
| 17 | `userManager_registerNewAccount_successAndPersists` | Pendaftaran Akun Baru & Validasi Sesi | ✅ PASS |
| 18 | `userManager_registerDuplicateFails` | Proteksi Duplikasi Email & Username | ✅ PASS |
| 19 | `loginViewModel_validationsAndSuccess` | Validasi ViewModel Login (Email/Username) | ✅ PASS |
| 20 | `signUpViewModel_validationsAndSuccess` | Validasi ViewModel Pendaftaran & Nama Lengkap | ✅ PASS |
| 21 | `roomEntities_courseAndTaskMappers_areLossless` | Pemetaan & Konversi Lossless Room Entities | ✅ PASS |
| 22 | `backgroundWorker_nameAndConstant_areCorrect` | Verifikasi Konstanta WorkManager Worker | ✅ PASS |
| 23 | `retrofitApi_quoteRepository_returnsFallbackWhenOffline` | Retrofit REST API Quote & Offline Fallback | ✅ PASS |

---

## 👥 Tim Pengembang (Kelompok 9)

| No | Nama Mahasiswa | NIM | Peran Utama |
| :---: | :--- | :---: | :--- |
| 1 | **Taqiyuddin Umar** | `245150219111001` | Dokumentasi Sistem & Standardisasi Desain |
| 2 | **Alviensyah Mohammad** | `245150219111002` | UI/UX Jetpack Compose, NavGraph, Hybrid Architecture & Firebase Integration |
| 3 | **Muhammad Naufal Dzakwan** | `245150207111098` | Manajemen Modul Data Course & Schedules |
| 4 | **Michael Asaloei** | `215150201111067` | Manajemen Modul Data Task & Quality Testing |

---

<p align="center">
  <sub>TaskMate — Dikembangkan dengan dedikasi untuk Ujian Akhir Praktikum (UAP) Pemrograman Aplikasi Perangkat Bergerak (PAPB) Kelas D.</sub>
</p>
